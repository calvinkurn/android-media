package com.tokopedia.checkout.revamp.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.checkout.R
import com.tokopedia.checkout.analytics.CheckoutEgoldAnalytics
import com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics
import com.tokopedia.checkout.analytics.CornerAnalytics
import com.tokopedia.checkout.databinding.BottomSheetPlatformFeeInfoBinding
import com.tokopedia.checkout.databinding.FragmentCheckoutBinding
import com.tokopedia.checkout.databinding.HeaderCheckoutBinding
import com.tokopedia.checkout.databinding.ToastRectangleBinding
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData
import com.tokopedia.checkout.domain.model.checkout.Prompt
import com.tokopedia.checkout.revamp.di.CheckoutModule
import com.tokopedia.checkout.revamp.di.DaggerCheckoutComponent
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapter
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.adapter.CheckoutDiffUtilCallback
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.utils.CheckoutFingerprintUtil
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.checkout.view.dialog.ExpireTimeDialogListener
import com.tokopedia.checkout.view.dialog.ExpiredTimeDialog
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.checkout.webview.CheckoutWebViewActivity
import com.tokopedia.checkout.webview.UpsellWebViewActivity
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_epharmacy.EPHARMACY_CONSULTATION_RESULT_EXTRA
import com.tokopedia.common_epharmacy.EPHARMACY_REDIRECT_CART_RESULT_CODE
import com.tokopedia.common_epharmacy.EPHARMACY_REDIRECT_CHECKOUT_RESULT_CODE
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.fingerprint.util.FingerPrintUtil
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressTokonow
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.AddEditAddressSource
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticCommon.util.PinpointRolloutHelper
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheet
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheetListener
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheet
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheetListener
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.EPharmacyAnalytics
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics
import com.tokopedia.purchase_platform.common.constant.ARGS_BBO_PROMO_CODES
import com.tokopedia.purchase_platform.common.constant.ARGS_CHOSEN_ADDRESS
import com.tokopedia.purchase_platform.common.constant.ARGS_CLEAR_PROMO_RESULT
import com.tokopedia.purchase_platform.common.constant.ARGS_FINISH_ERROR
import com.tokopedia.purchase_platform.common.constant.ARGS_LAST_VALIDATE_USE_REQUEST
import com.tokopedia.purchase_platform.common.constant.ARGS_PAGE_SOURCE
import com.tokopedia.purchase_platform.common.constant.ARGS_PROMO_ERROR
import com.tokopedia.purchase_platform.common.constant.ARGS_PROMO_MVC_LOCK_COURIER_FLOW
import com.tokopedia.purchase_platform.common.constant.ARGS_PROMO_REQUEST
import com.tokopedia.purchase_platform.common.constant.ARGS_VALIDATE_USE_DATA_RESULT
import com.tokopedia.purchase_platform.common.constant.ARGS_VALIDATE_USE_REQUEST
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.constant.PAGE_CHECKOUT
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionListener
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionViewHolder
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnMetadata
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnNote
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnWordingData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AvailableBottomSheetData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.PopUpData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.UnavailableBottomSheetData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.utils.animateGone
import com.tokopedia.purchase_platform.common.utils.animateShow
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.purchase_platform.common.utils.removeSingleDecimalSuffix
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.utils.time.TimeHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.Product as GiftingProduct

class CheckoutFragment :
    BaseDaggerFragment(),
    CheckoutAdapterListener,
    UploadPrescriptionListener,
    ShippingDurationBottomsheetListener,
    ShippingCourierBottomsheetListener,
    ExpireTimeDialogListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var checkoutAnalyticsCourierSelection: CheckoutAnalyticsCourierSelection

    @Inject
    lateinit var ePharmacyAnalytics: EPharmacyAnalytics

    @Inject
    lateinit var mTrackerCorner: CornerAnalytics

    @Inject
    lateinit var checkoutTradeInAnalytics: CheckoutTradeInAnalytics

    @Inject
    lateinit var checkoutEgoldAnalytics: CheckoutEgoldAnalytics

    @Inject
    lateinit var shippingCourierConverter: ShippingCourierConverter

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val viewModel: CheckoutViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CheckoutViewModel::class.java]
    }

    private var binding by autoCleared<FragmentCheckoutBinding>()

    private var header by autoCleared<HeaderCheckoutBinding>()

    private val adapter: CheckoutAdapter = CheckoutAdapter(this, this)

    private var loader: LoaderDialog? = null

    private var toasterErrorAkamai: Snackbar? = null

    private var shipmentTracePerformance: PerformanceMonitoring? = null
    private var isShipmentTraceStopped = false

    private val isPlusSelected: Boolean
        get() = arguments?.getBoolean(ShipmentFragment.ARG_IS_PLUS_SELECTED, false) ?: false

    private val deviceId: String
        get() = if (arguments?.getString(ShipmentFormRequest.EXTRA_DEVICE_ID) != null) {
            arguments!!.getString(ShipmentFormRequest.EXTRA_DEVICE_ID)!!
        } else {
            ""
        }

    private val isOneClickShipment: Boolean
        get() = arguments != null && arguments!!.getBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT)

    private val checkoutLeasingId: String
        get() {
            var leasingId = "0"
            if (arguments != null && arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID) != null &&
                !arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID)
                    .equals("null", ignoreCase = true)
            ) {
                leasingId = arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID)!!
            }
            return leasingId
        }

    private val isTradeIn: Boolean
        get() = arguments != null && arguments!!.getString(
            ShipmentFormRequest.EXTRA_DEVICE_ID,
            ""
        ) != null && arguments!!.getString(ShipmentFormRequest.EXTRA_DEVICE_ID, "").isNotEmpty()

    private val checkoutPageSource: String
        get() {
            var pageSource: String = CheckoutConstant.CHECKOUT_PAGE_SOURCE_PDP
            if (arguments != null && arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_PAGE_SOURCE) != null) {
                pageSource = arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_PAGE_SOURCE)!!
            }
            return pageSource
        }

    override fun getScreenName(): String {
        return if (isOneClickShipment) {
            ConstantTransactionAnalytics.ScreenName.ONE_CLICK_SHIPMENT
        } else {
            ConstantTransactionAnalytics.ScreenName.CHECKOUT
        }
    }

    override fun initInjector() {
        if (activity != null) {
            val baseMainApplication = activity!!.application as BaseMainApplication
            DaggerCheckoutComponent.builder()
                .baseAppComponent(baseMainApplication.baseAppComponent)
                .checkoutModule(CheckoutModule(this))
                .build()
                .inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.pageState.value == CheckoutPageState.Loading) {
            shipmentTracePerformance = PerformanceMonitoring.start(SHIPMENT_TRACE)
        }
    }

    fun stopTrace() {
        if (!isShipmentTraceStopped) {
            shipmentTracePerformance?.stopTrace()
            isShipmentTraceStopped = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.findViewById<View>(com.tokopedia.abstraction.R.id.toolbar)?.isVisible = false
        binding = FragmentCheckoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerCheckout.setNavigationOnClickListener {
            onBackPressed()
        }
        header = HeaderCheckoutBinding.inflate(LayoutInflater.from(context))
        header.tvCheckoutHeaderAddressHeader.isVisible = false
        header.icCheckoutHeaderAddress.isVisible = false
        header.tvCheckoutHeaderAddressName.isVisible = false
        binding.headerCheckout.customView(header.root)

        binding.rvCheckout.adapter = adapter
        binding.rvCheckout.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvCheckout.itemAnimator = null
        binding.rvCheckout.clearOnScrollListeners()
        binding.rvCheckout.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as? LinearLayoutManager
                if (recyclerView.isVisible &&
                    (linearLayoutManager?.findFirstVisibleItemPosition() ?: -1) > 1
                ) {
                    header.tvCheckoutHeaderText.animateGone()
                    header.tvCheckoutHeaderAddressHeader.animateShow()
                    header.icCheckoutHeaderAddress.animateShow()
                    header.tvCheckoutHeaderAddressName.animateShow()
                } else if (!header.tvCheckoutHeaderText.isVisible) {
                    header.tvCheckoutHeaderText.animateShow()
                    header.tvCheckoutHeaderAddressHeader.animateGone()
                    header.icCheckoutHeaderAddress.animateGone()
                    header.tvCheckoutHeaderAddressName.animateGone()
                }
            }
        })

        initViewModel()
    }

    private fun initViewModel() {
        viewModel.isOneClickShipment = isOneClickShipment
        viewModel.isTradeIn = isTradeIn
        viewModel.deviceId = deviceId
        viewModel.checkoutLeasingId = checkoutLeasingId
        viewModel.isPlusSelected = isPlusSelected
        observeData()

        viewModel.loadSAF(
            skipUpdateOnboardingState = true,
            isReloadData = false,
            isReloadAfterPriceChangeHigher = false
        )
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        viewModel.listData.observe(viewLifecycleOwner) {
            val diffResult = DiffUtil.calculateDiff(CheckoutDiffUtilCallback(it, adapter.list))
            adapter.list = it
            if (binding.rvCheckout.isComputingLayout) {
                binding.rvCheckout.post {
                    diffResult.dispatchUpdatesTo(adapter)
                }
            } else {
                diffResult.dispatchUpdatesTo(adapter)
            }

            it.address()?.recipientAddressModel?.also { address ->
                header.tvCheckoutHeaderAddressName.text =
                    "${address.addressName} • ${address.recipientName}"
            }
        }

        viewModel.pageState.observe(viewLifecycleOwner) {
            when (it) {
                is CheckoutPageState.CacheExpired -> {
                    onCacheExpired(it.errorMessage)
                    stopTrace()
                }

                is CheckoutPageState.CheckNoAddress -> {
                    // no-op
                }

                CheckoutPageState.EmptyData -> {
                    onEmptyData()
                    stopTrace()
                }

                is CheckoutPageState.Error -> {
                    hideLoading()
                    var errorMessage = it.throwable.message ?: ""
                    if (!(it.throwable is CartResponseErrorException || it.throwable is AkamaiErrorException)) {
                        errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    }
                    if (errorMessage.isEmpty()) {
                        errorMessage =
                            getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_error_global_message)
                    }
                    Toaster.build(binding.root, errorMessage, type = Toaster.TYPE_ERROR).show()
                    stopTrace()
                }

                CheckoutPageState.Loading -> {
                    showLoading()
                }

                is CheckoutPageState.NoAddress -> {
                    val token = Token()
                    token.ut = it.cartShipmentAddressFormData.keroUnixTime
                    token.districtRecommendation = it.cartShipmentAddressFormData.keroDiscomToken
                    if (it.eligible) {
                        val intent =
                            RouteManager.getIntent(
                                activity,
                                ApplinkConstInternalLogistic.ADD_ADDRESS_V3
                            )
                        intent.putExtra(CheckoutConstant.KERO_TOKEN, token)
                        intent.putExtra(
                            ChooseAddressBottomSheet.EXTRA_REF,
                            CartConstant.SCREEN_NAME_CART_NEW_USER
                        )
                        intent.putExtra(
                            ApplinkConstInternalLogistic.PARAM_SOURCE,
                            AddEditAddressSource.CART.source
                        )
                        startActivityForResult(
                            intent,
                            LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY
                        )
                    } else {
                        val intent =
                            RouteManager.getIntent(
                                activity,
                                ApplinkConstInternalLogistic.ADD_ADDRESS_V2
                            )
                        intent.putExtra(CheckoutConstant.KERO_TOKEN, token)
                        intent.putExtra(
                            ChooseAddressBottomSheet.EXTRA_REF,
                            CartConstant.SCREEN_NAME_CART_NEW_USER
                        )
                        startActivityForResult(
                            intent,
                            LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY
                        )
                    }
                    stopTrace()
                }

                is CheckoutPageState.NoMatchedAddress -> {
                    onNoMatchedAddress(it.state)
                    stopTrace()
                }

                is CheckoutPageState.Success -> {
                    hideLoading()
                    updateLocalCacheAddressData(it.cartShipmentAddressFormData.groupAddress.first().userAddress)
                    binding.rvCheckout.isVisible = true
                    if (it.cartShipmentAddressFormData.popUpMessage.isNotEmpty()) {
                        showToastNormal(it.cartShipmentAddressFormData.popUpMessage)
                    }
                    val popUpData = it.cartShipmentAddressFormData.popup
                    if (popUpData.title.isNotEmpty() && popUpData.description.isNotEmpty()) {
                        showPopUp(popUpData)
                    }
                    stopTrace()
                    if (it.cartShipmentAddressFormData.epharmacyData.showImageUpload) {
                        val uploadPrescriptionUiModel =
                            (viewModel.listData.value.firstOrNullInstanceOf(CheckoutEpharmacyModel::class.java) as? CheckoutEpharmacyModel)?.epharmacy
                        delayEpharmacyProcess(uploadPrescriptionUiModel)
                    }
                    setCampaignTimer()
                }

                is CheckoutPageState.Normal -> {
                    hideLoading()
                }

                is CheckoutPageState.ScrollTo -> {
                    binding.rvCheckout.scrollToPosition(it.index)
                }

                is CheckoutPageState.PriceValidation -> {
                    hideLoading()
                    renderCheckoutPriceUpdated(it.priceValidationData)
                }

                is CheckoutPageState.Prompt -> {
                    hideLoading()
                    renderPrompt(it.prompt)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.commonToaster.collect {
                var message = it.toasterMessage
                if (message.isEmpty()) {
                    message = it.throwable?.message ?: ""
                    if (!(it.throwable is CartResponseErrorException || it.throwable is AkamaiErrorException)) {
                        message = ErrorHandler.getErrorMessage(context, it.throwable)
                    }
                    if (message.isEmpty()) {
                        message =
                            getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_error_global_message)
                    }
                }
                Toaster.build(binding.root, message, type = it.toasterType).show()
            }
        }
    }

    private fun onCacheExpired(message: String?) {
        if (activity != null && view != null) {
            val intent = Intent()
            intent.putExtra(CheckoutConstant.EXTRA_CACHE_EXPIRED_ERROR_MESSAGE, message)
            activity!!.setResult(CheckoutConstant.RESULT_CHECKOUT_CACHE_EXPIRED, intent)
            activity!!.finish()
        }
    }

    private fun onEmptyData() {
        if (activity != null) {
            activity!!.finish()
        }
    }

    private fun onNoMatchedAddress(
        addressState: Int
    ) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
        intent.putExtra(CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS, addressState)
        intent.putExtra(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_SNIPPET, true)
        intent.putExtra(ApplinkConstInternalLogistic.PARAM_SOURCE, ManageAddressSource.CART.source)
        startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS)
    }

    private fun showToastNormal(message: String) {
        view?.let { v ->
            val actionText =
                activity!!.getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_toaster_action_ok)
            val listener = View.OnClickListener { }
            Toaster.build(
                v,
                message,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_NORMAL,
                actionText,
                listener
            )
                .show()
        }
    }

    private fun showPopUp(popUpData: PopUpData) {
        if (activity != null) {
            val popUpDialog =
                DialogUnify(activity!!, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            popUpDialog.setTitle(popUpData.title)
            popUpDialog.setDescription(popUpData.description)
            popUpDialog.setPrimaryCTAText(popUpData.button.text)
            popUpDialog.setPrimaryCTAClickListener {
                popUpDialog.dismiss()
            }
            popUpDialog.show()
        }
    }

    fun showToastErrorAkamai(message: String?) {
        if (toasterErrorAkamai == null) {
            val actionText =
                activity!!.getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_toaster_action_ok)
            toasterErrorAkamai = Toaster.build(
                view!!,
                message!!,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                actionText
            )
        }
        if (toasterErrorAkamai?.isShownOrQueued == false) {
            toasterErrorAkamai?.show()
        }
    }

    fun onBackPressed() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickBackArrow()
        if (isTradeIn) {
            checkoutTradeInAnalytics.eventTradeInClickBackButton(viewModel.isTradeInByDropOff)
        }
        val epharmacy = viewModel.validatePrescriptionOnBackPressed()
        if (epharmacy == null) {
            finish()
        } else {
            showPrescriptionReminderDialog(epharmacy.epharmacy)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS -> {
                onResultFromRequestCodeAddressOptions(resultCode, data)
            }

            LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY -> {
                onResultFromAddNewAddress(resultCode, data)
            }

            REQUEST_CODE_COURIER_PINPOINT -> {
                onResultFromCourierPinpoint(resultCode, data)
            }

            REQUEST_CODE_UPSELL -> {
                onResultFromUpsell(data)
            }

            CheckoutConstant.REQUEST_ADD_ON_PRODUCT_LEVEL_BOTTOMSHEET -> {
                onUpdateResultAddOnProductLevelBottomSheet(data)
            }

            CheckoutConstant.REQUEST_ADD_ON_ORDER_LEVEL_BOTTOMSHEET -> {
                onUpdateResultAddOnOrderLevelBottomSheet(data)
            }

            REQUEST_CODE_UPLOAD_PRESCRIPTION -> {
                onUploadPrescriptionResult(data, false)
            }

            REQUEST_CODE_MINI_CONSULTATION -> {
                onMiniConsultationResult(resultCode, data)
            }

            REQUEST_CODE_PROMO -> {
                onResultFromPromo(resultCode, data)
            }
        }
    }

    private fun onResultFromRequestCodeAddressOptions(resultCode: Int, data: Intent?) {
        when (resultCode) {
            CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS -> {
                val currentAddress = viewModel.listData.value.address()?.recipientAddressModel
                val chosenAddressModel =
                    data!!.getParcelableExtra<ChosenAddressModel>(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA)
                if (currentAddress != null && chosenAddressModel != null) {
                    viewModel.changeAddress(
                        currentAddress,
                        chosenAddressModel,
                        false
                    )
                }
            }

            Activity.RESULT_CANCELED -> if (activity != null && data == null && viewModel.listData.value.isEmpty()) {
                activity!!.finish()
            }

            else -> viewModel.loadSAF(
                isReloadData = false,
                skipUpdateOnboardingState = false,
                isReloadAfterPriceChangeHigher = false
            )
        }
    }

    private fun onResultFromAddNewAddress(resultCode: Int, data: Intent?) {
        val activity: Activity? = activity
        if (activity != null) {
            if (resultCode == Activity.RESULT_CANCELED) {
                activity.finish()
            } else {
                if (data != null) {
                    val addressDataModel = data.getParcelableExtra<SaveAddressDataModel>(
                        LogisticConstant.EXTRA_ADDRESS_NEW
                    )
                    addressDataModel?.let { updateLocalCacheAddressData(it) }
                }
                viewModel.loadSAF(
                    isReloadData = false,
                    skipUpdateOnboardingState = false,
                    isReloadAfterPriceChangeHigher = false
                )
            }
        }
    }

    @Suppress("ImplicitDefaultLocale")
    private fun updateLocalCacheAddressData(saveAddressDataModel: SaveAddressDataModel) {
        val activity: Activity? = activity
        if (activity != null) {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                activity,
                saveAddressDataModel.id.toString(),
                saveAddressDataModel.cityId.toString(),
                saveAddressDataModel.districtId.toString(),
                saveAddressDataModel.latitude,
                saveAddressDataModel.longitude,
                String.format(
                    "%s %s",
                    saveAddressDataModel.addressName,
                    saveAddressDataModel.receiverName
                ),
                saveAddressDataModel.postalCode,
                saveAddressDataModel.shopId.toString(),
                saveAddressDataModel.warehouseId.toString(),
                TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(saveAddressDataModel.warehouses),
                saveAddressDataModel.serviceType,
                ""
            )
        }
    }

    @Suppress("ImplicitDefaultLocale")
    fun updateLocalCacheAddressData(userAddress: UserAddress) {
        val activity: Activity? = activity
        if (activity != null) {
            val lca = ChooseAddressUtils.getLocalizingAddressData(
                activity
            )
            val tokonow = userAddress.tokoNow
            if (userAddress.state == UserAddress.STATE_ADDRESS_ID_NOT_MATCH || lca.address_id.isEmpty() || lca.address_id == "0") {
                ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                    activity,
                    userAddress.addressId,
                    userAddress.cityId,
                    userAddress.districtId,
                    userAddress.latitude,
                    userAddress.longitude,
                    String.format("%s %s", userAddress.addressName, userAddress.receiverName),
                    userAddress.postalCode,
                    if (tokonow.isModified) tokonow.shopId else lca.shop_id,
                    if (tokonow.isModified) tokonow.warehouseId else lca.warehouse_id,
                    if (tokonow.isModified) {
                        TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(
                            tokonow.warehouses
                        )
                    } else {
                        lca.warehouses
                    },
                    if (tokonow.isModified) tokonow.serviceType else lca.service_type,
                    ""
                )
            } else if (tokonow.isModified) {
                ChooseAddressUtils.updateTokoNowData(
                    activity,
                    tokonow.warehouseId,
                    tokonow.shopId,
                    TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(tokonow.warehouses),
                    tokonow.serviceType
                )
            }
        }
    }

    private fun onResultFromCourierPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data!!.extras != null) {
            val locationPass = getLocationPassFromIntent(data)
            if (locationPass != null) {
                viewModel.editAddressPinpoint(
                    locationPass.latitude,
                    locationPass.longitude,
                    locationPass
                )
            }
        } else {
//            shipmentAdapter.lastServiceId = 0
        }
    }

    private fun getLocationPassFromIntent(data: Intent): LocationPass? {
        var locationPass =
            data.extras!!.getParcelable<LocationPass>(LogisticConstant.EXTRA_EXISTING_LOCATION)
        if (locationPass == null) {
            val addressData =
                data.getParcelableExtra<SaveAddressDataModel>(AddressConstant.EXTRA_SAVE_DATA_UI_MODEL)
            if (addressData != null) {
                locationPass = LocationPass()
                locationPass.latitude = addressData.latitude
                locationPass.longitude = addressData.longitude
                locationPass.districtName = addressData.districtName
                locationPass.cityName = addressData.cityName
            }
        }
        return locationPass
    }

    fun showLoading() {
        if (context != null && loader?.dialog?.isShowing != true) {
            loader = LoaderDialog(context!!)
            loader!!.show()
        }
    }

    fun hideLoading() {
        if (loader != null && loader!!.dialog.isShowing) {
            loader!!.dismiss()
        }
    }

    fun navigateToSetPinpoint(message: String, locationPass: LocationPass?) {
//        sendAnalyticsOnClickEditPinPointErrorValidation(message)
        if (view != null) {
            val toastRectangleBinding = ToastRectangleBinding.inflate(layoutInflater, null, false)
            toastRectangleBinding.tvMessage.text = message
            val toast = Toast(activity)
            toast.duration = Toast.LENGTH_LONG
            toast.view = toastRectangleBinding.root
            toast.show()
        } else {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
        if (activity != null) {
            navigateToPinpointActivity(locationPass)
        }
    }

    private fun navigateToPinpointActivity(locationPass: LocationPass?) {
        val activity: Activity? = activity
        if (activity != null) {
            if (PinpointRolloutHelper.eligibleForRevamp(activity, true)) {
                val bundle = Bundle()
                bundle.putBoolean(AddressConstant.EXTRA_IS_GET_PINPOINT_ONLY, true)
                if (locationPass?.latitude != null &&
                    locationPass.latitude.isNotEmpty() && locationPass.longitude != null &&
                    locationPass.longitude.isNotEmpty()
                ) {
                    bundle.putDouble(AddressConstant.EXTRA_LAT, locationPass.latitude.toDouble())
                    bundle.putDouble(
                        AddressConstant.EXTRA_LONG,
                        locationPass.longitude.toDouble()
                    )
                }
                bundle.putString(AddressConstant.EXTRA_CITY_NAME, locationPass?.cityName)
                bundle.putString(AddressConstant.EXTRA_DISTRICT_NAME, locationPass?.districtName)
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.PINPOINT)
                intent.putExtra(AddressConstant.EXTRA_BUNDLE, bundle)
                startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT)
            } else {
                val intent =
                    RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
                val bundle = Bundle()
                bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
                bundle.putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
                intent.putExtras(bundle)
                startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkCampaignTimer()
    }

    companion object {

        private const val REQUEST_CODE_COURIER_PINPOINT = 13

        private const val REQUEST_CODE_UPSELL = 777

        const val REQUEST_CODE_UPLOAD_PRESCRIPTION = 10021
        const val REQUEST_CODE_MINI_CONSULTATION = 10022

        private const val REQUEST_CODE_PROMO = 954

        private const val SHIPMENT_TRACE = "mp_shipment"

        private const val KEY_UPLOAD_PRESCRIPTION_IDS_EXTRA = "epharmacy_prescription_ids"
        private const val KEY_PREFERENCE_COACHMARK_EPHARMACY = "has_seen_epharmacy_coachmark"

        fun newInstance(
            isOneClickShipment: Boolean,
            leasingId: String,
            pageSource: String,
            isPlusSelected: Boolean,
            bundle: Bundle?
        ): CheckoutFragment {
            val b = bundle ?: Bundle()
            b.putString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID, leasingId)
            if (leasingId.isNotEmpty()) {
                b.putBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT, true)
            } else {
                b.putBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT, isOneClickShipment)
            }
            b.putString(ShipmentFragment.ARG_CHECKOUT_PAGE_SOURCE, pageSource)
            b.putBoolean(ShipmentFragment.ARG_IS_PLUS_SELECTED, isPlusSelected)
            val checkoutFragment = CheckoutFragment()
            checkoutFragment.arguments = b
            return checkoutFragment
        }
    }

    // region adapter listener

    override fun onChangeAddress() {
        if (!viewModel.isLoading()) {
            val intent =
                RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
            intent.putExtra(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS, true)
            intent.putExtra(
                ApplinkConstInternalLogistic.PARAM_SOURCE,
                ManageAddressSource.CHECKOUT.source
            )
            startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS)
        }
    }

    override fun onViewNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel) {
        checkoutAnalyticsCourierSelection.eventViewNewUpsell(shipmentUpsellModel.isSelected)
    }

    override fun onClickApplyNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel) {
        startActivityForResult(
            UpsellWebViewActivity.getStartIntent(
                requireContext(),
                shipmentUpsellModel.appLink,
                showToolbar = true,
                allowOverride = true,
                needLogin = false,
                title = ""
            ),
            REQUEST_CODE_UPSELL
        )
        checkoutAnalyticsCourierSelection.eventClickNewUpsell(shipmentUpsellModel.isSelected)
    }

    override fun onClickCancelNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel) {
        viewModel.isPlusSelected = false
//        viewModel.cancelUpsell(
//            true,
//            true,
//            false
//        )
        checkoutAnalyticsCourierSelection.eventClickNewUpsell(shipmentUpsellModel.isSelected)
    }

    private fun onResultFromUpsell(data: Intent?) {
        if (data != null && data.hasExtra(CartConstant.CHECKOUT_IS_PLUS_SELECTED)) {
            viewModel.isPlusSelected =
                data.getBooleanExtra(CartConstant.CHECKOUT_IS_PLUS_SELECTED, false)
            viewModel.loadSAF(
                isReloadData = true,
                skipUpdateOnboardingState = true,
                isReloadAfterPriceChangeHigher = false
            )
        }
    }

    override fun onViewFreeShippingPlusBadge() {
        checkoutAnalyticsCourierSelection.eventViewGotoplusTicker()
    }

    override fun onCheckboxAddonProductListener(
        isChecked: Boolean,
        addOnProductDataItemModel: AddOnProductDataItemModel,
        product: CheckoutProductModel,
        bindingAdapterPosition: Int
    ) {
        viewModel.setAddon(isChecked, addOnProductDataItemModel, product, bindingAdapterPosition)
//        if (isChecked) {
//            addOnProductDataItemModel.status = AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK
//        } else {
//            addOnProductDataItemModel.status = AddOnConstant.ADD_ON_PRODUCT_STATUS_UNCHECK
//        }
//        cartItemModel.addOnProduct.listAddOnProductData.forEach {
//            if (it.uniqueId == addOnProductDataItemModel.uniqueId) {
//                it.status = addOnProductDataItemModel.status
//            }
//        }
//        shipmentViewModel.saveAddOnsProduct(cartItemModel)
//        shipmentAdapter.checkHasSelectAllCourier(true, -1, "", false, false)
//        shipmentAdapter.updateSubtotal()
//
        checkoutAnalyticsCourierSelection.eventClickAddOnsProductServiceWidget(addOnProductDataItemModel.type, product.productId.toString(), isChecked)
    }

    override fun onClickAddonProductInfoIcon(addOnDataInfoLink: String) {
        RouteManager.route(context, "${ApplinkConst.WEBVIEW}?url=$addOnDataInfoLink")
    }

    override fun onClickSeeAllAddOnProductService(product: CheckoutProductModel) {
        val productId = product.productId
        val cartId = product.cartId
        val addOnIds = arrayListOf<Long>()
        product.addOnProduct.listAddOnProductData.forEach { addOnItem ->
            if (addOnItem.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK) {
                addOnIds.add(addOnItem.id)
            }
        }

        val price: Double
        val discountedPrice: Double
        if (product.campaignId == 0) {
            price = product.price
            discountedPrice = product.price
        } else {
            price = product.originalPrice
            discountedPrice = product.price
        }

        val applinkAddon = ApplinkConst.ADDON.replace(AddOnConstant.QUERY_PARAM_ADDON_PRODUCT, productId.toString())
        val applink = UriUtil.buildUriAppendParams(
            applinkAddon,
            mapOf(
                AddOnConstant.QUERY_PARAM_CART_ID to cartId,
                AddOnConstant.QUERY_PARAM_SELECTED_ADDON_IDS to addOnIds.toString().replace("[", "").replace("]", ""),
                AddOnConstant.QUERY_PARAM_PAGE_ATC_SOURCE to AddOnConstant.SOURCE_NORMAL_CHECKOUT,
                ApplinkConstInternalMechant.QUERY_PARAM_WAREHOUSE_ID to product.warehouseId,
                AddOnConstant.QUERY_PARAM_IS_TOKOCABANG to product.isTokoCabang,
                AddOnConstant.QUERY_PARAM_CATEGORY_ID to product.productCatId,
                AddOnConstant.QUERY_PARAM_SHOP_ID to product.shopId,
                AddOnConstant.QUERY_PARAM_QUANTITY to product.quantity,
                AddOnConstant.QUERY_PARAM_PRICE to price.toString().removeSingleDecimalSuffix(),
                AddOnConstant.QUERY_PARAM_DISCOUNTED_PRICE to discountedPrice.toString().removeSingleDecimalSuffix()
            )
        )

        activity?.let {
            val intent = RouteManager.getIntent(it, applink)
            startActivityForResult(
                intent,
                ShipmentFragment.REQUEST_CODE_ADD_ON_PRODUCT_SERVICE_BOTTOMSHEET
            )
        }
        checkoutAnalyticsCourierSelection.eventClickLihatSemuaAddOnsProductServiceWidget()
    }

    override fun onImpressionAddOnProductService(addonType: Int, productId: String) {
        checkoutAnalyticsCourierSelection.eventViewAddOnsProductServiceWidget(addonType, productId)
    }

    override fun onClickAddOnGiftingProductLevel(
        product: CheckoutProductModel
    ) {
        if (activity != null) {
            val addOnsDataModel = product.addOnGiftingProductLevelModel
            val addOnBottomSheetModel = addOnsDataModel.addOnsBottomSheetModel

            // No need to open add on bottom sheet if action = 0
            if (addOnsDataModel.addOnsButtonModel.action == 0) return
            var availableBottomSheetData = AvailableBottomSheetData()
            var unavailableBottomSheetData = UnavailableBottomSheetData()
            if (addOnsDataModel.status == ShipmentFragment.ADD_ON_STATUS_DISABLE) {
//                unavailableBottomSheetData =
//                    ShipmentAddOnMapper.mapUnavailableBottomSheetProductLevelData(
//                        addOnBottomSheetModel,
//                        cartItemModel
//                    )
//                val listUnavailableProduct = arrayListOf<com.tokopedia.purchase_platform.common.feature.gifting.domain.model.Product>()
//                for ((productImageUrl, productName) in addOnBottomSheetModel.products) {
//                    val product =
//                        com.tokopedia.purchase_platform.common.feature.gifting.domain.model.Product()
//                    product.cartId = cartItemModel.cartId.toString()
//                    product.productId = cartItemModel.productId.toString()
//                    product.productPrice = cartItemModel.price.toLong()
//                    product.productQuantity = cartItemModel.quantity
//                    product.productName = productName
//                    product.productImageUrl = productImageUrl
//                    product.productParentId = cartItemModel.variantParentId
//                    listUnavailableProduct.add(product)
//                }

                unavailableBottomSheetData = UnavailableBottomSheetData(
                    description = addOnBottomSheetModel.description,
                    tickerMessage = addOnBottomSheetModel.ticker.text,
                    unavailableProducts = addOnBottomSheetModel.products.map {
                        GiftingProduct(
                            product.cartId.toString(),
                            product.productId.toString(),
                            it.productName,
                            it.productImageUrl,
                            product.price.toLong(),
                            product.quantity,
                            product.variantParentId
                        )
                    }
                )
            }
            if (addOnsDataModel.status == ShipmentFragment.ADD_ON_STATUS_ACTIVE) {
                availableBottomSheetData = AvailableBottomSheetData(
                    addOnInfoWording = AddOnWordingData(
                        product.addOnGiftingWording.packagingAndGreetingCard.replace(
                            ShipmentAddOnMapper.QTY,
                            product.quantity.toString()
                        ),
                        product.addOnGiftingWording.onlyGreetingCard.replace(
                            ShipmentAddOnMapper.QTY,
                            product.quantity.toString()
                        ),
                        product.addOnGiftingWording.invoiceNotSendToRecipient
                    ),
                    shopName = product.shopName,
                    products = listOf(
                        GiftingProduct(
                            product.cartId.toString(),
                            product.productId.toString(),
                            product.name,
                            product.imageUrl,
                            product.price.toLong(),
                            product.quantity,
                            product.variantParentId
                        )
                    ),
                    addOnSavedStates = product.addOnGiftingProductLevelModel.addOnsDataItemModelList.map {
                        AddOnData(
                            it.addOnId,
                            it.addOnUniqueId,
                            AddOnMetadata(
                                AddOnNote(
                                    it.addOnMetadata.addOnNoteItemModel.from,
                                    it.addOnMetadata.addOnNoteItemModel.isCustomNote,
                                    it.addOnMetadata.addOnNoteItemModel.notes,
                                    it.addOnMetadata.addOnNoteItemModel.to
                                )
                            ),
                            it.addOnPrice,
                            it.addOnQty.toInt()
                        )
                    },
                    cartString = product.cartStringGroup,
                    isTokoCabang = product.isTokoCabang,
                    warehouseId = product.warehouseId,
                    defaultFrom = product.addOnDefaultFrom,
                    defaultTo = product.addOnDefaultTo
                )
            }
            val addOnProductData = ShipmentAddOnMapper.mapAddOnBottomSheetParam(
                addOnsDataModel,
                availableBottomSheetData,
                unavailableBottomSheetData,
                isOneClickShipment
            )
            val intent =
                RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.ADD_ON_GIFTING)
            intent.putExtra(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA, addOnProductData)
            intent.putExtra(AddOnConstant.EXTRA_ADD_ON_SOURCE, AddOnConstant.ADD_ON_SOURCE_CHECKOUT)
            startActivityForResult(
                intent,
                CheckoutConstant.REQUEST_ADD_ON_PRODUCT_LEVEL_BOTTOMSHEET
            )
            checkoutAnalyticsCourierSelection.eventClickAddOnsDetail(product.productId.toString())
        }
    }

    override fun onImpressionAddOnGiftingProductLevel(productId: String) {
        checkoutAnalyticsCourierSelection.eventViewAddOnsWidget(productId)
    }

    private fun onUpdateResultAddOnProductLevelBottomSheet(data: Intent?) {
        if (data != null) {
            val saveAddOnStateResult =
                data.getParcelableExtra<SaveAddOnStateResult>(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA_RESULT)
            if (saveAddOnStateResult != null) {
                viewModel.updateAddOnGiftingProductLevelDataBottomSheet(saveAddOnStateResult)
            }
        }
    }

    override fun openAddOnGiftingOrderLevelBottomSheet(order: CheckoutOrderModel) {
        if (activity != null) {
            val addOnsDataModel = order.addOnsOrderLevelModel
            val addOnBottomSheetModel = addOnsDataModel.addOnsBottomSheetModel

            // No need to open add on bottom sheet if action = 0
            if (addOnsDataModel.addOnsButtonModel.action == 0) return
            var availableBottomSheetData = AvailableBottomSheetData()
            var unavailableBottomSheetData = UnavailableBottomSheetData()
            val orderProducts = viewModel.getOrderProducts(order.cartStringGroup)
            if (addOnsDataModel.status == ShipmentFragment.ADD_ON_STATUS_DISABLE) {
                val listUnavailableProduct: MutableList<GiftingProduct> =
                    arrayListOf()
                for ((_, productName) in addOnBottomSheetModel.products) {
                    for (item in orderProducts) {
                        if (productName.equals(item.name, ignoreCase = true)) {
                            val product = GiftingProduct()
                            product.cartId = item.cartId.toString()
                            product.productId = item.productId.toString()
                            product.productPrice = item.price.toLong()
                            product.productQuantity = item.quantity
                            product.productName = item.name
                            product.productImageUrl = item.imageUrl
                            product.productParentId = item.variantParentId
                            listUnavailableProduct.add(product)
                            break
                        }
                    }
                }

                unavailableBottomSheetData = UnavailableBottomSheetData(
                    description = addOnBottomSheetModel.description,
                    tickerMessage = addOnBottomSheetModel.ticker.text,
                    unavailableProducts = listUnavailableProduct
                )
            }
            if (addOnsDataModel.status == ShipmentFragment.ADD_ON_STATUS_ACTIVE) {
//                availableBottomSheetData =
//                    ShipmentAddOnMapper.mapAvailableBottomSheetOrderLevelData(
//                        addOnWordingModel!!,
//                        cartItemModel
//                    )
                val addOnsDataModel = order.addOnsOrderLevelModel

                val addOnWordingData = AddOnWordingData()
                val addOnWordingModel = order.addOnWordingModel
                addOnWordingData.onlyGreetingCard = addOnWordingModel.onlyGreetingCard
                addOnWordingData.packagingAndGreetingCard =
                    addOnWordingModel.packagingAndGreetingCard
                addOnWordingData.invoiceNotSendToRecipient =
                    addOnWordingModel.invoiceNotSendToRecipient

                val listProduct =
                    arrayListOf<GiftingProduct>()
                for (cartItemModel in orderProducts) {
                    val product = GiftingProduct()
                    product.cartId = cartItemModel.cartId.toString()
                    product.productId = cartItemModel.productId.toString()
                    product.productName = cartItemModel.name
                    product.productPrice = cartItemModel.price.toLong()
                    product.productQuantity = cartItemModel.quantity
                    product.productImageUrl = cartItemModel.imageUrl
                    product.productParentId = cartItemModel.variantParentId
                    listProduct.add(product)
                }

                val addOnDataList = arrayListOf<AddOnData>()
                if (addOnsDataModel.addOnsDataItemModelList.isNotEmpty()) {
                    for (addOnItemModel in addOnsDataModel.addOnsDataItemModelList) {
                        val addOnData = AddOnData()
                        addOnData.addOnId = addOnItemModel.addOnId
                        addOnData.addOnPrice = addOnItemModel.addOnPrice
                        addOnData.addOnQty = addOnItemModel.addOnQty.toInt()
                        val addOnNote = AddOnNote()
                        val addOnNoteItemModel = addOnItemModel.addOnMetadata.addOnNoteItemModel
                        addOnNote.isCustomNote = addOnNoteItemModel.isCustomNote
                        addOnNote.notes = addOnNoteItemModel.notes
                        addOnNote.from = addOnNoteItemModel.from
                        addOnNote.to = addOnNoteItemModel.to
                        val addOnMetadata = AddOnMetadata()
                        addOnMetadata.addOnNote = addOnNote
                        addOnData.addOnMetadata = addOnMetadata
                        addOnDataList.add(addOnData)
                    }
                }

                availableBottomSheetData = AvailableBottomSheetData(
                    addOnInfoWording = addOnWordingData,
                    shopName = order.shopName,
                    products = listProduct,
                    addOnSavedStates = addOnDataList,
                    cartString = order.cartStringGroup,
                    isTokoCabang = order.isFulfillment,
                    warehouseId = order.fulfillmentId.toString(),
                    defaultFrom = order.addOnDefaultFrom,
                    defaultTo = order.addOnDefaultTo
                )
            }
            val addOnProductData = ShipmentAddOnMapper.mapAddOnBottomSheetParam(
                addOnsDataModel,
                availableBottomSheetData,
                unavailableBottomSheetData,
                isOneClickShipment
            )
            val intent =
                RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.ADD_ON_GIFTING)
            intent.putExtra(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA, addOnProductData)
            startActivityForResult(intent, CheckoutConstant.REQUEST_ADD_ON_ORDER_LEVEL_BOTTOMSHEET)
            checkoutAnalyticsCourierSelection.eventClickAddOnsDetail(order.cartStringGroup)
        }
    }

    override fun addOnGiftingOrderLevelImpression(order: CheckoutOrderModel) {
        val listCartString = ArrayList<String>()
        val products = viewModel.getOrderProducts(order.cartStringGroup)
        for (cartItemModel in products) {
            listCartString.add(cartItemModel.cartStringGroup)
        }
        checkoutAnalyticsCourierSelection.eventViewAddOnsWidget(listCartString.toString())
    }

    private fun onUpdateResultAddOnOrderLevelBottomSheet(data: Intent?) {
//        if (data != null) {
//            val saveAddOnStateResult =
//                data.getParcelableExtra<SaveAddOnStateResult>(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA_RESULT)
//            if (saveAddOnStateResult != null) {
//                viewModel.updateAddOnGiftingOrderLevelDataBottomSheet(saveAddOnStateResult)
//            }
//        }
    }

    override fun onLoadShippingState(order: CheckoutOrderModel, position: Int) {
        viewModel.loadShipping(order, position)
    }

    override fun onChangeShippingDuration(order: CheckoutOrderModel, position: Int) {
        if (!viewModel.isLoading()) {
            showShippingDurationBottomsheet(
                order,
                viewModel.listData.value.address()!!.recipientAddressModel,
                position
            )
        }
    }

    private fun showShippingDurationBottomsheet(
        order: CheckoutOrderModel,
        recipientAddressModel: RecipientAddressModel,
        cartPosition: Int
    ) {
        if (order.shopShipmentList.isEmpty()) {
            onNoCourierAvailable(getString(com.tokopedia.logisticcart.R.string.label_no_courier_bottomsheet_message))
        } else {
//            val shipmentDetailData =
//                getShipmentDetailData(order, recipientAddressModel)
//            var codHistory = -1
//            if (viewModel.codData != null) {
//                codHistory = viewModel.codData!!.counterCod
//            }
            val activity: Activity? = activity
            if (activity != null) {
//                val pslCode = RatesDataConverter.getLogisticPromoCode(order)
//                val products = viewModel.getProductForRatesRequest(order)
                ShippingDurationBottomsheet.show(
                    fragmentManager = parentFragmentManager,
                    shippingDurationBottomsheetListener = this,
                    ratesParam = viewModel.generateRatesParam(order),
                    selectedSpId = order.shipment.courierItemData?.selectedShipper?.shipperProductId ?: -1,
//                    shipmentDetailData = generateShippingBottomsheetParam(
//                        order,
//                        recipientAddressModel
//                    ),
                    selectedServiceId = order.shipment.courierItemData?.selectedShipper?.serviceId ?: -1,
//                    shopShipmentList = order.shopShipmentList,
//                    recipientAddressModel = recipientAddressModel,
//                    cartPosition = cartPosition,
//                    codHistory = codHistory,
//                    isLeasing = order.isLeasingProduct,
//                    pslCode = order.shipment.courierItemData?.logPromoCode ?: "",
//                    products = products,
//                    cartString = order.cartStringGroup,
//                    isDisableOrderPrioritas = true,
                    isRatesTradeInApi = viewModel.isTradeInByDropOff,
                    isDisableOrderPrioritas = true,
                    recipientAddressModel = recipientAddressModel,
                    cartPosition = cartPosition,
//                    isFulFillment = order.isFulfillment,
//                    preOrderTime = order.preOrderDurationDay,
//                    mvc = viewModel.generateRatesMvcParam(
//                        order.cartStringGroup
//                    ),
//                    cartData = viewModel.cartDataForRates,
                    isOcc = false
//                    warehouseId = order.fulfillmentId.toString()
                )
            }
        }
    }

    fun generateShippingBottomsheetParam(
        order: CheckoutOrderModel,
        recipientAddressModel: RecipientAddressModel
    ): ShipmentDetailData {
        val orderProducts = viewModel.getOrderProducts(order.cartStringGroup)
        var orderValue = 0L
        var totalWeight = 0.0
        var totalWeightActual = 0.0
        var productFInsurance = 0
        var preOrder = false
        var productPreOrderDuration = 0
        val productList: ArrayList<Product> = ArrayList()
        val categoryList: HashSet<String> = hashSetOf()
        orderProducts.forEach {
            if (!it.isError) {
                orderValue += (it.quantity * it.price).toLong()
                totalWeight += it.quantity * it.weight
                totalWeightActual += if (it.weightActual > 0) {
                    it.quantity * it.weightActual
                } else {
                    it.quantity * it.weight
                }
                if (it.fInsurance) {
                    productFInsurance = 1
                }
                preOrder = it.isPreOrder
                productPreOrderDuration = it.preOrderDurationDay
                categoryList.add(it.productCatId.toString())
                productList.add(Product(it.productId, it.isFreeShipping, it.isFreeShippingExtra))
            }
        }
//        if (orderShop.shouldValidateWeight() && totalWeight > orderShop.maximumWeight) {
//            // overweight
//            return null to productList
//        }
        return ShipmentDetailData().apply {
            shopId = order.shopId.toString()
            preorder = preOrder
            isBlackbox = order.isBlackbox
            this.isTradein = viewModel.isTradeIn
            addressId =
                if (recipientAddressModel.selectedTabIndex == 1 && recipientAddressModel.locationDataModel != null) recipientAddressModel.locationDataModel.addrId else order.addressId
            shipmentCartData = ShipmentCartData(
                originDistrictId = order.districtId,
                originPostalCode = order.postalCode,
                originLatitude = order.latitude,
                originLongitude = order.longitude,
                weight = totalWeight,
                weightActual = totalWeightActual,
                shopTier = order.shopTypeInfoData.shopTier,
                groupType = order.groupType,
                token = order.keroToken,
                ut = order.keroUnixTime,
                insurance = 1,
                productInsurance = productFInsurance,
                orderValue = orderValue,
                categoryIds = categoryList.joinToString(","),
                preOrderDuration = productPreOrderDuration,
                isFulfillment = order.isFulfillment,
                boMetadata = order.boMetadata,
                destinationAddress = recipientAddressModel.addressName,
                destinationDistrictId = recipientAddressModel.destinationDistrictId,
                destinationPostalCode = recipientAddressModel.postalCode,
                destinationLatitude = recipientAddressModel.latitude,
                destinationLongitude = recipientAddressModel.longitude
            )
        }
    }

    override fun onShippingDurationChoosen(
        shippingCourierUiModels: List<ShippingCourierUiModel>,
        selectedCourier: ShippingCourierUiModel?,
        recipientAddressModel: RecipientAddressModel?,
        cartPosition: Int,
        selectedServiceId: Int,
        serviceData: ServiceData,
        flagNeedToSetPinpoint: Boolean,
        isDurationClick: Boolean,
        isClearPromo: Boolean
    ) {
        val courierItemData: CourierItemData?
        if (selectedCourier != null) {
            courierItemData =
                shippingCourierConverter.convertToCourierItemDataNew(selectedCourier)
            if (isTradeIn) {
                checkoutTradeInAnalytics.eventClickKurirTradeIn(serviceData.serviceName)
            }
//        sendAnalyticsOnClickChecklistShipmentRecommendationDuration(serviceData.serviceName)
            // Has courier promo means that one of duration has promo, not always current selected duration.
            // It's for analytics purpose
            if (shippingCourierUiModels.isNotEmpty()) {
                val serviceDataTracker = shippingCourierUiModels[0].serviceData
//            sendAnalyticsOnClickDurationThatContainPromo(
//                serviceDataTracker.isPromo == 1,
//                serviceDataTracker.serviceName,
//                serviceDataTracker.codData.isCod == 1,
//                CurrencyFormatUtil.convertPriceValueToIdrFormat(
//                    serviceDataTracker.rangePrice.minPrice,
//                    false
//                ).removeDecimalSuffix(),
//                CurrencyFormatUtil.convertPriceValueToIdrFormat(
//                    serviceDataTracker.rangePrice.maxPrice,
//                    false
//                ).removeDecimalSuffix()
//            )
            }
            if (flagNeedToSetPinpoint) {
                // If instant courier and has not set pinpoint
//            shipmentAdapter.lastServiceId = selectedServiceId
//            setPinpoint(cartItemPosition)
            } else if (courierItemData == null) {
                // If there's no recommendation, user choose courier manually
//            val shipmentCartItemModel =
//                shipmentAdapter.getShipmentCartItemModelByIndex(cartItemPosition)
//            onChangeShippingCourier(
//                recipientAddressModel,
//                shipmentCartItemModel,
//                cartItemPosition,
//                shippingCourierUiModels
//            )
            } else {
                if (courierItemData.isUsePinPoint &&
                    (
                        recipientAddressModel!!.latitude == null ||
                            recipientAddressModel.latitude.equals(
                                    "0",
                                    ignoreCase = true
                                ) || recipientAddressModel.longitude == null ||
                            recipientAddressModel.longitude.equals("0", ignoreCase = true)
                        )
                ) {
                    setPinpoint(cartPosition)
                } else {
                    val shipmentCartItemModel =
                        viewModel.listData.value[cartPosition] as CheckoutOrderModel
//                    shipmentAdapter.getShipmentCartItemModelByIndex(cartItemPosition)!!
                    if (viewModel.isTradeInByDropOff) {
//                    shipmentAdapter.setSelectedCourierTradeInPickup(courierItemData)
//                    shipmentViewModel.processSaveShipmentState(shipmentCartItemModel)
                    } else {
//                    sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(courierItemData.name)
//                    sendAnalyticsOnViewPreselectedCourierAfterPilihDurasi(courierItemData.shipperProductId)

                        // Clear logistic voucher data when any duration is selected and voucher is not null
//                    if (shipmentCartItemModel.voucherLogisticItemUiModel != null &&
//                        !TextUtils.isEmpty(shipmentCartItemModel.voucherLogisticItemUiModel!!.code) && isClearPromo
//                    ) {
//                        val promoLogisticCode =
//                            shipmentCartItemModel.voucherLogisticItemUiModel!!.code
//                        shipmentViewModel.cancelAutoApplyPromoStackLogistic(
//                            0,
//                            promoLogisticCode,
//                            shipmentCartItemModel.cartStringGroup,
//                            shipmentCartItemModel.voucherLogisticItemUiModel!!.uniqueId,
//                            shipmentCartItemModel
//                        )
//                        val validateUsePromoRequest = shipmentViewModel.lastValidateUseRequest
//                        if (validateUsePromoRequest != null) {
//                            if (shipmentCartItemModel.isFreeShippingPlus) {
//                                for (ordersItem in validateUsePromoRequest.orders) {
//                                    if (ordersItem.cartStringGroup == shipmentCartItemModel.cartStringGroup && ordersItem.codes.size > 0) {
//                                        ordersItem.codes.remove(promoLogisticCode)
//                                        ordersItem.boCode = ""
//                                    }
//                                }
//                            } else {
//                                for (ordersItem in validateUsePromoRequest.orders) {
//                                    if (ordersItem.codes.size > 0) {
//                                        ordersItem.codes.remove(promoLogisticCode)
//                                        ordersItem.boCode = ""
//                                    }
//                                }
//                            }
//                        }
//                        shipmentCartItemModel.voucherLogisticItemUiModel = null
//                        shipmentAdapter.clearTotalPromoStackAmount()
//                        shipmentViewModel.updateShipmentCostModel()
//                    }
                        viewModel.setSelectedCourier(
                            cartPosition,
                            courierItemData,
                            shippingCourierUiModels,
                            selectedCourier.productData.insurance
                        )
//                    shipmentAdapter.setSelectedCourier(
//                        cartItemPosition,
//                        selectedCourier,
//                        true,
//                        false
//                    )
//                    shipmentViewModel.processSaveShipmentState(shipmentCartItemModel)
//                    shipmentAdapter.setShippingCourierViewModels(
//                        shippingCourierUiModels,
//                        selectedCourier,
//                        cartItemPosition
//                    )
                    }
                }
            }
        }
    }

    private fun setPinpoint(cartItemPosition: Int) {
//        shipmentAdapter.lastChooseCourierItemPosition = cartItemPosition
        val locationPass = LocationPass()
        val address = viewModel.listData.value.address()
        if (address != null) {
            locationPass.cityName = address.recipientAddressModel.cityName
            locationPass.districtName =
                address.recipientAddressModel.destinationDistrictName
            navigateToPinpointActivity(locationPass)
        }
    }

    override fun onLogisticPromoChosen(
        shippingCourierUiModels: List<ShippingCourierUiModel>,
        courierData: ShippingCourierUiModel,
        recipientAddressModel: RecipientAddressModel?,
        cartPosition: Int,
        serviceData: ServiceData,
        flagNeedToSetPinpoint: Boolean,
        promoCode: String,
        selectedServiceId: Int,
        logisticPromo: LogisticPromoUiModel
    ) {
        // do not set courier to shipment item before success validate use
        checkoutAnalyticsCourierSelection.eventClickPromoLogisticTicker(promoCode)
//        setStateLoadingCourierStateAtIndex(
//            cartPosition,
//            true
//        )
        val courierItemData = shippingCourierConverter.convertToCourierItemDataWithPromo(
            courierData,
            logisticPromo
        )
        val cartString = viewModel.listData.value[cartPosition].cartStringGroup
        if (!flagNeedToSetPinpoint) {
            val shipmentCartItemModel =
                viewModel.listData.value[cartPosition] as CheckoutOrderModel
            val validateUsePromoRequest = viewModel.generateValidateUsePromoRequest().copy()
            if (promoCode.isNotEmpty()) {
                for (order in validateUsePromoRequest.orders) {
                    if (order.cartStringGroup == shipmentCartItemModel.cartStringGroup && !order.codes.contains(
                            promoCode
                        )
                    ) {
                        if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                            // remove previous logistic promo code
                            order.codes.remove(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                        }
                        order.codes.add(promoCode)
                        order.boCode = promoCode
                    }
                }
            }
            val shipmentCartItemModelLists =
                viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)
            if (shipmentCartItemModelLists.isNotEmpty() && !shipmentCartItemModel.isFreeShippingPlus) {
                for (tmpShipmentCartItemModel in shipmentCartItemModelLists) {
                    for (order in validateUsePromoRequest.orders) {
                        if (shipmentCartItemModel.cartStringGroup != tmpShipmentCartItemModel.cartStringGroup && tmpShipmentCartItemModel.cartStringGroup == order.cartStringGroup && tmpShipmentCartItemModel.voucherLogisticItemUiModel != null &&
                            !tmpShipmentCartItemModel.isFreeShippingPlus
                        ) {
                            order.codes.remove(tmpShipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                            order.boCode = ""
                        }
                    }
                }
            }
            for (ordersItem in validateUsePromoRequest.orders) {
                if (ordersItem.cartStringGroup == shipmentCartItemModel.cartStringGroup) {
                    ordersItem.spId = courierItemData.shipperProductId
                    ordersItem.shippingId = courierItemData.shipperId
                    ordersItem.freeShippingMetadata = courierItemData.freeShippingMetadata
                    ordersItem.boCampaignId = courierItemData.boCampaignId
                    ordersItem.shippingSubsidy = courierItemData.shippingSubsidy
                    ordersItem.benefitClass = courierItemData.benefitClass
                    ordersItem.shippingPrice = courierItemData.shippingRate.toDouble()
                    ordersItem.etaText = courierItemData.etaText!!
                }
            }
            viewModel.doValidateUseLogisticPromoNew(
                cartPosition,
                cartString,
                validateUsePromoRequest,
                promoCode,
                showLoading = true,
                courierItemData,
                courierData.productData.insurance
            )
        }
    }

    override fun onNoCourierAvailable(message: String?) {
        if (message!!.contains(getString(R.string.corner_error_stub))) mTrackerCorner.sendViewCornerError()
        if (activity != null) {
            checkoutAnalyticsCourierSelection.eventViewCourierImpressionErrorCourierNoAvailable()
            val generalBottomSheet = GeneralBottomSheet()
            generalBottomSheet.setTitle(activity!!.getString(R.string.label_no_courier_bottomsheet_title))
            generalBottomSheet.setDesc(message)
            generalBottomSheet.setButtonText(activity!!.getString(R.string.label_no_courier_bottomsheet_button))
            generalBottomSheet.setIcon(R.drawable.checkout_module_ic_dropshipper)
            generalBottomSheet.setButtonOnClickListener { bottomSheet: BottomSheetUnify ->
                bottomSheet.dismiss()
            }
            generalBottomSheet.show(activity!!, parentFragmentManager)
        }
        if (isTradeIn) {
            checkoutTradeInAnalytics.eventTradeInClickCourierGetOutOfCoverageError(
                viewModel.isTradeIn
            )
        }
    }

    override fun onChangeShippingCourier(order: CheckoutOrderModel, position: Int) {
//        if (shipmentLoadingIndex == -1 && !shipmentViewModel.shipmentButtonPayment.value.loading) {
//            var shippingCourierUiModels: List<ShippingCourierUiModel>?
//            shippingCourierUiModels = selectedShippingCourierUiModels
//                ?: shipmentCartItemModel!!.selectedShipmentDetailData!!.shippingCourierViewModels
//            sendAnalyticsOnClickChangeCourierShipmentRecommendation(shipmentCartItemModel)
//            if (shippingCourierUiModels == null || shippingCourierUiModels.isEmpty() &&
//                shipmentViewModel.getShippingCourierViewModelsState(shipmentCartItemModel!!.orderNumber) != null
//            ) {
//                shippingCourierUiModels = shipmentViewModel.getShippingCourierViewModelsState(
//                    shipmentCartItemModel!!.orderNumber
//                )
//            }
        if (!viewModel.isLoading()) {
            val activity: Activity? = activity
            if (activity != null) {
                ShippingCourierBottomsheet.show(
                    fragmentManager!!,
                    this,
                    order.shipment.shippingCourierUiModels,
                    viewModel.listData.value.address()!!.recipientAddressModel,
                    position,
                    false
                )
//                shippingCourierUiModels?.let { checkHasCourierPromo(it) }
            }
        }
//        }
    }

    override fun onCourierChoosen(
        shippingCourierUiModel: ShippingCourierUiModel,
        courierItemData: CourierItemData,
        recipientAddressModel: RecipientAddressModel?,
        cartPosition: Int,
        isCod: Boolean,
        isPromoCourier: Boolean,
        isNeedPinpoint: Boolean,
        shippingCourierList: List<ShippingCourierUiModel>
    ) {
//        sendAnalyticsOnClickLogisticThatContainPromo(
//            isPromoCourier,
//            courierItemData.shipperProductId,
//            isCod
//        )
        if (isNeedPinpoint || courierItemData.isUsePinPoint && (
            recipientAddressModel!!.latitude == null ||
                recipientAddressModel.latitude.equals(
                        "0",
                        ignoreCase = true
                    ) || recipientAddressModel.longitude == null ||
                recipientAddressModel.longitude.equals("0", ignoreCase = true)
            )
        ) {
            setPinpoint(cartPosition)
        } else {
//            val shipmentCartItemModel =
//                shipmentAdapter.setSelectedCourier(cartItemPosition, courierItemData, true, false)!!
//            if (shipmentCartItemModel.selectedShipmentDetailData != null) {
//                shipmentCartItemModel.selectedShipmentDetailData!!.shippingCourierViewModels =
//                    shippingCourierList
//            }
//            shipmentViewModel.processSaveShipmentState(shipmentCartItemModel)
            viewModel.setSelectedCourier(
                cartPosition,
                courierItemData,
                shippingCourierList,
                shippingCourierUiModel.productData.insurance
            )
        }
    }

    override fun onCourierShipmentRecommendationCloseClicked() {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickXPadaKurirPengiriman()
    }

    override fun onChangeScheduleDelivery(
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel,
        order: CheckoutOrderModel,
        position: Int
    ) {
        if (view != null) {
            val courierItemData = order.shipment.courierItemData
            if (courierItemData != null) {
                val newCourierItemData =
                    CourierItemData.clone(courierItemData, scheduleDeliveryUiModel)
                viewModel.setSelectedScheduleDelivery(
                    position,
                    order,
                    courierItemData,
                    scheduleDeliveryUiModel,
                    newCourierItemData
                )
//                val hasNoPromo =
//                    TextUtils.isEmpty(courierItemData.selectedShipper.logPromoCode) && TextUtils.isEmpty(
//                        newCourierItemData.selectedShipper.logPromoCode
//                    )
//                if (scheduleDeliveryUiModel.isSelected) {
//                    order.scheduleDate = scheduleDeliveryUiModel.scheduleDate
//                    order.timeslotId = scheduleDeliveryUiModel.timeslotId
//                    order.validationMetadata =
//                        scheduleDeliveryUiModel.deliveryProduct.validationMetadata
//                } else {
//                    order.scheduleDate = ""
//                    order.timeslotId = 0
//                    order.validationMetadata = ""
//                }
//                val selectedShipper = newCourierItemData.selectedShipper
//                val shouldValidateUse =
//                    selectedShipper.logPromoCode != null && selectedShipper.logPromoCode!!.isNotEmpty()
// //                val hasCheckAllCourier =
// //                    shipmentAdapter.checkHasSelectAllCourier(true, -1, "", false, false)
//                val haveToClearCache = (
//                    (
//                        order.voucherLogisticItemUiModel != null && !TextUtils.isEmpty(
//                            order.voucherLogisticItemUiModel!!.code
//                        )
//                        ) ||
//                        !courierItemData.selectedShipper.logPromoCode.isNullOrEmpty()
//                    ) &&
//                    TextUtils.isEmpty(newCourierItemData.selectedShipper.logPromoCode)
// //                val shouldStopInClearCache = haveToClearCache && !hasCheckAllCourier
// //                val shouldStopInDoValidateUseLogistic = shouldValidateUse && !hasCheckAllCourier
// //                viewModel.setScheduleDeliveryMapData(
// //                    order.cartStringGroup,
// //                    ShipmentScheduleDeliveryMapData(
// //                        donePublisher,
// //                        shouldStopInClearCache,
// //                        shouldStopInDoValidateUseLogistic
// //                    )
// //                )
//                if (haveToClearCache) {
//                    val promoLogisticCode = order.voucherLogisticItemUiModel?.code
//                        ?: courierItemData.selectedShipper.logPromoCode!!
// //                    viewModel.cancelAutoApplyPromoStackLogistic(
// //                        position,
// //                        promoLogisticCode,
// //                        shipmentCartItemModel.cartStringGroup,
// //                        shipmentCartItemModel.voucherLogisticItemUiModel?.uniqueId
// //                            ?: shipmentCartItemModel.cartItemModels.first().cartStringOrder,
// //                        shipmentCartItemModel
// //                    )
// //                    val validateUsePromoRequest = viewModel.lastValidateUseRequest
// //                    if (validateUsePromoRequest != null) {
// //                        for (ordersItem in validateUsePromoRequest.orders) {
// //                            if (ordersItem.codes.size > 0) {
// //                                ordersItem.codes.remove(promoLogisticCode)
// //                                ordersItem.boCode = ""
// //                            }
// //                        }
// //                    }
//                    order.voucherLogisticItemUiModel = null
// //                    shipmentAdapter.clearTotalPromoStackAmount()
// //                    shipmentViewModel.updateShipmentCostModel()
// //                    shipmentViewModel.updateCheckoutButtonData()
//                }
// //                shipmentAdapter.setSelectedCourier(
// //                    position,
// //                    newCourierItemData,
// //                    true,
// //                    shouldValidateUse
// //                )
// //                shipmentViewModel.processSaveShipmentState(shipmentCartItemModel)
//                if (shouldValidateUse) {
// //                    val validateUsePromoRequest =
// //                        shipmentViewModel.generateValidateUsePromoRequest().copy()
// //                    if (selectedShipper.logPromoCode != null && selectedShipper.logPromoCode!!.isNotEmpty()) {
// //                        for (order in validateUsePromoRequest.orders) {
// //                            if (order.cartStringGroup == shipmentCartItemModel.cartStringGroup && !order.codes.contains(
// //                                    newCourierItemData.selectedShipper.logPromoCode
// //                                )
// //                            ) {
// //                                if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
// //                                    // remove previous logistic promo code
// //                                    order.codes.remove(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
// //                                }
// //                                order.codes.add(selectedShipper.logPromoCode!!)
// //                                order.boCode = selectedShipper.logPromoCode!!
// //                            }
// //                        }
// //                    }
// //                    val shipmentCartItemModelLists = shipmentAdapter.shipmentCartItemModelList
// //                    if (!shipmentCartItemModelLists.isNullOrEmpty()) {
// //                        for (tmpShipmentCartItemModel in shipmentCartItemModelLists) {
// //                            for (order in validateUsePromoRequest.orders) {
// //                                if (shipmentCartItemModel.cartStringGroup != tmpShipmentCartItemModel.cartStringGroup && tmpShipmentCartItemModel.cartStringGroup == order.cartStringGroup && tmpShipmentCartItemModel.voucherLogisticItemUiModel != null &&
// //                                    !tmpShipmentCartItemModel.isFreeShippingPlus
// //                                ) {
// //                                    order.codes.remove(tmpShipmentCartItemModel.voucherLogisticItemUiModel!!.code)
// //                                    order.boCode = ""
// //                                }
// //                            }
// //                        }
// //                    }
// //                    for (ordersItem in validateUsePromoRequest.orders) {
// //                        if (ordersItem.cartStringGroup == shipmentCartItemModel.cartStringGroup) {
// //                            ordersItem.spId = selectedShipper.shipperProductId
// //                            ordersItem.shippingId = selectedShipper.shipperId
// //                            ordersItem.freeShippingMetadata = selectedShipper.freeShippingMetadata
// //                            ordersItem.boCampaignId = selectedShipper.boCampaignId
// //                            ordersItem.shippingSubsidy = selectedShipper.shippingSubsidy
// //                            ordersItem.benefitClass = selectedShipper.benefitClass
// //                            ordersItem.shippingPrice = selectedShipper.shippingRate.toDouble()
// //                            ordersItem.etaText = selectedShipper.etaText!!
// //                            ordersItem.validationMetadata = shipmentCartItemModel.validationMetadata
// //                        }
// //                    }
// //                    shipmentViewModel.doValidateUseLogisticPromoNew(
// //                        position,
// //                        shipmentCartItemModel.cartStringGroup,
// //                        validateUsePromoRequest,
// //                        selectedShipper.logPromoCode!!,
// //                        false,
// //                        null
// //                    )
//                } /* else if (!shouldStopInClearCache && !shouldStopInDoValidateUseLogistic && !hasCheckAllCourier || hasNoPromo) {
//                    donePublisher.onCompleted()
//                }*/
            }
        }
    }

    override fun onViewErrorInCourierSection(errorMessage: String) {
        checkoutAnalyticsCourierSelection.eventViewErrorInCourierSection(errorMessage)
    }

    override fun onOntimeDeliveryClicked(url: String) {
        context?.let {
            val intent = CheckoutWebViewActivity.newInstance(
                it,
                url,
                getString(R.string.title_activity_checkout_tnc_webview)
            )
            startActivity(intent)
        }
    }

    override fun onClickSetPinpoint(position: Int) {
        setPinpoint(position)
    }

    override fun onClickRefreshErrorLoadCourier() {
        checkoutAnalyticsCourierSelection.eventClickRefreshWhenErrorLoadCourier()
    }

    override fun getHostFragmentManager(): FragmentManager {
        return parentFragmentManager
    }

    override fun onInsuranceCheckedForTrackingAnalytics() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickAsuransiPengiriman()
    }

    override fun onInsuranceChecked(isChecked: Boolean, order: CheckoutOrderModel, position: Int) {
        viewModel.setSelectedCourierInsurance(isChecked, order, position)
    }

    override fun onInsuranceInfoTooltipClickedTrackingAnalytics() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionInsuranceInfoTooltip(
            userSessionInterface.userId
        )
    }

    override fun onClickPromoCheckout(lastApplyUiModel: LastApplyUiModel) {
//        if (shipmentLoadingIndex == -1 && !shipmentViewModel.shipmentButtonPayment.value.loading) {
//            if (lastApplyUiModel == null) return
        if (!viewModel.isLoading()) {
            val validateUseRequestParam = viewModel.generateValidateUsePromoRequest()
            val promoRequestParam = viewModel.generateCouponListRecommendationRequest()
            val intent =
                RouteManager.getIntent(
                    activity,
                    ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE
                )
            intent.putExtra(ARGS_PAGE_SOURCE, PAGE_CHECKOUT)
            intent.putExtra(ARGS_PROMO_REQUEST, promoRequestParam)
            intent.putExtra(ARGS_VALIDATE_USE_REQUEST, validateUseRequestParam)
            intent.putStringArrayListExtra(ARGS_BBO_PROMO_CODES, viewModel.getBboPromoCodes())
            setChosenAddressForTradeInDropOff(intent)
            setPromoExtraMvcLockCourierFlow(intent)
            startActivityForResult(intent, REQUEST_CODE_PROMO)
            if (isTradeIn) {
                checkoutTradeInAnalytics.eventTradeInClickPromo(viewModel.isTradeInByDropOff)
            }
        }
    }

    private fun setChosenAddressForTradeInDropOff(intent: Intent) {
        val activity: Activity? = activity
        val recipientAddressModel = viewModel.recipientAddressModel
        if (activity != null && viewModel.isTradeInByDropOff) {
            val lca = ChooseAddressUtils.getLocalizingAddressData(
                activity.applicationContext
            )
            val locationDataModel = recipientAddressModel.locationDataModel
            val chosenAddress: ChosenAddress = if (locationDataModel != null) {
                ChosenAddress(
                    ChosenAddress.MODE_ADDRESS,
                    locationDataModel.addrId,
                    locationDataModel.district,
                    locationDataModel.postalCode,
                    if (locationDataModel.latitude.isNotEmpty() &&
                        locationDataModel.longitude.isNotEmpty()
                    ) {
                        locationDataModel.latitude + "," + locationDataModel.longitude
                    } else {
                        ""
                    },
                    ChosenAddressTokonow(
                        lca.shop_id,
                        lca.warehouse_id,
                        lca.warehouses,
                        lca.service_type,
                        lca.warehouse_ids
                    )
                )
            } else {
                ChosenAddress(
                    ChosenAddress.MODE_ADDRESS,
                    recipientAddressModel.id,
                    recipientAddressModel.destinationDistrictId,
                    recipientAddressModel.postalCode,
                    if (recipientAddressModel.latitude.isNotEmpty() &&
                        recipientAddressModel.longitude.isNotEmpty()
                    ) {
                        recipientAddressModel.latitude + "," + recipientAddressModel.longitude
                    } else {
                        ""
                    },
                    ChosenAddressTokonow(
                        lca.shop_id,
                        lca.warehouse_id,
                        lca.warehouses,
                        lca.service_type,
                        lca.warehouse_ids
                    )
                )
            }
            intent.putExtra(ARGS_CHOSEN_ADDRESS, chosenAddress)
        }
    }

    private fun setPromoExtraMvcLockCourierFlow(
        intent: Intent
    ) {
        var promoMvcLockCourierFlow = false
        val promo = viewModel.listData.value.promo()
        if (promo != null && promo.promo.additionalInfo.promoSpIds.isNotEmpty()) {
            promoMvcLockCourierFlow = true
        }
        intent.putExtra(ARGS_PROMO_MVC_LOCK_COURIER_FLOW, promoMvcLockCourierFlow)
    }

    override fun onSendAnalyticsClickPromoCheckout(
        isApplied: Boolean,
        listAllPromoCodes: List<String>
    ) {
        PromoRevampAnalytics.eventCheckoutClickPromoSection(
            listAllPromoCodes,
            isApplied,
            userSessionInterface.userId
        )
    }

    override fun onSendAnalyticsViewPromoCheckoutApplied() {
        PromoRevampAnalytics.eventCheckoutViewPromoAlreadyApplied()
    }

    override fun showPlatformFeeTooltipInfoBottomSheet(platformFeeModel: ShipmentPaymentFeeModel) {
        val bottomSheetPlatformFeeInfoBinding =
            BottomSheetPlatformFeeInfoBinding.inflate(LayoutInflater.from(context))
        bottomSheetPlatformFeeInfoBinding.tvPlatformFeeInfo.text = platformFeeModel.tooltip
        val bottomSheetUnify = BottomSheetUnify()
        bottomSheetUnify.setTitle(
            getString(
                R.string.platform_fee_title_info,
                platformFeeModel.title
            )
        )
        bottomSheetUnify.showCloseIcon = true
        bottomSheetUnify.setChild(bottomSheetPlatformFeeInfoBinding.root)
        bottomSheetUnify.show(childFragmentManager, null)
        checkoutAnalyticsCourierSelection.eventClickPlatformFeeInfoButton(
            userSessionInterface.userId,
            CurrencyFormatUtil.convertPriceValueToIdrFormat(platformFeeModel.fee.toLong(), false)
                .removeDecimalSuffix()
        )
    }

    override fun getParentWidth(): Int {
        return binding.root.width
    }

    override fun onEgoldChecked(checked: Boolean) {
        viewModel.updateEgold(checked)
//        shipmentAdapter.checkHasSelectAllCourier(true, -1, "", false, false)
        checkoutEgoldAnalytics.eventClickEgoldRoundup(checked)
        if (isTradeIn) {
            checkoutTradeInAnalytics.eventTradeInClickEgoldOption(
                viewModel.isTradeInByDropOff,
                checked
            )
        }
    }

    override fun onInsuranceTncClicked() {
        context?.let {
            val intent = CheckoutWebViewActivity.newInstance(
                it,
                CartConstant.TERM_AND_CONDITION_URL,
                getString(R.string.title_activity_checkout_tnc_webview)
            )
            startActivity(intent)
        }
    }

    override fun onProcessToPayment() {
        var publicKey: String? = null
        if (CheckoutFingerprintUtil.getEnableFingerprintPayment(activity)) {
            val fpk = CheckoutFingerprintUtil.getFingerprintPublicKey(
                activity
            )
            if (fpk != null) {
                publicKey = FingerPrintUtil.getPublicKey(fpk)
            }
        }
        viewModel.checkout(publicKey) {
            val paymentPassData = PaymentPassData()
            paymentPassData.redirectUrl = it.checkoutData!!.redirectUrl
            paymentPassData.transactionId = it.checkoutData.transactionId
            paymentPassData.paymentId = it.checkoutData.paymentId
            paymentPassData.callbackSuccessUrl = it.checkoutData.callbackSuccessUrl
            paymentPassData.callbackFailedUrl = it.checkoutData.callbackFailedUrl
            paymentPassData.queryString = it.checkoutData.queryString
            val intent =
                RouteManager.getIntent(activity, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
            intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
            intent.putExtra(
                PaymentConstant.EXTRA_HAS_CLEAR_RED_STATE_PROMO_BEFORE_CHECKOUT,
                it.hasClearPromoBeforeCheckout
            )
            startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
        }
    }

    fun renderCheckoutPriceUpdated(priceValidationData: PriceValidationData) {
        if (activity != null) {
            val message = priceValidationData.message
            val priceValidationDialog =
                DialogUnify(activity!!, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            priceValidationDialog.setTitle(message.title)
            priceValidationDialog.setDescription(message.desc)
            priceValidationDialog.setPrimaryCTAText(message.action)
            priceValidationDialog.setPrimaryCTAClickListener {
                viewModel.loadSAF(
                    isReloadData = true,
                    skipUpdateOnboardingState = true,
                    isReloadAfterPriceChangeHigher = true
                )
                priceValidationDialog.dismiss()
            }
            priceValidationDialog.show()
            val eventLabelBuilder = StringBuilder()
            val trackerData = priceValidationData.trackerData
            eventLabelBuilder.append(trackerData.productChangesType)
            eventLabelBuilder.append(" - ")
            eventLabelBuilder.append(trackerData.campaignType)
            eventLabelBuilder.append(" - ")
            eventLabelBuilder.append(trackerData.productIds.joinToString(","))
            checkoutAnalyticsCourierSelection.eventViewPopupPriceIncrease(eventLabelBuilder.toString())
        }
    }

    fun renderPrompt(prompt: Prompt) {
        val activity: Activity? = activity
        if (activity != null) {
            val promptDialog =
                DialogUnify(activity, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            promptDialog.setTitle(prompt.title)
            promptDialog.setDescription(prompt.description)
            promptDialog.setPrimaryCTAText(prompt.button.text)
            promptDialog.setPrimaryCTAClickListener {
                val mActivity: Activity? = getActivity()
                if (mActivity != null) {
                    if (!TextUtils.isEmpty(prompt.button.link)) {
                        RouteManager.route(mActivity, prompt.button.link)
                    }
                    mActivity.finish()
                }
            }
            promptDialog.setOverlayClose(false)
            promptDialog.setCancelable(false)
            promptDialog.show()
        }
    }
    // endregion

    // region epharmacy
    override fun uploadPrescriptionAction(
        uploadPrescriptionUiModel: UploadPrescriptionUiModel,
        buttonText: String,
        buttonNotes: String
    ) {
        if (!uploadPrescriptionUiModel.consultationFlow) {
            ePharmacyAnalytics.sendPrescriptionWidgetClick(uploadPrescriptionUiModel.checkoutId)
            val uploadPrescriptionIntent = RouteManager.getIntent(
                activity,
                UploadPrescriptionViewHolder.EPharmacyAppLink
            )
            uploadPrescriptionIntent.putExtra(
                ShipmentFragment.EXTRA_CHECKOUT_ID_STRING,
                uploadPrescriptionUiModel.checkoutId
            )
            startActivityForResult(
                uploadPrescriptionIntent,
                ShipmentFragment.REQUEST_CODE_UPLOAD_PRESCRIPTION
            )
        } else {
            val uploadPrescriptionIntent = RouteManager.getIntent(
                activity,
                UploadPrescriptionViewHolder.EPharmacyMiniConsultationAppLink
            )
            startActivityForResult(
                uploadPrescriptionIntent,
                ShipmentFragment.REQUEST_CODE_MINI_CONSULTATION
            )
            ePharmacyAnalytics.clickLampirkanResepDokter(
                uploadPrescriptionUiModel.getWidgetState(),
                buttonText,
                buttonNotes,
                uploadPrescriptionUiModel.epharmacyGroupIds,
                uploadPrescriptionUiModel.enablerNames,
                uploadPrescriptionUiModel.shopIds,
                uploadPrescriptionUiModel.cartIds
            )
        }
    }

    private fun delayEpharmacyProcess(uploadPrescriptionUiModel: UploadPrescriptionUiModel?) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                delay(1_000)
                if (isActive && activity != null) {
                    if (uploadPrescriptionUiModel?.consultationFlow == true) {
                        viewModel.fetchEpharmacyData()
                    }
                }
            } catch (t: Throwable) {
                Timber.d(t)
            }
        }
    }

    fun showCoachMarkEpharmacy(uploadPrescriptionUiModel: UploadPrescriptionUiModel) {
        if (activity != null && !CoachMarkPreference.hasShown(
                activity!!,
                KEY_PREFERENCE_COACHMARK_EPHARMACY
            )
        ) {
            val uploadPrescriptionPosition = adapter.uploadPrescriptionPosition
            binding.rvCheckout.scrollToPosition(uploadPrescriptionPosition)
            binding.rvCheckout.post {
                if (activity != null) {
                    val viewHolder =
                        binding?.rvCheckout?.findViewHolderForAdapterPosition(
                            uploadPrescriptionPosition
                        )
                    if (viewHolder is UploadPrescriptionViewHolder) {
                        val item = CoachMark2Item(
                            viewHolder.itemView,
                            activity!!.getString(R.string.checkout_epharmacy_coachmark_title),
                            activity!!.getString(R.string.checkout_epharmacy_coachmark_description),
                            CoachMark2.POSITION_TOP
                        )
                        val list = ArrayList<CoachMark2Item>()
                        list.add(item)
                        val coachMark = CoachMark2(requireContext())
                        coachMark.showCoachMark(list, null, 0)
                        CoachMarkPreference.setShown(
                            activity!!,
                            KEY_PREFERENCE_COACHMARK_EPHARMACY,
                            true
                        )
                        ePharmacyAnalytics.viewBannerPesananButuhResepInCheckoutPage(
                            uploadPrescriptionUiModel.epharmacyGroupIds,
                            uploadPrescriptionUiModel.enablerNames,
                            uploadPrescriptionUiModel.shopIds,
                            uploadPrescriptionUiModel.cartIds
                        )
                    }
                }
            }
        }
    }

    private fun onUploadPrescriptionResult(data: Intent?, isApi: Boolean) {
        if (data != null && data.extras != null &&
            data.extras!!.containsKey(KEY_UPLOAD_PRESCRIPTION_IDS_EXTRA) && activity != null
        ) {
            val uploadModel = viewModel.listData.value.getOrNull(adapter.uploadPrescriptionPosition)
            if (uploadModel is CheckoutEpharmacyModel) {
                val prescriptions = data.extras!!.getStringArrayList(
                    KEY_UPLOAD_PRESCRIPTION_IDS_EXTRA
                )
                uploadModel.epharmacy.isError = false
                if (!isApi || !prescriptions.isNullOrEmpty()) {
                    viewModel.setPrescriptionIds(prescriptions!!)
                }
                if (!isApi) {
                    showToastNormal(activity!!.getString(com.tokopedia.purchase_platform.common.R.string.pp_epharmacy_upload_success_text))
                }
                updateUploadPrescription()
            }
        }
    }

    private fun onMiniConsultationResult(resultCode: Int, data: Intent?) {
        if (resultCode == EPHARMACY_REDIRECT_CART_RESULT_CODE) {
            finish()
        } else if (resultCode == EPHARMACY_REDIRECT_CHECKOUT_RESULT_CODE) {
            if (data == null) {
                return
            }
            val results = data.getParcelableArrayListExtra<EPharmacyMiniConsultationResult>(
                EPHARMACY_CONSULTATION_RESULT_EXTRA
            )
            if (results != null) {
                viewModel.setMiniConsultationResult(results)
            }
        }
    }

    fun updateUploadPrescription() {
        adapter.notifyItemChanged(adapter.uploadPrescriptionPosition)
    }

    fun showPrescriptionReminderDialog(uploadPrescriptionUiModel: UploadPrescriptionUiModel) {
        val epharmacyGroupIds = uploadPrescriptionUiModel.epharmacyGroupIds
        val hasAttachedPrescription =
            uploadPrescriptionUiModel.uploadedImageCount > 0 || uploadPrescriptionUiModel.hasInvalidPrescription
        val reminderDialog =
            DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        reminderDialog.setTitle(getString(R.string.checkout_epharmacy_reminder_prescription_dialog_title))
        reminderDialog.setDescription(getString(R.string.checkout_epharmacy_reminder_prescription_dialog_description))
        reminderDialog.setPrimaryCTAText(getString(R.string.checkout_epharmacy_reminder_prescription_dialog_positive_button))
        reminderDialog.setSecondaryCTAText(getString(R.string.checkout_epharmacy_reminder_prescription_dialog_negative_button))
        reminderDialog.setPrimaryCTAClickListener {
            ePharmacyAnalytics.clickLanjutBayarInAbandonPage(
                epharmacyGroupIds,
                hasAttachedPrescription
            )
            reminderDialog.dismiss()
        }
        reminderDialog.setSecondaryCTAClickListener {
            ePharmacyAnalytics.clickKeluarInAbandonPage(
                epharmacyGroupIds,
                hasAttachedPrescription
            )
            reminderDialog.dismiss()
            finish()
        }
        reminderDialog.show()
        ePharmacyAnalytics.viewAbandonCheckoutPage(
            activity!!,
            epharmacyGroupIds,
            hasAttachedPrescription
        )
    }

//    fun updateShipmentCartItemGroup(shipmentCartItemModel: ShipmentCartItemModel) {
//        adapter.updateShipmentCartItemGroup(shipmentCartItemModel)
//    }

    private fun sendAnalyticsEpharmacyClickPembayaran() {
        val viewHolder =
            binding.rvCheckout.findViewHolderForAdapterPosition(adapter.uploadPrescriptionPosition)
        val epharmacyItem = viewModel.listData.value.getOrNull(adapter.uploadPrescriptionPosition)
        if (viewHolder is UploadPrescriptionViewHolder && epharmacyItem is CheckoutEpharmacyModel) {
            if (epharmacyItem.epharmacy.consultationFlow && epharmacyItem.epharmacy.showImageUpload) {
                ePharmacyAnalytics.clickPilihPembayaran(
                    viewHolder.getButtonNotes(),
                    epharmacyItem.epharmacy.epharmacyGroupIds,
                    false,
                    "success"
                )
            }
        }
    }
    // endregion

    // region timer
    /*
     * This method is to solve expired dialog not shown up after time expired in background
     * Little caveat: what if device's time is tempered and not synchronized with server?
     * Later: consider serverTimeOffset, need more time
     * */
    private fun checkCampaignTimer() {
        val timer = viewModel.getCampaignTimer()
        if (timer != null && timer.showTimer) {
            val diff = TimeHelper.timeSinceNow(timer.timerExpired)
            showCampaignTimerExpiredDialog(timer, diff, checkoutAnalyticsCourierSelection)
        }
    }

    private fun showCampaignTimerExpiredDialog(
        timer: CampaignTimerUi,
        diff: Long,
        checkoutAnalyticsCourierSelection: CheckoutAnalyticsCourierSelection
    ) {
        if (isAdded) {
            val fragmentManager = parentFragmentManager
            if (diff <= 0) {
                val dialog =
                    ExpiredTimeDialog.newInstance(timer, checkoutAnalyticsCourierSelection, this)
                dialog.show(fragmentManager, "expired dialog")
            }
        }
    }

    private fun setCampaignTimer() {
        val timer = viewModel.getCampaignTimer()
        if (timer != null && timer.showTimer) {
            val diff = TimeHelper.timeBetweenRFC3339(timer.timerServer, timer.timerExpired)
            binding.tvCountDown.visibility = View.VISIBLE
            binding.countDown.visibility = View.VISIBLE
            binding.tvCountDown.text = timer.timerDescription
            binding.countDown.remainingMilliseconds = diff
            binding.countDown.onFinish = {
                val dialog =
                    ExpiredTimeDialog.newInstance(
                        timer,
                        checkoutAnalyticsCourierSelection,
                        this@CheckoutFragment
                    )
                dialog.show(parentFragmentManager, "expired dialog")
            }
        } else {
            binding.tvCountDown.visibility = View.GONE
            binding.countDown.visibility = View.GONE
        }
    }

    override fun onPrimaryCTAClicked() {
        releaseBookingIfAny()
    }
    // endregion

    private fun onResultFromPromo(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (data!!.getStringExtra(ARGS_PROMO_ERROR) != null && data.getStringExtra(
                    ARGS_PROMO_ERROR
                ) == ARGS_FINISH_ERROR && activity != null
            ) {
                activity!!.finish()
            } else {
//                shipmentViewModel.couponStateChanged = true
                val validateUsePromoRequest =
                    data.getParcelableExtra<ValidateUsePromoRequest>(ARGS_LAST_VALIDATE_USE_REQUEST)
                if (validateUsePromoRequest != null) {
                    var stillHasPromo = false
                    for (promoGlobalCode in validateUsePromoRequest.codes) {
                        if (promoGlobalCode.isNotEmpty()) {
                            stillHasPromo = true
                            break
                        }
                    }
                    if (!stillHasPromo) {
                        for (order in validateUsePromoRequest.orders) {
                            for (promoMerchantCode in order.codes) {
                                if (promoMerchantCode.isNotEmpty()) {
                                    stillHasPromo = true
                                    break
                                }
                            }
                        }
                    }
                    val validateUsePromoRevampUiModel =
                        data.getParcelableExtra<ValidateUsePromoRevampUiModel>(
                            ARGS_VALIDATE_USE_DATA_RESULT
                        )
                    if (validateUsePromoRevampUiModel != null) {
                        for (voucherOrdersItemUiModel in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
                            for (order in validateUsePromoRequest.orders) {
                                if (voucherOrdersItemUiModel.uniqueId == order.uniqueId && voucherOrdersItemUiModel.isTypeLogistic()) {
                                    order.codes.remove(voucherOrdersItemUiModel.code)
                                    order.boCode = ""
                                }
                            }
                        }
                    }
//                    shipmentViewModel.setLastValidateUseRequest(validateUsePromoRequest)
                    if (!stillHasPromo) {
//                        doResetButtonPromoCheckout()
                    }
                }
                val validateUsePromoRevampUiModel =
                    data.getParcelableExtra<ValidateUsePromoRevampUiModel>(
                        ARGS_VALIDATE_USE_DATA_RESULT
                    )
                var reloadedUniqueIds = ArrayList<String>()
                if (validateUsePromoRevampUiModel != null) {
                    val messageInfo =
                        validateUsePromoRevampUiModel.promoUiModel.additionalInfoUiModel.errorDetailUiModel.message
//                    shipmentViewModel.validateUsePromoRevampUiModel =
//                        validateUsePromoRevampUiModel
//                    doUpdateButtonPromoCheckout(validateUsePromoRevampUiModel.promoUiModel)
//                    updatePromoTrackingData(validateUsePromoRevampUiModel.promoUiModel.trackingDetailUiModels)
//                    sendEEStep3()
//                    val validateBoResult =
                    viewModel.validateBoPromo(validateUsePromoRevampUiModel)
//                    reloadedUniqueIds = validateBoResult.first
//                    val unappliedUniqueIds = validateBoResult.second
//                    if (messageInfo.isNotEmpty()) {
//                        showToastNormal(messageInfo)
//                    } else if (unappliedUniqueIds.size > 0) {
//                        // when messageInfo is empty and has unapplied BO show hard coded toast
//                        showToastNormal(getString(com.tokopedia.purchase_platform.common.R.string.pp_auto_unapply_bo_toaster_message))
//                    }
//                    doSetPromoBenefit(
//                        validateUsePromoRevampUiModel.promoUiModel.benefitSummaryInfoUiModel.summaries
//                    )
                }
                val clearPromoUiModel =
                    data.getParcelableExtra<ClearPromoUiModel>(ARGS_CLEAR_PROMO_RESULT)
                if (clearPromoUiModel != null) {
                    val promoUiModel = PromoUiModel()
                    promoUiModel.titleDescription =
                        clearPromoUiModel.successDataModel.defaultEmptyPromoMessage
//                    val tickerAnnouncementHolderData =
//                        shipmentViewModel.tickerAnnouncementHolderData.value
//                    if (clearPromoUiModel.successDataModel.tickerMessage.isNotEmpty()) {
//                        tickerAnnouncementHolderData.title = ""
//                        tickerAnnouncementHolderData.message =
//                            clearPromoUiModel.successDataModel.tickerMessage
//                        shipmentViewModel.tickerAnnouncementHolderData.value =
//                            tickerAnnouncementHolderData
//                    }
//                    doUpdateButtonPromoCheckout(promoUiModel)
//                    shipmentViewModel.validateUsePromoRevampUiModel = null
                    if (validateUsePromoRequest != null) {
                        viewModel.validateClearAllBoPromo(validateUsePromoRequest, promoUiModel)
                    }
//                    shipmentAdapter.checkHasSelectAllCourier(false, -1, "", false, false)
                }
            }
        }
    }

    private fun releaseBookingIfAny() {
        if (view != null && binding.countDown.visibility == View.VISIBLE) {
            viewModel.releaseBooking()
        }
    }

    private fun finish() {
        if (activity != null) {
            releaseBookingIfAny()
//            shipmentViewModel.clearAllBoOnTemporaryUpsell()
//            activity?.setResult(resultCode)
            activity?.finish()
        }
    }
}
