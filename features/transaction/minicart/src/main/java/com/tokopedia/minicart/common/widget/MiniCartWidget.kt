package com.tokopedia.minicart.common.widget

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
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
import com.tokopedia.minicart.chatlist.MiniCartChatListBottomSheet
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.config.MiniCartRemoteConfig
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartCheckoutData
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.di.DaggerMiniCartWidgetComponent
import com.tokopedia.oldminicart.common.widget.MiniCartWidget
import com.tokopedia.oldminicart.common.widget.MiniCartWidgetMapper.mapToOldMiniCartData
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
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
    lateinit var miniCartChatListBottomSheet: MiniCartChatListBottomSheet

    @Inject
    lateinit var globalErrorBottomSheet: GlobalErrorBottomSheet

    @Inject
    lateinit var remoteConfig: MiniCartRemoteConfig

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
    private var coachMark: CoachMark2? = null

    private var viewModel: MiniCartViewModel? = null
    private var oldMiniCart: MiniCartWidget? = null

    init {
        view = LayoutInflater.from(context).inflate(R.layout.widget_mini_cart, this, false)
        totalAmount = view?.findViewById(R.id.mini_cart_total_amount)
        chatIcon = view?.findViewById(R.id.chat_icon)
        textCannotProcess = view?.findViewById(R.id.text_cannot_process)
        textCannotProcessQuantity = view?.findViewById(R.id.text_cannot_process_quantity)
        imageChevronUnavailable = view?.findViewById(R.id.image_chevron_unavailable)
        val application = (context as? Activity)?.application
        initializeInjector(application)
    }

    /*
    * Function to initialize the widget
    * */
    fun initialize(shopIds: List<String>, fragment: Fragment, listener: MiniCartWidgetListener,
                   autoInitializeData: Boolean = true, pageName: MiniCartAnalytics.Page, source: MiniCartSource) {
        removeAllViews()

        if (remoteConfig.isNewMiniCartEnabled()) {
            if (viewModel == null) {
                initializeView(fragment)
                initializeListener(listener)
                initializeViewModel(fragment)
                viewModel?.initializeCurrentPage(pageName)
                viewModel?.currentSource = source
                if (autoInitializeData) {
                    updateData(shopIds)
                } else {
                    viewModel?.initializeShopIds(shopIds)
                }
            } else {
                updateData(shopIds)
            }
            addView(view)
        } else {
            oldMiniCart = MiniCartWidget(context)
            oldMiniCart?.initialize(shopIds, fragment, listener, autoInitializeData, pageName)
            addView(oldMiniCart)
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
        miniCartChatListBottomSheet.dismiss()
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
            val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
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
            val errorMessage = miniCartCheckoutData.errorMessage
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
            val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
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
                    val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
                    analytics.eventClickBuyThenGetBottomSheetError(context.getString(com.tokopedia.globalerror.R.string.noConnectionTitle), isOCCFlow)
                }
                is SocketTimeoutException -> {
                    val message = context.getString(R.string.mini_cart_message_error_checkout_timeout)
                    val ctaText = context.getString(R.string.mini_cart_cta_ok)
                    showToaster(view, message, Toaster.TYPE_ERROR, ctaText) {
                        analytics.eventClickAtcToasterErrorCta(message, ctaText)
                    }
                    val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
                    analytics.eventClickBuyThenGetToasterError(message, isOCCFlow)
                }
                else -> {
                    val message = context.getString(R.string.mini_cart_message_error_checkout_failed)
                    val ctaText = context.getString(R.string.mini_cart_cta_ok)
                    showToaster(view, message, Toaster.TYPE_ERROR, ctaText) {
                        analytics.eventClickAtcToasterErrorCta(message, ctaText)
                    }
                    val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
                    analytics.eventClickBuyThenGetToasterError(message, isOCCFlow)
                }
            }
        }
    }

    private fun onSuccessGoToCheckout(context: Context) {
        val intent = if (viewModel?.miniCartABTestData?.value?.isOCCFlow == true) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
        } else {
            val pageName = viewModel?.currentPage?.value ?: MiniCartAnalytics.Page.HOME_PAGE
            val pageSource = when (pageName) {
                MiniCartAnalytics.Page.HOME_PAGE -> "$MINICART_PAGE_SOURCE - homepage"
                MiniCartAnalytics.Page.SEARCH_PAGE -> "$MINICART_PAGE_SOURCE - search result"
                MiniCartAnalytics.Page.CATEGORY_PAGE -> "$MINICART_PAGE_SOURCE category page"
                MiniCartAnalytics.Page.DISCOVERY_PAGE -> "$MINICART_PAGE_SOURCE discovery page"
                MiniCartAnalytics.Page.RECOMMENDATION_INFINITE -> "$MINICART_PAGE_SOURCE recommendation infinite page"
                else -> ""
            }
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
                    .putExtra(CheckoutConstant.EXTRA_CHECKOUT_PAGE_SOURCE, pageSource)
        }

        context.startActivity(intent)
    }

    private fun onFailedToLoadMiniCartBottomSheet(globalEvent: GlobalEvent, fragment: Fragment) {
        miniCartListBottomSheet.dismiss()
        miniCartChatListBottomSheet.dismiss()
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
        initializeChatButton(fragment)
        validateTotalAmountView()
        initializeProgressDialog(fragment.context)
    }

    private fun sendEventClickBuy() {
        val pageName = viewModel?.currentPage?.value ?: MiniCartAnalytics.Page.HOME_PAGE
        val products = viewModel?.miniCartSimplifiedData?.value?.miniCartItems?.values?.toList() ?: emptyList()
        val isOCCFlow = viewModel?.miniCartABTestData?.value?.isOCCFlow ?: false
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
        if(remoteConfig.isNewMiniCartEnabled()) {
            viewModel?.let {
                miniCartListBottomSheet.show(fragment.context, fragment.parentFragmentManager, fragment.viewLifecycleOwner, it, this)
            }
        } else {
            oldMiniCart?.showMiniCartListBottomSheet(fragment)
        }
    }

    /*
    * Function to show mini cart chat bottom sheet
    * */
    private fun showMiniCartChatListBottomSheet(fragment: Fragment) {
        viewModel?.let {
            miniCartChatListBottomSheet.show(fragment.context, fragment.parentFragmentManager, fragment.viewLifecycleOwner, it)
        }
    }

    override fun showToaster(view: View?, message: String, type: Int, ctaText: String, isShowCta: Boolean, onClickListener: OnClickListener?) {
        if(remoteConfig.isNewMiniCartEnabled()) {
            if (message.isBlank()) return
            var toasterViewRoot = view
            if (toasterViewRoot == null) toasterViewRoot = this.view
            toasterViewRoot?.let {
                Toaster.toasterCustomBottomHeight = it.resources?.getDimensionPixelSize(
                    com.tokopedia.abstraction.R.dimen.dp_72
                ) ?: 0
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
        } else {
            oldMiniCart?.showToaster(view, message, type, ctaText, isShowCta, onClickListener)
        }
    }

    override fun showProgressLoading() {
        if (remoteConfig.isNewMiniCartEnabled()) {
            if (progressDialog?.isShowing == false) {
                progressDialog?.show()
            }
        } else {
            oldMiniCart?.showProgressLoading()
        }
    }

    override fun hideProgressLoading() {
        if (remoteConfig.isNewMiniCartEnabled()) {
            if (progressDialog?.isShowing == true) {
                progressDialog?.dismiss()
            }
        } else {
            oldMiniCart?.hideProgressLoading()
        }
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger view model to fetch latest data from backend and update the UI
    * */
    fun updateData(shopIds: List<String>) {
        if(remoteConfig.isNewMiniCartEnabled()) {
            setTotalAmountLoading(true)
            viewModel?.getLatestWidgetState(shopIds)
        } else {
            oldMiniCart?.updateData(shopIds)
        }
    }

    /*
    * Function to trigger update mini cart data
    * This will trigger widget to update the UI with provided data
    * */
    fun updateData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if(remoteConfig.isNewMiniCartEnabled()) {
            setTotalAmountLoading(true)
            viewModel?.setMiniCartABTestData(miniCartSimplifiedData.miniCartWidgetData.isOCCFlow, miniCartSimplifiedData.miniCartWidgetData.buttonBuyWording)
            viewModel?.updateMiniCartSimplifiedData(miniCartSimplifiedData)
        } else {
            val data = mapToOldMiniCartData(miniCartSimplifiedData)
            oldMiniCart?.updateData(data)
        }
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
            showOnBoarding()
        }
        setTotalAmountLoading(false)
        setAmountViewLayoutParams()
        validateAmountCtaLabel(miniCartSimplifiedData)
    }

    private fun validateAmountCtaLabel(miniCartSimplifiedData: MiniCartSimplifiedData) {
        if (viewModel?.miniCartABTestData?.value?.isOCCFlow == true) {
            // Change button from `Beli Langsung` to `Beli` if ellipsis
            totalAmount?.post {
                val ellipsis = totalAmount?.amountCtaView?.layout?.getEllipsisCount(0) ?: 0
                if (ellipsis > 0) {
                    val ctaText = context.getString(R.string.mini_cart_widget_cta_text_default)
                    if (miniCartSimplifiedData.miniCartWidgetData.containsOnlyUnavailableItems) {
                        totalAmount?.setCtaText(ctaText)
                    } else {
                        totalAmount?.setCtaText("$ctaText (${miniCartSimplifiedData.miniCartWidgetData.totalProductCount})")
                    }
                }
            }
        }
    }

    private fun renderAvailableWidget(miniCartSimplifiedData: MiniCartSimplifiedData) {
        totalAmount?.apply {
            setLabelTitle(context.getString(R.string.mini_cart_widget_label_see_cart))
            setAmount(CurrencyFormatUtil.convertPriceValueToIdrFormat(miniCartSimplifiedData.miniCartWidgetData.totalProductPrice, false).removeDecimalSuffix())
            val ctaText = viewModel?.miniCartABTestData?.value?.buttonBuyWording
                    ?: context.getString(R.string.mini_cart_widget_cta_text_default)
            setCtaText("$ctaText (${miniCartSimplifiedData.miniCartWidgetData.totalProductCount})")
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
            val ctaText = viewModel?.miniCartABTestData?.value?.buttonBuyWording
                    ?: context.getString(R.string.mini_cart_widget_cta_text_default)
            setCtaText(ctaText)
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

    private fun initializeChatButton(fragment: Fragment) {
        val chatIcon = getIconUnifyDrawable(context, IconUnify.CHAT, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
        totalAmount?.setAdditionalButton(chatIcon)
        totalAmount?.totalAmountAdditionalButton?.setOnClickListener {
            analytics.eventClickChatOnMiniCart()
            showMiniCartChatListBottomSheet(fragment)
        }
        this.chatIcon?.setImageDrawable(chatIcon)
    }

    private fun validateTotalAmountView() {
        totalAmount?.context?.let { context ->
            totalAmount?.enableAmountChevron(true)
            totalAmount?.amountChevronView?.setOnClickListener(miniCartChevronClickListener)
        }
    }

    override fun onMiniCartListBottomSheetDismissed() {
        if(remoteConfig.isNewMiniCartEnabled()) {
            viewModel?.getLatestMiniCartData()?.let {
                updateData(it)
                miniCartWidgetListener?.onCartItemsUpdated(it)
            }
            viewModel?.resetTemporaryHiddenUnavailableItems()
        } else {
            oldMiniCart?.onMiniCartListBottomSheetDismissed()
        }
    }

    override fun onBottomSheetSuccessGoToCheckout() {
        if(remoteConfig.isNewMiniCartEnabled()) {
            context?.let {
                onSuccessGoToCheckout(it)
            }
        } else {
            oldMiniCart?.onBottomSheetSuccessGoToCheckout()
        }
    }

    override fun onBottomSheetFailedGoToCheckout(toasterAnchorView: View, fragmentManager: FragmentManager, globalEvent: GlobalEvent) {
        if(remoteConfig.isNewMiniCartEnabled()) {
            context?.let {
                handleFailedGoToCheckout(toasterAnchorView, it, fragmentManager, globalEvent)
            }
        } else {
            oldMiniCart?.onBottomSheetFailedGoToCheckout(toasterAnchorView, fragmentManager, globalEvent)
        }
    }

    private fun showOnBoarding() {
        context?.let { context ->
            if (!CoachMarkPreference.hasShown(context, COACH_MARK_TAG)) {
                coachMark = CoachMark2(context)
                this.totalAmount?.labelTitleView?.let { anchor ->
                    coachMark?.let { coachMark2 ->
                        anchor.post {
                            val coachMarkItems: ArrayList<CoachMark2Item> = ArrayList()
                            coachMarkItems.add(
                                CoachMark2Item(
                                    anchor,
                                    context.getString(R.string.mini_cart_coachmark_title),
                                    context.getString(R.string.mini_cart_coachmark_desc),
                                    CoachMark2.POSITION_TOP
                                )
                            )
                            coachMark2.showCoachMark(step = coachMarkItems)
                            CoachMarkPreference.setShown(context, COACH_MARK_TAG, true)
                        }
                    }
                }
            }
        }
    }

    fun hideCoachMark() {
        if(remoteConfig.isNewMiniCartEnabled()) {
            coachMark?.dismissCoachMark()
        } else {
            oldMiniCart?.hideCoachMark()
        }
    }

    companion object {
        private const val COACH_MARK_TAG = "coachmark_tokonow"

        private const val MINICART_PAGE_SOURCE = "minicart - tokonow"
    }

}