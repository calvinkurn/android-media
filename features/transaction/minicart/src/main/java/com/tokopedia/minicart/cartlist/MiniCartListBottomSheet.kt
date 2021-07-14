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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapter
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory
import com.tokopedia.minicart.cartlist.subpage.summarytransaction.SummaryTransactionBottomSheet
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.domain.data.RemoveFromCartDomainModel
import com.tokopedia.minicart.common.domain.data.UndoDeleteCartDomainModel
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.minicart.databinding.LayoutBottomsheetMiniCartListBinding
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*
import javax.inject.Inject

class MiniCartListBottomSheet @Inject constructor(private var miniCartListDecoration: MiniCartListDecoration,
                                                  var summaryTransactionBottomSheet: SummaryTransactionBottomSheet,
                                                  var analytics: MiniCartAnalytics)
    : MiniCartListActionListener {

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
            showKnob = true
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
        initializeBottomSheetUiModelObserver(viewBinding)
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
                viewModel?.addToCartOcc(GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET)
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
        analytics.eventClickBuy(pageName, products)
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
                GlobalEvent.STATE_SUCCESS_ADD_TO_CART_FOR_CHECKOUT -> {
                    onSuccessUpdateCartForCheckout(it)
                }
                GlobalEvent.STATE_FAILED_ADD_TO_CART_FOR_CHECKOUT -> {
                    onFailedUpdateCartForCheckout(viewBinding, it, viewModel, fragmentManager)
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
                    message = throwable.message
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

    private fun initializeBottomSheetUiModelObserver(viewBinding: LayoutBottomsheetMiniCartListBinding) {
        bottomSheetUiModelObserver = Observer<MiniCartListUiModel> {
            if (it.miniCartWidgetUiModel.totalProductCount == 0 && it.miniCartWidgetUiModel.totalProductError == 0) {
                dismiss()
            }

            if (it.needToCalculateAfterLoad) {
                calculateProduct()
            } else {
                hideLoading()
                hideProgressLoading()
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
                }
            }
        }
    }

    private fun sendFirstLoadAnalytics(miniCartListUiModel: MiniCartListUiModel) {
        analytics.eventLoadMiniCartBottomSheetSuccess(miniCartListUiModel.getMiniCartProductUiModelList())
        val overweightData = miniCartListUiModel.getMiniCartTickerWarningUiModel()
        if (overweightData != null) {
            analytics.eventViewErrorTickerOverweightInMiniCart(overweightData.warningMessage)
        }
        val tickerErrorUiModel = miniCartListUiModel.getMiniCartTickerErrorUiModel()
        if (tickerErrorUiModel != null) {
            val message = bottomSheet?.context?.getString(R.string.mini_cart_message_ticker_error, tickerErrorUiModel.unavailableItemCount)
                    ?: ""
            analytics.eventViewTickerErrorUnavailableProduct(message)
        }
    }

    private fun observeGlobalEvent(viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        globalEventObserver?.let {
            viewModel.globalEvent.observe(lifecycleOwner, it)
        }
    }

    private fun onFailedUpdateCartForCheckout(viewBinding: LayoutBottomsheetMiniCartListBinding, globalEvent: GlobalEvent, viewModel: MiniCartViewModel, fragmentManager: FragmentManager) {
        if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            hideProgressLoading()
            viewBinding.bottomsheetContainer.let { view ->
                bottomSheetListener?.onBottomSheetFailedAddToCartForCheckout(view, fragmentManager, globalEvent)
            }
        }
    }

    private fun onSuccessUpdateCartForCheckout(globalEvent: GlobalEvent) {
        if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            bottomSheet?.context.let {
                hideProgressLoading()
                bottomSheetListener?.onBottomSheetSuccessAddToCartForCheckout()
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
                    message = throwable.message
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
            delay(500)
            with(viewBinding) {
                if (rvMiniCartList.canScrollVertically(-1) || rvMiniCartList.canScrollVertically(1)) {
                    rvMiniCartList.setPadding(0, 0, 0, rvMiniCartList.resources?.getDimensionPixelOffset(R.dimen.dp_64)
                            ?: 0)
                } else {
                    rvMiniCartList.setPadding(0, 0, 0, 0)
                }
            }
        }
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
            totalAmount.totalAmountAdditionalButton.setOnClickListener {
                analytics.eventClickChatOnMiniCart()
                val shopId = viewModel?.currentShopIds?.value?.firstOrNull() ?: "0"
                val intent = RouteManager.getIntent(
                        context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId
                )
                context.startActivity(intent)
            }
            this.chatIcon.setImageDrawable(chatIcon)
            totalAmount.amountChevronView.setOnClickListener(miniCartChevronClickListener)
        }
    }

    private fun updateTotalAmount(viewBinding: LayoutBottomsheetMiniCartListBinding, miniCartWidgetData: MiniCartWidgetData) {
        viewBinding.totalAmount.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_total_price))
            if (miniCartWidgetData.totalProductCount == 0) {
                setAmount("-")
                setCtaText(context.getString(R.string.mini_cart_widget_label_buy_occ_empty))
                amountCtaView.isEnabled = false
                enableAmountChevron(false)
            } else {
                setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartWidgetData.totalProductPrice, false))
                setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy_occ), miniCartWidgetData.totalProductCount))
                amountCtaView.isEnabled = true
                enableAmountChevron(true)
            }
            amountCtaView.layoutParams.width = resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
            post {
                val ellipsis = amountCtaView.layout?.getEllipsisCount(0) ?: 0
                if (ellipsis > 0) {
                    if (miniCartWidgetData.totalProductCount == 0) {
                        setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy_empty)))
                    } else {
                        setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy), miniCartWidgetData.totalProductCount))
                    }
                }
            }
        }
        setTotalAmountLoading(viewBinding, false)
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
            delay(500)
            viewModel?.updateCart()
        }
    }

    private fun calculateProduct() {
        calculationDebounceJob?.cancel()
        calculationDebounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(200)
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
        bottomSheet?.context?.let {
            DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(it.getString(R.string.mini_cart_label_dialog_title_delete_unavailable_multiple_item, unavailableProducts.size))
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