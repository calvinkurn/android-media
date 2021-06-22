package com.tokopedia.minicart.common.widget

import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheet
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheetListener
import com.tokopedia.minicart.cartlist.subpage.GlobalErrorBottomSheet
import com.tokopedia.minicart.cartlist.subpage.globalerror.GlobalErrorBottomSheetActionListener
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.updatecart.Data
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.RemoveFromCartDomainModel
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.totalamount.TotalAmount
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class MiniCartWidget @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr), MiniCartListBottomSheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var miniCartListBottomSheet: MiniCartListBottomSheet

    @Inject
    lateinit var globalErrorBottomSheet: GlobalErrorBottomSheet

    @Inject
    lateinit var analytics: MiniCartAnalytics

    private var view: View? = null
    private var totalAmount: TotalAmount? = null
    private var chatIcon: ImageUnify? = null
    private var labelUnavailable: Typography? = null
    private var imageChevronUnavailable: ImageUnify? = null
    private var miniCartWidgetListener: MiniCartWidgetListener? = null
    private var progressDialog: AlertDialog? = null
    private var miniCartChevronClickListener: OnClickListener? = null

    private var viewModel: MiniCartViewModel? = null

    init {
        view = inflate(context, R.layout.widget_mini_cart, this)
        totalAmount = view?.findViewById(R.id.mini_cart_total_amount)
        chatIcon = view?.findViewById(R.id.chat_icon)
        labelUnavailable = view?.findViewById(R.id.label_unavailable)
        imageChevronUnavailable = view?.findViewById(R.id.image_chevron_unavailable)
    }

    /*
    * Function to initialize the widget
    * */
    fun initialize(shopIds: List<String>, fragment: Fragment, listener: MiniCartWidgetListener, autoInitializeData: Boolean = true, pageName: MiniCartAnalytics.Page) {
        if (viewModel == null) {
            val application = fragment.activity?.application
            initializeInjector(application)
            initializeView(fragment)
            initializeListener(listener)
            initializeViewModel(fragment)
            viewModel?.initializeCurrentPage(pageName)
            if (autoInitializeData) {
                updateData(shopIds)
            } else {
                viewModel?.initializeShopIds(shopIds)
            }
        } else {
            updateData(shopIds)
        }
    }

    private fun initializeListener(listener: MiniCartWidgetListener) {
        miniCartWidgetListener = listener
    }

    private fun initializeViewModel(fragment: Fragment) {
        viewModel = ViewModelProvider(fragment, viewModelFactory).get(MiniCartViewModel::class.java)
        viewModel?.initializeGlobalState()
        observeGlobalEvent(fragment)
        observeMiniCartWidgetUiModel(fragment)
    }

    private fun observeGlobalEvent(fragment: Fragment) {
        viewModel?.globalEvent?.observe(fragment.viewLifecycleOwner, {
            when (it.state) {
                GlobalEvent.STATE_SUCCESS_DELETE_CART_ITEM -> {
                    onSuccessDeleteCartItem(it)
                }
                GlobalEvent.STATE_FAILED_LOAD_MINI_CART_LIST_BOTTOM_SHEET -> {
                    onFailedToLoadMiniCartBottomSheet(it, fragment)
                }
                GlobalEvent.STATE_SUCCESS_UPDATE_CART_FOR_CHECKOUT -> {
                    if (it.observer == GlobalEvent.OBSERVER_MINI_CART_WIDGET) {
                        context?.let { context ->
                            hideProgressLoading()
                            onSuccessUpdateCartForCheckout(context)
                        }
                    }
                }
                GlobalEvent.STATE_FAILED_UPDATE_CART_FOR_CHECKOUT -> {
                    if (it.observer == GlobalEvent.OBSERVER_MINI_CART_WIDGET) {
                        hideProgressLoading()
                        onFailedUpdateCartForCheckout(it, fragment)
                    }
                }
            }
        })
    }

    private fun onSuccessDeleteCartItem(globalEvent: GlobalEvent) {
        val data = globalEvent.data as? RemoveFromCartDomainModel
        // last item should be handled by mini cart widget, since the bottomsheet already dismissed
        if (data?.isLastItem == false) return

        hideProgressLoading()
        miniCartListBottomSheet.dismiss()
        val message = data?.removeFromCartData?.data?.message?.firstOrNull() ?: ""
        if (message.isNotBlank()) {
            if (data?.isBulkDelete == true) {
                showToaster(
                        message = message,
                        type = Toaster.TYPE_NORMAL
                )
            } else {
                showToaster(
                        message = message,
                        type = Toaster.TYPE_NORMAL
                )
            }
        }
    }

    private fun onFailedUpdateCartForCheckout(globalEvent: GlobalEvent, fragment: Fragment) {
        setTotalAmountLoading(true)
        viewModel?.getLatestWidgetState()
        fragment.context?.let { context ->
            handleFailedUpdateCartForCheckout(view, context, fragment.parentFragmentManager, globalEvent)
        }
    }

    private fun handleFailedUpdateCartForCheckout(view: View?, context: Context, fragmentManager: FragmentManager, globalEvent: GlobalEvent) {
        val data = globalEvent.data
        if (data != null) {
            // Goes here if failed but get response from BE
            handleFailedUpdateCartWithOutOfService(view, data, fragmentManager, context, globalEvent)
        } else {
            // Goes here if failed and get no response from BE
            handleFailedUpdateCartWithThrowable(view, globalEvent, fragmentManager, context)
        }
    }

    private fun handleFailedUpdateCartWithOutOfService(view: View?, data: Any, fragmentManager: FragmentManager, context: Context, globalEvent: GlobalEvent) {
        if (data is Data) {
            if (data.outOfService.id.isNotBlank() && data.outOfService.id != "0") {
                // Prioritize to show out of service data
                analytics.eventClickBuyThenGetBottomSheetError(data.outOfService.description)
                globalErrorBottomSheet.show(fragmentManager, context, GlobalError.SERVER_ERROR, data.outOfService, object : GlobalErrorBottomSheetActionListener {
                    override fun onGoToHome() {
                        RouteManager.route(context, ApplinkConst.HOME)
                    }

                    override fun onRefreshErrorPage() {
                        showProgressLoading()
                        viewModel?.updateCart(true, GlobalEvent.OBSERVER_MINI_CART_WIDGET)
                    }
                })
            } else {
                // Show toaster error if have no out of service data
                analytics.eventClickBuyThenGetToasterError(data.error)
                var ctaText = "Oke"
                if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
                    ctaText = data.toasterAction.text
                }
                if (data.toasterAction.showCta) {
                    showToaster(view, data.error, Toaster.TYPE_ERROR, ctaText, isShowCta = true)
                } else {
                    showToaster(view, data.error, Toaster.TYPE_ERROR, isShowCta = false)
                }
            }
        }
    }

    private fun handleFailedUpdateCartWithThrowable(view: View?, globalEvent: GlobalEvent, fragmentManager: FragmentManager, context: Context) {
        val throwable = globalEvent.throwable
        if (throwable != null) {
            when (throwable) {
                is UnknownHostException -> {
                    globalErrorBottomSheet.show(fragmentManager, context, GlobalError.NO_CONNECTION, null, object : GlobalErrorBottomSheetActionListener {
                        override fun onGoToHome() {
                            // No-op
                        }

                        override fun onRefreshErrorPage() {
                            showProgressLoading()
                            viewModel?.updateCart(true, GlobalEvent.OBSERVER_MINI_CART_WIDGET)
                        }
                    })
                }
                is SocketTimeoutException -> {
                    val message = context.getString(R.string.mini_cart_message_error_checkout_timeout)
                    showToaster(view, message, Toaster.TYPE_ERROR)
                }
                else -> {
                    val message = context.getString(R.string.mini_cart_message_error_checkout_failed)
                    showToaster(view, message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun onSuccessUpdateCartForCheckout(context: Context) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
        context.startActivity(intent)
    }

    private fun onFailedToLoadMiniCartBottomSheet(globalEvent: GlobalEvent, fragment: Fragment) {
        miniCartListBottomSheet.dismiss()
        if (globalEvent.data != null && globalEvent.data is MiniCartData) {
            val outOfService = (globalEvent.data as MiniCartData).data.outOfService
            if (outOfService.id.isNotBlank() && outOfService.id != "0") {
                fragment.context?.let {
                    globalErrorBottomSheet.show(fragment.parentFragmentManager, it, GlobalError.SERVER_ERROR, outOfService, object : GlobalErrorBottomSheetActionListener {
                        override fun onGoToHome() {
                            // No-op
                        }

                        override fun onRefreshErrorPage() {
                            showMiniCartListBottomSheet(fragment)
                        }
                    })
                }
            } else {
                showGlobalErrorNoConnection(fragment)
            }
        } else {
            showGlobalErrorNoConnection(fragment)
        }
    }

    private fun showGlobalErrorNoConnection(fragment: Fragment) {
        fragment.context?.let {
            globalErrorBottomSheet.show(fragment.parentFragmentManager, it, GlobalError.NO_CONNECTION, null, object : GlobalErrorBottomSheetActionListener {
                override fun onGoToHome() {
                    // No-op
                }

                override fun onRefreshErrorPage() {
                    showMiniCartListBottomSheet(fragment)
                }
            })
        }
    }

    private fun observeMiniCartWidgetUiModel(fragment: Fragment) {
        viewModel?.miniCartSimplifiedData?.observe(fragment.viewLifecycleOwner, {
            renderWidget(it)
        })
    }

    private fun initializeView(fragment: Fragment) {
        totalAmount?.let {
            it.enableAmountChevron(true)
            miniCartChevronClickListener = OnClickListener {
                analytics.eventClickChevronToShowMiniCartBottomSheet()
                showMiniCartListBottomSheet(fragment)
            }
            it.amountChevronView.setOnClickListener(miniCartChevronClickListener)
            it.amountCtaView.setOnClickListener {
                sendEventClickBuy()
                showProgressLoading()
                viewModel?.updateCart(true, GlobalEvent.OBSERVER_MINI_CART_WIDGET)
            }
        }
        imageChevronUnavailable?.setOnClickListener(miniCartChevronClickListener)
        validateTotalAmountView()
        initializeProgressDialog(fragment.context)
    }

    private fun sendEventClickBuy() {
        val pageName = viewModel?.currentPage?.value ?: MiniCartAnalytics.Page.HOME_PAGE
        val products = viewModel?.miniCartSimplifiedData?.value?.miniCartItems ?: emptyList()
        analytics.eventClickBuy(pageName, products)
    }

    private fun initializeProgressDialog(context: Context?) {
        context?.let {
            progressDialog = AlertDialog.Builder(it)
                    .setView(R.layout.mini_cart_progress_dialog_view)
                    .setCancelable(true)
                    .create()
        }
    }

    private fun showMiniCartListBottomSheet(fragment: Fragment) {
        viewModel?.let {
            miniCartListBottomSheet.show(fragment.context, fragment.parentFragmentManager, fragment.viewLifecycleOwner, it, this)
        }
    }

    override fun showToaster(view: View?, message: String, type: Int, ctaText: String, isShowCta: Boolean, onClickListener: OnClickListener?) {
        if (message.isBlank()) return

        var toasterViewRoot = view
        if (toasterViewRoot == null) toasterViewRoot = this.view
        toasterViewRoot?.let {
            Toaster.toasterCustomBottomHeight = it.resources?.getDimensionPixelSize(R.dimen.dp_72)
                    ?: 0
            if (isShowCta && ctaText.isNotBlank()) {
                var tmpCtaClickListener = OnClickListener { }
                if (onClickListener != null) {
                    tmpCtaClickListener = onClickListener
                }
                Toaster.build(it, message, Toaster.LENGTH_LONG, type, ctaText, tmpCtaClickListener).show()
            } else {
                Toaster.build(it, message, Toaster.LENGTH_LONG, type).show()
            }
        }
    }

    override fun showProgressLoading() {
        if (progressDialog?.isShowing == false) {
            progressDialog?.show()
        }
    }

    override fun hideProgressLoading() {
        if (progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
        }
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger view model to fetch latest data from backend and update the UI
    * */
    fun updateData(shopIds: List<String>) {
        setTotalAmountLoading(true)
        viewModel?.getLatestWidgetState(shopIds)
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger widget to update the UI with provided data
    * */
    fun updateData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        setTotalAmountLoading(true)
        viewModel?.updateMiniCartSimplifiedData(miniCartSimplifiedData)
    }

    private fun initializeInjector(baseAppComponent: Application?) {
        if (baseAppComponent is BaseMainApplication) {
            DaggerMiniCartWidgetComponent.builder()
                    .baseAppComponent(baseAppComponent.baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    private fun renderWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (miniCartSimplifiedData.miniCartWidgetData.containsOnlyUnavailableItems) {
            totalAmount?.apply {
                setLabelTitle("")
                setAmount("")
                setCtaText(context.getString(R.string.mini_cart_widget_label_buy_empty))
                amountCtaView.isEnabled = false
            }
            labelUnavailable?.apply {
                text = context.getString(R.string.mini_cart_widget_label_unavailable, miniCartSimplifiedData.miniCartWidgetData.unavailableItemsCount)
                show()
            }
            imageChevronUnavailable?.show()
        } else {
            totalAmount?.apply {
                setLabelTitle(context.getString(R.string.mini_cart_widget_label_see_cart))
                setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartSimplifiedData.miniCartWidgetData.totalProductPrice, false))
                setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy), miniCartSimplifiedData.miniCartWidgetData.totalProductCount))
                amountCtaView.isEnabled = true
            }
            labelUnavailable?.gone()
            imageChevronUnavailable?.gone()
        }
        setTotalAmountLoading(false)
        setAmountViewLayoutParams()
    }

    private fun setAmountViewLayoutParams() {
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.weight = 0f
        lp.setMargins(0, 0, 4.toPx(), 0)
        totalAmount?.amountView?.layoutParams = lp
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
            totalAmount?.enableAmountChevron(true)
            totalAmount?.amountChevronView?.setOnClickListener(miniCartChevronClickListener)
        }
    }

    override fun onMiniCartListBottomSheetDismissed() {
        viewModel?.getLatestMiniCartData()?.let {
            updateData(it)
            miniCartWidgetListener?.onCartItemsUpdated(it)
        }
        viewModel?.resetTemporaryHiddenUnavailableItems()
    }

    override fun onBottomSheetSuccessUpdateCartForCheckout() {
        context?.let {
            onSuccessUpdateCartForCheckout(it)
        }
    }

    override fun onBottomSheetFailedUpdateCartForCheckout(toasterAnchorView: View, fragmentManager: FragmentManager, globalEvent: GlobalEvent) {
        context?.let {
            handleFailedUpdateCartForCheckout(toasterAnchorView, it, fragmentManager, globalEvent)
        }
    }

}