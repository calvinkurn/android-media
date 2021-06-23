package com.tokopedia.minicart.cartlist

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache
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
import com.tokopedia.minicart.common.data.response.minicartlist.Action
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.domain.data.RemoveFromCartDomainModel
import com.tokopedia.minicart.common.domain.data.UndoDeleteCartDomainModel
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*
import javax.inject.Inject

class MiniCartListBottomSheet @Inject constructor(private var miniCartListDecoration: MiniCartListDecoration,
                                                  var summaryTransactionBottomSheet: SummaryTransactionBottomSheet,
                                                  var analytics: MiniCartAnalytics)
    : MiniCartListActionListener {

    companion object {
        const val QUERY_APP_CLIENT_ID = "{app_client_id}"
    }

    private var viewModel: MiniCartViewModel? = null
    private var bottomsheetContainer: CoordinatorLayout? = null
    private var bottomSheet: BottomSheetUnify? = null
    private var totalAmount: TotalAmount? = null
    private var chatIcon: ImageUnify? = null
    private var rvMiniCartList: RecyclerView? = null
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
                initializeView(it, fragmentManager)
                initializeViewModel(fragmentManager, viewModel, lifecycleOwner)
                initializeCartData(viewModel)
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

    private fun initializeBottomSheet(view: View, fragmentManager: FragmentManager) {
        bottomsheetContainer = view.findViewById(R.id.bottomsheet_container)
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
            }
            setChild(view)
            show(fragmentManager, this.javaClass.simpleName)
        }
    }

    private fun initializeCartData(viewModel: MiniCartViewModel) {
        adapter?.clearAllElements()
        showLoading()
        setTotalAmountLoading(true)
        viewModel.getCartList(isFirstLoad = true)
    }

    private fun initializeView(context: Context, fragmentManager: FragmentManager) {
        context.let {
            val view = View.inflate(it, R.layout.layout_bottomsheet_mini_cart_list, null)
            initializeBottomSheet(view, fragmentManager)
            initializeTotalAmount(view, fragmentManager, context)
            initializeRecyclerView(view)
        }
    }

    private fun initializeViewModel(fragmentManager: FragmentManager, viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        this.viewModel = viewModel
        viewModel.initializeGlobalState()
        initializeGlobalEventObserver(viewModel, fragmentManager)
        initializeBottomSheetUiModelObserver()
        observeGlobalEvent(viewModel, lifecycleOwner)
        observeMiniCartListUiModel(viewModel, lifecycleOwner)
    }

    private fun initializeRecyclerView(view: View) {
        rvMiniCartList = view.findViewById(R.id.rv_mini_cart_list)
        val adapterTypeFactory = MiniCartListAdapterTypeFactory(this)
        adapter = MiniCartListAdapter(adapterTypeFactory)
        rvMiniCartList?.adapter = adapter
        rvMiniCartList?.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        rvMiniCartList?.addItemDecoration(miniCartListDecoration)
    }

    private fun initializeTotalAmount(view: View, fragmentManager: FragmentManager, context: Context) {
        totalAmount = view.findViewById(R.id.total_amount)
        chatIcon = view.findViewById(R.id.chat_icon)
        totalAmount?.let {
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
                viewModel?.updateCart(true, GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET)
            }
            validateTotalAmountView()
            setTotalAmountLoading(true)
        }
    }

    private fun sendEventClickBuy() {
        val pageName = viewModel?.currentPage?.value ?: MiniCartAnalytics.Page.HOME_PAGE
        val products = viewModel?.miniCartListBottomSheetUiModel?.value?.getMiniCartProductUiModelList()
                ?: emptyList()
        analytics.eventClickBuy(pageName, products)
    }

    private fun initializeGlobalEventObserver(viewModel: MiniCartViewModel, fragmentManager: FragmentManager) {
        globalEventObserver = Observer<GlobalEvent> {
            when (it.state) {
                GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM -> {
                    onSuccessDeleteCartItem(it, viewModel)
                }
                GlobalEvent.STATE_FAILED_DELETE_CART_ITEM -> {
                    onFailedDeleteCartItem(it)
                }
                GlobalEvent.STATE_SUCCESS_UNDO_DELETE_CART_ITEM -> {
                    onSuccessUndoDeleteCartItem(it, viewModel)
                }
                GlobalEvent.STATE_FAILED_UNDO_DELETE_CART_ITEM -> {
                    onFailedUndoDeleteCartItem(it)
                }
                GlobalEvent.STATE_SUCCESS_UPDATE_CART_FOR_CHECKOUT -> {
                    onSuccessUpdateCartForCheckout(it)
                }
                GlobalEvent.STATE_FAILED_UPDATE_CART_FOR_CHECKOUT -> {
                    onFailedUpdateCartForCheckout(it, viewModel, fragmentManager)
                }
            }
        }
    }

    private fun onFailedUndoDeleteCartItem(globalEvent: GlobalEvent) {
        hideProgressLoading()
        globalEvent.throwable?.let { throwable ->
            bottomSheet?.context?.let { context ->
                var message = ErrorHandler.getErrorMessage(context, throwable)
                if (throwable is ResponseErrorException) {
                    message = throwable.message
                }
                bottomsheetContainer?.let { view ->
                    bottomSheetListener?.showToaster(view, message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun onSuccessUndoDeleteCartItem(globalEvent: GlobalEvent, viewModel: MiniCartViewModel) {
        hideProgressLoading()
        viewModel.getCartList()
        val data = globalEvent.data as? UndoDeleteCartDomainModel
        val message = data?.undoDeleteCartDataResponse?.data?.message?.firstOrNull() ?: ""
        if (message.isNotBlank()) {
            val ctaText = bottomSheet?.context?.getString(R.string.mini_cart_cta_ok)
                    ?: ""
            bottomsheetContainer?.let { view ->
                bottomSheetListener?.showToaster(view, message, Toaster.TYPE_NORMAL, ctaText)
            }
        }
    }

    private fun initializeBottomSheetUiModelObserver() {
        bottomSheetUiModelObserver = Observer<MiniCartListUiModel> {
            if (it.miniCartWidgetUiModel.totalProductCount == 0 && it.miniCartWidgetUiModel.totalProductError == 0) {
                dismiss()
            }

            if (it.isFirstLoad) {
                analytics.eventLoadMiniCartBottomSheetSuccess(it.getMiniCartProductUiModelList())
                val overweightData = it.getMiniCartTickerWarningUiModel()
                if (overweightData != null) {
                    analytics.eventViewErrorTickerOverweightInMiniCart(overweightData.warningMessage)
                }
            }

            if (it.needToCalculateAfterLoad) {
                calculateProduct()
            } else {
                hideLoading()
                hideProgressLoading()
                bottomSheet?.setTitle(it.title)
                if (rvMiniCartList?.isComputingLayout == true) {
                    rvMiniCartList?.post {
                        adapter?.updateList(it.visitables)
                    }
                } else {
                    adapter?.updateList(it.visitables)
                }
                updateTotalAmount(it.miniCartWidgetUiModel)
                adjustRecyclerViewPaddingBottom()
            }
        }
    }

    private fun observeGlobalEvent(viewModel: MiniCartViewModel, lifecycleOwner: LifecycleOwner) {
        globalEventObserver?.let {
            viewModel.globalEvent.observe(lifecycleOwner, it)
        }
    }

    private fun onFailedUpdateCartForCheckout(globalEvent: GlobalEvent, viewModel: MiniCartViewModel, fragmentManager: FragmentManager) {
        if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            hideProgressLoading()
            viewModel.getCartList()
            bottomsheetContainer?.let { view ->
                bottomSheetListener?.onBottomSheetFailedUpdateCartForCheckout(view, fragmentManager, globalEvent)
            }
        }
    }

    private fun onSuccessUpdateCartForCheckout(globalEvent: GlobalEvent) {
        if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            bottomSheet?.context.let {
                hideProgressLoading()
                bottomSheetListener?.onBottomSheetSuccessUpdateCartForCheckout()
                dismiss()
            }
        }
    }

    private fun onFailedDeleteCartItem(globalEvent: GlobalEvent) {
        hideProgressLoading()
        globalEvent.throwable?.let { throwable ->
            bottomSheet?.context?.let { context ->
                var message = ErrorHandler.getErrorMessage(context, throwable)
                if (throwable is ResponseErrorException) {
                    message = throwable.message
                }
                bottomsheetContainer?.let { view ->
                    bottomSheetListener?.showToaster(view, message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun onSuccessDeleteCartItem(globalEvent: GlobalEvent, viewModel: MiniCartViewModel) {
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
                bottomsheetContainer?.let { view ->
                    bottomSheetListener?.showToaster(view, message, Toaster.TYPE_NORMAL)
                }
            } else {
                bottomsheetContainer?.let { view ->
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

    private fun adjustRecyclerViewPaddingBottom() {
        measureRecyclerViewPaddingDebounceJob?.cancel()
        measureRecyclerViewPaddingDebounceJob = GlobalScope.launch(Dispatchers.Main) {
            delay(500)
            if (rvMiniCartList?.canScrollVertically(-1) == true || rvMiniCartList?.canScrollVertically(1) == true) {
                rvMiniCartList?.setPadding(0, 0, 0, rvMiniCartList?.resources?.getDimensionPixelOffset(R.dimen.dp_64)
                        ?: 0)
            } else {
                rvMiniCartList?.setPadding(0, 0, 0, 0)
            }
        }
    }

    private fun cancelAllDebounceJob() {
        measureRecyclerViewPaddingDebounceJob?.cancel()
        updateCartDebounceJob?.cancel()
        calculationDebounceJob?.cancel()
    }

    private fun setTotalAmountLoading(isLoading: Boolean) {
        if (isLoading) {
            if (totalAmount?.isTotalAmountLoading == false) {
                totalAmount?.isTotalAmountLoading = true
            }
        } else {
            if (totalAmount?.isTotalAmountLoading == true) {
                totalAmount?.isTotalAmountLoading = false
            }
        }
        validateTotalAmountView()
    }

    private fun validateTotalAmountView() {
        totalAmount?.context?.let { context ->
            val chatIcon = getIconUnifyDrawable(context, IconUnify.CHAT, ContextCompat.getColor(context, R.color.Unify_GN500))
            totalAmount?.setAdditionalButton(chatIcon)
            totalAmount?.totalAmountAdditionalButton?.setOnClickListener {
                analytics.eventClickChatOnMiniCart()
                val shopId = viewModel?.currentShopIds?.value?.firstOrNull() ?: "0"
                val intent = RouteManager.getIntent(
                        context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId
                )
                context.startActivity(intent)
            }
            this.chatIcon?.setImageDrawable(chatIcon)
            totalAmount?.amountChevronView?.setOnClickListener(miniCartChevronClickListener)
        }
    }

    private fun updateTotalAmount(miniCartWidgetData: MiniCartWidgetData) {
        totalAmount?.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_total_price))
            if (miniCartWidgetData.totalProductCount == 0) {
                setAmount("-")
                setCtaText(context.getString(R.string.mini_cart_widget_label_buy_empty))
                amountCtaView.isEnabled = false
                enableAmountChevron(false)
            } else {
                setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartWidgetData.totalProductPrice, false))
                setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy), miniCartWidgetData.totalProductCount))
                amountCtaView.isEnabled = true
                enableAmountChevron(true)
            }
        }
        setTotalAmountLoading(false)
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
            viewModel?.updateCart(isForCheckout = false, observer = GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET)
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

    override fun onCheckoutInBrowserRedirectionClicked(url: String, element: MiniCartProductUiModel, action: Action) {
        bottomSheet?.context?.let {
            showProgressLoading()
            val localCacheHandler = LocalCacheHandler(it, TkpdCache.ADVERTISINGID)
            val adsId = localCacheHandler.getString(TkpdCache.Key.KEY_ADVERTISINGID)
            if (adsId != null && adsId.trim { it <= ' ' }.isNotEmpty()) {
                val newUrl = url.replace(QUERY_APP_CLIENT_ID, adsId)
                viewModel?.generateSeamlessUrl(newUrl, ::onGenerateUrlSuccess, ::onGenerateUrlError)
            } else {
                hideProgressLoading()
            }
        }
    }

    private fun onGenerateUrlSuccess(url: String) {
        hideProgressLoading()
        bottomSheet?.context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun onGenerateUrlError(messageError: String) {
        hideProgressLoading()
        bottomsheetContainer?.let { view ->
            bottomSheetListener?.showToaster(view, messageError, Toaster.TYPE_ERROR)
        }
    }

    override fun onShowUnavailableItemsCLicked() {
        val data = adapter?.data ?: emptyList()
        loop@ for ((index, visitable) in data.withIndex()) {
            if (visitable is MiniCartProductUiModel && visitable.isProductDisabled) {
                rvMiniCartList?.smoothScrollToPosition(index)
                break@loop
            }
        }
    }

    override fun onToggleShowHideUnavailableItemsClicked() {
        viewModel?.toggleUnavailableItemsAccordion()
        val lastItemPosition = (adapter?.list?.size ?: 0) - 1
        if (lastItemPosition != -1) {
            rvMiniCartList?.smoothScrollToPosition(lastItemPosition)
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

    override fun onShowUnavailableItem(element: MiniCartProductUiModel) {
        analytics.eventViewTickerErrorUnavailableProduct(element.errorType)
    }

}