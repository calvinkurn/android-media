package com.tokopedia.minicart.cartlist

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cartcommon.domain.data.RemoveFromCartDomainModel
import com.tokopedia.cartcommon.domain.data.UndoDeleteCartDomainModel
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapter
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory
import com.tokopedia.minicart.cartlist.subpage.summarytransaction.SummaryTransactionBottomSheet
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.chatlist.MiniCartChatListBottomSheet
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.databinding.LayoutBottomsheetMiniCartListBinding
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MiniCartListBottomSheet @Inject constructor(private var miniCartListDecoration: MiniCartListDecoration,
                                                  var summaryTransactionBottomSheet: SummaryTransactionBottomSheet,
                                                  var analytics: MiniCartAnalytics)
    : MiniCartListActionListener {

    companion object {
        private const val LONG_DELAY = 500L
        private const val SHORT_DELAY = 200L
    }

    private var viewBinding: LayoutBottomsheetMiniCartListBinding? = null
    private var viewModel: MiniCartViewModel? = null
    private var bottomSheet: BottomSheetUnify? = null
    private var adapter: MiniCartListAdapter? = null
    private var bottomSheetListener: MiniCartListBottomSheetListener? = null
    private var miniCartChevronClickListener: View.OnClickListener? = null

    private var measureRecyclerViewPaddingDebounceJob: Job? = null
    private var updateCartDebounceJob: Job? = null
    private var calculationDebounceJob: Job? = null

    private var globalEventObserver: Observer<GlobalEvent>? = null
    private var bottomSheetUiModelObserver: Observer<MiniCartListUiModel>? = null

    private var isShow: Boolean = false

    @Inject
    lateinit var miniCartChatListBottomSheet: MiniCartChatListBottomSheet

    fun show(context: Context?,
             fragmentManager: FragmentManager,
             lifecycleOwner: LifecycleOwner,
             viewModel: MiniCartViewModel,
             bottomSheetListener: MiniCartListBottomSheetListener) {
        context?.let {
            if (!isShow) {
                this.bottomSheetListener = bottomSheetListener
                val viewBinding = LayoutBottomsheetMiniCartListBinding.inflate(LayoutInflater.from(context))
                this.viewBinding = viewBinding
                initializeView(it, viewBinding, fragmentManager)
                initializeViewModel(viewBinding, fragmentManager, viewModel, lifecycleOwner)
                initializeCartData(viewBinding, viewModel)
            }
        }
    }

    fun dismiss() {
        bottomSheet?.dismiss()
    }

    private fun resetObserver() {
        globalEventObserver?.let {
            viewModel?.globalEvent?.removeObserver(it)
            globalEventObserver = null
        }
        bottomSheetUiModelObserver?.let {
            viewModel?.miniCartListBottomSheetUiModel?.removeObserver(it)
            bottomSheetUiModelObserver = null
        }
    }

    private fun initializeBottomSheet(viewBinding: LayoutBottomsheetMiniCartListBinding, fragmentManager: FragmentManager) {
        bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = false
            showHeader = true
            isDragable = true
            isHideable = true
            clearContentPadding = true
            customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
            setShowListener {
                isShow = true
                knobView.setOnClickListener {
                    analytics.eventClickKnobToExpandMiniCartBottomSheet()
                }
            }
            setOnDismissListener {
                isShow = false
                cancelAllDebounceJob()
                resetObserver()
                bottomSheetListener?.onMiniCartListBottomSheetDismissed()
                this@MiniCartListBottomSheet.viewBinding = null
            }
            setChild(viewBinding.root)
            show(fragmentManager, this.javaClass.simpleName)
        }
    }

    private fun initializeCartData(viewBinding: LayoutBottomsheetMiniCartListBinding, viewModel: MiniCartViewModel) {
        adapter?.clearAllElements()
        bottomSheet?.setTitle("")
        showLoading()
        setTotalAmountLoading(viewBinding, true)
        viewModel.getCartList(isFirstLoad = true)
    }

    private fun initializeView(context: Context, viewBinding: LayoutBottomsheetMiniCartListBinding, fragmentManager: FragmentManager) {
        initializeBottomSheet(viewBinding, fragmentManager)
        initializeTotalAmount(viewBinding, fragmentManager, context)
        initializeRecyclerView(viewBinding)
    }

    private fun initializeViewModel(viewBinding: LayoutBottomsheetMiniCartListBinding, fragmentManager: FragmentManager, viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        this.viewModel = viewModel
        viewModel.initializeGlobalState()
        initializeGlobalEventObserver(viewBinding, viewModel, fragmentManager)
        initializeBottomSheetUiModelObserver(viewBinding, fragmentManager, viewModel, lifecycleOwner)
        observeGlobalEvent(viewModel, lifecycleOwner)
        observeMiniCartListUiModel(viewModel, lifecycleOwner)
    }

    private fun initializeRecyclerView(viewBinding: LayoutBottomsheetMiniCartListBinding) {
        val adapterTypeFactory = MiniCartListAdapterTypeFactory(this)
        adapter = MiniCartListAdapter(adapterTypeFactory)
        viewBinding.rvMiniCartList.adapter = adapter
        viewBinding.rvMiniCartList.layoutManager = LinearLayoutManager(viewBinding.root.context, LinearLayoutManager.VERTICAL, false)
        viewBinding.rvMiniCartList.addItemDecoration(miniCartListDecoration)
    }

    private fun initializeTotalAmount(viewBinding: LayoutBottomsheetMiniCartListBinding, fragmentManager: FragmentManager, context: Context) {
        viewBinding.totalAmount.let {
            miniCartChevronClickListener = View.OnClickListener {
                analytics.eventClickChevronToShowSummaryTransaction()
                viewModel?.miniCartListBottomSheetUiModel?.value?.miniCartSummaryTransactionUiModel?.let { miniCartSummaryTransaction ->
                    summaryTransactionBottomSheet.show(miniCartSummaryTransaction, fragmentManager, context)
                }
            }
            it.amountChevronView.setOnClickListener(miniCartChevronClickListener)
            it.amountCtaView.setOnClickListener {
                sendEventClickBuy()
                showProgressLoading()
                viewModel?.goToCheckout(GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET)
            }
            it.context?.let { context ->
                validateTotalAmountView(context, viewBinding)
            }
            setTotalAmountLoading(viewBinding, true)
        }
    }

    private fun sendEventClickBuy() {
        val pageName = viewModel?.currentPage?.value ?: MiniCartAnalytics.Page.HOME_PAGE
        val products = viewModel?.miniCartListBottomSheetUiModel?.value?.getMiniCartProductUiModelList()
                ?: emptyList()
        val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
        analytics.eventClickBuy(pageName, products, isOCCFlow)
    }

    private fun initializeGlobalEventObserver(viewBinding: LayoutBottomsheetMiniCartListBinding, viewModel: MiniCartViewModel, fragmentManager: FragmentManager) {
        globalEventObserver = Observer<GlobalEvent> {
            when (it.state) {
                GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM -> {
                    onSuccessDeleteCartItem(viewBinding, it, viewModel)
                }
                GlobalEvent.STATE_FAILED_DELETE_CART_ITEM -> {
                    onFailedDeleteCartItem(viewBinding, it)
                }
                GlobalEvent.STATE_SUCCESS_UNDO_DELETE_CART_ITEM -> {
                    onSuccessUndoDeleteCartItem(viewBinding, it, viewModel)
                }
                GlobalEvent.STATE_FAILED_UNDO_DELETE_CART_ITEM -> {
                    onFailedUndoDeleteCartItem(viewBinding, it)
                }
                GlobalEvent.STATE_SUCCESS_TO_CHECKOUT -> {
                    onSuccessGoToCheckout(it)
                }
                GlobalEvent.STATE_FAILED_TO_CHECKOUT -> {
                    onFailedGoToCheckout(viewBinding, it, fragmentManager)
                }
            }
        }
    }

    private fun onFailedUndoDeleteCartItem(viewBinding: LayoutBottomsheetMiniCartListBinding, globalEvent: GlobalEvent) {
        hideProgressLoading()
        globalEvent.throwable?.let { throwable ->
            bottomSheet?.context?.let { context ->
                var message = ErrorHandler.getErrorMessage(context, throwable)
                if (throwable is ResponseErrorException) {
                    message = throwable.message.toString()
                }
                viewBinding.bottomsheetContainer.let { view ->
                    bottomSheetListener?.showToaster(view, message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun onSuccessUndoDeleteCartItem(viewBinding: LayoutBottomsheetMiniCartListBinding, globalEvent: GlobalEvent, viewModel: MiniCartViewModel) {
        hideProgressLoading()
        viewModel.getCartList()
        val data = globalEvent.data as? UndoDeleteCartDomainModel
        val message = data?.undoDeleteCartDataResponse?.data?.message?.firstOrNull() ?: ""
        if (message.isNotBlank()) {
            val ctaText = bottomSheet?.context?.getString(R.string.mini_cart_cta_ok)
                    ?: ""
            viewBinding.bottomsheetContainer.let { view ->
                bottomSheetListener?.showToaster(view, message, Toaster.TYPE_NORMAL, ctaText)
            }
        }
    }

    private fun initializeBottomSheetUiModelObserver(viewBinding: LayoutBottomsheetMiniCartListBinding, fragmentManager: FragmentManager, viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        bottomSheetUiModelObserver = Observer<MiniCartListUiModel> {
            if (it.miniCartWidgetUiModel.totalProductCount == 0 && it.miniCartWidgetUiModel.totalProductError == 0) {
                dismiss()
            }

            if (it.needToCalculateAfterLoad) {
                calculateProduct()
            } else {
                hideLoading()
                hideProgressLoading()
                // Need to try-catch since can't check is lateinit `bottomSheetClose` has already initialized or not using `isInitialized`
                try {
                    bottomSheet?.bottomSheetClose?.show()
                } catch (e: UninitializedPropertyAccessException) {
                    // No-op
                }
                bottomSheet?.setTitle(it.title)
                if (viewBinding.rvMiniCartList.isComputingLayout) {
                    viewBinding.rvMiniCartList.post {
                        adapter?.updateList(it.visitables)
                    }
                } else {
                    adapter?.updateList(it.visitables)
                }
                updateTotalAmount(viewBinding, it.miniCartWidgetUiModel)
                adjustRecyclerViewPaddingBottom(viewBinding)

                if (it.isFirstLoad) {
                    sendFirstLoadAnalytics(it)
                    it.isFirstLoad = false
                    // Collapse unavailable items on first load
                    viewModel?.toggleUnavailableItemsAccordion()
                }
            }

            viewBinding.totalAmount.totalAmountAdditionalButton.setOnClickListener {
                analytics.eventClickChatOnMiniCart()
                miniCartChatListBottomSheet.show(context = viewBinding.totalAmount.context, fragmentManager = fragmentManager, lifecycleOwner = lifecycleOwner, viewModel = viewModel)
                dismiss()
            }
        }
    }

    private fun sendFirstLoadAnalytics(miniCartListUiModel: MiniCartListUiModel) {
        analytics.eventLoadMiniCartBottomSheetSuccess(miniCartListUiModel.getMiniCartProductUiModelList())
        val overweightData = miniCartListUiModel.getMiniCartTickerWarningUiModel()
        if (overweightData != null) {
            analytics.eventViewErrorTickerOverweightInMiniCart(overweightData.warningMessage)
        }
        val shopId = viewModel?.currentShopIds?.value?.firstOrNull() ?: ""
        loop@ for (visitable in miniCartListUiModel.visitables) {
            if (visitable is MiniCartProductUiModel && visitable.isProductDisabled) {
                analytics.eventViewTickerErrorUnavailableProduct(shopId, visitable.errorType)
                break@loop
            }
        }
    }

    private fun observeGlobalEvent(viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        globalEventObserver?.let {
            viewModel.globalEvent.observe(lifecycleOwner, it)
        }
    }

    private fun onFailedGoToCheckout(viewBinding: LayoutBottomsheetMiniCartListBinding, globalEvent: GlobalEvent, fragmentManager: FragmentManager) {
        if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            hideProgressLoading()
            viewBinding.bottomsheetContainer.let { view ->
                bottomSheetListener?.onBottomSheetFailedGoToCheckout(view, fragmentManager, globalEvent)
            }
        }
    }

    private fun onSuccessGoToCheckout(globalEvent: GlobalEvent) {
        if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            bottomSheet?.context.let {
                hideProgressLoading()
                bottomSheetListener?.onBottomSheetSuccessGoToCheckout()
                dismiss()
            }
        }
    }

    private fun onFailedDeleteCartItem(viewBinding: LayoutBottomsheetMiniCartListBinding, globalEvent: GlobalEvent) {
        hideProgressLoading()
        globalEvent.throwable?.let { throwable ->
            bottomSheet?.context?.let { context ->
                var message = ErrorHandler.getErrorMessage(context, throwable)
                if (throwable is ResponseErrorException) {
                    message = throwable.message.toString()
                }
                viewBinding.bottomsheetContainer.let { view ->
                    bottomSheetListener?.showToaster(view, message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun onSuccessDeleteCartItem(viewBinding: LayoutBottomsheetMiniCartListBinding, globalEvent: GlobalEvent, viewModel: MiniCartViewModel) {
        val data = globalEvent.data as? RemoveFromCartDomainModel
        // last item should be handled by mini cart widget, since the bottomsheet already dismissed
        if (data?.isLastItem == true) return

        hideProgressLoading()
        val message = data?.removeFromCartData?.data?.message?.firstOrNull() ?: ""
        if (message.isNotBlank()) {
            val ctaText = bottomSheet?.context?.getString(R.string.mini_cart_cta_cancel)
                    ?: ""
            viewModel.getCartList()
            if (data?.isBulkDelete == true) {
                viewBinding.bottomsheetContainer.let { view ->
                    bottomSheetListener?.showToaster(view, message, Toaster.TYPE_NORMAL)
                }
            } else {
                viewBinding.bottomsheetContainer.let { view ->
                    bottomSheetListener?.showToaster(view, message, Toaster.TYPE_NORMAL, ctaText) {
                        analytics.eventClickUndoDelete()
                        showProgressLoading()
                        viewModel.undoDeleteCartItem(false)
                    }
                }
            }
        }
    }

    private fun observeMiniCartListUiModel(viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        bottomSheetUiModelObserver?.let {
            viewModel.miniCartListBottomSheetUiModel.observe(lifecycleOwner, it)
        }
    }

    private fun adjustRecyclerViewPaddingBottom(viewBinding: LayoutBottomsheetMiniCartListBinding) {
        measureRecyclerViewPaddingDebounceJob?.cancel()
        measureRecyclerViewPaddingDebounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(LONG_DELAY)
            adjustRecyclerViewPadding(viewBinding)
        }
    }

    private fun adjustRecyclerViewPadding(viewBinding: LayoutBottomsheetMiniCartListBinding) {
        with(viewBinding) {
            if (rvMiniCartList.canScrollVertically(-1) || rvMiniCartList.canScrollVertically(1) || isBottomSheetFullPage(viewBinding)) {
                rvMiniCartList.setPadding(0, 0, 0, rvMiniCartList.resources?.getDimensionPixelSize(R.dimen.dp_64)
                        ?: 0)
            } else {
                rvMiniCartList.setPadding(0, 0, 0, 0)
            }
        }
    }

    private fun isBottomSheetFullPage(viewBinding: LayoutBottomsheetMiniCartListBinding): Boolean {
        val displayMetrics = Resources.getSystem().displayMetrics
        bottomSheet?.activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        val bottomSheetHeight = (bottomSheet?.bottomSheetWrapper?.parent as? View)?.height ?: 0
        val recyclerViewPaddingBottom = viewBinding.rvMiniCartList.resources?.getDimensionPixelSize(R.dimen.dp_64)
                ?: 0
        val displayHeight = displayMetrics?.heightPixels ?: 0
        return bottomSheetHeight != 0 && displayHeight != 0 && (bottomSheetHeight + (recyclerViewPaddingBottom / 2)) >= displayHeight
    }

    private fun cancelAllDebounceJob() {
        measureRecyclerViewPaddingDebounceJob?.cancel()
        updateCartDebounceJob?.cancel()
        calculationDebounceJob?.cancel()
    }

    private fun setTotalAmountLoading(viewBinding: LayoutBottomsheetMiniCartListBinding, isLoading: Boolean) {
        with(viewBinding) {
            if (isLoading) {
                if (!totalAmount.isTotalAmountLoading) {
                    totalAmount.isTotalAmountLoading = true
                }
            } else {
                if (totalAmount.isTotalAmountLoading) {
                    totalAmount.isTotalAmountLoading = false
                }
            }
            root.context?.let {
                validateTotalAmountView(it, viewBinding)
            }
        }
    }

    private fun validateTotalAmountView(context: Context, viewBinding: LayoutBottomsheetMiniCartListBinding) {
        with(viewBinding) {
            val chatIcon = getIconUnifyDrawable(context, IconUnify.CHAT, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            totalAmount.setAdditionalButton(chatIcon)
            this.chatIcon.setImageDrawable(chatIcon)
            totalAmount.amountChevronView.setOnClickListener(miniCartChevronClickListener)
        }
    }

    private fun updateTotalAmount(viewBinding: LayoutBottomsheetMiniCartListBinding, miniCartWidgetData: MiniCartWidgetData) {
        viewBinding.totalAmount.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_total_price))
            if (miniCartWidgetData.totalProductCount == 0) {
                setAmount("-")
                val ctaText = viewModel?.miniCartABTestData?.value?.buttonBuyWording
                        ?: context.getString(R.string.mini_cart_widget_cta_text_default)
                setCtaText(ctaText)
                amountCtaView.isEnabled = false
                enableAmountChevron(false)
            } else {
                setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartWidgetData.totalProductPrice, false).removeDecimalSuffix())
                val ctaText = viewModel?.miniCartABTestData?.value?.buttonBuyWording
                        ?: context.getString(R.string.mini_cart_widget_cta_text_default)
                setCtaText("$ctaText (${miniCartWidgetData.totalProductCount})")
                amountCtaView.isEnabled = true
                enableAmountChevron(true)
            }
            amountCtaView.layoutParams.width = resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
            validateAmountCtaLabel(viewBinding, miniCartWidgetData)
        }
        setTotalAmountLoading(viewBinding, false)
    }

    private fun validateAmountCtaLabel(viewBinding: LayoutBottomsheetMiniCartListBinding, miniCartWidgetData: MiniCartWidgetData) {
        if (viewModel?.miniCartABTestData?.value?.isOCCFlow == true) {
            viewBinding.totalAmount.post {
                val ellipsis = viewBinding.totalAmount.amountCtaView.layout?.getEllipsisCount(0)
                        ?: 0
                if (ellipsis > 0) {
                    val ctaText = viewBinding.totalAmount.context.getString(R.string.mini_cart_widget_cta_text_default)
                    if (miniCartWidgetData.totalProductCount == 0) {
                        viewBinding.totalAmount.setCtaText(ctaText)
                    } else {
                        viewBinding.totalAmount.setCtaText("$ctaText (${miniCartWidgetData.totalProductCount})")
                    }
                }
            }
        }
    }

    private fun showLoading() {
        adapter?.let {
            it.removeErrorNetwork()
            it.setLoadingModel(LoadingModel())
            it.showLoading()
        }
    }

    private fun hideLoading() {
        adapter?.hideLoading()
    }

    private fun showProgressLoading() {
        bottomSheetListener?.showProgressLoading()
    }

    private fun hideProgressLoading() {
        bottomSheetListener?.hideProgressLoading()
    }

    private fun updateCart() {
        updateCartDebounceJob?.cancel()
        updateCartDebounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(LONG_DELAY)
            viewModel?.updateCart()
        }
    }

    private fun calculateProduct() {
        calculationDebounceJob?.cancel()
        calculationDebounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(SHORT_DELAY)
            viewModel?.calculateProduct()
        }
    }

    override fun onDeleteClicked(element: MiniCartProductUiModel) {
        analytics.eventClickDeleteFromTrashBin()
        bottomSheetListener?.showProgressLoading()
        viewModel?.deleteSingleCartItem(element)
    }

    override fun onBulkDeleteUnavailableItems() {
        analytics.eventClickDeleteAllUnavailableProduct()
        val unavailableProducts = viewModel?.miniCartListBottomSheetUiModel?.value?.getUnavailableProduct()
                ?: emptyList()
        val hiddenUnavailableProducts = viewModel?.tmpHiddenUnavailableItems ?: emptyList()
        val allUnavailableProducts = unavailableProducts + hiddenUnavailableProducts
        bottomSheet?.context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(it.getString(R.string.mini_cart_label_dialog_title_delete_unavailable_multiple_item, allUnavailableProducts.size))
                setDescription(it.getString(R.string.mini_cart_label_dialog_message_remove_cart_unavailable_multiple_item))
                setPrimaryCTAText(it.getString(R.string.mini_cart_label_dialog_action_delete))
                setSecondaryCTAText(it.getString(R.string.mini_cart_label_dialog_action_cancel))
                setPrimaryCTAClickListener {
                    dismiss()
                    showProgressLoading()
                    viewModel?.bulkDeleteUnavailableCartItems()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
                show()
            }
        }
    }

    override fun onQuantityChanged(productId: String, newQty: Int) {
        viewModel?.updateProductQty(productId, newQty)
        calculateProduct()
        updateCart()
    }

    override fun onNotesChanged(productId: String, newNotes: String) {
        viewModel?.updateProductNotes(productId, newNotes)
        updateCart()
    }

    override fun onShowSimilarProductClicked(appLink: String, element: MiniCartProductUiModel) {
        analytics.eventClickSeeSimilarProductOnUnavailableSection(element.productId, element.errorType)
        bottomSheet?.context?.let {
            RouteManager.route(it, appLink)
        }
    }

    override fun onShowUnavailableItemsCLicked() {
        scrollToUnavailableSection()
    }

    fun scrollToUnavailableSection() {
        val data = adapter?.data ?: emptyList()
        loop@ for ((index, visitable) in data.withIndex()) {
            if (visitable is MiniCartProductUiModel && visitable.isProductDisabled) {
                viewBinding?.rvMiniCartList?.smoothScrollToPosition(index)
                break@loop
            }
        }
    }

    override fun onToggleShowHideUnavailableItemsClicked() {
        viewModel?.toggleUnavailableItemsAccordion()
        val lastItemPosition = (adapter?.list?.size ?: 0) - 1
        if (lastItemPosition != -1) {
            viewBinding?.rvMiniCartList?.smoothScrollToPosition(lastItemPosition)
        }
    }

    override fun onProductInfoClicked(element: MiniCartProductUiModel) {
        analytics.eventClickProductName(element.productId)
        bottomSheet?.context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, element.productId)
            it.startActivity(intent)
        }
    }

    override fun onQuantityPlusClicked() {
        analytics.eventClickQuantityPlus()
    }

    override fun onQuantityMinusClicked() {
        analytics.eventClickQuantityMinus()
    }

    override fun onInputQuantityClicked(qty: Int) {
        analytics.eventClickInputQuantity(qty)
    }

    override fun onWriteNotesClicked() {
        analytics.eventClickWriteNotes()
    }

    override fun onChangeNotesClicked() {
        analytics.eventClickChangeNotes()
    }

}