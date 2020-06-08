package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.design.component.Tooltip
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticdata.data.constant.InsuranceConstant
import com.tokopedia.logisticdata.data.constant.LogisticConstant
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.utils.Utils.convertDpToPixel
import com.tokopedia.purchase_platform.features.checkout.view.PromoNotEligibleActionListener
import com.tokopedia.purchase_platform.features.checkout.view.PromoNotEligibleBottomsheet
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.Address
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccGlobalEvent
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProfileResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.checkout.Data
import com.tokopedia.purchase_platform.features.one_click_checkout.order.di.OrderSummaryPageComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.ErrorCheckoutBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.OccInfoBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.OrderPriceSummaryBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet.PreferenceListBottomSheet
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OrderPreferenceCard
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OrderProductCard
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.*
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.features.promo.data.request.validate_use.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.features.promo.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.PromoUiModel
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.ValidateUsePromoRevampUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
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

    private val swipeRefreshLayout by lazy { view?.findViewById<SwipeToRefresh>(R.id.swipe_refresh_layout) }
    private val globalError by lazy { view?.findViewById<GlobalError>(R.id.global_error) }
    private val mainContent by lazy { view?.findViewById<ConstraintLayout>(R.id.main_content) }
    private val tvHeader2 by lazy { view?.findViewById<Typography>(R.id.tv_header_2) }
    private val tvHeader3 by lazy { view?.findViewById<Typography>(R.id.tv_header_3) }
    private val tvSubheader by lazy { view?.findViewById<Typography>(R.id.tv_subheader) }
    private val tvSubheaderAction by lazy { view?.findViewById<Typography>(R.id.tv_subheader_action) }
    private val ivSubheader by lazy { view?.findViewById<ImageView>(R.id.iv_subheader) }
    private val tickerPreferenceInfo by lazy { view?.findViewById<Ticker>(R.id.ticker_preference_info) }
    private val emptyPreferenceCard by lazy { view?.findViewById<View>(R.id.empty_preference_card) }
    private val preferenceCard by lazy { view?.findViewById<View>(R.id.preference_card) }
    private val tvTotalPaymentLabel by lazy { view?.findViewById<Typography>(R.id.tv_total_payment_label) }
    private val tvTotalPaymentValue by lazy { view?.findViewById<Typography>(R.id.tv_total_payment_value) }
    private val btnOrderDetail by lazy { view?.findViewById<ImageView>(R.id.btn_order_detail) }
    private val btnPay by lazy { view?.findViewById<UnifyButton>(R.id.btn_pay) }
    private val btnPromoCheckout by lazy { view?.findViewById<ButtonPromoCheckoutView>(R.id.btn_promo_checkout) }
    private val imageEmptyProfile by lazy { view?.findViewById<ImageView>(R.id.image_empty_profile) }
    private val groupInsurance by lazy { view?.findViewById<Group>(R.id.group_insurance) }
    private val buttonAturPilihan by lazy { view?.findViewById<UnifyButton>(R.id.button_atur_pilihan) }
    private val tickerPaymentError by lazy { view?.findViewById<Ticker>(R.id.ticker_payment_error) }
    private val tvInsurance by lazy { view?.findViewById<Typography>(R.id.tv_insurance) }
    private val cbInsurance by lazy { view?.findViewById<CheckboxUnify>(R.id.cb_insurance) }
    private val imgBtInsuranceInfo by lazy { view?.findViewById<ImageView>(R.id.img_bt_insurance_info) }
    private val tvInsurancePrice by lazy { view?.findViewById<Typography>(R.id.tv_insurance_price) }

    private lateinit var orderProductCard: OrderProductCard
    private lateinit var orderPreferenceCard: OrderPreferenceCard

    private var progressDialog: AlertDialog? = null

    private var shouldUpdateCart: Boolean = true

    override fun getScreenName(): String {
        return this::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(OrderSummaryPageComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CREATE_PREFERENCE || requestCode == REQUEST_EDIT_PREFERENCE) {
            viewModel.clearBboIfExist()
            refresh()
        } else if (requestCode == REQUEST_CODE_COURIER_PINPOINT) {
            onResultFromCourierPinpoint(resultCode, data)
        } else if (requestCode == REQUEST_CODE_PROMO) {
            onResultFromPromo(resultCode, data)
        } else if (requestCode == PaymentConstant.REQUEST_CODE) {
            onResultFromPayment(resultCode, data)
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

    private fun onResultFromPayment(resultCode: Int, data: Intent?) {
        if (activity != null) {
            if (resultCode != PaymentConstant.PAYMENT_CANCELLED && resultCode != PaymentConstant.PAYMENT_FAILED) {
                activity?.finish()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order_summary_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout?.isRefreshing = true
        initViews(view)
        initViewModel()
    }

    private fun initViews(view: View) {
        orderProductCard = OrderProductCard(view, this, orderSummaryAnalytics)
        orderPreferenceCard = OrderPreferenceCard(view, getOrderPreferenceCardListener(), orderSummaryAnalytics)
    }

    private fun initViewModel() {
        viewModel.orderPreference.observe(this, Observer {
            if (it is OccState.FirstLoad) {
                swipeRefreshLayout?.isRefreshing = false
                globalError?.gone()
                mainContent?.visible()
                view?.let { _ ->
                    orderProductCard.setProduct(viewModel.orderProduct)
                    orderProductCard.setShop(viewModel.orderShop)
                    orderProductCard.initView()
                    showMessage(it.data.preference)
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
            } else if (it is OccState.Success) {
                swipeRefreshLayout?.isRefreshing = false
                mainContent?.visible()
                view?.let { _ ->
                    if (it.data.preference.address.addressId > 0) {
                        orderPreferenceCard.setPreference(it.data)
                    }
                    setupInsurance(it)
                    if (it.data.shipping?.needPinpoint == true) {
                        goToPinpoint(it.data.preference.address)
                    }
                }
            } else if (it is OccState.Loading) {
                swipeRefreshLayout?.isRefreshing = true
            } else if (it is OccState.Fail) {
                swipeRefreshLayout?.isRefreshing = false
                if (it.throwable != null) {
                    handleError(it.throwable)
                }
            }
        })

        viewModel.orderTotal.observe(this, Observer {
            setupPaymentError(it.paymentErrorMessage)
            setupButtonBayar(it)
        })

        viewModel.orderPromo.observe(this, Observer {
            setupButtonPromo(it)
        })

        viewModel.globalEvent.observe(this, Observer {
            when (it) {
                is OccGlobalEvent.Loading -> {
                    if (progressDialog == null) {
                        progressDialog = AlertDialog.Builder(context!!)
                                .setView(R.layout.purchase_platform_progress_dialog_view)
                                .setCancelable(false)
                                .create()
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
                            Toaster.make(v, message, type = Toaster.TYPE_ERROR)
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
                        Toaster.make(v, message, type = Toaster.TYPE_ERROR)
                    }
                }
                is OccGlobalEvent.CheckoutError -> {
                    progressDialog?.dismiss()
                    view?.let { _ ->
                        ErrorCheckoutBottomSheet().show(this, it, object : ErrorCheckoutBottomSheet.Listener {
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
                        val messageData = it.priceValidation.message
                        if (messageData != null) {
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
                }
                is OccGlobalEvent.PromoClashing -> {
                    progressDialog?.dismiss()
                    if (activity != null) {
                        fragmentManager?.let { fm ->
                            val promoNotEligibleBottomsheet = PromoNotEligibleBottomsheet.createInstance()
                            promoNotEligibleBottomsheet.notEligiblePromoHolderDataList = it.notEligiblePromoHolderdataList
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
                                    viewModel.cancelIneligiblePromoCheckout(it.notEligiblePromoHolderdataList, onSuccessCheckout())
                                    orderSummaryAnalytics.eventClickLanjutBayarPromoErrorOSP()
                                }

                                override fun onButtonChooseOtherPromo() {
                                    val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
                                    intent.putExtra(ARGS_PAGE_SOURCE, PromoCheckoutAnalytics.PAGE_OCC)
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
            }
        })

        // first load
        if (viewModel.orderProduct.productId == 0) {
            refresh()
        }
    }

    private fun showMessage(preference: ProfileResponse) {
        if (preference.hasPreference && preference.profileId > 0) {
            tvHeader2?.text = getString(R.string.lbl_osp_secondary_header)
            tvHeader2?.visible()
            tvHeader3?.gone()
            tvSubheader?.gone()
            tvSubheaderAction?.gone()
            ivSubheader?.gone()
        } else if (preference.profileId > 0) {
            tvHeader2?.gone()
            tvHeader3?.gone()
            ivSubheader?.let {
                ImageHandler.LoadImage(it, BELI_LANGSUNG_CART_IMAGE)
                it.visible()
            }
            tvSubheader?.text = preference.onboardingHeaderMessage
            tvSubheaderAction?.setOnClickListener {
                orderSummaryAnalytics.eventClickInfoOnOSPNewOcc()
                OccInfoBottomSheet().show(this, preference.onboardingComponent)
                orderSummaryAnalytics.eventViewOnboardingInfo()
            }
            tvSubheaderAction?.visible()
            tvSubheader?.visible()
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
            tvSubheader?.gone()
            tvSubheaderAction?.gone()
            ivSubheader?.gone()
        }
        tickerPreferenceInfo?.visibility = if (preference.isChangedProfile) View.VISIBLE else View.GONE
    }

    private fun showPreferenceCard() {
        emptyPreferenceCard?.gone()
        preferenceCard?.visible()
        tvTotalPaymentLabel?.visible()
        tvTotalPaymentValue?.visible()
        btnOrderDetail?.visible()
        btnPay?.visible()
        btnPromoCheckout?.visible()
    }

    private fun showEmptyPreferenceCard() {
        imageEmptyProfile?.let {
            ImageHandler.LoadImage(it, EMPTY_PROFILE_IMAGE)
        }
        emptyPreferenceCard?.visible()
        preferenceCard?.gone()
        tvTotalPaymentLabel?.gone()
        tvTotalPaymentValue?.gone()
        btnOrderDetail?.gone()
        btnPay?.gone()
        btnPromoCheckout?.gone()
        groupInsurance?.gone()

        buttonAturPilihan?.setOnClickListener {
            orderSummaryAnalytics.eventUserSetsFirstPreference(userSession.userId)
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, "${getString(R.string.preference_number_summary)} 1")
                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
            }
            startActivityForResult(intent, REQUEST_CREATE_PREFERENCE)
        }
    }

    private fun goToPinpoint(address: Address) {
        val locationPass = LocationPass()
        locationPass.cityName = address.cityName
        locationPass.districtName = address.districtName
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
        val bundle = Bundle()
        bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
        bundle.putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT)
        viewModel.changePinpoint()
    }

    private fun setupInsurance(it: OccState.Success<OrderPreference>) {
        val insuranceData = it.data.shipping?.insuranceData
        val productId = viewModel.orderProduct.productId
        if (insuranceData != null) {
            if (insuranceData.insurancePrice > 0) {
                tvInsurancePrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(insuranceData.insurancePrice, false)
                tvInsurancePrice?.visible()
            } else {
                tvInsurancePrice?.gone()
            }
            imgBtInsuranceInfo?.let { iv ->
                iv.setOnClickListener {
                    showBottomSheet(iv.context,
                            iv.context.getString(R.string.title_bottomsheet_insurance),
                            insuranceData.insuranceUsedInfo,
                            R.drawable.ic_pp_insurance)
                }
            }
            cbInsurance?.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    orderSummaryAnalytics.eventClickOnInsurance(productId.toString(), "uncheck", insuranceData.insurancePrice.toString())
                } else {
                    orderSummaryAnalytics.eventClickOnInsurance(productId.toString(), "check", insuranceData.insurancePrice.toString())
                }
                viewModel.setInsuranceCheck(isChecked)
            }
            if (insuranceData.insuranceType == InsuranceConstant.INSURANCE_TYPE_MUST) {
                tvInsurance?.setText(R.string.label_must_insurance)
                cbInsurance?.isChecked = true
                cbInsurance?.isEnabled = false
                viewModel.setInsuranceCheck(true)
                groupInsurance?.visible()
            } else if (insuranceData.insuranceType == InsuranceConstant.INSURANCE_TYPE_NO) {
                viewModel.setInsuranceCheck(false)
                groupInsurance?.gone()
            } else if (insuranceData.insuranceType == InsuranceConstant.INSURANCE_TYPE_OPTIONAL) {
                tvInsurance?.setText(R.string.label_shipment_insurance)
                cbInsurance?.isEnabled = true
                if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_YES) {
                    cbInsurance?.isChecked = true
                    viewModel.setInsuranceCheck(true)
                } else if (insuranceData.insuranceUsedDefault == InsuranceConstant.INSURANCE_USED_DEFAULT_NO) {
                    cbInsurance?.isChecked = false
                    viewModel.setInsuranceCheck(false)
                }
                groupInsurance?.visible()
            }
        } else {
            groupInsurance?.gone()
        }
    }

    private fun showBottomSheet(context: Context, title: String, message: String, image: Int) {
        Tooltip(context).apply {
            setTitle(title)
            setDesc(message)
            setTextButton(context.getString(R.string.label_button_bottomsheet_close))
            setIcon(image)
            btnAction.setOnClickListener { this.dismiss() }
            show()
        }
    }

    private fun setupButtonBayar(orderTotal: OrderTotal) {
        if (orderTotal.isButtonChoosePayment) {
            if (orderTotal.buttonState == ButtonBayarState.NORMAL) {
                btnPay?.setText(R.string.label_choose_payment)
                btnPay?.layoutParams?.width = convertDpToPixel(160f, context!!)
                btnPay?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                btnPay?.isLoading = false
                btnPay?.isEnabled = true
                btnPay?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                btnPay?.setText(R.string.label_choose_payment)
            } else if (orderTotal.buttonState == ButtonBayarState.DISABLE) {
                btnPay?.setText(R.string.label_choose_payment)
                btnPay?.layoutParams?.width = convertDpToPixel(160f, context!!)
                btnPay?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                btnPay?.isLoading = false
                btnPay?.isEnabled = false
                btnPay?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                btnPay?.setText(R.string.label_choose_payment)
            } else {
                btnPay?.layoutParams?.width = convertDpToPixel(160f, context!!)
                btnPay?.layoutParams?.height = convertDpToPixel(48f, context!!)
                btnPay?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                btnPay?.isLoading = true
            }
        } else {
            if (orderTotal.buttonState == ButtonBayarState.NORMAL) {
                btnPay?.setText(R.string.pay)
                btnPay?.layoutParams?.width = convertDpToPixel(140f, context!!)
                btnPay?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                btnPay?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_btn_pay_shield, 0, 0, 0)
                btnPay?.isLoading = false
                btnPay?.isEnabled = true
                btnPay?.setText(R.string.pay)
            } else if (orderTotal.buttonState == ButtonBayarState.DISABLE) {
                btnPay?.setText(R.string.pay)
                btnPay?.layoutParams?.width = convertDpToPixel(140f, context!!)
                btnPay?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                btnPay?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_btn_pay_shield, 0, 0, 0)
                btnPay?.isLoading = false
                btnPay?.isEnabled = false
                btnPay?.setText(R.string.pay)
            } else {
                btnPay?.layoutParams?.width = convertDpToPixel(140f, context!!)
                btnPay?.layoutParams?.height = convertDpToPixel(48f, context!!)
                btnPay?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                btnPay?.isLoading = true
            }
        }

        if (orderTotal.orderCost.totalPrice > 0.0) {
            tvTotalPaymentValue?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderTotal.orderCost.totalPrice, false)
        } else {
            tvTotalPaymentValue?.text = "-"
        }

        btnOrderDetail?.setOnClickListener {
            if (orderTotal.orderCost.totalPrice > 0.0) {
                val totalPrice = orderTotal.orderCost.totalPrice.toInt()
                orderSummaryAnalytics.eventClickRingkasanBelanjaOSP(viewModel.orderProduct.productId.toString(), totalPrice.toString())
                OrderPriceSummaryBottomSheet().show(this, orderTotal.orderCost)
            }
        }

        btnPay?.setOnClickListener {
            viewModel.finalUpdate(onSuccessCheckout())
        }
    }

    private fun setupPaymentError(paymentErrorMessage: String?) {
        if (paymentErrorMessage.isNullOrEmpty()) {
            tickerPaymentError?.gone()
        } else {
            tickerPaymentError?.setTextDescription(paymentErrorMessage)
            tickerPaymentError?.visible()
        }
    }

    private fun setupButtonPromo(orderPromo: OrderPromo) {
        //loading
        if (orderPromo.state == ButtonBayarState.LOADING) {
            btnPromoCheckout?.state = ButtonPromoCheckoutView.State.LOADING
        } else if (orderPromo.state == ButtonBayarState.DISABLE) {
            //failed
            btnPromoCheckout?.state = ButtonPromoCheckoutView.State.INACTIVE
            btnPromoCheckout?.title = getString(R.string.promo_checkout_inactive_label)
            btnPromoCheckout?.desc = getString(R.string.promo_checkout_inactive_desc)
            btnPromoCheckout?.setOnClickListener {
                viewModel.validateUsePromo()
            }
        } else {
            val lastApply = orderPromo.lastApply
            var title = getString(R.string.promo_funnel_label)
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
                    intent.putExtra(ARGS_PAGE_SOURCE, PromoCheckoutAnalytics.PAGE_OCC)
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
                        val orderCodes = validateUsePromoRequest.orders[0]?.codes ?: mutableListOf()
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

    private fun getOrderPreferenceCardListener() = object : OrderPreferenceCard.OrderPreferenceCardListener {

        override fun onChangePreferenceClicked() {
            orderSummaryAnalytics.eventChangesProfile()
            showPreferenceListBottomSheet()
        }

        override fun onCourierChange(shippingCourierViewModel: ShippingCourierUiModel) {
            orderSummaryAnalytics.eventChooseCourierSelectionOSP(viewModel._orderPreference?.shipping?.shipperId.toString())
            viewModel.chooseCourier(shippingCourierViewModel)
        }

        override fun onDurationChange(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean) {
            viewModel.chooseDuration(selectedServiceId, selectedShippingCourierUiModel, flagNeedToSetPinpoint)
        }

        override fun onLogisticPromoClick(logisticPromoUiModel: LogisticPromoUiModel) {
            orderSummaryAnalytics.eventChooseBboAsDuration()
            viewModel.chooseLogisticPromo(logisticPromoUiModel)
        }

        override fun chooseCourier() {
            orderSummaryAnalytics.eventChangeCourierOSP(viewModel._orderPreference?.shipping?.shipperId.toString())
            if (viewModel.orderTotal.value?.buttonState != ButtonBayarState.LOADING) {
                orderPreferenceCard.showCourierBottomSheet(this@OrderSummaryPageFragment)
            }
        }

        override fun chooseDuration() {
            if (viewModel.orderTotal.value?.buttonState != ButtonBayarState.LOADING) {
                orderPreferenceCard.showDurationBottomSheet(this@OrderSummaryPageFragment)
            }
        }

        override fun onPreferenceEditClicked(preference: OrderPreference) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
                putExtra(PreferenceEditActivity.EXTRA_SHOW_DELETE_BUTTON, false)
                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, preference.profileIndex)
                putExtra(PreferenceEditActivity.EXTRA_PROFILE_ID, preference.preference.profileId)
                putExtra(PreferenceEditActivity.EXTRA_ADDRESS_ID, preference.preference.address.addressId)
                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_ID, preference.preference.shipment.serviceId)
                putExtra(PreferenceEditActivity.EXTRA_GATEWAY_CODE, preference.preference.payment.gatewayCode)
                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
            }
            startActivityForResult(intent, REQUEST_EDIT_PREFERENCE)
        }
    }

    fun showPreferenceListBottomSheet() {
        viewModel.updateCart()
        val profileId = viewModel._orderPreference?.preference?.profileId ?: 0
        val updateCartParam = viewModel.generateUpdateCartParam()
        if (profileId > 0 && updateCartParam != null) {
            PreferenceListBottomSheet(
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
                                putExtra(PreferenceEditActivity.EXTRA_SHOW_DELETE_BUTTON, profileSize > 1)
                                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, preferenceIndex)
                                putExtra(PreferenceEditActivity.EXTRA_PROFILE_ID, preference.profileId)
                                putExtra(PreferenceEditActivity.EXTRA_ADDRESS_ID, preference.addressModel?.addressId)
                                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_ID, preference.shipmentModel?.serviceId)
                                putExtra(PreferenceEditActivity.EXTRA_GATEWAY_CODE, preference.paymentModel?.gatewayCode
                                        ?: "")
                                putExtra(PreferenceEditActivity.EXTRA_SHIPPING_PARAM, viewModel.generateShippingParam())
                                putParcelableArrayListExtra(PreferenceEditActivity.EXTRA_LIST_SHOP_SHIPMENT, ArrayList(viewModel.generateListShopShipment()))
                            }
                            startActivityForResult(intent, REQUEST_EDIT_PREFERENCE)
                        }

                        override fun onAddPreference(itemCount: Int) {
                            orderSummaryAnalytics.eventAddPreferensiFromOSP()
                            val preferenceIndex = "${getString(R.string.preference_number_summary)} ${itemCount + 1}"
                            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PREFERENCE_EDIT).apply {
                                putExtra(PreferenceEditActivity.EXTRA_PREFERENCE_INDEX, preferenceIndex)
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

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull() ?: 0) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.make(it, getString(R.string.default_osp_error_message), type = Toaster.TYPE_ERROR)
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.make(it, throwable.message
                            ?: getString(R.string.default_osp_error_message), type = Toaster.TYPE_ERROR)
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

    private fun refresh(shouldHideAll: Boolean = true, isFullRefresh: Boolean = true) {
        swipeRefreshLayout?.isRefreshing = true
        if (shouldHideAll) {
            mainContent?.gone()
            globalError?.gone()
        }
        viewModel.getOccCart(isFullRefresh = isFullRefresh)
    }

    private fun onSuccessCheckout(): (Data) -> Unit = { checkoutData: Data ->
        view?.let { v ->
            activity?.let {
                val redirectParam = checkoutData.paymentParameter.redirectParam
                if (redirectParam.url.isNotEmpty() && redirectParam.method.isNotEmpty()) {
                    val paymentPassData = PaymentPassData()
                    paymentPassData.redirectUrl = redirectParam.url
                    paymentPassData.queryString = redirectParam.form
                    paymentPassData.method = redirectParam.method

                    shouldUpdateCart = false
                    val intent = RouteManager.getIntent(activity, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
                    intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
                    intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_TOASTER_MESSAGE, checkoutData.error.message)
                    startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
                } else {
                    Toaster.make(v, getString(R.string.default_osp_error_message), type = Toaster.TYPE_ERROR)
                }
            }
        }
    }

    override fun onStart() {
        shouldUpdateCart = true
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
    }

    companion object {

        const val REQUEST_EDIT_PREFERENCE = 11
        const val REQUEST_CREATE_PREFERENCE = 12

        const val REQUEST_CODE_COURIER_PINPOINT = 13

        const val REQUEST_CODE_PROMO = 14

        private const val EMPTY_PROFILE_IMAGE = "https://ecs7.tokopedia.net/android/others/beli_langsung_intro.png"
        private const val BELI_LANGSUNG_CART_IMAGE = "https://ecs7.tokopedia.net/android/others/beli_langsung_keranjang.png"

        private const val COLOR_INFO = "#03AC0E"
    }
}