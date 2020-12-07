package com.tokopedia.oneclickcheckout.order.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface.BOLD
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.*
import com.tokopedia.coachmark.*
import com.tokopedia.coachmark.util.ViewHelper
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.OVO_ACTIVATION_URL
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding.Companion.COACHMARK_TYPE_EXISTING_USER_MULTI_PROFILE
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding.Companion.COACHMARK_TYPE_EXISTING_USER_ONE_PROFILE
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding.Companion.COACHMARK_TYPE_NEW_BUYER_AFTER_CREATE_PROFILE
import com.tokopedia.oneclickcheckout.order.data.get.OccMainOnboarding.Companion.COACHMARK_TYPE_NEW_BUYER_BEFORE_CREATE_PROFILE
import com.tokopedia.oneclickcheckout.order.di.OrderSummaryPageComponent
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.*
import com.tokopedia.oneclickcheckout.order.view.card.*
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.oneclickcheckout.preference.edit.view.address.AddressListFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.creditcard.CreditCardPickerActivity
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.creditcard.CreditCardPickerFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.payment.topup.OvoTopUpWebViewActivity
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
import dagger.Lazy
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Named

class OrderSummaryPageFragment : BaseDaggerFragment(), OrderProductCard.OrderProductCardListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var orderSummaryAnalytics: OrderSummaryAnalytics

    @Inject
    lateinit var userSession: Lazy<UserSessionInterface>

    @Inject
    @field:Named(OVO_ACTIVATION_URL)
    lateinit var ovoActivationUrl: Lazy<String>

    private val viewModel: OrderSummaryPageViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[OrderSummaryPageViewModel::class.java]
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

    private val newOnboardingCard by lazy { view?.findViewById<View>(R.id.layout_new_occ_onboarding) }
    private val ivNewOnboarding by lazy { view?.findViewById<ImageUnify>(R.id.iv_new_occ_onboarding) }
    private val tvNewOnboardingMessage by lazy { view?.findViewById<Typography>(R.id.tv_new_occ_onboarding_message) }

    private val tvHeader2 by lazy { view?.findViewById<Typography>(R.id.tv_header_2) }
    private val tvHeader3 by lazy { view?.findViewById<Typography>(R.id.tv_header_3) }

    private val tickerPreferenceInfo by lazy { view?.findViewById<Ticker>(R.id.ticker_preference_info) }
    private val emptyPreferenceCard by lazy { view?.findViewById<View>(R.id.empty_preference_card) }
    private val preferenceCard by lazy { view?.findViewById<View>(R.id.preference_card) }
    private val newPreferenceCard by lazy { view?.findViewById<View>(R.id.new_preference_card) }

    private val btnPromoCheckout by lazy { view?.findViewById<ButtonPromoCheckoutView>(R.id.btn_promo_checkout) }

    private val imageEmptyProfile by lazy { view?.findViewById<ImageUnify>(R.id.image_empty_profile) }
    private val buttonAturPilihan by lazy { view?.findViewById<UnifyButton>(R.id.button_atur_pilihan) }

    private var orderProductCard: OrderProductCard? = null
    private lateinit var orderPreferenceCard: OrderPreferenceCard
    private lateinit var newOrderPreferenceCard: NewOrderPreferenceCard
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
                REQUEST_CODE_OVO_TOP_UP -> refresh()
                REQUEST_CODE_EDIT_PAYMENT -> onResultFromEditPayment(resultCode, data)
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
                // trigger validate to reset BBO benefit
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
        val gatewayCode = data?.getStringExtra(CreditCardPickerFragment.EXTRA_RESULT_GATEWAY_CODE)
        if (gatewayCode != null && metadata != null) {
            viewModel.choosePayment(gatewayCode, metadata)
        }
    }

    private fun onResultFromEditPayment(resultCode: Int, data: Intent?) {
        val gateway = data?.getStringExtra(PreferenceEditActivity.EXTRA_RESULT_GATEWAY)
        val metadata = data?.getStringExtra(PreferenceEditActivity.EXTRA_RESULT_METADATA)
        if (gateway != null && metadata != null) {
            orderSummaryAnalytics.eventClickSelectedPaymentOption(gateway, userSession.get().userId)
            viewModel.choosePayment(gateway, metadata)
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
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
        swipeRefreshLayout?.isRefreshing = true
        orderProductCard = OrderProductCard(view, this, orderSummaryAnalytics)
        newOrderPreferenceCard = NewOrderPreferenceCard(view, getNewOrderPreferenceCardListener(), orderSummaryAnalytics)
        orderPreferenceCard = OrderPreferenceCard(view, getOrderPreferenceCardListener(), orderSummaryAnalytics)
        orderInsuranceCard = OrderInsuranceCard(view, getOrderInsuranceCardListener(), orderSummaryAnalytics)
        orderTotalPaymentCard = OrderTotalPaymentCard(view, getOrderTotalPaymentCardListener())
        btnPromoCheckout?.margin = ButtonPromoCheckoutView.Margin.NO_BOTTOM
    }

    private fun initViewModel(savedInstanceState: Bundle?) {
        viewModel.orderPreference.observe(viewLifecycleOwner, {
            when (it) {
                is OccState.FirstLoad -> {
                    orderPreference = it.data
                    swipeRefreshLayout?.isRefreshing = false
                    globalError?.gone()
                    mainContent?.visible()
                    view?.let { _ ->
                        orderProductCard?.setProduct(viewModel.orderProduct)
                        orderProductCard?.setShop(viewModel.orderShop)
                        orderProductCard?.initView()
                        showMessage(it.data)
                        if (it.data.preference.profileId > 0 &&
                                it.data.preference.address.addressId > 0 &&
                                it.data.preference.shipment.serviceId > 0 &&
                                it.data.preference.payment.gatewayCode.isNotEmpty()) {
                            showPreferenceCard()
                            orderPreferenceCard.setPreference(it.data)
                            newOrderPreferenceCard.setPreference(it.data, viewModel.revampData)
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
                        if (orderProductCard?.isProductInitialized() == false) {
                            orderProductCard?.setProduct(viewModel.orderProduct)
                            orderProductCard?.setShop(viewModel.orderShop)
                            orderProductCard?.initView()
                            showMessage(it.data)
                            if (it.data.preference.profileId > 0 &&
                                    it.data.preference.address.addressId > 0 &&
                                    it.data.preference.shipment.serviceId > 0 &&
                                    it.data.preference.payment.gatewayCode.isNotEmpty()) {
                                showPreferenceCard()
                                orderPreferenceCard.setPreference(it.data)
                                newOrderPreferenceCard.setPreference(it.data, viewModel.revampData)
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

        viewModel.orderShipment.observe(viewLifecycleOwner, {
            orderPreferenceCard.setShipment(it)
            newOrderPreferenceCard.setShipment(it)
            orderInsuranceCard.setupInsurance(it?.insuranceData, viewModel.orderProduct.productId.toString())
            if (it?.needPinpoint == true && orderPreference?.preference?.address != null) {
                goToPinpoint(orderPreference?.preference?.address)
            }
        })

        viewModel.orderPayment.observe(viewLifecycleOwner, {
            orderPreferenceCard.setPayment(it)
            newOrderPreferenceCard.setPayment(it)
        })

        viewModel.orderTotal.observe(viewLifecycleOwner, {
            orderTotalPaymentCard.setupPayment(it, viewModel.isNewFlow)
        })

        viewModel.orderPromo.observe(viewLifecycleOwner, {
            setupButtonPromo(it)
        })

        viewModel.globalEvent.observe(viewLifecycleOwner, {
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
                        source = SOURCE_OTHERS
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
                            source = SOURCE_OTHERS
                            refresh()
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
                is OccGlobalEvent.ForceOnboarding -> {
                    forceShowOnboarding(it.onboarding)
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
            tvHeader2?.text = if (viewModel.isNewFlow) getString(R.string.lbl_osp_secondary_header) else getString(R.string.lbl_osp_secondary_header_intro)
            val message = MethodChecker.fromHtml(preference.onboardingHeaderMessage)
            val infoButton = getString(R.string.lbl_osp_secondary_header_info)
            val spannableString = SpannableString("$message $infoButton")
            spannableString.setSpan(ForegroundColorSpan(Color.parseColor(COLOR_INFO)), spannableString.length - infoButton.length, spannableString.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(StyleSpan(BOLD), spannableString.length - infoButton.length, spannableString.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
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

        if (!viewModel.isNewFlow && orderPreference.onboarding.isShowOnboardingTicker) {
            lblOnboardingHeader?.text = orderPreference.onboarding.onboardingTicker.title
            lblOnboardingMessage?.text = orderPreference.onboarding.onboardingTicker.message
            ivOnboarding?.setImageUrl(orderPreference.onboarding.onboardingTicker.image)
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
            newOnboardingCard?.gone()
        } else if (viewModel.isNewFlow && orderPreference.onboarding.isShowOnboardingTicker) {
            onboardingCard?.gone()
            newOnboardingCard?.visible()
            ivNewOnboarding?.setImageUrl(orderPreference.onboarding.onboardingTicker.image)
            tvNewOnboardingMessage?.text = orderPreference.onboarding.onboardingTicker.message
        } else {
            onboardingCard?.gone()
            newOnboardingCard?.gone()
        }
    }

    private fun showOnboarding(onboarding: OccMainOnboarding) {
        view?.let {
            it.post {
                val scrollview = it.findViewById<ScrollView>(R.id.nested_scroll_view)
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
                    val color = context?.let { ctx ->
                        ContextCompat.getColor(ctx, com.tokopedia.unifyprinciples.R.color.Unify_N0)
                    } ?: com.tokopedia.unifyprinciples.R.color.Unify_N0
                    coachMarkItems.add(CoachMarkItem(view, detailIndexed.value.title, detailIndexed.value.message, tintBackgroundColor = color))
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
    }

    private fun showNewOnboarding(onboarding: OccMainOnboarding) {
        view?.let {
            it.post {
                val scrollview = it.findViewById<ScrollView>(R.id.nested_scroll_view)
                val coachMarkItems = ArrayList<CoachMark2Item>()
                for (detailIndexed in onboarding.onboardingCoachMark.details.withIndex()) {
                    val newView: View = when (onboarding.coachmarkType) {
                        COACHMARK_TYPE_NEW_BUYER_BEFORE_CREATE_PROFILE -> generateNewCoachMarkAnchorForNewBuyerBeforeCreateProfile(it, detailIndexed.index)
                        COACHMARK_TYPE_NEW_BUYER_AFTER_CREATE_PROFILE -> generateNewCoachMarkAnchorForNewBuyerAfterCreateProfile(it, detailIndexed.index)
                        COACHMARK_TYPE_EXISTING_USER_ONE_PROFILE -> generateNewCoachMarkAnchorForExistingUserOneProfile(it, detailIndexed.index)
                        COACHMARK_TYPE_EXISTING_USER_MULTI_PROFILE -> generateNewCoachMarkAnchorForExistingUserMultiProfile(it, detailIndexed.index)
                        else -> it.findViewById(R.id.tv_header_2)
                    }
                    coachMarkItems.add(CoachMark2Item(newView, detailIndexed.value.title, detailIndexed.value.message))
                }
                val coachMark = CoachMark2(it.context)
                coachMark.setStepListener(object : CoachMark2.OnStepListener {
                    override fun onStep(currentIndex: Int, coachMarkItem: CoachMark2Item) {
                        triggerCoachMarkAnalytics(onboarding, currentIndex)
                    }
                })
                coachMark.onFinishListener = {
                    when (onboarding.coachmarkType) {
                        COACHMARK_TYPE_NEW_BUYER_BEFORE_CREATE_PROFILE -> orderSummaryAnalytics.eventClickLanjutOnCoachmark2ForNewBuyerBeforeCreateProfile(userSession.get().userId)
                        COACHMARK_TYPE_NEW_BUYER_AFTER_CREATE_PROFILE -> orderSummaryAnalytics.eventClickDoneOnCoachmark3ForNewBuyerAfterCreateProfile(userSession.get().userId)
                        COACHMARK_TYPE_EXISTING_USER_ONE_PROFILE -> orderSummaryAnalytics.eventClickDoneOnCoachmark2ForExistingUserOneProfile(userSession.get().userId)
                        COACHMARK_TYPE_EXISTING_USER_MULTI_PROFILE -> orderSummaryAnalytics.eventClickDoneOnCoachmark2ForExistingUserMultiProfile(userSession.get().userId)
                    }
                }
                // manual scroll first item
                val firstView = coachMarkItems.first().anchorView
                firstView.post {
                    val relativeLocation = IntArray(2)
                    ViewHelper.getRelativePositionRec(firstView, scrollview, relativeLocation)
                    scrollview.scrollTo(0, relativeLocation.last())
                    coachMark.showCoachMark(coachMarkItems, scrollview)
                    // trigger first analytics
                    triggerCoachMarkAnalytics(onboarding, 0)
                }
            }
        }
    }

    private fun triggerCoachMarkAnalytics(onboarding: OccMainOnboarding, currentIndex: Int) {
        when (onboarding.coachmarkType) {
            COACHMARK_TYPE_NEW_BUYER_BEFORE_CREATE_PROFILE -> when (currentIndex) {
                0 -> orderSummaryAnalytics.eventViewCoachmark1ForNewBuyerBeforeCreateProfile(userSession.get().userId)
                1 -> orderSummaryAnalytics.eventViewCoachmark2ForNewBuyerBeforeCreateProfile(userSession.get().userId)
            }
            COACHMARK_TYPE_NEW_BUYER_AFTER_CREATE_PROFILE -> when (currentIndex) {
                0 -> orderSummaryAnalytics.eventViewCoachmark1ForNewBuyerAfterCreateProfile(userSession.get().userId)
                1 -> orderSummaryAnalytics.eventViewCoachmark2ForNewBuyerAfterCreateProfile(userSession.get().userId)
                2 -> orderSummaryAnalytics.eventViewCoachmark3ForNewBuyerAfterCreateProfile(userSession.get().userId)
            }
            COACHMARK_TYPE_EXISTING_USER_ONE_PROFILE -> when (currentIndex) {
                0 -> orderSummaryAnalytics.eventViewCoachmark1ForExistingUserOneProfile(userSession.get().userId)
                1 -> orderSummaryAnalytics.eventViewCoachmark2ForExistingUserOneProfile(userSession.get().userId)
            }
            COACHMARK_TYPE_EXISTING_USER_MULTI_PROFILE -> when (currentIndex) {
                0 -> orderSummaryAnalytics.eventViewCoachmark1ForExistingUserMultiProfile(userSession.get().userId)
                1 -> orderSummaryAnalytics.eventViewCoachmark2ForExistingUserMultiProfile(userSession.get().userId)
            }
        }
    }

    private fun generateNewCoachMarkAnchorForNewBuyerBeforeCreateProfile(view: View, index: Int): View {
        return when (index) {
            1 -> view.findViewById(R.id.button_atur_pilihan)
            else -> view.findViewById(R.id.tv_header_2)
        }
    }

    private fun generateNewCoachMarkAnchorForNewBuyerAfterCreateProfile(view: View, index: Int): View {
        return when (index) {
            1 -> view.findViewById(R.id.btn_new_change_duration)
            2 -> view.findViewById(R.id.tv_new_choose_preference)
            else -> view.findViewById(R.id.tv_header_2)
        }
    }

    private fun generateNewCoachMarkAnchorForExistingUserOneProfile(view: View, index: Int): View {
        return when (index) {
            0 -> view.findViewById(R.id.btn_new_change_duration)
            else -> view.findViewById(R.id.tv_new_choose_preference)
        }
    }

    private fun generateNewCoachMarkAnchorForExistingUserMultiProfile(view: View, index: Int): View {
        return when (index) {
            0 -> view.findViewById(R.id.btn_new_change_address)
            else -> view.findViewById(R.id.tv_new_choose_preference)
        }
    }

    private fun forceShowOnboarding(onboarding: OccMainOnboarding?) {
        if (onboarding?.isForceShowCoachMark == true) {
            if (viewModel.isNewFlow) {
                showNewOnboarding(onboarding)
            } else {
                showOnboarding(onboarding)
            }
            viewModel.consumeForceShowOnboarding()
        }
    }

    private fun showPreferenceCard() {
        emptyPreferenceCard?.gone()
        if (viewModel.isNewFlow) {
            preferenceCard?.gone()
            newPreferenceCard?.visible()
        } else {
            newPreferenceCard?.gone()
            preferenceCard?.visible()
        }
        orderTotalPaymentCard.setPaymentVisible(true)
        btnPromoCheckout?.visible()
    }

    private fun showEmptyPreferenceCard() {
        imageEmptyProfile?.setImageUrl(EMPTY_PROFILE_IMAGE)
        emptyPreferenceCard?.visible()
        preferenceCard?.gone()
        newPreferenceCard?.gone()
        orderTotalPaymentCard.setPaymentVisible(false)
        btnPromoCheckout?.gone()
        orderInsuranceCard.setGroupInsuranceVisible(false)

        if (viewModel.isNewFlow) {
            buttonAturPilihan?.text = getString(R.string.lbl_add_new_occ_profile_name)
        } else {
            buttonAturPilihan?.text = getString(R.string.atur_pilihan)
        }

        buttonAturPilihan?.setOnClickListener {
            if (viewModel.isNewFlow) {
                orderSummaryAnalytics.eventClickTambahTemplateBeliLangsungOnOrderSummary(userSession.get().userId)
            } else {
                orderSummaryAnalytics.eventUserSetsFirstPreference(userSession.get().userId)
            }
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
                putExtra(PreferenceEditActivity.EXTRA_FROM_FLOW, PreferenceEditActivity.FROM_FLOW_OSP)
                putExtra(PreferenceEditActivity.EXTRA_IS_EXTRA_PROFILE, false)
                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, "${getString(R.string.preference_number_summary)} 1")
                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_PROFILE, viewModel.getPaymentProfile())
                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
                putExtra(PreferenceEditActivity.EXTRA_IS_NEW_FLOW, viewModel.isNewFlow)
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
        when (orderPromo.state) {
            OccButtonState.LOADING -> {
                btnPromoCheckout?.state = ButtonPromoCheckoutView.State.LOADING
            }
            OccButtonState.DISABLE -> {
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

    private fun getNewOrderPreferenceCardListener() = object : NewOrderPreferenceCard.OrderPreferenceCardListener {

        override fun onChangePreferenceClicked() {
            orderSummaryAnalytics.eventClickPilihTemplateLain(userSession.get().userId)
            showPreferenceListBottomSheet()
        }

        override fun onAddPreferenceClicked(preference: OrderPreference) {
            orderSummaryAnalytics.eventClickTambahTemplate(userSession.get().userId)
            val preferenceIndex = "${getString(R.string.preference_number_summary)} 2"
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
                putExtra(PreferenceEditActivity.EXTRA_FROM_FLOW, PreferenceEditActivity.FROM_FLOW_OSP)
                putExtra(PreferenceEditActivity.EXTRA_IS_EXTRA_PROFILE, true)
                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, preferenceIndex)
                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_PROFILE, viewModel.getPaymentProfile())
                val orderCost = viewModel.orderTotal.value.orderCost
                val priceWithoutPaymentFee = orderCost.totalPrice - orderCost.paymentFee
                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_AMOUNT, priceWithoutPaymentFee)
                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
                putExtra(PreferenceEditActivity.EXTRA_IS_NEW_FLOW, viewModel.isNewFlow)
            }
            startActivityForResult(intent, REQUEST_CREATE_PREFERENCE)
        }

        override fun onAddAddress(token: Token?) {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2).apply {
                putExtra(AddressListFragment.EXTRA_IS_FULL_FLOW, true)
                putExtra(AddressListFragment.EXTRA_IS_LOGISTIC_LABEL, false)
                putExtra(CheckoutConstant.KERO_TOKEN, token)
            }, REQUEST_CODE_ADD_ADDRESS)
        }

        override fun onAddressChange(addressId: String) {
            orderSummaryAnalytics.eventClickSelectedAddressOption(addressId, userSession.get().userId)
            viewModel.chooseAddress(addressId)
        }

        override fun onCourierChange(shippingCourierViewModel: ShippingCourierUiModel) {
            orderSummaryAnalytics.eventChooseCourierSelectionOSP(shippingCourierViewModel.productData.shipperId.toString())
            viewModel.chooseCourier(shippingCourierViewModel)
        }

        override fun onDurationChange(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
            orderSummaryAnalytics.eventClickSelectedDurationOptionNew(selectedShippingCourierUiModel.productData.shipperProductId.toString(), userSession.get().userId)
            viewModel.chooseDuration(selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint)
        }

        override fun onLogisticPromoClick(logisticPromoUiModel: LogisticPromoUiModel) {
            orderSummaryAnalytics.eventChooseBboAsDuration()
            viewModel.chooseLogisticPromo(logisticPromoUiModel)
        }

        override fun reloadShipping() {
            viewModel.reloadRates()
        }

        override fun chooseAddress(currentAddressId: String) {
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
                orderSummaryAnalytics.eventClickArrowToChangeAddressOption(currentAddressId, userSession.get().userId)
                newOrderPreferenceCard.showAddressBottomSheet(this@OrderSummaryPageFragment, viewModel.getAddressCornerUseCase.get())
            }
        }

        override fun chooseCourier() {
            orderSummaryAnalytics.eventChangeCourierOSP(viewModel.getCurrentShipperId().toString())
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
                newOrderPreferenceCard.showCourierBottomSheet(this@OrderSummaryPageFragment)
            }
        }

        override fun chooseDuration(isDurationError: Boolean, currentSpId: String) {
            if (isDurationError) {
                orderSummaryAnalytics.eventClickUbahWhenDurationError(userSession.get().userId)
            } else if (currentSpId.isNotEmpty()) {
                orderSummaryAnalytics.eventClickArrowToChangeDurationOption(currentSpId, userSession.get().userId)
            }
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
                newOrderPreferenceCard.showDurationBottomSheet(this@OrderSummaryPageFragment)
            }
        }

        override fun choosePayment(preference: OrderPreference) {
            val currentGatewayCode = preference.preference.payment.gatewayCode
            orderSummaryAnalytics.eventClickArrowToChangePaymentOption(currentGatewayCode, userSession.get().userId)
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
                putExtra(PreferenceEditActivity.EXTRA_FROM_FLOW, PreferenceEditActivity.FROM_FLOW_OSP)
                putExtra(PreferenceEditActivity.EXTRA_IS_EXTRA_PROFILE, false)
                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, preference.profileIndex)
                putExtra(PreferenceEditActivity.EXTRA_PROFILE_ID, preference.preference.profileId)
                putExtra(PreferenceEditActivity.EXTRA_ADDRESS_ID, preference.preference.address.addressId)
                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_ID, preference.preference.shipment.serviceId)
                putExtra(PreferenceEditActivity.EXTRA_GATEWAY_CODE, currentGatewayCode)
                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_PROFILE, viewModel.getPaymentProfile())
                val orderCost = viewModel.orderTotal.value.orderCost
                val priceWithoutPaymentFee = orderCost.totalPrice - orderCost.paymentFee
                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_AMOUNT, priceWithoutPaymentFee)
                putExtra(PreferenceEditActivity.EXTRA_DIRECT_PAYMENT_STEP, true)
                putExtra(PreferenceEditActivity.EXTRA_IS_NEW_FLOW, viewModel.isNewFlow)
            }
            startActivityForResult(intent, REQUEST_CODE_EDIT_PAYMENT)
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
                val orderCost = viewModel.orderTotal.value.orderCost
                val priceWithoutPaymentFee = orderCost.totalPrice - orderCost.paymentFee
                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_AMOUNT, priceWithoutPaymentFee)
                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
                putExtra(PreferenceEditActivity.EXTRA_IS_NEW_FLOW, viewModel.isNewFlow)
            }
            startActivityForResult(intent, REQUEST_EDIT_PREFERENCE)
        }

        override fun onInstallmentDetailClicked() {
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
                newOrderPreferenceCard.showInstallmentDetailBottomSheet(this@OrderSummaryPageFragment)
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

        override fun onOvoActivateClicked(callbackUrl: String) {
            OvoActivationWebViewBottomSheet(ovoActivationUrl.get(), callbackUrl, object : OvoActivationWebViewBottomSheet.OvoActivationWebViewBottomSheetListener {
                override fun onActivationResult(isSuccess: Boolean) {
                    view?.let {
                        it.post {
                            if (isSuccess) {
                                Toaster.build(it, getString(R.string.message_ovo_activation_success), actionText = getString(R.string.button_ok_message_ovo_activation)).show()
                            } else {
                                Toaster.build(it, getString(R.string.message_ovo_activation_failed), type = Toaster.TYPE_ERROR, actionText = getString(R.string.button_ok_message_ovo_activation)).show()
                            }
                            source = SOURCE_OTHERS
                            refresh()
                        }
                    }
                }
            }).show(this@OrderSummaryPageFragment, userSession.get())
        }

        override fun onOvoTopUpClicked(callbackUrl: String, isHideDigital: Int, customerData: OrderPaymentOvoCustomerData) {
            context?.let {
                startActivityForResult(OvoTopUpWebViewActivity.createIntent(it, callbackUrl, isHideDigital, customerData), REQUEST_CODE_OVO_TOP_UP)
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
            orderSummaryAnalytics.eventClickSelectedDurationOption(selectedServiceId.toString(), userSession.get().userId)
            viewModel.chooseDuration(selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint)
        }

        override fun onLogisticPromoClick(logisticPromoUiModel: LogisticPromoUiModel) {
            orderSummaryAnalytics.eventChooseBboAsDuration()
            viewModel.chooseLogisticPromo(logisticPromoUiModel)
        }

        override fun chooseCourier() {
            orderSummaryAnalytics.eventChangeCourierOSP(viewModel.getCurrentShipperId().toString())
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
                orderPreferenceCard.showCourierBottomSheet(this@OrderSummaryPageFragment)
            }
        }

        override fun chooseDuration(isDurationError: Boolean) {
            if (isDurationError) {
                orderSummaryAnalytics.eventClickUbahWhenDurationError(userSession.get().userId)
            }
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
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
                val orderCost = viewModel.orderTotal.value.orderCost
                val priceWithoutPaymentFee = orderCost.totalPrice - orderCost.paymentFee
                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_AMOUNT, priceWithoutPaymentFee)
                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
                putExtra(PreferenceEditActivity.EXTRA_IS_NEW_FLOW, viewModel.isNewFlow)
            }
            startActivityForResult(intent, REQUEST_EDIT_PREFERENCE)
        }

        override fun onInstallmentDetailClicked() {
            if (viewModel.orderTotal.value.buttonState != OccButtonState.LOADING) {
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

        override fun onOvoActivateClicked(callbackUrl: String) {
            OvoActivationWebViewBottomSheet(ovoActivationUrl.get(), callbackUrl, object : OvoActivationWebViewBottomSheet.OvoActivationWebViewBottomSheetListener {
                override fun onActivationResult(isSuccess: Boolean) {
                    view?.let {
                        it.post {
                            if (isSuccess) {
                                Toaster.build(it, getString(R.string.message_ovo_activation_success), actionText = getString(R.string.button_ok_message_ovo_activation)).show()
                            } else {
                                Toaster.build(it, getString(R.string.message_ovo_activation_failed), type = Toaster.TYPE_ERROR, actionText = getString(R.string.button_ok_message_ovo_activation)).show()
                            }
                            source = SOURCE_OTHERS
                            refresh()
                        }
                    }
                }
            }).show(this@OrderSummaryPageFragment, userSession.get())
        }

        override fun onOvoTopUpClicked(callbackUrl: String, isHideDigital: Int, customerData: OrderPaymentOvoCustomerData) {
            context?.let {
                startActivityForResult(OvoTopUpWebViewActivity.createIntent(it, callbackUrl, isHideDigital, customerData), REQUEST_CODE_OVO_TOP_UP)
            }
        }
    }

    fun showPreferenceListBottomSheet() {
        viewModel.updateCart()
        val profileId = viewModel.getCurrentProfileId()
        val updateCartParam = viewModel.generateUpdateCartParam()
        if (profileId > 0 && updateCartParam != null) {
            PreferenceListBottomSheet(
                    isNewFlow = viewModel.isNewFlow,
                    paymentProfile = viewModel.getPaymentProfile(),
                    getPreferenceListUseCase = viewModel.getPreferenceListUseCase.get(),
                    listener = object : PreferenceListBottomSheet.PreferenceListBottomSheetListener {
                        override fun onChangePreference(preference: ProfilesItemModel) {
                            if (viewModel.isNewFlow) {
                                orderSummaryAnalytics.eventClickProfileOptionOnProfileList(preference.profileId.toString(), userSession.get().userId)
                            } else {
                                orderSummaryAnalytics.eventClickGunakanPilihanIniFromGantiPilihanOSP()
                            }
                            viewModel.updatePreference(preference)
                        }

                        override fun onEditPreference(preference: ProfilesItemModel, position: Int, profileSize: Int) {
                            if (viewModel.isNewFlow) {
                                orderSummaryAnalytics.eventClickEditProfileOnProfileList(preference.profileId.toString(), userSession.get().userId)
                            } else {
                                orderSummaryAnalytics.eventClickGearLogoInPreferenceFromGantiPilihanOSP()
                            }
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
                                val orderCost = viewModel.orderTotal.value.orderCost
                                val priceWithoutPaymentFee = orderCost.totalPrice - orderCost.paymentFee
                                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_AMOUNT, priceWithoutPaymentFee)
                                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
                                putExtra(PreferenceEditActivity.EXTRA_IS_NEW_FLOW, viewModel.isNewFlow)
                            }
                            startActivityForResult(intent, REQUEST_EDIT_PREFERENCE)
                        }

                        override fun onAddPreference(itemCount: Int) {
                            if (viewModel.isNewFlow) {
                                orderSummaryAnalytics.eventClickTambahTemplateBeliLangsungOnProfileList(userSession.get().userId)
                            } else {
                                orderSummaryAnalytics.eventAddPreferensiFromOSP()
                            }
                            val preferenceIndex = "${getString(R.string.preference_number_summary)} ${itemCount + 1}"
                            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
                                putExtra(PreferenceEditActivity.EXTRA_FROM_FLOW, PreferenceEditActivity.FROM_FLOW_OSP)
                                putExtra(PreferenceEditActivity.EXTRA_IS_EXTRA_PROFILE, itemCount >= 1)
                                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, preferenceIndex)
                                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_PROFILE, viewModel.getPaymentProfile())
                                val orderCost = viewModel.orderTotal.value.orderCost
                                val priceWithoutPaymentFee = orderCost.totalPrice - orderCost.paymentFee
                                putExtra(PreferenceEditActivity.EXTRA_PAYMENT_AMOUNT, priceWithoutPaymentFee)
                                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
                                putExtra(PreferenceEditActivity.EXTRA_IS_NEW_FLOW, viewModel.isNewFlow)
                            }
                            startActivityForResult(intent, REQUEST_CREATE_PREFERENCE)
                        }

                        override fun onShowNewLayout() {
                            orderSummaryAnalytics.eventViewProfileList(userSession.get().userId)
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
            showDialogPrompt(prompt, ctx)
        } else if (prompt.type == OccPrompt.TYPE_BOTTOM_SHEET) {
            val fm = fragmentManager ?: return
            showBottomSheetPrompt(prompt, fm, ctx)
        }
    }

    private fun showDialogPrompt(prompt: OccPrompt, ctx: Context) {
        val actionType = if (prompt.buttons.size > 1) DialogUnify.HORIZONTAL_ACTION else DialogUnify.SINGLE_ACTION
        val dialogUnify = DialogUnify(ctx, actionType, DialogUnify.NO_IMAGE)
        dialogUnify.apply {
            setTitle(prompt.title)
            setDescription(prompt.description)
            prompt.getPrimaryButton()?.also { primaryButton ->
                setPrimaryCTAText(primaryButton.text)
                setPrimaryCTAClickListener { onDialogPromptButtonClicked(dialogUnify, primaryButton) }
                prompt.getSecondButton(primaryButton)?.also { secondaryButton ->
                    setSecondaryCTAText(secondaryButton.text)
                    setSecondaryCTAClickListener { onDialogPromptButtonClicked(dialogUnify, secondaryButton) }
                }
            }
            setOverlayClose(false)
            setCancelable(false)
        }.show()
    }

    private fun onDialogPromptButtonClicked(dialog: DialogUnify, button: OccPromptButton) {
        when (button.action) {
            OccPromptButton.ACTION_OPEN -> {
                RouteManager.route(context, button.link)
                activity?.finish()
            }
            OccPromptButton.ACTION_RELOAD -> {
                dialog.dismiss()
                source = SOURCE_OTHERS
                refresh()
            }
            OccPromptButton.ACTION_RETRY -> {
                dialog.dismiss()
                viewModel.finalUpdate(onSuccessCheckout(), false)
            }
        }
    }

    private fun showBottomSheetPrompt(prompt: OccPrompt, fm: FragmentManager, ctx: Context) {
        val bottomSheetUnify = BottomSheetUnify()
        bottomSheetUnify.apply {
            showCloseIcon = true
            val child = View.inflate(ctx, R.layout.bottom_sheet_error_checkout, null)
            child.findViewById<EmptyStateUnify>(R.id.es_checkout).apply {
                setImageUrl(prompt.imageUrl)
                setTitle(prompt.title)
                setDescription(prompt.description)
                prompt.getPrimaryButton()?.also { primaryButton ->
                    setPrimaryCTAText(primaryButton.text)
                    setPrimaryCTAClickListener { onBottomSheetPromptButtonClicked(bottomSheetUnify, primaryButton) }
                    prompt.getSecondButton(primaryButton)?.also { secondaryButton ->
                        setSecondaryCTAText(secondaryButton.text)
                        setSecondaryCTAClickListener { onBottomSheetPromptButtonClicked(bottomSheetUnify, secondaryButton) }
                    }
                }
            }
            setChild(child)
        }.show(fm, null)
    }

    private fun onBottomSheetPromptButtonClicked(bottomSheet: BottomSheetUnify, button: OccPromptButton) {
        when (button.action) {
            OccPromptButton.ACTION_OPEN -> {
                RouteManager.route(context, button.link)
                activity?.finish()
            }
            OccPromptButton.ACTION_RELOAD -> {
                bottomSheet.dismiss()
                source = SOURCE_OTHERS
                refresh()
            }
            OccPromptButton.ACTION_RETRY -> {
                bottomSheet.dismiss()
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

    override fun onDestroyView() {
        orderProductCard?.clearJob()
        super.onDestroyView()
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

        const val REQUEST_CODE_OVO_TOP_UP = 17

        const val REQUEST_CODE_ADD_ADDRESS = 18

        const val REQUEST_CODE_EDIT_PAYMENT = 19

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