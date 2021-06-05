package com.tokopedia.minicart.cartlist

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapter
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.common.data.response.deletecart.RemoveFromCartData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.viewmodel.MiniCartWidgetViewModel
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
                                                  var globalErrorBottomSheet: GlobalErrorBottomSheet)
    : MiniCartListActionListener {

    private var viewModel: MiniCartWidgetViewModel? = null
    private var bottomsheetContainer: CoordinatorLayout? = null
    private var bottomSheet: BottomSheetUnify? = null
    private var totalAmount: TotalAmount? = null
    private var chatIcon: ImageUnify? = null
    private var rvMiniCartList: RecyclerView? = null
    private var adapter: MiniCartListAdapter? = null
    private var progressDialog: AlertDialog? = null

    private var measureRecyclerViewPaddingDebounceJob: Job? = null
    private var updateCartDebounceJob: Job? = null
    private var calculationDebounceJob: Job? = null

    fun show(context: Context?,
             fragmentManager: FragmentManager,
             lifecycleOwner: LifecycleOwner,
             viewModel: MiniCartWidgetViewModel,
             bottomSheetListener: MiniCartListBottomSheetListener) {
        context?.let {
            initializeView(it, fragmentManager, bottomSheetListener)
            initializeViewModel(fragmentManager, viewModel, lifecycleOwner, bottomSheetListener)
            initializeCartData(viewModel)
        }
    }

    fun dismiss() {
        bottomSheet?.dismiss()
    }

    fun initializeViewModel(fragmentManager: FragmentManager, viewModel: MiniCartWidgetViewModel, lifecycleOwner: LifecycleOwner, bottomSheetListener: MiniCartListBottomSheetListener) {
        this.viewModel = viewModel
        observeGlobalEvent(fragmentManager, viewModel, lifecycleOwner, bottomSheetListener)
        observeMiniCartListUiModel(viewModel, lifecycleOwner)
    }

    private fun observeGlobalEvent(fragmentManager: FragmentManager, viewModel: MiniCartWidgetViewModel, lifecycleOwner: LifecycleOwner, bottomSheetListener: MiniCartListBottomSheetListener) {
        viewModel.globalEvent.observe(lifecycleOwner, {
            when (it.state) {
                GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM -> {
                    hideProgressLoading()
                    val data = it.data as? RemoveFromCartData
                    val message = data?.data?.message?.firstOrNull() ?: ""
                    if (message.isNotBlank()) {
                        val ctaText = bottomSheet?.context?.getString(R.string.mini_cart_label_cancel)
                                ?: ""
                        viewModel.getCartList()
                        bottomsheetContainer?.let { view ->
                            bottomSheetListener.showToaster(view, message, Toaster.TYPE_NORMAL, ctaText) {
                                showProgressLoading()
                                viewModel.undoDeleteCartItems()
                            }
                        }
                    }
                }
                GlobalEvent.STATE_FAILED_DELETE_CART_ITEM -> {
                    hideProgressLoading()
                    it.throwable?.let { throwable ->
                        bottomSheet?.context?.let { context ->
                            var message = ErrorHandler.getErrorMessage(context, throwable)
                            if (throwable is ResponseErrorException) {
                                message = throwable.message
                            }
                            bottomsheetContainer?.let { view ->
                                bottomSheetListener.showToaster(view, message, Toaster.TYPE_ERROR)
                            }
                        }
                    }
                }
                GlobalEvent.STATE_SUCCESS_UNDO_DELETE_CART_ITEM -> {
                    hideProgressLoading()
                }
                GlobalEvent.STATE_FAILED_UNDO_DELETE_CART_ITEM -> {
                    hideProgressLoading()
                }
                GlobalEvent.STATE_SUCCESS_UPDATE_CART_FOR_CHECKOUT -> {
                    if (it.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
                        bottomSheet?.context.let {
                            hideProgressLoading()
                            bottomSheetListener.onBottomSheetSuccessUpdateCartForCheckout()
                            bottomSheet?.dismiss()
                        }
                    }
                }
                GlobalEvent.STATE_FAILED_UPDATE_CART_FOR_CHECKOUT -> {
                    if (it.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
                        hideProgressLoading()
                        setTotalAmountLoading(true)
                        viewModel.getCartList()
                        bottomSheet?.context?.let { context ->
                            bottomsheetContainer?.let { view ->
                                bottomSheetListener.onBottomSheetFailedUpdateCartForCheckout(view, fragmentManager, it)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun observeMiniCartListUiModel(viewModel: MiniCartWidgetViewModel, lifecycleOwner: LifecycleOwner) {
        viewModel.miniCartListListBottomSheetUiModel.observe(lifecycleOwner, {
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
        })
    }

    private fun initializeCartData(viewModel: MiniCartWidgetViewModel) {
        showLoading()
        viewModel.getCartList(true)
    }

    private fun initializeView(context: Context, fragmentManager: FragmentManager, bottomSheetListener: MiniCartListBottomSheetListener) {
        context.let {
            val view = View.inflate(it, R.layout.layout_bottomsheet_mini_cart_list, null)
            initializeBottomSheet(view, fragmentManager, bottomSheetListener)
            initializeProgressDialog(it)
            initializeTotalAmount(view, fragmentManager, context)
            initializeRecyclerView(view)
        }
    }

    private fun initializeBottomSheet(view: View, fragmentManager: FragmentManager, bottomSheetListener: MiniCartListBottomSheetListener) {
        bottomsheetContainer = view.findViewById(R.id.bottomsheet_container)
        bottomSheet = BottomSheetUnify().apply {
            showCloseIcon = false
            showHeader = true
            isDragable = true
            showKnob = true
            isHideable = true
            clearContentPadding = true
            customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
            setOnDismissListener {
                cancelAllDebounceJob()
                bottomSheetListener.onMiniCartListBottomSheetDismissed()
            }
            setChild(view)
            show(fragmentManager, this.javaClass.simpleName)
        }
    }

    private fun cancelAllDebounceJob() {
        measureRecyclerViewPaddingDebounceJob?.cancel()
        updateCartDebounceJob?.cancel()
        calculationDebounceJob?.cancel()
    }

    private fun initializeProgressDialog(context: Context) {
        progressDialog = AlertDialog.Builder(context)
                .setView(R.layout.mini_cart_progress_dialog_view)
                .setCancelable(true)
                .create()
    }

    private fun initializeRecyclerView(view: View) {
        rvMiniCartList = view.findViewById(R.id.rv_mini_cart_list)
        val adapterTypeFactory = MiniCartListAdapterTypeFactory(this)
        adapter = MiniCartListAdapter(adapterTypeFactory)
        rvMiniCartList?.adapter = adapter
        rvMiniCartList?.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        rvMiniCartList?.addItemDecoration(miniCartListDecoration)
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

    private fun initializeTotalAmount(view: View, fragmentManager: FragmentManager, context: Context) {
        totalAmount = view.findViewById(R.id.total_amount)
        chatIcon = view.findViewById(R.id.chat_icon)
        totalAmount?.let {
            it.amountChevronView.setOnClickListener {
                viewModel?.miniCartListListBottomSheetUiModel?.value?.miniCartSummaryTransactionUiModel?.let {
                    summaryTransactionBottomSheet.show(it, fragmentManager, context)
                }
            }
            it.amountCtaView.setOnClickListener {
                showProgressLoading()
                viewModel?.updateCart(true, GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET)
            }
            it.enableAmountChevron(true)
            setTotalAmountChatIcon()
            setTotalAmountLoading(true)
        }
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
        setTotalAmountChatIcon()
    }

    private fun setTotalAmountChatIcon() {
        totalAmount?.context?.let { context ->
            val chatIcon = getIconUnifyDrawable(context, IconUnify.CHAT, ContextCompat.getColor(context, R.color.Unify_G500))
            totalAmount?.setAdditionalButton(chatIcon)
            totalAmount?.totalAmountAdditionalButton?.setOnClickListener {
                val shopId = viewModel?.currentShopIds?.value?.firstOrNull() ?: "0"
                val intent = RouteManager.getIntent(
                        context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId
                )
                context.startActivity(intent)
            }
            this.chatIcon?.setImageDrawable(chatIcon)
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
        if (progressDialog?.isShowing == false) {
            progressDialog?.show()
        }
    }

    private fun hideProgressLoading() {
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
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
        showProgressLoading()
        viewModel?.singleDeleteCartItems(element)
    }

    override fun onBulkDeleteUnavailableItems() {
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

    override fun onShowSimilarProductClicked(appLink: String) {
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
        val lastItemPosition = adapter?.list?.size ?: 0 - 1
        if (lastItemPosition != -1) {
            rvMiniCartList?.smoothScrollToPosition(lastItemPosition)
        }
    }

}