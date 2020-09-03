package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticdata.data.constant.LogisticConstant
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding
import com.tokopedia.oneclickcheckout.order.di.OrderSummaryPageComponent
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.ErrorCheckoutBottomSheet
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.OccInfoBottomSheet
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.OrderPriceSummaryBottomSheet
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.PreferenceListBottomSheet
import com.tokopedia.oneclickcheckout.order.view.card.OrderInsuranceCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderPreferenceCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderProductCard
import com.tokopedia.oneclickcheckout.order.view.card.OrderTotalPaymentCard
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.creditcard.CreditCardPickerActivity
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.creditcard.CreditCardPickerFragment
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.PromoNotEligibleActionListener
import com.tokopedia.purchase_platform.common.feature.promonoteligible.PromoNotEligibleBottomsheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class OrderSummaryPageFragment : BaseDaggerFragment(), OrderProductCard.OrderProductCardListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var orderSummaryAnalytics: OrderSummaryAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModel: OrderSummaryPageViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[OrderSummaryPageViewModel::class.java]
    }

    private var orderPreference: OrderPreference? = null

    private val swipeRefreshLayout by lazy { view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout) }
    private val globalError by lazy { view?.findViewById<GlobalError>(R.id.global_error) }
    private val mainContent by lazy { view?.findViewById<ConstraintLayout>(R.id.main_content) }

    private val tickerOsp by lazy { view?.findViewById<Ticker>(R.id.ticker_osp) }

    private val onboardingCard by lazy { view?.findViewById<View>(R.id.layout_occ_onboarding) }
    private val btnOnboardingAction by lazy { view?.findViewById<Typography>(R.id.lbl_occ_onboarding_action) }
    private val lblOnboardingMessage by lazy { view?.findViewById<Typography>(R.id.lbl_occ_onboarding_message) }
    private val lblOnboardingHeader by lazy { view?.findViewById<Typography>(R.id.lbl_occ_onboarding_header) }
    private val ivOnboarding by lazy { view?.findViewById<ImageUnify>(R.id.iv_occ_onboarding) }

    private val tvHeader2 by lazy { view?.findViewById<Typography>(R.id.tv_header_2) }
    private val tvHeader3 by lazy { view?.findViewById<Typography>(R.id.tv_header_3) }

    private val tickerPreferenceInfo by lazy { view?.findViewById<Ticker>(R.id.ticker_preference_info) }
    private val emptyPreferenceCard by lazy { view?.findViewById<View>(R.id.empty_preference_card) }
    private val preferenceCard by lazy { view?.findViewById<View>(R.id.preference_card) }

    private val btnPromoCheckout by lazy { view?.findViewById<ButtonPromoCheckoutView>(R.id.btn_promo_checkout) }

    private val imageEmptyProfile by lazy { view?.findViewById<ImageUnify>(R.id.image_empty_profile) }
    private val buttonAturPilihan by lazy { view?.findViewById<UnifyButton>(R.id.button_atur_pilihan) }

    private lateinit var orderProductCard: OrderProductCard
    private lateinit var orderPreferenceCard: OrderPreferenceCard
    private lateinit var orderInsuranceCard: OrderInsuranceCard
    private lateinit var orderTotalPaymentCard: OrderTotalPaymentCard

    private var progressDialog: AlertDialog? = null

    private var shouldUpdateCart: Boolean = true
    private var shouldDismissProgressDialog: Boolean = false

    private var source: String = SOURCE_OTHERS

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(OrderSummaryPageComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CREATE_PREFERENCE) {
            if (resultCode == Activity.RESULT_OK) {
                source = SOURCE_ADD_PROFILE
            }
            onResultFromPreference(data)
        } else if (requestCode == REQUEST_EDIT_PREFERENCE) {
            if (resultCode == Activity.RESULT_OK) {
                source = SOURCE_OTHERS
            }
            onResultFromPreference(data)
        } else {
            source = SOURCE_OTHERS
            when (requestCode) {
                REQUEST_CODE_COURIER_PINPOINT -> onResultFromCourierPinpoint(resultCode, data)
                REQUEST_CODE_PROMO -> onResultFromPromo(resultCode, data)
                PaymentConstant.REQUEST_CODE -> onResultFromPayment(resultCode)
                REQUEST_CODE_CREDIT_CARD -> onResultFromCreditCardPicker(resultCode, data)
                REQUEST_CODE_CREDIT_CARD_ERROR -> refresh()
            }
        }
    }

    private fun onResultFromPromo(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel? = data?.getParcelableExtra(ARGS_VALIDATE_USE_DATA_RESULT)
            if (validateUsePromoRevampUiModel != null) {
                viewModel.validateUsePromoRevampUiModel = validateUsePromoRevampUiModel
                viewModel.updatePromoState(validateUsePromoRevampUiModel.promoUiModel)
            }

            val validateUsePromoRequest: ValidateUsePromoRequest? = data?.getParcelableExtra(ARGS_LAST_VALIDATE_USE_REQUEST)
            if (validateUsePromoRequest != null) {
                viewModel.lastValidateUsePromoRequest = validateUsePromoRequest
            }

            val clearPromoUiModel: ClearPromoUiModel? = data?.getParcelableExtra(ARGS_CLEAR_PROMO_RESULT)
            if (clearPromoUiModel != null) {
                //reset
                viewModel.validateUsePromoRevampUiModel = null
                viewModel.updatePromoState(PromoUiModel().apply {
                    titleDescription = clearPromoUiModel.successDataModel.defaultEmptyPromoMessage
                })
                viewModel.validateUsePromo()
            }
        }
    }

    private fun onResultFromCourierPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data?.extras != null) {
            val locationPass: LocationPass? = data.extras?.getParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION)
            if (locationPass != null) {
                viewModel.savePinpoint(locationPass.longitude, locationPass.latitude)
            }
        }
    }

    private fun onResultFromPayment(resultCode: Int) {
        if (activity != null) {
            if (resultCode != PaymentConstant.PAYMENT_CANCELLED && resultCode != PaymentConstant.PAYMENT_FAILED) {
                activity?.finish()
            }
        }
    }

    private fun onResultFromPreference(data: Intent?) {
        val message = data?.getStringExtra(PreferenceEditActivity.EXTRA_RESULT_MESSAGE)
        if (message != null && message.isNotBlank()) {
            view?.let {
                Toaster.build(it, message).show()
            }
        }
        viewModel.clearBboIfExist()
        refresh()
    }

    private fun onResultFromCreditCardPicker(resultCode: Int, data: Intent?) {
        val metadata = data?.getStringExtra(CreditCardPickerFragment.EXTRA_RESULT_METADATA)
        if (metadata != null) {
            viewModel.updateCreditCard(metadata)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_summary_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initViewModel(savedInstanceState)
    }

    private fun initViews(view: View) {
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
        swipeRefreshLayout?.isRefreshing = true
        orderProductCard = OrderProductCard(view, this, orderSummaryAnalytics)
        orderPreferenceCard = OrderPreferenceCard(view, getOrderPreferenceCardListener(), orderSummaryAnalytics)
        orderInsuranceCard = OrderInsuranceCard(view, getOrderInsuranceCardListener(), orderSummaryAnalytics)
        orderTotalPaymentCard = OrderTotalPaymentCard(view, getOrderTotalPaymentCardListener())
        btnPromoCheckout?.margin = ButtonPromoCheckoutView.Margin.NO_BOTTOM
    }

    private fun initViewModel(savedInstanceState: Bundle?) {
        viewModel.orderPreference.observe(viewLifecycleOwner, Observer {
            when (it) {
                is OccState.FirstLoad -> {
                    orderPreference = it.data
                    swipeRefreshLayout?.isRefreshing = false
                    globalError?.gone()
                    mainContent?.visible()
                    view?.let { _ ->
                        orderProductCard.setProduct(viewModel.orderProduct)
                        orderProductCard.setShop(viewModel.orderShop)
                        orderProductCard.initView()
                        showMessage(it.data)
                        if (it.data.preference.profileId > 0 &&
                                it.data.preference.address.addressId > 0 &&
                                it.data.preference.shipment.serviceId > 0 &&
                                it.data.preference.payment.gatewayCode.isNotEmpty()) {
                            showPreferenceCard()
                            orderPreferenceCard.setPreference(it.data)
                        } else {
                            showEmptyPreferenceCard()
                        }
                    }
                }
                is OccState.Success -> {
                    orderPreference = it.data
                    swipeRefreshLayout?.isRefreshing = false
                    globalError?.gone()
                    mainContent?.visible()
                    view?.let { _ ->
                        if (!orderProductCard.isProductInitialized()) {
                            orderProductCard.setProduct(viewModel.orderProduct)
                            orderProductCard.setShop(viewModel.orderShop)
                            orderProductCard.initView()
                            showMessage(it.data)
                            if (it.data.preference.profileId > 0 &&
                                    it.data.preference.address.addressId > 0 &&
                                    it.data.preference.shipment.serviceId > 0 &&
                                    it.data.preference.payment.gatewayCode.isNotEmpty()) {
                                showPreferenceCard()
                                orderPreferenceCard.setPreference(it.data)
                            } else {
                                showEmptyPreferenceCard()
                            }
                        }
                    }
                }
                is OccState.Loading -> {
                    swipeRefreshLayout?.isRefreshing = true
                }
                is OccState.Failed -> {
                    swipeRefreshLayout?.isRefreshing = false
                    it.getFailure()?.let { failure ->
                        handleError(failure.throwable)
                    }
                }
            }
        })

        viewModel.orderShipment.observe(viewLifecycleOwner, Observer {
            orderPreferenceCard.setShipment(it)
            orderInsuranceCard.setupInsurance(it?.insuranceData, viewModel.orderProduct.productId.toString())
            if (it?.needPinpoint == true && orderPreference?.preference?.address != null) {
                goToPinpoint(orderPreference?.preference?.address)
            } else if (orderPreference != null) {
                forceShowOnboarding(orderPreference?.onboarding)
            }
        })

        viewModel.orderPayment.observe(viewLifecycleOwner, Observer {
            orderPreferenceCard.setPayment(it)
        })

        viewModel.orderTotal.observe(viewLifecycleOwner, Observer {
            orderTotalPaymentCard.setupPayment(it)
        })

        viewModel.orderPromo.observe(viewLifecycleOwner, Observer {
            setupButtonPromo(it)
        })

        viewModel.globalEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                is OccGlobalEvent.Loading -> {
                    if (progressDialog == null) {
                        context?.let { ctx ->
                            progressDialog = AlertDialog.Builder(ctx)
                                    .setView(com.tokopedia.purchase_platform.common.R.layout.purchase_platform_progress_dialog_view)
                                    .setCancelable(false)
                                    .create()
                        }
                    }
                    if (progressDialog?.isShowing == false) {
                        progressDialog?.show()
                    }
                }
                is OccGlobalEvent.Normal -> {
                    progressDialog?.dismiss()
                }
                is OccGlobalEvent.TriggerRefresh -> {
                    progressDialog?.dismiss()
                    view?.let { v ->
                        var message = it.errorMessage
                        if (message.isBlank() && it.throwable != null) {
                            message = ErrorHandler.getErrorMessage(context, it.throwable)
                        }
                        if (message.isNotBlank()) {
                            Toaster.build(v, message, type = Toaster.TYPE_ERROR).show()
                        }
                        refresh(false, isFullRefresh = it.isFullRefresh)
                    }
                }
                is OccGlobalEvent.Error -> {
                    progressDialog?.dismiss()
                    view?.let { v ->
                        var message = it.errorMessage
                        if (message.isBlank()) {
                            message = ErrorHandler.getErrorMessage(context, it.throwable)
                        }
                        Toaster.build(v, message, type = Toaster.TYPE_ERROR).show()
                    }
                }
                is OccGlobalEvent.CheckoutError -> {
                    progressDialog?.dismiss()
                    view?.let { _ ->
                        ErrorCheckoutBottomSheet().show(this, it.error, object : ErrorCheckoutBottomSheet.Listener {
                            override fun onClickSimilarProduct(errorCode: String) {
                                if (errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_SHOP_CLOSED) {
                                    orderSummaryAnalytics.eventClickSimilarProductShopClosed()
                                } else {
                                    orderSummaryAnalytics.eventClickSimilarProductEmptyStock()
                                }
                                RouteManager.route(context, ApplinkConstInternalDiscovery.SIMILAR_SEARCH_RESULT, viewModel.orderProduct.productId.toString())
                                activity?.finish()
                            }
                        })
                        if (it.error.code == ErrorCheckoutBottomSheet.ERROR_CODE_SHOP_CLOSED) {
                            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_SHOP_CLOSED)
                        } else {
                            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_STOCK)
                        }
                    }
                }
                is OccGlobalEvent.PriceChangeError -> {
                    progressDialog?.dismiss()
                    if (activity != null) {
                        val messageData = it.message
                        val priceValidationDialog = DialogUnify(activity!!, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
                        priceValidationDialog.setTitle(messageData.title)
                        priceValidationDialog.setDescription(messageData.desc)
                        priceValidationDialog.setPrimaryCTAText(messageData.action)
                        priceValidationDialog.setPrimaryCTAClickListener {
                            priceValidationDialog.dismiss()
                            refresh(isFullRefresh = false)
                        }
                        priceValidationDialog.show()
                        orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_PRICE_CHANGE)
                    }
                }
                is OccGlobalEvent.PromoClashing -> {
                    progressDialog?.dismiss()
                    if (activity != null) {
                        fragmentManager?.let { fm ->
                            val promoNotEligibleBottomsheet = PromoNotEligibleBottomsheet.createInstance()
                            promoNotEligibleBottomsheet.notEligiblePromoHolderDataList = it.notEligiblePromoHolderDataList
                            promoNotEligibleBottomsheet.actionListener = object : PromoNotEligibleActionListener {
                                override fun onShow() {
                                    val bottomSheetBehavior = promoNotEligibleBottomsheet.bottomSheetBehavior
                                    bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
                                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                                            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                                                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                                            }
                                        }

                                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                                    })
                                }

                                override fun onButtonContinueClicked(checkoutType: Int) {
                                    viewModel.cancelIneligiblePromoCheckout(it.notEligiblePromoHolderDataList, onSuccessCheckout())
                                    orderSummaryAnalytics.eventClickLanjutBayarPromoErrorOSP()
                                }

                                override fun onButtonChooseOtherPromo() {
                                    val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
                                    intent.putExtra(ARGS_PAGE_SOURCE, PAGE_OCC)
                                    intent.putExtra(ARGS_VALIDATE_USE_REQUEST, viewModel.generateValidateUsePromoRequest())
                                    intent.putExtra(ARGS_PROMO_REQUEST, viewModel.generatePromoRequest())
                                    intent.putStringArrayListExtra(ARGS_BBO_PROMO_CODES, viewModel.generateBboPromoCodes())

                                    orderSummaryAnalytics.eventClickPilihPromoLainPromoErrorOSP()
                                    startActivityForResult(intent, REQUEST_CODE_PROMO)
                                }
                            }
                            promoNotEligibleBottomsheet.show(fm, "")
                            orderSummaryAnalytics.eventViewBottomSheetPromoError()
                        }
                    }
                }
                is OccGlobalEvent.AtcError -> {
                    progressDialog?.dismiss()
                    swipeRefreshLayout?.isRefreshing = false
                    handleAtcError(it)
                }
                is OccGlobalEvent.AtcSuccess -> {
                    progressDialog?.dismiss()
                    swipeRefreshLayout?.isRefreshing = false
                    view?.let { v ->
                        if (it.message.isNotBlank()) {
                            Toaster.build(v, it.message).show()
                        }
                    }
                    setSourceFromPDP()
                    refresh()
                }
                is OccGlobalEvent.Prompt -> {
                    progressDialog?.dismiss()
                    showPrompt(it.prompt)
                }
            }
        })

        // first load
        if (viewModel.orderProduct.productId == 0) {
            val productId = arguments?.getString(QUERY_PRODUCT_ID)
            if (productId.isNullOrBlank() || savedInstanceState?.getBoolean(SAVE_HAS_DONE_ATC) == true) {
                setSourceFromPDP()
                refresh()
            } else {
                atcOcc(productId)
            }
        }
    }

    private fun showMessage(orderPreference: OrderPreference) {
        if (orderPreference.ticker != null) {
            tickerOsp?.tickerTitle = orderPreference.ticker.title
            tickerOsp?.setHtmlDescription(orderPreference.ticker.message)
            tickerOsp?.visible()
        } else {
            tickerOsp?.gone()
        }

        val preference = orderPreference.preference
        if (preference.profileId > 0) {
            tvHeader2?.text = getString(R.string.lbl_osp_secondary_header)
            tvHeader2?.visible()
            tvHeader3?.gone()
        } else {
            tvHeader2?.text = getString(R.string.lbl_osp_secondary_header_intro)
            val spannableString = SpannableString("${preference.onboardingHeaderMessage} Info")
            spannableString.setSpan(ForegroundColorSpan(Color.parseColor(COLOR_INFO)), preference.onboardingHeaderMessage.length, spannableString.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            tvHeader3?.text = spannableString
            tvHeader3?.setOnClickListener {
                orderSummaryAnalytics.eventClickInfoOnOSPNewBuyer()
                OccInfoBottomSheet().show(this, preference.onboardingComponent)
                orderSummaryAnalytics.eventViewOnboardingInfo()
            }
            tvHeader2?.visible()
            tvHeader3?.visible()
        }
        tickerPreferenceInfo?.setHtmlDescription(preference.message)
        tickerPreferenceInfo?.visibility = if (preference.message.isNotBlank()) View.VISIBLE else View.GONE

        if (orderPreference.onboarding.isShowOnboardingTicker) {
            lblOnboardingHeader?.text = orderPreference.onboarding.onboardingTicker.title
            lblOnboardingMessage?.text = orderPreference.onboarding.onboardingTicker.message
            ivOnboarding?.let {
                ImageHandler.LoadImage(it, orderPreference.onboarding.onboardingTicker.image)
            }
            if (orderPreference.onboarding.onboardingTicker.showActionButton) {
                btnOnboardingAction?.text = orderPreference.onboarding.onboardingTicker.actionText
                btnOnboardingAction?.setOnClickListener {
                    orderSummaryAnalytics.eventClickYukCobaLagiInOnboardingTicker()
                    showOnboarding(orderPreference.onboarding)
                }
                btnOnboardingAction?.visible()
            } else {
                btnOnboardingAction?.gone()
            }
            onboardingCard?.visible()
        } else {
            onboardingCard?.gone()
        }
    }

    private fun showOnboarding(onboarding: OccMainOnboarding) {
        view?.let {
            val scrollview = it.findViewById<NestedScrollView>(R.id.nested_scroll_view)
            val layoutPayment = it.findViewById<View>(R.id.layout_payment)
            val coachMarkItems = ArrayList<CoachMarkItem>()
            for (detailIndexed in onboarding.onboardingCoachMark.details.withIndex()) {
                val view = when (detailIndexed.index) {
                    0 -> it.findViewById(R.id.preference_card)
                    1 -> it.findViewById(R.id.iv_edit_preference)
                    2 -> it.findViewById(R.id.layout_order_preference_shipping)
                    3 -> layoutPayment
                    else -> null
                }
                coachMarkItems.add(CoachMarkItem(view, detailIndexed.value.title, detailIndexed.value.message, tintBackgroundColor = Color.WHITE))
            }
            val coachMark = CoachMarkBuilder().build()
            coachMark.enableSkip = true
            if (onboarding.onboardingCoachMark.skipButtonText.isNotEmpty()) {
                coachMark.setSkipText(onboarding.onboardingCoachMark.skipButtonText)
            }
            coachMark.setShowCaseStepListener(object : CoachMark.OnShowCaseStepListener {
                override fun onShowCaseGoTo(previousStep: Int, nextStep: Int, coachMarkItem: CoachMarkItem): Boolean {
                    if (nextStep == 0) {
                        scrollview.scrollTo(0, it.findViewById<View>(R.id.tv_header_2).top)
                    } else if (nextStep == 3) {
                        scrollview.scrollTo(0, layoutPayment.bottom)
                    }
                    return false
                }
            })
            coachMark.show(activity, COACH_MARK_TAG, coachMarkItems)
            orderSummaryAnalytics.eventViewOnboardingTicker()
        }
    }

    private fun forceShowOnboarding(onboarding: OccMainOnboarding?) {
        if (onboarding?.isForceShowCoachMark == true) {
            showOnboarding(onboarding)
            viewModel.consumeForceShowOnboarding()
        }
    }

    private fun showPreferenceCard() {
        emptyPreferenceCard?.gone()
        preferenceCard?.visible()
        orderTotalPaymentCard.setPaymentVisible(true)
        btnPromoCheckout?.visible()
    }

    private fun showEmptyPreferenceCard() {
        imageEmptyProfile?.let {
            ImageHandler.LoadImage(it, EMPTY_PROFILE_IMAGE)
        }
        emptyPreferenceCard?.visible()
        preferenceCard?.gone()
        orderTotalPaymentCard.setPaymentVisible(false)
        btnPromoCheckout?.gone()
        orderInsuranceCard.setGroupInsuranceVisible(false)

        buttonAturPilihan?.setOnClickListener {
            orderSummaryAnalytics.eventUserSetsFirstPreference(userSession.userId)
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
                putExtra(PreferenceEditActivity.EXTRA_FROM_FLOW, PreferenceEditActivity.FROM_FLOW_OSP)
                putExtra(PreferenceEditActivity.EXTRA_IS_EXTRA_PROFILE, false)
                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, "${getString(R.string.preference_number_summary)} 1")
                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_PROFILE, viewModel.getPaymentProfile())
                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
            }
            startActivityForResult(intent, REQUEST_CREATE_PREFERENCE)
        }
    }

    private fun goToPinpoint(address: OrderProfileAddress?) {
        address?.let {
            val locationPass = LocationPass()
            locationPass.cityName = it.cityName
            locationPass.districtName = it.districtName
            val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
            val bundle = Bundle()
            bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
            bundle.putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
            intent.putExtras(bundle)
            startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT)
            viewModel.changePinpoint()
        }
    }

    private fun getOrderInsuranceCardListener(): OrderInsuranceCard.OrderInsuranceCardListener {
        return object : OrderInsuranceCard.OrderInsuranceCardListener {
            override fun onInsuranceChecked(isChecked: Boolean) {
                viewModel.setInsuranceCheck(isChecked)
            }
        }
    }

    private fun getOrderTotalPaymentCardListener(): OrderTotalPaymentCard.OrderTotalPaymentCardListener {
        return object : OrderTotalPaymentCard.OrderTotalPaymentCardListener {
            override fun onOrderDetailClicked(orderCost: OrderCost) {
                orderSummaryAnalytics.eventClickRingkasanBelanjaOSP(viewModel.orderProduct.productId.toString(), orderCost.totalPrice.toInt().toString())
                OrderPriceSummaryBottomSheet().show(this@OrderSummaryPageFragment, orderCost)
            }

            override fun onPayClicked() {
                viewModel.finalUpdate(onSuccessCheckout(), false)
            }
        }
    }

    private fun setupButtonPromo(orderPromo: OrderPromo) {
        //loading
        when (orderPromo.state) {
            ButtonBayarState.LOADING -> {
                btnPromoCheckout?.state = ButtonPromoCheckoutView.State.LOADING
            }
            ButtonBayarState.DISABLE -> {
                //failed
                btnPromoCheckout?.state = ButtonPromoCheckoutView.State.INACTIVE
                btnPromoCheckout?.title = getString(com.tokopedia.purchase_platform.common.R.string.promo_checkout_inactive_label)
                btnPromoCheckout?.desc = getString(com.tokopedia.purchase_platform.common.R.string.promo_checkout_inactive_desc)
                btnPromoCheckout?.setOnClickListener {
                    viewModel.validateUsePromo()
                }
            }
            else -> {
                val lastApply = orderPromo.lastApply
                var title = getString(com.tokopedia.purchase_platform.common.R.string.promo_funnel_label)
                if (lastApply?.additionalInfo?.messageInfo?.message?.isNotEmpty() == true) {
                    title = lastApply.additionalInfo.messageInfo.message
                } else if (lastApply?.defaultEmptyPromoMessage?.isNotBlank() == true) {
                    title = lastApply.defaultEmptyPromoMessage
                }
                btnPromoCheckout?.state = ButtonPromoCheckoutView.State.ACTIVE
                btnPromoCheckout?.title = title
                btnPromoCheckout?.desc = lastApply?.additionalInfo?.messageInfo?.detail ?: ""

                if (lastApply?.additionalInfo?.usageSummaries?.isNotEmpty() == true) {
                    orderSummaryAnalytics.eventViewPromoAlreadyApplied()
                }

                btnPromoCheckout?.setOnClickListener {
                    viewModel.updateCartPromo { validateUsePromoRequest, promoRequest, bboCodes ->
                        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
                        intent.putExtra(ARGS_PAGE_SOURCE, PAGE_OCC)
                        intent.putExtra(ARGS_PROMO_REQUEST, promoRequest)
                        intent.putExtra(ARGS_VALIDATE_USE_REQUEST, validateUsePromoRequest)
                        intent.putStringArrayListExtra(ARGS_BBO_PROMO_CODES, bboCodes)

                        val codes = validateUsePromoRequest.codes
                        val promoCodes = ArrayList<String>()
                        for (code in codes) {
                            if (code != null) {
                                promoCodes.add(code)
                            }
                        }
                        if (validateUsePromoRequest.orders.isNotEmpty()) {
                            val orderCodes = validateUsePromoRequest.orders[0]?.codes
                                    ?: mutableListOf()
                            for (code in orderCodes) {
                                promoCodes.add(code)
                            }
                        }
                        orderSummaryAnalytics.eventClickPromoOSP(promoCodes)
                        startActivityForResult(intent, REQUEST_CODE_PROMO)
                    }
                }
            }
        }
    }

    private fun getOrderPreferenceCardListener() = object : OrderPreferenceCard.OrderPreferenceCardListener {

        override fun onChangePreferenceClicked() {
            orderSummaryAnalytics.eventChangesProfile()
            showPreferenceListBottomSheet()
        }

        override fun onCourierChange(shippingCourierViewModel: ShippingCourierUiModel) {
            orderSummaryAnalytics.eventChooseCourierSelectionOSP(shippingCourierViewModel.productData.shipperId.toString())
            viewModel.chooseCourier(shippingCourierViewModel)
        }

        override fun onDurationChange(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
            orderSummaryAnalytics.eventClickSelectedDurationOption(selectedServiceId.toString(), userSession.userId)
            viewModel.chooseDuration(selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint)
        }

        override fun onLogisticPromoClick(logisticPromoUiModel: LogisticPromoUiModel) {
            orderSummaryAnalytics.eventChooseBboAsDuration()
            viewModel.chooseLogisticPromo(logisticPromoUiModel)
        }

        override fun chooseCourier() {
            orderSummaryAnalytics.eventChangeCourierOSP(viewModel.getCurrentShipperId().toString())
            if (viewModel.orderTotal.value.buttonState != ButtonBayarState.LOADING) {
                orderPreferenceCard.showCourierBottomSheet(this@OrderSummaryPageFragment)
            }
        }

        override fun chooseDuration(isDurationError: Boolean) {
            if (isDurationError) {
                orderSummaryAnalytics.eventClickUbahWhenDurationError(userSession.userId)
            }
            if (viewModel.orderTotal.value.buttonState != ButtonBayarState.LOADING) {
                orderPreferenceCard.showDurationBottomSheet(this@OrderSummaryPageFragment)
            }
        }

        override fun onPreferenceEditClicked(preference: OrderPreference) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
                putExtra(PreferenceEditActivity.EXTRA_FROM_FLOW, PreferenceEditActivity.FROM_FLOW_OSP)
                putExtra(PreferenceEditActivity.EXTRA_IS_EXTRA_PROFILE, false)
                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, preference.profileIndex)
                putExtra(PreferenceEditActivity.EXTRA_PROFILE_ID, preference.preference.profileId)
                putExtra(PreferenceEditActivity.EXTRA_ADDRESS_ID, preference.preference.address.addressId)
                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_ID, preference.preference.shipment.serviceId)
                putExtra(PreferenceEditActivity.EXTRA_GATEWAY_CODE, preference.preference.payment.gatewayCode)
                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_PROFILE, viewModel.getPaymentProfile())
                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
            }
            startActivityForResult(intent, REQUEST_EDIT_PREFERENCE)
        }

        override fun onInstallmentDetailClicked() {
            if (viewModel.orderTotal.value.buttonState != ButtonBayarState.LOADING) {
                orderPreferenceCard.showInstallmentDetailBottomSheet(this@OrderSummaryPageFragment)
            }
        }

        override fun onInstallmentDetailChange(selectedInstallmentTerm: OrderPaymentInstallmentTerm) {
            viewModel.chooseInstallment(selectedInstallmentTerm)
        }

        override fun onChangeCreditCardClicked(additionalData: OrderPaymentCreditCardAdditionalData) {
            context?.let {
                startActivityForResult(CreditCardPickerActivity.createIntent(it, additionalData), REQUEST_CODE_CREDIT_CARD)
            }
        }
    }

    fun showPreferenceListBottomSheet() {
        viewModel.updateCart()
        val profileId = viewModel.getCurrentProfileId()
        val updateCartParam = viewModel.generateUpdateCartParam()
        if (profileId > 0 && updateCartParam != null) {
            PreferenceListBottomSheet(
                    paymentProfile = viewModel.getPaymentProfile(),
                    getPreferenceListUseCase = viewModel.getPreferenceListUseCase,
                    listener = object : PreferenceListBottomSheet.PreferenceListBottomSheetListener {
                        override fun onChangePreference(preference: ProfilesItemModel) {
                            viewModel.updatePreference(preference)
                            orderSummaryAnalytics.eventClickGunakanPilihanIniFromGantiPilihanOSP()
                        }

                        override fun onEditPreference(preference: ProfilesItemModel, position: Int, profileSize: Int) {
                            orderSummaryAnalytics.eventClickGearLogoInPreferenceFromGantiPilihanOSP()
                            val preferenceIndex = "${getString(R.string.lbl_summary_preference_option)} $position"
                            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
                                putExtra(PreferenceEditActivity.EXTRA_FROM_FLOW, PreferenceEditActivity.FROM_FLOW_OSP)
                                putExtra(PreferenceEditActivity.EXTRA_IS_EXTRA_PROFILE, profileSize > 1)
                                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, preferenceIndex)
                                putExtra(PreferenceEditActivity.EXTRA_PROFILE_ID, preference.profileId)
                                putExtra(PreferenceEditActivity.EXTRA_ADDRESS_ID, preference.addressModel.addressId)
                                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_ID, preference.shipmentModel.serviceId)
                                putExtra(PreferenceEditActivity.EXTRA_GATEWAY_CODE, preference.paymentModel.gatewayCode)
                                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_PROFILE, viewModel.getPaymentProfile())
                                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
                            }
                            startActivityForResult(intent, REQUEST_EDIT_PREFERENCE)
                        }

                        override fun onAddPreference(itemCount: Int) {
                            orderSummaryAnalytics.eventAddPreferensiFromOSP()
                            val preferenceIndex = "${getString(R.string.preference_number_summary)} ${itemCount + 1}"
                            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
                                putExtra(PreferenceEditActivity.EXTRA_FROM_FLOW, PreferenceEditActivity.FROM_FLOW_OSP)
                                putExtra(PreferenceEditActivity.EXTRA_IS_EXTRA_PROFILE, itemCount >= 1)
                                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, preferenceIndex)
                                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_PROFILE, viewModel.getPaymentProfile())
                                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
                            }
                            startActivityForResult(intent, REQUEST_CREATE_PREFERENCE)
                        }
                    }).show(this@OrderSummaryPageFragment, profileId)
        }
    }

    override fun onProductChange(product: OrderProduct, shouldReloadRates: Boolean) {
        viewModel.updateProduct(product, shouldReloadRates)
    }

    private fun handleError(throwable: Throwable?) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                showGlobalError(GlobalError.NO_CONNECTION)
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(it, getString(R.string.default_osp_error_message), type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.build(it, throwable?.message
                            ?: getString(R.string.default_osp_error_message), type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalError?.setType(type)
        globalError?.setActionClickListener {
            refresh(false)
        }
        mainContent?.gone()
        globalError?.visible()
    }

    private fun handleAtcError(atcError: OccGlobalEvent.AtcError) {
        if (atcError.throwable != null) {
            when (atcError.throwable) {
                is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                    view?.let {
                        showAtcGlobalError(GlobalError.NO_CONNECTION)
                    }
                }
                is RuntimeException -> {
                    when (atcError.throwable.localizedMessage?.toIntOrNull() ?: 0) {
                        ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showAtcGlobalError(GlobalError.NO_CONNECTION)
                        ReponseStatus.NOT_FOUND -> showAtcGlobalError(GlobalError.PAGE_NOT_FOUND)
                        ReponseStatus.INTERNAL_SERVER_ERROR -> showAtcGlobalError(GlobalError.SERVER_ERROR)
                        else -> {
                            view?.let {
                                showAtcGlobalError(GlobalError.SERVER_ERROR)
                            }
                        }
                    }
                }
                else -> {
                    view?.let {
                        showAtcGlobalError(GlobalError.SERVER_ERROR)
                    }
                }
            }
        } else {
            globalError?.setType(GlobalError.SERVER_ERROR)
            globalError?.setActionClickListener {
                arguments?.getString(QUERY_PRODUCT_ID)?.let { productId ->
                    atcOcc(productId)
                }
            }
            if (atcError.errorMessage.isNotBlank()) {
                globalError?.errorDescription?.text = atcError.errorMessage
            }
            globalError?.errorAction?.text = context?.getString(R.string.lbl_try_again)
            globalError?.errorSecondaryAction?.text = context?.getString(R.string.lbl_go_to_home)
            globalError?.errorSecondaryAction?.visible()
            globalError?.setSecondaryActionClickListener {
                RouteManager.route(context, ApplinkConst.HOME)
                activity?.finish()
            }
            mainContent?.gone()
            globalError?.visible()
        }
    }

    private fun showAtcGlobalError(type: Int) {
        globalError?.setType(type)
        globalError?.setActionClickListener {
            arguments?.getString(QUERY_PRODUCT_ID)?.let { productId ->
                atcOcc(productId)
            }
        }
        globalError?.errorAction?.text = context?.getString(R.string.lbl_try_again)
        globalError?.errorSecondaryAction?.text = context?.getString(R.string.lbl_go_to_home)
        globalError?.errorSecondaryAction?.visible()
        globalError?.setSecondaryActionClickListener {
            RouteManager.route(context, ApplinkConst.HOME)
            activity?.finish()
        }
        mainContent?.gone()
        globalError?.visible()
    }

    private fun refresh(shouldHideAll: Boolean = true, isFullRefresh: Boolean = true) {
        swipeRefreshLayout?.isRefreshing = true
        if (shouldHideAll) {
            mainContent?.gone()
            globalError?.gone()
        }
        viewModel.getOccCart(isFullRefresh, source)
    }

    private fun atcOcc(productId: String) {
        viewModel.atcOcc(productId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVE_HAS_DONE_ATC, viewModel.orderProduct.productId > 0)
    }

    private fun onSuccessCheckout(): (CheckoutOccResult) -> Unit = { checkoutOccResult: CheckoutOccResult ->
        view?.let { v ->
            activity?.let {
                val redirectParam = checkoutOccResult.paymentParameter.redirectParam
                if (redirectParam.url.isNotEmpty() && redirectParam.method.isNotEmpty()) {
                    val paymentPassData = PaymentPassData()
                    paymentPassData.redirectUrl = redirectParam.url
                    paymentPassData.queryString = redirectParam.form
                    paymentPassData.method = redirectParam.method

                    shouldUpdateCart = false
                    val intent = RouteManager.getIntent(activity, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
                    intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
                    intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_TOASTER_MESSAGE, checkoutOccResult.error.message)
                    startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
                    shouldDismissProgressDialog = true
                } else {
                    viewModel.globalEvent.value = OccGlobalEvent.Normal
                    Toaster.build(v, getString(R.string.default_osp_error_message), type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showPrompt(prompt: OccPrompt) {
        val ctx = context ?: return
        if (prompt.type == OccPrompt.TYPE_DIALOG) {
            val actionType = if (prompt.buttons.size > 1) DialogUnify.HORIZONTAL_ACTION else DialogUnify.SINGLE_ACTION
            val dialogUnify = DialogUnify(ctx, actionType, DialogUnify.NO_IMAGE)
            dialogUnify.apply {
                setTitle(prompt.title)
                setDescription(prompt.description)
                prompt.getPrimaryButton()?.also { primaryButton ->
                    setPrimaryCTAText(primaryButton.text)
                    setPrimaryCTAClickListener { onDialogPromptButtonClicked(dialogUnify, prompt, primaryButton) }
                    prompt.getSecondButton(primaryButton)?.also { secondaryButton ->
                        setSecondaryCTAText(secondaryButton.text)
                        setSecondaryCTAClickListener { onDialogPromptButtonClicked(dialogUnify, prompt, secondaryButton) }
                    }
                }
                setOverlayClose(false)
                setCancelable(false)
            }.show()
        } else if (prompt.type == OccPrompt.TYPE_BOTTOM_SHEET) {
            fragmentManager?.let {
                val bottomSheetUnify = BottomSheetUnify()
                bottomSheetUnify.apply {
                    showCloseIcon = true
                    val child = View.inflate(ctx, R.layout.bottom_sheet_error_checkout, null)
                    child.findViewById<EmptyStateUnify>(R.id.es_checkout).apply {
                        setTitle(prompt.title)
                        setDescription(prompt.description)
                        prompt.getPrimaryButton()?.also { primaryButton ->
                            setPrimaryCTAText(primaryButton.text)
                            setPrimaryCTAClickListener { onBottomSheetPromptButtonClicked(bottomSheetUnify, prompt, primaryButton) }
                            prompt.getSecondButton(primaryButton)?.also { secondaryButton ->
                                setSecondaryCTAText(secondaryButton.text)
                                setSecondaryCTAClickListener { onBottomSheetPromptButtonClicked(bottomSheetUnify, prompt, secondaryButton) }
                            }
                        }
                    }
                    setChild(child)
                }.show(it, null)
            }
        }
    }

    private fun onDialogPromptButtonClicked(dialog: DialogUnify, prompt: OccPrompt, button: OccPromptButton) {
        if (button.action == OccPromptButton.ACTION_OPEN) {
            RouteManager.route(context, button.link)
            activity?.finish()
        } else if (button.action == OccPromptButton.ACTION_RELOAD) {
            dialog.dismiss()
            if (prompt.from == OccPrompt.FROM_CART) {
                refresh()
            } else if (prompt.from == OccPrompt.FROM_CHECKOUT) {
                viewModel.finalUpdate(onSuccessCheckout(), false)
            }
        }
    }

    private fun onBottomSheetPromptButtonClicked(bottomSheet: BottomSheetUnify, prompt: OccPrompt, button: OccPromptButton) {
        if (button.action == OccPromptButton.ACTION_OPEN) {
            RouteManager.route(context, button.link)
            activity?.finish()
        } else if (button.action == OccPromptButton.ACTION_RELOAD) {
            bottomSheet.dismiss()
            if (prompt.from == OccPrompt.FROM_CART) {
                refresh()
            } else if (prompt.from == OccPrompt.FROM_CHECKOUT) {
                viewModel.finalUpdate(onSuccessCheckout(), false)
            }
        }
    }

    override fun onStart() {
        shouldUpdateCart = true
        shouldDismissProgressDialog = false
        super.onStart()
    }

    fun setIsFinishing() {
        shouldUpdateCart = false
    }

    override fun onStop() {
        super.onStop()
        if (swipeRefreshLayout?.isRefreshing == false && shouldUpdateCart) {
            viewModel.updateCart()
        }
        if (shouldDismissProgressDialog && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
            viewModel.globalEvent.value = OccGlobalEvent.Normal
        }
    }

    private fun setSourceFromPDP() {
        if (arguments?.getBoolean(SOURCE_PDP, false) == true) {
            source = SOURCE_PDP
        }
    }

    companion object {
        const val REQUEST_EDIT_PREFERENCE = 11
        const val REQUEST_CREATE_PREFERENCE = 12

        const val REQUEST_CODE_COURIER_PINPOINT = 13

        const val REQUEST_CODE_PROMO = 14

        const val REQUEST_CODE_CREDIT_CARD = 15
        const val REQUEST_CODE_CREDIT_CARD_ERROR = 16

        const val QUERY_PRODUCT_ID = "product_id"

        private const val EMPTY_PROFILE_IMAGE = "https://ecs7.tokopedia.net/android/others/beli_langsung_intro.png"
        private const val BELI_LANGSUNG_CART_IMAGE = "https://ecs7.tokopedia.net/android/others/beli_langsung_keranjang.png"

        private const val COLOR_INFO = "#03AC0E"

        private const val COACH_MARK_TAG = "osp_coach_mark"

        private const val SOURCE_ADD_PROFILE = "add_profile"
        private const val SOURCE_PDP = "pdp"
        private const val SOURCE_OTHERS = "others"

        private const val SAVE_HAS_DONE_ATC = "has_done_atc"

        @JvmStatic
        fun newInstance(isFromPDP: Boolean, productId: String?): OrderSummaryPageFragment {
            return OrderSummaryPageFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(SOURCE_PDP, isFromPDP)
                    putString(QUERY_PRODUCT_ID, productId)
                }
            }
        }
    }
}