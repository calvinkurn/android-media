package com.tokopedia.minicart.cartlist

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.minicart.common.data.response.deletecart.RemoveFromCartData
import com.tokopedia.minicart.common.data.response.undodeletecart.UndoDeleteCartDataResponse
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartWidgetViewModel
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*
import javax.inject.Inject

class MiniCartListBottomSheet @Inject constructor(var miniCartListDecoration: MiniCartListDecoration,
                                                  var summaryTransactionBottomSheet: SummaryTransactionBottomSheet,
                                                  var analytics: MiniCartAnalytics)
    : MiniCartListActionListener {

    private var viewModel: MiniCartWidgetViewModel? = null
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
             viewModel: MiniCartWidgetViewModel,
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
        globalEventObserver?.let {
            viewModel?.globalEvent?.removeObserver(it)
            globalEventObserver = null
        }
        bottomSheetUiModelObserver?.let {
            viewModel?.miniCartListListBottomSheetUiModel?.removeObserver(it)
            bottomSheetUiModelObserver = null
        }
        bottomSheet?.dismiss()
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
                bottomSheetListener?.onMiniCartListBottomSheetDismissed()
            }
            setChild(view)
            show(fragmentManager, this.javaClass.simpleName)
        }
    }

    private fun initializeCartData(viewModel: MiniCartWidgetViewModel) {
        showLoading()
        viewModel.getCartList(true)
    }

    private fun initializeView(context: Context, fragmentManager: FragmentManager) {
        context.let {
            val view = View.inflate(it, R.layout.layout_bottomsheet_mini_cart_list, null)
            initializeBottomSheet(view, fragmentManager)
            initializeTotalAmount(view, fragmentManager, context)
            initializeRecyclerView(view)
        }
    }

    private fun initializeViewModel(fragmentManager: FragmentManager, viewModel: MiniCartWidgetViewModel, lifecycleOwner: LifecycleOwner) {
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
                viewModel?.miniCartListListBottomSheetUiModel?.value?.miniCartSummaryTransactionUiModel?.let {
                    summaryTransactionBottomSheet.show(it, fragmentManager, context)
                }
            }
            it.amountChevronView.setOnClickListener(miniCartChevronClickListener)
            it.amountCtaView.setOnClickListener {
                sendEventClickBuy()
                showProgressLoading()
                viewModel?.updateCart(true, GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET)
            }
            it.enableAmountChevron(true)
            validateTotalAmountView()
            setTotalAmountLoading(true)
        }
    }

    private fun sendEventClickBuy() {
        val pageName = viewModel?.currentPage?.value ?: ""
        val products = viewModel?.miniCartListListBottomSheetUiModel?.value?.getMiniCartProductUiModelList()
                ?: emptyList()
        analytics.eventClickBuy(pageName, products)
    }

    private fun initializeGlobalEventObserver(viewModel: MiniCartWidgetViewModel, fragmentManager: FragmentManager) {
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

    private fun onSuccessUndoDeleteCartItem(globalEvent: GlobalEvent, viewModel: MiniCartWidgetViewModel) {
        hideProgressLoading()
        viewModel.getCartList()
        val data = globalEvent.data as? UndoDeleteCartDataResponse
        val message = data?.data?.message?.firstOrNull() ?: ""
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
            if (it.isFirstLoad) {
                analytics.eventLoadMiniCartBottomSheetSuccess(it.getMiniCartProductUiModelList())
                val overweightData = it.getMiniCartTickerWarningUiModel()
                if (overweightData != null) {
                    analytics.eventViewErrorTickerOverweightInMiniCart(overweightData.warningMessage)
                }
            }
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

    private fun observeGlobalEvent(viewModel: MiniCartWidgetViewModel, lifecycleOwner: LifecycleOwner) {
        globalEventObserver?.let {
            viewModel.globalEvent.observe(lifecycleOwner, it)
        }
    }

    private fun onFailedUpdateCartForCheckout(globalEvent: GlobalEvent, viewModel: MiniCartWidgetViewModel, fragmentManager: FragmentManager) {
        if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
            hideProgressLoading()
            setTotalAmountLoading(true)
            viewModel.getCartList()
            bottomSheet?.context?.let { context ->
                bottomsheetContainer?.let { view ->
                    bottomSheetListener?.onBottomSheetFailedUpdateCartForCheckout(view, fragmentManager, globalEvent)
                }
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

    private fun onSuccessDeleteCartItem(globalEvent: GlobalEvent, viewModel: MiniCartWidgetViewModel) {
        hideProgressLoading()
        val data = globalEvent.data as? RemoveFromCartData
        val message = data?.data?.message?.firstOrNull() ?: ""
        if (message.isNotBlank()) {
            val ctaText = bottomSheet?.context?.getString(R.string.mini_cart_cta_cancel)
                    ?: ""
            viewModel.getCartList()
            bottomsheetContainer?.let { view ->
                bottomSheetListener?.showToaster(view, message, Toaster.TYPE_NORMAL, ctaText) {
                    analytics.eventClickUndoDelete()
                    showProgressLoading()
                    viewModel.undoDeleteCartItems()
                }
            }
        }
    }

    private fun observeMiniCartListUiModel(viewModel: MiniCartWidgetViewModel, lifecycleOwner: LifecycleOwner) {
        bottomSheetUiModelObserver?.let {
            viewModel.miniCartListListBottomSheetUiModel.observe(lifecycleOwner, it)
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
            val chatIcon = getIconUnifyDrawable(context, IconUnify.CHAT, ContextCompat.getColor(context, R.color.Unify_G500))
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
            totalAmount?.enableAmountChevron(true)
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
        viewModel?.singleDeleteCartItems(element)
    }

    override fun onBulkDeleteUnavailableItems() {
        analytics.eventClickDeleteAllUnavailableProduct()
        val unavailableProducts = viewModel?.getUnavailableItems() ?: emptyList()
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
                rvMiniCartList?.smoothScrollToPosition(index)
                break@loop
            }
        }
    }

    override fun onToggleShowHideUnavailableItemsClicked() {
        viewModel?.handleUnavailableItemsAccordion()
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