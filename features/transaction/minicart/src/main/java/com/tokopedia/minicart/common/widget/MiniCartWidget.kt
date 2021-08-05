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
import com.tokopedia.cartcommon.domain.data.RemoveFromCartDomainModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheet
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheetListener
import com.tokopedia.minicart.cartlist.subpage.globalerror.GlobalErrorBottomSheet
import com.tokopedia.minicart.cartlist.subpage.globalerror.GlobalErrorBottomSheetActionListener
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartCheckoutData
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
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
    private var textCannotProcess: Typography? = null
    private var textCannotProcessQuantity: Typography? = null
    private var imageChevronUnavailable: ImageUnify? = null
    private var miniCartWidgetListener: MiniCartWidgetListener? = null
    private var progressDialog: AlertDialog? = null
    private var miniCartChevronClickListener: OnClickListener? = null

    private var viewModel: MiniCartViewModel? = null

    init {
        view = inflate(context, R.layout.widget_mini_cart, this)
        totalAmount = view?.findViewById(R.id.mini_cart_total_amount)
        chatIcon = view?.findViewById(R.id.chat_icon)
        textCannotProcess = view?.findViewById(R.id.text_cannot_process)
        textCannotProcessQuantity = view?.findViewById(R.id.text_cannot_process_quantity)
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
                GlobalEvent.STATE_SUCCESS_TO_CHECKOUT -> {
                    if (it.observer == GlobalEvent.OBSERVER_MINI_CART_WIDGET) {
                        context?.let { context ->
                            hideProgressLoading()
                            onSuccessGoToCheckout(context)
                        }
                    }
                }
                GlobalEvent.STATE_FAILED_TO_CHECKOUT -> {
                    if (it.observer == GlobalEvent.OBSERVER_MINI_CART_WIDGET) {
                        onFailedGoToCheckout(it, fragment)
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

    private fun onFailedGoToCheckout(globalEvent: GlobalEvent, fragment: Fragment) {
        hideProgressLoading()
        setTotalAmountLoading(true)
        fragment.context?.let { context ->
            handleFailedGoToCheckout(view, context, fragment.parentFragmentManager, globalEvent)
        }
    }

    private fun handleFailedGoToCheckout(view: View?, context: Context, fragmentManager: FragmentManager, globalEvent: GlobalEvent) {
        val data = globalEvent.data
        if (data != null && data is MiniCartCheckoutData) {
            // Goes here if failed and get response from BE
            handleFailedGoToCheckoutWithData(view, data, fragmentManager, context, globalEvent)
        } else {
            // Goes here if failed and get no response from BE
            handleFailedGoToCheckoutWithThrowable(view, globalEvent, fragmentManager, context)
        }
    }

    private fun handleFailedGoToCheckoutWithData(view: View?, miniCartCheckoutData: MiniCartCheckoutData, fragmentManager: FragmentManager, context: Context, globalEvent: GlobalEvent) {
        if (miniCartCheckoutData.outOfService.id.isNotBlank() && miniCartCheckoutData.outOfService.id != "0") {
            // Prioritize to show out of service data
            globalErrorBottomSheet.show(fragmentManager, context, GlobalError.SERVER_ERROR, miniCartCheckoutData.outOfService, object : GlobalErrorBottomSheetActionListener {
                override fun onGoToHome() {
                    RouteManager.route(context, ApplinkConst.HOME)
                }

                override fun onRefreshErrorPage() {
                    showProgressLoading()
                    viewModel?.goToCheckout(globalEvent.observer)
                }
            })
            val isOCCFlow = viewModel?.isOCCFlow?.value ?: false
            analytics.eventClickBuyThenGetBottomSheetError(miniCartCheckoutData.outOfService.description, isOCCFlow)
        } else {
            // Reload data
            if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_WIDGET) {
                viewModel?.getLatestWidgetState()
            } else if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
                viewModel?.getCartList()
            }

            // Show toaster error if have no out of service data
            var ctaText = context.getString(R.string.mini_cart_cta_ok)
            if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
                ctaText = miniCartCheckoutData.toasterAction.text
            }
            val errorMessage = miniCartCheckoutData.errorMessage ?: ""
            if (miniCartCheckoutData.toasterAction.showCta) {
                showToaster(view, errorMessage, Toaster.TYPE_ERROR, ctaText) {
                    if (globalEvent.observer == GlobalEvent.OBSERVER_MINI_CART_LIST_BOTTOM_SHEET) {
                        miniCartListBottomSheet.scrollToUnavailableSection()
                    }
                    analytics.eventClickAtcToasterErrorCta(errorMessage, ctaText)
                }
            } else {
                showToaster(view, errorMessage, Toaster.TYPE_ERROR, isShowCta = false)
            }
            val isOCCFlow = viewModel?.isOCCFlow?.value ?: false
            analytics.eventClickBuyThenGetToasterError(errorMessage, isOCCFlow)
        }
    }

    private fun handleFailedGoToCheckoutWithThrowable(view: View?, globalEvent: GlobalEvent, fragmentManager: FragmentManager, context: Context) {
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
                            viewModel?.goToCheckout(globalEvent.observer)
                        }
                    })
                    val isOCCFlow = viewModel?.isOCCFlow?.value ?: false
                    analytics.eventClickBuyThenGetBottomSheetError(context.getString(com.tokopedia.globalerror.R.string.noConnectionTitle), isOCCFlow)
                }
                is SocketTimeoutException -> {
                    val message = context.getString(R.string.mini_cart_message_error_checkout_timeout)
                    val ctaText = context.getString(R.string.mini_cart_cta_ok)
                    showToaster(view, message, Toaster.TYPE_ERROR, ctaText) {
                        analytics.eventClickAtcToasterErrorCta(message, ctaText)
                    }
                    val isOCCFlow = viewModel?.isOCCFlow?.value ?: false
                    analytics.eventClickBuyThenGetToasterError(message, isOCCFlow)
                }
                else -> {
                    val message = context.getString(R.string.mini_cart_message_error_checkout_failed)
                    val ctaText = context.getString(R.string.mini_cart_cta_ok)
                    showToaster(view, message, Toaster.TYPE_ERROR, ctaText) {
                        analytics.eventClickAtcToasterErrorCta(message, ctaText)
                    }
                    val isOCCFlow = viewModel?.isOCCFlow?.value ?: false
                    analytics.eventClickBuyThenGetToasterError(message, isOCCFlow)
                }
            }
        }
    }

    private fun onSuccessGoToCheckout(context: Context) {
        val intent = if (viewModel?.isOCCFlow?.value == true) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
        } else {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
        }

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
                            RouteManager.route(context, ApplinkConst.HOME)
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
                viewModel?.goToCheckout(GlobalEvent.OBSERVER_MINI_CART_WIDGET)
            }
        }
        imageChevronUnavailable?.setOnClickListener(miniCartChevronClickListener)
        validateTotalAmountView()
        initializeProgressDialog(fragment.context)
    }

    private fun sendEventClickBuy() {
        val pageName = viewModel?.currentPage?.value ?: MiniCartAnalytics.Page.HOME_PAGE
        val products = viewModel?.miniCartSimplifiedData?.value?.miniCartItems ?: emptyList()
        val isOCCFlow = viewModel?.isOCCFlow?.value ?: false
        analytics.eventClickBuy(pageName, products, isOCCFlow)
    }

    private fun initializeProgressDialog(context: Context?) {
        context?.let {
            progressDialog = AlertDialog.Builder(it)
                    .setView(R.layout.mini_cart_progress_dialog_view)
                    .setCancelable(true)
                    .create()
        }
    }

    /*
    * Function to show mini cart bottom sheet
    * */
    fun showMiniCartListBottomSheet(fragment: Fragment) {
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
            renderUnavailableWidget(miniCartSimplifiedData)
        } else {
            renderAvailableWidget(miniCartSimplifiedData)
        }
        setTotalAmountLoading(false)
        setAmountViewLayoutParams()
        validateAmountCtaLabel(miniCartSimplifiedData)
    }

    private fun validateAmountCtaLabel(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (viewModel?.isOCCFlow?.value == true) {
            // Change button from `Beli Langsung` to `Beli` if ellipsis
            totalAmount?.post {
                val ellipsis = totalAmount?.amountCtaView?.layout?.getEllipsisCount(0) ?: 0
                if (ellipsis > 0) {
                    if (miniCartSimplifiedData.miniCartWidgetData.containsOnlyUnavailableItems) {
                        totalAmount?.setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy_empty)))
                    } else {
                        totalAmount?.setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy), miniCartSimplifiedData.miniCartWidgetData.totalProductCount))
                    }
                }
            }
        }
    }

    private fun renderAvailableWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        totalAmount?.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_see_cart))
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartSimplifiedData.miniCartWidgetData.totalProductPrice, false))
            if (viewModel?.isOCCFlow?.value == true) {
                setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy_occ), miniCartSimplifiedData.miniCartWidgetData.totalProductCount))
            } else {
                setCtaText(String.format(context.getString(R.string.mini_cart_widget_label_buy), miniCartSimplifiedData.miniCartWidgetData.totalProductCount))
            }
            amountCtaView.isEnabled = true
            amountCtaView.layoutParams.width = resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
        }
        textCannotProcess?.gone()
        textCannotProcessQuantity?.gone()
        imageChevronUnavailable?.gone()
    }

    private fun renderUnavailableWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        totalAmount?.apply {
            setLabelTitle("")
            setAmount("")
            if (viewModel?.isOCCFlow?.value == true) {
                setCtaText(context.getString(R.string.mini_cart_widget_label_buy_occ_empty))
            } else {
                setCtaText(context.getString(R.string.mini_cart_widget_label_buy_empty))
            }
            amountCtaView.isEnabled = false
            amountCtaView.layoutParams.width = resources.getDimensionPixelSize(R.dimen.mini_cart_button_buy_width)
            amountCtaView.requestLayout()
        }
        textCannotProcess?.apply {
            text = context.getString(R.string.mini_cart_label_cannot_process)
            show()
        }
        textCannotProcessQuantity?.apply {
            text = context.getString(R.string.mini_cart_cannot_process_quantity, miniCartSimplifiedData.miniCartWidgetData.unavailableItemsCount)
            show()
        }
        imageChevronUnavailable?.show()
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
            val chatIcon = getIconUnifyDrawable(context, IconUnify.CHAT, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
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

    override fun onBottomSheetSuccessGoToCheckout() {
        context?.let {
            onSuccessGoToCheckout(it)
        }
    }

    override fun onBottomSheetFailedGoToCheckout(toasterAnchorView: View, fragmentManager: FragmentManager, globalEvent: GlobalEvent) {
        context?.let {
            handleFailedGoToCheckout(toasterAnchorView, it, fragmentManager, globalEvent)
        }
    }

}