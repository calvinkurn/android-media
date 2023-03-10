package com.tokopedia.checkout.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.util.EmbraceKey
import com.tokopedia.analytics.performance.util.EmbraceMonitoring.stopMoments
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalFintech
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.checkout.R
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.analytics.CheckoutEgoldAnalytics
import com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics
import com.tokopedia.checkout.analytics.CornerAnalytics
import com.tokopedia.checkout.data.model.request.checkout.old.DataCheckoutRequest
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.ATTRIBUTE_ADDON_DETAILS
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.ATTRIBUTE_DONATION
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.ORDER_LEVEL
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.PAYMENT_LEVEL
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.PRODUCT_LEVEL
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.SOURCE_NORMAL
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.SOURCE_OCS
import com.tokopedia.checkout.domain.mapper.DynamicDataPassingMapper.getAddOn
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnMapper.mapAddOnBottomSheetParam
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnMapper.mapAvailableBottomSheetOrderLevelData
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnMapper.mapAvailableBottomSheetProductLevelData
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnMapper.mapUnavailableBottomSheetOrderLevelData
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnMapper.mapUnavailableBottomSheetProductLevelData
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.domain.model.checkout.PriceValidationData
import com.tokopedia.checkout.domain.model.checkout.Prompt
import com.tokopedia.checkout.view.CheckoutLogger.logOnErrorApplyBo
import com.tokopedia.checkout.view.CheckoutLogger.logOnErrorCheckout
import com.tokopedia.checkout.view.CheckoutLogger.logOnErrorLoadCheckoutPage
import com.tokopedia.checkout.view.CheckoutLogger.logOnErrorLoadCourier
import com.tokopedia.checkout.view.ShipmentContract.AnalyticsActionListener
import com.tokopedia.checkout.view.adapter.ShipmentAdapter
import com.tokopedia.checkout.view.converter.RatesDataConverter
import com.tokopedia.checkout.view.converter.RatesDataConverter.Companion.getLogisticPromoCode
import com.tokopedia.checkout.view.di.CheckoutModule
import com.tokopedia.checkout.view.di.DaggerCheckoutComponent
import com.tokopedia.checkout.view.dialog.ExpireTimeDialogListener
import com.tokopedia.checkout.view.dialog.ExpiredTimeDialog.Companion.newInstance
import com.tokopedia.checkout.view.helper.ShipmentScheduleDeliveryMapData
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentTickerErrorModel
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel
import com.tokopedia.checkout.webview.CheckoutWebViewActivity.Companion.newInstance
import com.tokopedia.checkout.webview.UpsellWebViewActivity.Companion.getStartIntent
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.coachmark.CoachMarkPreference.hasShown
import com.tokopedia.coachmark.CoachMarkPreference.setShown
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common_epharmacy.EPHARMACY_CONSULTATION_RESULT_EXTRA
import com.tokopedia.common_epharmacy.EPHARMACY_REDIRECT_CART_RESULT_CODE
import com.tokopedia.common_epharmacy.EPHARMACY_REDIRECT_CHECKOUT_RESULT_CODE
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.localizationchooseaddress.common.ChosenAddressTokonow
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet.Companion.EXTRA_REF
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.getLocalizingAddressData
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.updateLocalizingAddressDataFromOther
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.updateTokoNowData
import com.tokopedia.logisticCommon.data.constant.AddEditAddressSource
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheet
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheetListener
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheet
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheetListener
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.CourierItemData.Companion.clone
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.PreOrderModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.promocheckout.common.view.uimodel.BenefitSummaryInfoUiModel
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.analytics.EPharmacyAnalytics
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics.eventCartViewPromoMessage
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics.eventCheckoutClickPilihPromoLainOnBottomsheetPromoError
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics.eventCheckoutClickPromoSection
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics.eventCheckoutViewBottomsheetPromoError
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics.eventCheckoutViewPromoAlreadyApplied
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment
import com.tokopedia.purchase_platform.common.constant.ARGS_APPLIED_MVC_CART_STRINGS
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
import com.tokopedia.purchase_platform.common.constant.CartConstant.PARAM_DEFAULT
import com.tokopedia.purchase_platform.common.constant.CartConstant.SCREEN_NAME_CART_NEW_USER
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_DROPOFF_LATITUDE
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_DROPOFF_LONGITUDE
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_SNIPPET
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.KERO_TOKEN
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.PARAM_CHECKOUT
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.REQUEST_ADD_ON_ORDER_LEVEL_BOTTOMSHEET
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.REQUEST_ADD_ON_PRODUCT_LEVEL_BOTTOMSHEET
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.RESULT_CODE_COUPON_STATE_CHANGED
import com.tokopedia.purchase_platform.common.constant.PAGE_CHECKOUT
import com.tokopedia.purchase_platform.common.feature.bottomsheet.GeneralBottomSheet
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest.DynamicDataParam
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionListener
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionViewHolder
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AvailableBottomSheetData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.PopUpData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.UnavailableBottomSheetData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.TrackingDetailsItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata.Companion.TYPE_ICON_GLOBAL
import com.tokopedia.purchase_platform.common.feature.promonoteligible.PromoNotEligibleActionListener
import com.tokopedia.purchase_platform.common.feature.promonoteligible.PromoNotEligibleBottomSheet
import com.tokopedia.purchase_platform.common.feature.sellercashback.SellerCashbackListener
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.utils.Utils.removeDecimalSuffix
import com.tokopedia.purchase_platform.common.utils.Utils.setToasterCustomBottomHeight
import com.tokopedia.purchase_platform.common.utils.isNullOrEmpty
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TimerUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat
import com.tokopedia.utils.currency.CurrencyFormatUtil.getThousandSeparatorString
import com.tokopedia.utils.time.TimeHelper.timeBetweenRFC3339
import com.tokopedia.utils.time.TimeHelper.timeSinceNow
import rx.Emitter
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author Irfan Khoirul on 23/04/18.
 * Originaly authored by Aghny, Angga, Kris
 */
class ShipmentFragment :
    BaseCheckoutFragment(),
    ShipmentContract.View,
    AnalyticsActionListener,
    ShipmentAdapterActionListener,
    ShippingDurationBottomsheetListener,
    ShippingCourierBottomsheetListener,
    PromoNotEligibleActionListener,
    SellerCashbackListener,
    ExpireTimeDialogListener,
    UploadPrescriptionListener {

    private var rvShipment: RecyclerView? = null
    private var swipeToRefresh: SwipeToRefresh? = null
    private var llNetworkErrorView: LinearLayout? = null
    private var progressDialogNormal: AlertDialog? = null
    private var shippingCourierBottomsheet: ShippingCourierBottomsheet? = null
    private var shipmentTracePerformance: PerformanceMonitoring? = null
    private var isShipmentTraceStopped = false
    var cornerId: String? = null
    private var promoNotEligibleBottomsheet: PromoNotEligibleBottomSheet? = null

    @Inject
    lateinit var shipmentAdapter: ShipmentAdapter

    @Inject
    lateinit var shipmentPresenter: ShipmentContract.Presenter

    @Inject
    lateinit var ratesDataConverter: RatesDataConverter

    @Inject
    lateinit var checkoutAnalyticsCourierSelection: CheckoutAnalyticsCourierSelection

    @Inject
    lateinit var checkoutAnalyticsChangeAddress: CheckoutAnalyticsChangeAddress

    @Inject
    lateinit var mTrackerPurchaseProtection: CheckoutAnalyticsPurchaseProtection

    @Inject
    lateinit var mTrackerCorner: CornerAnalytics

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var checkoutTradeInAnalytics: CheckoutTradeInAnalytics

    @Inject
    lateinit var checkoutEgoldAnalytics: CheckoutEgoldAnalytics

    @Inject
    lateinit var shippingCourierConverter: ShippingCourierConverter

    @Inject
    lateinit var ePharmacyAnalytics: EPharmacyAnalytics
    private var saveInstanceCacheManager: SaveInstanceCacheManager? = null
    private var savedTickerAnnouncementModel: TickerAnnouncementHolderData? = null
    private var savedShipmentCartItemModelList: List<ShipmentCartItemModel>? = null
    private var savedShipmentCostModel: ShipmentCostModel? = null
    private var savedEgoldAttributeModel: EgoldAttributeModel? = null
    private var savedRecipientAddressModel: RecipientAddressModel? = null
    private var savedShipmentDonationModel: ShipmentDonationModel? = null
    private var savedListShipmentCrossSellModel: ArrayList<ShipmentCrossSellModel>? = null
    private var benefitSummaryInfoUiModel: BenefitSummaryInfoUiModel? = null
    private var savedShipmentButtonPaymentModel: ShipmentButtonPaymentModel? = null
    private var savedLastApplyData: LastApplyUiModel? = null
    private var savedUploadPrescriptionUiModel: UploadPrescriptionUiModel? = null
    private var hasClearPromoBeforeCheckout = false
    private var hasRunningApiCall = false
    private var bboPromoCodes = ArrayList<String>()
    private var shipmentLoadingIndex = -1
    private var isPlusSelected: Boolean? = null
    private var delayScrollToFirstShopSubscription: Subscription? = null
    private var delayScrollToCoachmarkEpharmacySubscription: Subscription? = null
    private var toasterThrottleSubscription: Subscription? = null
    private var toasterEmitter: Emitter<String>? = null

    // count down component
    private var cdLayout: View? = null
    private var cdView: TimerUnify? = null
    private var cdText: Typography? = null
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
        if (context != null) {
            saveInstanceCacheManager = SaveInstanceCacheManager(context!!, savedInstanceState)
        }
        if (savedInstanceState != null) {
            savedShipmentCartItemModelList =
                saveInstanceCacheManager?.get<List<ShipmentCartItemModel>>(
                    ShipmentCartItemModel::class.java.simpleName,
                    object : TypeToken<ArrayList<ShipmentCartItemModel>>() {}.type
                )
            if (savedShipmentCartItemModelList != null) {
                savedTickerAnnouncementModel =
                    saveInstanceCacheManager?.get<TickerAnnouncementHolderData>(
                        TickerAnnouncementHolderData::class.java.simpleName,
                        TickerAnnouncementHolderData::class.java
                    )
                savedRecipientAddressModel = saveInstanceCacheManager?.get<RecipientAddressModel>(
                    RecipientAddressModel::class.java.simpleName,
                    RecipientAddressModel::class.java
                )
                savedShipmentCostModel = saveInstanceCacheManager?.get<ShipmentCostModel>(
                    ShipmentCostModel::class.java.simpleName,
                    ShipmentCostModel::class.java
                )
                savedEgoldAttributeModel = saveInstanceCacheManager?.get<EgoldAttributeModel>(
                    EgoldAttributeModel::class.java.simpleName,
                    EgoldAttributeModel::class.java
                )
                savedShipmentDonationModel = saveInstanceCacheManager?.get<ShipmentDonationModel>(
                    ShipmentDonationModel::class.java.simpleName,
                    ShipmentDonationModel::class.java
                )
                savedListShipmentCrossSellModel =
                    saveInstanceCacheManager?.get<ArrayList<ShipmentCrossSellModel>>(
                        ShipmentCrossSellModel::class.java.simpleName,
                        object : TypeToken<ArrayList<ShipmentCrossSellModel>>() {}.type
                    )
                savedShipmentButtonPaymentModel =
                    saveInstanceCacheManager?.get<ShipmentButtonPaymentModel>(
                        ShipmentButtonPaymentModel::class.java.simpleName,
                        ShipmentButtonPaymentModel::class.java
                    )
                savedLastApplyData = saveInstanceCacheManager?.get<LastApplyUiModel>(
                    LastApplyUiModel::class.java.simpleName,
                    LastApplyUiModel::class.java
                )
                savedUploadPrescriptionUiModel =
                    saveInstanceCacheManager?.get<UploadPrescriptionUiModel>(
                        UploadPrescriptionUiModel::class.java.simpleName,
                        UploadPrescriptionUiModel::class.java
                    )
            }
        }
        shipmentPresenter.attachView(this)
        shipmentTracePerformance = PerformanceMonitoring.start(SHIPMENT_TRACE)
    }

    override fun onStop() {
        super.onStop()
        hideLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        delayScrollToFirstShopSubscription?.unsubscribe()
        delayScrollToCoachmarkEpharmacySubscription?.unsubscribe()
        toasterThrottleSubscription?.unsubscribe()
        shippingCourierBottomsheet = null
        val countDownTimer = cdView?.timer
        countDownTimer?.cancel()
        shipmentPresenter.detachView()
    }

    override fun onDestroy() {
        shipmentAdapter.clearCompositeSubscription()
        super.onDestroy()
    }

    override fun getOptionsMenuEnable(): Boolean {
        return false
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_shipment
    }

    override fun initView(view: View) {
        swipeToRefresh = view.findViewById(R.id.swipe_refresh_layout)
        rvShipment = view.findViewById(R.id.rv_shipment)
        llNetworkErrorView = view.findViewById(R.id.ll_network_error_view)
        cdLayout = view.findViewById(R.id.partial_countdown)
        cdView = view.findViewById(R.id.count_down)
        cdText = view.findViewById(R.id.tv_count_down)
        progressDialogNormal = AlertDialog.Builder(activity!!)
            .setView(com.tokopedia.purchase_platform.common.R.layout.purchase_platform_progress_dialog_view)
            .setCancelable(false)
            .create()
        (rvShipment?.itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = false
        rvShipment?.addItemDecoration(ShipmentItemDecoration())
    }

    override fun onResume() {
        super.onResume()
        checkCampaignTimer()
        restoreProgressLoading()
    }

    fun onBackPressed(): Boolean {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickBackArrow()
        if (isTradeIn) {
            checkoutTradeInAnalytics.eventTradeInClickBackButton(isTradeInByDropOff)
        }
        val validatePrescriptionOnBackPressed =
            shipmentPresenter.validatePrescriptionOnBackPressed()
        if (validatePrescriptionOnBackPressed) {
            finish()
        }
        return validatePrescriptionOnBackPressed
    }

    private fun finish() {
        if (activity != null) {
            releaseBookingIfAny()
            shipmentPresenter.clearAllBoOnTemporaryUpsell()
            activity?.setResult(resultCode)
            activity?.finish()
        }
    }

    private fun restoreProgressLoading() {
        if (hasRunningApiCall) {
            showLoading()
        }
    }

    override fun setHasRunningApiCall(hasRunningApiCall: Boolean) {
        this.hasRunningApiCall = hasRunningApiCall
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackground()
        if (savedInstanceState == null || savedShipmentCartItemModelList == null) {
            shipmentPresenter.processInitialLoadCheckoutPage(
                false, isOneClickShipment, isTradeIn, true,
                false, null, deviceId, checkoutLeasingId,
                isPlusSelected()
            )
        } else {
            shipmentPresenter.tickerAnnouncementHolderData = savedTickerAnnouncementModel!!
            shipmentPresenter.shipmentCartItemModelList = savedShipmentCartItemModelList
            shipmentPresenter.recipientAddressModel = savedRecipientAddressModel
            shipmentPresenter.setShipmentCostModel(savedShipmentCostModel)
            shipmentPresenter.shipmentDonationModel = savedShipmentDonationModel
            shipmentPresenter.setListShipmentCrossSellModel(savedListShipmentCrossSellModel)
            shipmentPresenter.setShipmentButtonPaymentModel(savedShipmentButtonPaymentModel)
            shipmentPresenter.egoldAttributeModel = savedEgoldAttributeModel
            shipmentAdapter.lastChooseCourierItemPosition =
                savedInstanceState.getInt(DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION)
            shipmentAdapter.lastServiceId =
                savedInstanceState.getInt(DATA_STATE_LAST_CHOOSEN_SERVICE_ID)
            shipmentPresenter.lastApplyData = savedLastApplyData
            shipmentPresenter.setUploadPrescriptionData(savedUploadPrescriptionUiModel)
            renderCheckoutPage(true, false, isOneClickShipment)
            swipeToRefresh?.isEnabled = false
        }
    }

    private fun setBackground() {
        val activity: Activity? = activity
        activity?.window?.decorView?.setBackgroundColor(
            ContextCompat.getColor(
                activity,
                com.tokopedia.unifyprinciples.R.color.Unify_N50
            )
        )
    }

    private val deviceId: String
        get() = if (arguments?.getString(ShipmentFormRequest.EXTRA_DEVICE_ID) != null) {
            arguments!!.getString(ShipmentFormRequest.EXTRA_DEVICE_ID)!!
        } else {
            ""
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveInstanceCacheManager?.onSave(outState)
        if (shipmentPresenter.shipmentCartItemModelList != null) {
            saveInstanceCacheManager?.put(
                TickerAnnouncementHolderData::class.java.simpleName,
                shipmentPresenter.tickerAnnouncementHolderData
            )
            saveInstanceCacheManager?.put(
                ShipmentCartItemModel::class.java.simpleName,
                shipmentPresenter.shipmentCartItemModelList?.let {
                    ArrayList(
                        it
                    )
                }
            )
            saveInstanceCacheManager?.put(
                RecipientAddressModel::class.java.simpleName,
                shipmentPresenter.recipientAddressModel
            )
            saveInstanceCacheManager?.put(
                ShipmentCostModel::class.java.simpleName,
                shipmentPresenter.getShipmentCostModel()
            )
            saveInstanceCacheManager?.put(
                ShipmentDonationModel::class.java.simpleName,
                shipmentPresenter.shipmentDonationModel
            )
            saveInstanceCacheManager?.put(
                ShipmentButtonPaymentModel::class.java.simpleName,
                shipmentPresenter.getShipmentButtonPaymentModel()
            )
            outState.putInt(
                DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION,
                shipmentAdapter.lastChooseCourierItemPosition
            )
            outState.putInt(DATA_STATE_LAST_CHOOSEN_SERVICE_ID, shipmentAdapter.lastServiceId)
            saveInstanceCacheManager?.put(
                UploadPrescriptionUiModel::class.java.simpleName,
                shipmentPresenter.uploadPrescriptionUiModel
            )
        } else {
            saveInstanceCacheManager?.put(ShipmentCartItemModel::class.java.simpleName, null)
        }
    }

    private val isOneClickShipment: Boolean
        get() = arguments != null && arguments!!.getBoolean(ARG_IS_ONE_CLICK_SHIPMENT)

    private val checkoutLeasingId: String
        get() {
            var leasingId = "0"
            if (arguments != null && arguments!!.getString(ARG_CHECKOUT_LEASING_ID) != null &&
                !arguments!!.getString(ARG_CHECKOUT_LEASING_ID).equals("null", ignoreCase = true)
            ) {
                leasingId = arguments!!.getString(ARG_CHECKOUT_LEASING_ID)!!
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
            if (arguments != null && arguments!!.getString(ARG_CHECKOUT_PAGE_SOURCE) != null) {
                pageSource = arguments!!.getString(ARG_CHECKOUT_PAGE_SOURCE)!!
            }
            return pageSource
        }

    private fun isPlusSelected(): Boolean {
        if (isPlusSelected == null) {
            isPlusSelected = if (arguments != null) {
                arguments!!.getBoolean(ARG_IS_PLUS_SELECTED, false)
            } else {
                false
            }
        }
        return isPlusSelected!!
    }

    private fun initRecyclerViewData(
        shipmentTickerErrorModel: ShipmentTickerErrorModel,
        tickerAnnouncementHolderData: TickerAnnouncementHolderData?,
        recipientAddressModel: RecipientAddressModel?,
        shipmentUpsellModel: ShipmentUpsellModel,
        shipmentNewUpsellModel: ShipmentNewUpsellModel,
        shipmentCartItemModelList: List<ShipmentCartItemModel>,
        shipmentDonationModel: ShipmentDonationModel?,
        shipmentCrossSellModelList: List<ShipmentCrossSellModel>,
        lastApplyUiModel: LastApplyUiModel?,
        shipmentCostModel: ShipmentCostModel?,
        egoldAttributeModel: EgoldAttributeModel?,
        shipmentButtonPaymentModel: ShipmentButtonPaymentModel?,
        uploadPrescriptionUiModel: UploadPrescriptionUiModel?,
        isInitialRender: Boolean,
        isReloadAfterPriceChangeHigher: Boolean,
        isLocalReload: Boolean
    ) {
        shipmentAdapter.clearData()
        rvShipment?.layoutManager = LinearLayoutManager(activity)
        rvShipment?.adapter = shipmentAdapter
        shipmentAdapter.addTickerErrorData(shipmentTickerErrorModel)
        if (tickerAnnouncementHolderData != null) {
            shipmentAdapter.addTickerAnnouncementData(tickerAnnouncementHolderData)
        }
        if (recipientAddressModel != null) {
            shipmentAdapter.addAddressShipmentData(recipientAddressModel)
        }
        if (shipmentUpsellModel.isShow) {
            shipmentAdapter.addUpsellData(shipmentUpsellModel)
        }
        if (shipmentNewUpsellModel.isShow) {
            shipmentAdapter.addNewUpsellData(shipmentNewUpsellModel)
        }
        isPlusSelected = shipmentNewUpsellModel.isSelected
         shipmentAdapter.addCartItemDataList(shipmentCartItemModelList)
        val cartIdsStringBuilder = StringBuilder()
        for (i in shipmentCartItemModelList.indices) {
            if (shipmentCartItemModelList[i].cartItemModels.isNotEmpty()) {
                for ((cartId) in shipmentCartItemModelList[i].cartItemModels) {
                    cartIdsStringBuilder.append(cartId)
                    cartIdsStringBuilder.append(",")
                }
            }
        }
        cartIdsStringBuilder.replace(
            cartIdsStringBuilder.lastIndexOf(","),
            cartIdsStringBuilder.lastIndexOf(",") + 1,
            ""
        )
        var hasEpharmacyWidget = false
        if (uploadPrescriptionUiModel != null && uploadPrescriptionUiModel.showImageUpload) {
            shipmentAdapter.addUploadPrescriptionUiDataModel(uploadPrescriptionUiModel)
            hasEpharmacyWidget = true
        }
        if (shipmentDonationModel != null) {
            if (shipmentDonationModel.donation.title.isNotEmpty() && shipmentDonationModel.donation.nominal != 0) {
                shipmentAdapter.addShipmentDonationModel(shipmentDonationModel)
                if (shipmentDonationModel.isChecked && shipmentDonationModel.isEnabled) {
                    checkoutAnalyticsCourierSelection.eventViewAutoCheckDonation(
                        userSessionInterface.userId
                    )
                }
            }
        }
        if (egoldAttributeModel != null && egoldAttributeModel.isEligible) {
            shipmentAdapter.updateEgold(false)
            shipmentAdapter.addEgoldAttributeData(egoldAttributeModel)
        }
        if (shipmentCrossSellModelList.isNotEmpty()) {
            shipmentAdapter.addListShipmentCrossSellModel(shipmentCrossSellModelList)
            for (i in shipmentCrossSellModelList.indices) {
                val crossSellModel = shipmentCrossSellModelList[i].crossSellModel
                val digitalCategoryName = crossSellModel.orderSummary.title
                val eventLabel = "$digitalCategoryName - ${crossSellModel.id}"
                val digitalProductName = crossSellModel.info.title
                checkoutAnalyticsCourierSelection.eventViewAutoCheckCrossSell(
                    userSessionInterface.userId,
                    (i + 1).toString(),
                    eventLabel,
                    digitalProductName,
                    getCrossSellChildCategoryId(shipmentCartItemModelList)
                )
            }
        }
        if (shipmentCartItemModelList.isNotEmpty()) {
            // Don't show donation, egold, promo section if all shop is error
            var errorShopCount = 0
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                if (shipmentCartItemModel.isError) {
                    errorShopCount++
                }
            }
            if (errorShopCount == 0 || errorShopCount < shipmentCartItemModelList.size) {
                if (lastApplyUiModel != null && lastApplyUiModel.additionalInfo.errorDetail.message.isNotEmpty()) {
                    eventCartViewPromoMessage(lastApplyUiModel.additionalInfo.errorDetail.message)
                }
                shipmentAdapter.addLastApplyUiDataModel(lastApplyUiModel)
            }
        }
        shipmentAdapter.addShipmentCostData(shipmentCostModel)
        shipmentAdapter.updateShipmentSellerCashbackVisibility()
        if (isTradeIn) {
            checkoutTradeInAnalytics.eventViewCheckoutPageTradeIn()
        }
        shipmentAdapter.addShipmentButtonPaymentModel(shipmentButtonPaymentModel)
        if (shipmentCartItemModelList.isNotEmpty()) {
            addShippingCompletionTicker(shipmentCartItemModelList[0].isEligibleNewShippingExperience)
        }
        if (isInitialRender) {
            sendEEStep2()
        }
        if (!isLocalReload) {
            sendErrorAnalytics()
        }
        if (isReloadAfterPriceChangeHigher) {
            delayScrollToFirstShop()
        } else if (hasEpharmacyWidget) {
            triggerEpharmacyCoachmark(uploadPrescriptionUiModel)
        }
    }

    private fun triggerEpharmacyCoachmark(uploadPrescriptionUiModel: UploadPrescriptionUiModel?) {
        delayScrollToCoachmarkEpharmacySubscription = Observable.just(uploadPrescriptionUiModel)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<UploadPrescriptionUiModel>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    Timber.d(e)
                }

                override fun onNext(UploadPrescriptionUiModel1: UploadPrescriptionUiModel?) {
                    if (!isUnsubscribed && activityContext != null) {
                        if (UploadPrescriptionUiModel1?.consultationFlow == true) {
                            shipmentPresenter.fetchEpharmacyData()
                        }
                    }
                }
            })
    }

    override fun showCoachMarkEpharmacy(uploadPrescriptionUiModel: UploadPrescriptionUiModel?) {
        if (activityContext != null && !hasShown(
                activityContext!!,
                KEY_PREFERENCE_COACHMARK_EPHARMACY
            )
        ) {
            val uploadPrescriptionPosition = shipmentAdapter.uploadPrescriptionPosition
            rvShipment!!.scrollToPosition(uploadPrescriptionPosition)
            rvShipment!!.post {
                val viewHolder =
                    rvShipment!!.findViewHolderForAdapterPosition(uploadPrescriptionPosition)
                if (viewHolder is UploadPrescriptionViewHolder) {
                    val item = CoachMark2Item(
                        viewHolder.itemView,
                        activityContext!!.getString(R.string.checkout_epharmacy_coachmark_title),
                        activityContext!!.getString(R.string.checkout_epharmacy_coachmark_description),
                        CoachMark2.POSITION_TOP
                    )
                    val list = ArrayList<CoachMark2Item>()
                    list.add(item)
                    val coachMark = CoachMark2(requireContext())
                    coachMark.showCoachMark(list, null, 0)
                    setShown(activityContext!!, KEY_PREFERENCE_COACHMARK_EPHARMACY, true)
                    ePharmacyAnalytics.viewBannerPesananButuhResepInCheckoutPage(
                        uploadPrescriptionUiModel!!.epharmacyGroupIds,
                        uploadPrescriptionUiModel.enablerNames,
                        uploadPrescriptionUiModel.shopIds,
                        uploadPrescriptionUiModel.cartIds
                    )
                }
            }
        }
    }

    private fun getCrossSellChildCategoryId(shipmentCartItemModelList: List<ShipmentCartItemModel>?): ArrayList<Long> {
        val childCategoryIds = ArrayList<Long>()
        for (i in shipmentCartItemModelList!!.indices) {
            for (cartItemModel in shipmentCartItemModelList[i].cartItemModels) {
                childCategoryIds.add(cartItemModel.productCatId)
            }
        }
        return childCategoryIds
    }

    private fun addShippingCompletionTicker(isEligibleNewShippingExperience: Boolean) {
        if (isEligibleNewShippingExperience) {
            shipmentAdapter.updateShippingCompletionTickerVisibility()
        }
    }

    private fun delayScrollToFirstShop() {
        delayScrollToFirstShopSubscription = Observable.timer(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<Long>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(aLong: Long) {
                    if (!isUnsubscribed) {
                        if (rvShipment!!.layoutManager != null) {
                            val linearSmoothScroller: LinearSmoothScroller =
                                object : LinearSmoothScroller(
                                    rvShipment!!.context
                                ) {
                                    override fun getVerticalSnapPreference(): Int {
                                        return SNAP_TO_START
                                    }
                                }
                            linearSmoothScroller.targetPosition =
                                shipmentAdapter.firstShopPosition
                            rvShipment?.layoutManager?.startSmoothScroll(linearSmoothScroller)
                        }
                    }
                }
            })
    }

    private fun sendEEStep2() {
        val shipmentCartItemModelList = shipmentAdapter.shipmentCartItemModelList!!
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            val dataCheckoutRequests: List<DataCheckoutRequest>? = if (shipmentCartItemModel.isSaveStateFlag) {
                shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(
                    shipmentCartItemModel.cartString,
                    "",
                    "",
                    ""
                )
            } else {
                shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(
                    shipmentCartItemModel.cartString,
                    "",
                    "",
                    shipmentCartItemModel.spId.toString()
                )
            }
            shipmentPresenter.setDataCheckoutRequestList(dataCheckoutRequests)
        }
        val dataCheckoutRequests =
            shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(
                shipmentCartItemModelList
            )
        shipmentPresenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
            dataCheckoutRequests,
            null,
            EnhancedECommerceActionField.STEP_2,
            ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
            ConstantTransactionAnalytics.EventAction.VIEW_CHECKOUT_PAGE,
            ConstantTransactionAnalytics.EventLabel.SUCCESS,
            checkoutLeasingId,
            checkoutPageSource
        )
    }

    private fun sendErrorAnalytics() {
        if (shipmentPresenter.shipmentTickerErrorModel.isError) {
            onViewTickerPaymentError(shipmentPresenter.shipmentTickerErrorModel.errorMessage)
        }
        for (shipmentCartItemModel in shipmentAdapter.shipmentCartItemModelList!!) {
            if (shipmentCartItemModel.isError && !TextUtils.isEmpty(shipmentCartItemModel.errorTitle)) {
                onViewTickerOrderError(
                    shipmentCartItemModel.shopId.toString(),
                    shipmentCartItemModel.errorTitle
                )
            } else if ((
                !shipmentCartItemModel.isError && shipmentCartItemModel.isHasUnblockingError &&
                    !TextUtils.isEmpty(shipmentCartItemModel.unblockingErrorMessage)
                ) && shipmentCartItemModel.firstProductErrorIndex > 0
            ) {
                onViewTickerOrderError(
                    shipmentCartItemModel.shopId.toString(),
                    shipmentCartItemModel.unblockingErrorMessage
                )
            }
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                if (cartItemModel.isError && !TextUtils.isEmpty(cartItemModel.errorMessage)) {
                    onViewTickerProductError(
                        shipmentCartItemModel.shopId.toString(),
                        cartItemModel.errorMessage
                    )
                }
            }
        }
    }

    override fun showInitialLoading() {
        swipeToRefresh?.isRefreshing = true
    }

    override fun hideInitialLoading() {
        swipeToRefresh?.isRefreshing = false
        swipeToRefresh?.isEnabled = false
    }

    override fun showLoading() {
        if (progressDialogNormal != null && !progressDialogNormal!!.isShowing) {
            progressDialogNormal!!.show()
        }
    }

    override fun hideLoading() {
        if (progressDialogNormal != null && progressDialogNormal!!.isShowing) {
            progressDialogNormal!!.dismiss()
        }
        swipeToRefresh?.isEnabled = false
    }

    override fun showToastNormal(message: String) {
        if (toasterEmitter == null) {
            toasterThrottleSubscription =
                Observable.create(
                    { emitter: Emitter<String> ->
                        toasterEmitter = emitter
                    },
                    Emitter.BackpressureMode.BUFFER
                )
                    .throttleFirst(TOASTER_THROTTLE, TimeUnit.MILLISECONDS)
                    .subscribe { s: String ->
                        if (view != null && activity != null) {
                            initializeToasterLocation()
                            val actionText =
                                activity!!.getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_toaster_action_ok)
                            val listener = View.OnClickListener { }
                            Toaster.build(
                                view!!,
                                s,
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_NORMAL,
                                actionText,
                                listener
                            )
                                .show()
                        }
                    }
        }
        toasterEmitter?.onNext(message)
    }

    @SuppressLint("WrongConstant")
    override fun showToastError(message: String?) {
        var msg = message ?: ""
        if (view != null && activity != null) {
            initializeToasterLocation()
            if (msg.isEmpty()) {
                msg =
                    activity!!.getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_error_global_message)
            }
            if (shipmentAdapter.itemCount == 0) {
                renderErrorPage(msg)
            } else {
                val listener = View.OnClickListener { }
                val actionText =
                    activity!!.getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_toaster_action_ok)
                Toaster.build(
                    view!!,
                    msg,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                    actionText,
                    listener
                )
                    .show()
            }
        }
    }

    private fun initializeToasterLocation() {
        val layoutManager = rvShipment?.layoutManager as LinearLayoutManager?
            ?: return
        val lastItemPosition = layoutManager.findLastVisibleItemPosition()
        if (lastItemPosition == RecyclerView.NO_POSITION || lastItemPosition >= shipmentAdapter.getShipmentDataList().size) {
            return
        }
        if (shipmentAdapter.getShipmentDataList()[lastItemPosition] is ShipmentButtonPaymentModel) {
            setToasterCustomBottomHeight(
                context!!.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_48)
            )
        } else {
            setToasterCustomBottomHeight(
                context!!.resources.getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16)
            )
        }
    }

    override fun renderErrorPage(message: String?) {
        rvShipment?.visibility = View.GONE
        llNetworkErrorView?.visibility = View.VISIBLE
        NetworkErrorHelper.showEmptyState(
            activity,
            llNetworkErrorView,
            message
        ) {
            llNetworkErrorView?.visibility = View.GONE
            rvShipment?.visibility = View.VISIBLE
            shipmentPresenter.processInitialLoadCheckoutPage(
                false, isOneClickShipment, isTradeIn, true,
                false, null, deviceId, checkoutLeasingId,
                isPlusSelected()
            )
        }
    }

    override fun onCacheExpired(message: String?) {
        if (activity != null && view != null) {
            val intent = Intent()
            intent.putExtra(CheckoutConstant.EXTRA_CACHE_EXPIRED_ERROR_MESSAGE, message)
            activity!!.setResult(CheckoutConstant.RESULT_CHECKOUT_CACHE_EXPIRED, intent)
            activity!!.finish()
        }
    }

    override fun onShipmentAddressFormEmpty() {
        if (activity != null) {
            activity!!.finish()
        }
    }

    override fun renderCheckoutPage(
        isInitialRender: Boolean,
        isReloadAfterPriceChangeHigher: Boolean,
        isOneClickShipment: Boolean
    ) {
        val recipientAddressModel = shipmentPresenter.recipientAddressModel
        shipmentAdapter.setShowOnboarding(shipmentPresenter.isShowOnboarding)
        setCampaignTimer()
        initRecyclerViewData(
            shipmentPresenter.shipmentTickerErrorModel,
            shipmentPresenter.tickerAnnouncementHolderData,
            recipientAddressModel,
            shipmentPresenter.shipmentUpsellModel,
            shipmentPresenter.shipmentNewUpsellModel,
            shipmentPresenter.shipmentCartItemModelList!!,
            shipmentPresenter.shipmentDonationModel,
            shipmentPresenter.getListShipmentCrossSellModel(),
            shipmentPresenter.lastApplyData,
            shipmentPresenter.getShipmentCostModel(),
            shipmentPresenter.egoldAttributeModel,
            shipmentPresenter.getShipmentButtonPaymentModel(),
            shipmentPresenter.uploadPrescriptionUiModel,
            isInitialRender,
            isReloadAfterPriceChangeHigher,
            false
        )
    }

    override fun stopTrace() {
        if (!isShipmentTraceStopped) {
            shipmentTracePerformance?.stopTrace()
            isShipmentTraceStopped = true
        }
    }

    override fun stopEmbraceTrace() {
        val emptyMap: Map<String, Any> = HashMap()
        stopMoments(EmbraceKey.KEY_ACT_BUY, null, emptyMap)
    }

    override fun renderDataChanged() {
        initRecyclerViewData(
            shipmentPresenter.shipmentTickerErrorModel,
            shipmentPresenter.tickerAnnouncementHolderData,
            shipmentPresenter.recipientAddressModel,
            shipmentPresenter.shipmentUpsellModel,
            shipmentPresenter.shipmentNewUpsellModel,
            shipmentPresenter.shipmentCartItemModelList!!,
            shipmentPresenter.shipmentDonationModel,
            shipmentPresenter.getListShipmentCrossSellModel(),
            shipmentPresenter.lastApplyData,
            shipmentPresenter.getShipmentCostModel(),
            shipmentPresenter.egoldAttributeModel,
            shipmentPresenter.getShipmentButtonPaymentModel(),
            shipmentPresenter.uploadPrescriptionUiModel,
            false,
            false,
            true
        )
    }

    override fun renderCheckoutPageNoAddress(
        shipmentAddressFormData: CartShipmentAddressFormData?,
        isEligibleForRevampAna: Boolean
    ) {
        val token = Token()
        token.ut = shipmentAddressFormData!!.keroUnixTime
        token.districtRecommendation = shipmentAddressFormData.keroDiscomToken
        if (isEligibleForRevampAna) {
            val intent =
                RouteManager.getIntent(activity, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
            intent.putExtra(KERO_TOKEN, token)
            intent.putExtra(EXTRA_REF, SCREEN_NAME_CART_NEW_USER)
            intent.putExtra(PARAM_SOURCE, AddEditAddressSource.CART.source)
            startActivityForResult(intent, LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY)
        } else {
            val intent =
                RouteManager.getIntent(activity, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
            intent.putExtra(KERO_TOKEN, token)
            intent.putExtra(EXTRA_REF, SCREEN_NAME_CART_NEW_USER)
            startActivityForResult(intent, LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY)
        }
    }

    override fun renderCheckoutPageNoMatchedAddress(
        cartShipmentAddressFormData: CartShipmentAddressFormData?,
        addressState: Int
    ) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
        intent.putExtra(EXTRA_PREVIOUS_STATE_ADDRESS, addressState)
        intent.putExtra(EXTRA_IS_FROM_CHECKOUT_SNIPPET, true)
        intent.putExtra(PARAM_SOURCE, ManageAddressSource.CART.source)
        startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS)
    }

    override fun renderCheckoutCartSuccess(checkoutData: CheckoutData?) {
        val paymentPassData = PaymentPassData()
        paymentPassData.redirectUrl = checkoutData!!.redirectUrl
        paymentPassData.transactionId = checkoutData.transactionId
        paymentPassData.paymentId = checkoutData.paymentId
        paymentPassData.callbackSuccessUrl = checkoutData.callbackSuccessUrl
        paymentPassData.callbackFailedUrl = checkoutData.callbackFailedUrl
        paymentPassData.queryString = checkoutData.queryString
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
        intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
        intent.putExtra(
            PaymentConstant.EXTRA_HAS_CLEAR_RED_STATE_PROMO_BEFORE_CHECKOUT,
            hasClearPromoBeforeCheckout
        )
        startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
    }

    override fun renderCheckoutPriceUpdated(priceValidationData: PriceValidationData?) {
        if (activity != null) {
            val (title, desc, action) = priceValidationData!!.message
            val priceValidationDialog =
                DialogUnify(activity!!, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            priceValidationDialog.setTitle(title)
            priceValidationDialog.setDescription(desc)
            priceValidationDialog.setPrimaryCTAText(action)
            priceValidationDialog.setPrimaryCTAClickListener {
                shipmentPresenter.processInitialLoadCheckoutPage(
                    true, isOneClickShipment, isTradeIn, true,
                    true, null, deviceId, checkoutLeasingId,
                    isPlusSelected()
                )
                priceValidationDialog.dismiss()
            }
            priceValidationDialog.show()
            val eventLabelBuilder = StringBuilder()
            val (productChangesType, campaignType, productIds) = priceValidationData.trackerData
            eventLabelBuilder.append(productChangesType)
            eventLabelBuilder.append(" - ")
            eventLabelBuilder.append(campaignType)
            eventLabelBuilder.append(" - ")
            eventLabelBuilder.append(TextUtils.join(",", productIds))
            checkoutAnalyticsCourierSelection.eventViewPopupPriceIncrease(eventLabelBuilder.toString())
        }
    }

    override fun renderCheckoutCartError(message: String) {
        if (message.contains("Pre Order") && message.contains("Corner")) mTrackerCorner.sendViewCornerPoError()
        showToastError(message)
    }

    override fun renderPrompt(prompt: Prompt?) {
        val activity: Activity? = activity
        if (activity != null) {
            val promptDialog =
                DialogUnify(activity, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
            promptDialog.setTitle(prompt!!.title)
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
                Unit
            }
            promptDialog.setOverlayClose(false)
            promptDialog.setCancelable(false)
            promptDialog.show()
        }
    }

    override fun sendAnalyticsChoosePaymentMethodFailed(errorMessage: String?) {
        if (isTradeIn) {
            checkoutTradeInAnalytics.eventClickBayarTradeInFailed()
        }
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickPilihMetodePembayaranNotSuccess(
            errorMessage
        )
    }

    override fun renderPromoCheckoutFromCourierSuccess(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        itemPosition: Int,
        noToast: Boolean
    ) {
        if (!noToast && !shipmentAdapter.isCourierPromoStillExist) {
            if (validateUsePromoRevampUiModel.message.isNotEmpty()) {
                showToastNormal(validateUsePromoRevampUiModel.message[0])
            }
        }
        setCourierPromoApplied(itemPosition)
        updateButtonPromoCheckout(validateUsePromoRevampUiModel.promoUiModel, true)
    }

    override fun setCourierPromoApplied(itemPosition: Int) {
        shipmentAdapter.setCourierPromoApplied(itemPosition)
    }

    override fun renderErrorCheckPromoShipmentData(message: String?) {
        showToastError(message)
        shipmentAdapter.resetCourierPromoState()
    }

    override fun renderEditAddressSuccess(latitude: String, longitude: String) {
        shipmentAdapter.updateShipmentDestinationPinpoint(latitude, longitude)
        val position = shipmentAdapter.lastChooseCourierItemPosition
        val shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(position)
        if (shipmentCartItemModel != null) {
            shippingCourierBottomsheet = null
            val recipientAddressModel: RecipientAddressModel?
            if (shipmentPresenter.recipientAddressModel != null) {
                recipientAddressModel = shipmentPresenter.recipientAddressModel
                if (shipmentCartItemModel.isDisableChangeCourier) {
                    // refresh page
                    shipmentPresenter.processInitialLoadCheckoutPage(
                        true, isOneClickShipment, isTradeIn, true,
                        false, null, deviceId, checkoutLeasingId,
                        isPlusSelected()
                    )
                } else {
                    onChangeShippingDuration(shipmentCartItemModel, recipientAddressModel, position)
                }
            }
        }
    }

    override fun renderCourierStateSuccess(
        courierItemData: CourierItemData,
        itemPosition: Int,
        isTradeInDropOff: Boolean,
        isForceReloadRates: Boolean
    ) {
        if (context != null) {
            val shipmentCartItemModel =
                shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition)
                    ?: return
            shipmentCartItemModel.isStateLoadingCourierState = false
            if (isTradeInDropOff) {
                shipmentAdapter.setSelectedCourierTradeInPickup(courierItemData)
            } else {
                val shouldValidatePromo =
                    !isForceReloadRates && courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                if (shouldValidatePromo) {
                    val cartString = shipmentCartItemModel.cartString
                    val validateUsePromoRequest = generateValidateUsePromoRequest()
                    for (ordersItem in validateUsePromoRequest.orders) {
                        if (ordersItem.uniqueId == shipmentCartItemModel.cartString) {
                            if (!ordersItem.codes.contains(courierItemData.selectedShipper.logPromoCode)) {
                                ordersItem.codes.add(courierItemData.selectedShipper.logPromoCode!!)
                            }
                            ordersItem.shippingId = courierItemData.selectedShipper.shipperId
                            ordersItem.spId = courierItemData.selectedShipper.shipperProductId
                            ordersItem.freeShippingMetadata =
                                courierItemData.selectedShipper.freeShippingMetadata
                            ordersItem.shopId = shipmentCartItemModel.shopId
                            ordersItem.isPo = shipmentCartItemModel.isProductIsPreorder
                            ordersItem.poDuration =
                                shipmentCartItemModel.cartItemModels[0].preOrderDurationDay
                            ordersItem.warehouseId = shipmentCartItemModel.fulfillmentId
                            ordersItem.boCampaignId = courierItemData.selectedShipper.boCampaignId
                            ordersItem.shippingSubsidy =
                                courierItemData.selectedShipper.shippingSubsidy
                            ordersItem.benefitClass = courierItemData.selectedShipper.benefitClass
                            ordersItem.shippingPrice =
                                courierItemData.selectedShipper.shippingRate.toDouble()
                            ordersItem.etaText = courierItemData.selectedShipper.etaText!!
                            ordersItem.validationMetadata = shipmentCartItemModel.validationMetadata
                            break
                        }
                    }
                    val shipmentCartItemModelLists = shipmentAdapter.shipmentCartItemModelList
                    if (shipmentCartItemModelLists != null && shipmentCartItemModelLists.isNotEmpty() && !shipmentCartItemModel.isFreeShippingPlus) {
                        for (tmpShipmentCartItemModel in shipmentCartItemModelLists) {
                            for (order in validateUsePromoRequest.orders) {
                                if (shipmentCartItemModel.cartString != tmpShipmentCartItemModel.cartString && tmpShipmentCartItemModel.cartString == order.uniqueId && tmpShipmentCartItemModel.selectedShipmentDetailData != null && tmpShipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null &&
                                    !tmpShipmentCartItemModel.isFreeShippingPlus
                                ) {
                                    order.codes.remove(tmpShipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.logPromoCode)
                                }
                            }
                        }
                    }
                    shipmentPresenter.doValidateUseLogisticPromo(
                        itemPosition,
                        cartString,
                        validateUsePromoRequest,
                        courierItemData.selectedShipper.logPromoCode!!,
                        true
                    )
                }
                checkCourierPromo(courierItemData, itemPosition)
                shipmentAdapter.setSelectedCourier(
                    itemPosition,
                    courierItemData,
                    false,
                    shouldValidatePromo
                )
                if (!shouldValidatePromo && shipmentPresenter.logisticDonePublisher != null) {
                    shipmentPresenter.logisticDonePublisher?.onCompleted()
                }
            }
            onNeedUpdateViewItem(itemPosition)
            shipmentPresenter.processSaveShipmentState(shipmentCartItemModel)
        }
    }

    override fun renderCourierStateFailed(
        itemPosition: Int,
        isTradeInDropOff: Boolean,
        isBoAutoApplyFlow: Boolean
    ) {
        if (context != null) {
            val shipmentCartItemModel =
                shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition)
            if (shipmentCartItemModel != null) {
                shipmentCartItemModel.isStateLoadingCourierState = false
                if (isTradeInDropOff) {
                    shipmentCartItemModel.isStateHasLoadCourierTradeInDropOffState = true
                } else {
                    shipmentCartItemModel.isStateHasLoadCourierState = true
                }
                if (shipmentCartItemModel.boCode.isNotEmpty()) {
                    shipmentPresenter.cancelAutoApplyPromoStackLogistic(
                        itemPosition,
                        shipmentCartItemModel.boCode,
                        shipmentCartItemModel
                    )
                    shipmentPresenter.clearOrderPromoCodeFromLastValidateUseRequest(
                        shipmentCartItemModel.cartString,
                        shipmentCartItemModel.boCode
                    )
                    shipmentCartItemModel.boCode = ""
                    showToastNormal(getString(R.string.checkout_failed_auto_apply_bo_message))
                } else if (isBoAutoApplyFlow) {
                    showToastNormal(getString(R.string.checkout_failed_auto_apply_bo_message))
                }
                onNeedUpdateViewItem(itemPosition)
            }
        }
    }

    override fun cancelAllCourierPromo() {
        shipmentAdapter.cancelAllCourierPromo()
    }

    override fun navigateToSetPinpoint(message: String, locationPass: LocationPass?) {
        sendAnalyticsOnClickEditPinPointErrorValidation(message)
        if (view != null) {
            val inflater = layoutInflater
            val layout =
                inflater.inflate(R.layout.toast_rectangle, view!!.findViewById(R.id.toast_layout))
            val tvMessage = layout.findViewById<TextView>(R.id.tv_message)
            tvMessage.text = message
            val toast = Toast(activity)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        } else {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
        if (activity != null) {
            navigateToPinpointActivity(locationPass)
        }
    }

    override fun sendAnalyticsOnClickEditPinPointErrorValidation(message: String) {
        checkoutAnalyticsChangeAddress.eventViewShippingCartChangeAddressViewValidationErrorTandaiLokasi(
            message
        )
    }

    override val activityContext: Activity?
        get() = activity

    override fun sendEnhancedEcommerceAnalyticsCheckout(
        stringObjectMap: Map<String, Any>,
        tradeInCustomDimension: Map<String, String>?,
        transactionId: String?,
        userId: String,
        promoFlag: Boolean,
        eventCategory: String,
        eventAction: String,
        eventLabel: String
    ) {
        checkoutAnalyticsCourierSelection.sendEnhancedECommerceCheckout(
            stringObjectMap,
            tradeInCustomDimension,
            transactionId,
            userId,
            promoFlag,
            eventCategory,
            eventAction,
            eventLabel
        )
        checkoutAnalyticsCourierSelection.flushEnhancedECommerceCheckout()
    }

    override fun sendEnhancedEcommerceAnalyticsCrossSellClickPilihPembayaran(
        eventLabel: String?,
        userId: String?,
        listProducts: List<Any>?
    ) {
        checkoutAnalyticsCourierSelection.sendCrossSellClickPilihPembayaran(
            eventLabel!!,
            userId!!,
            listProducts
        )
    }

    override fun sendAnalyticsOnClickChooseOtherAddressShipment() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickPilihAlamatLain()
    }

    override fun sendAnalyticsOnClickTopDonation() {
        checkoutAnalyticsCourierSelection.eventClickCourierSelectionClickTopDonasi()
    }

    override fun sendAnalyticsOnClickChangeAddress() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickGantiAlamatAtauKirimKeBeberapaAlamat()
    }

    override fun renderChangeAddressSuccess(refreshCheckoutPage: Boolean) {
        if (refreshCheckoutPage) {
            shipmentPresenter.processInitialLoadCheckoutPage(
                true, isOneClickShipment, isTradeIn, true,
                false, shipmentAdapter.addressShipmentData?.cornerId, deviceId, checkoutLeasingId,
                isPlusSelected()
            )
        }
    }

    override fun renderChangeAddressFailed(refreshCheckoutPageIfSuccess: Boolean) {
        val recipientAddressModel = shipmentAdapter.addressShipmentData!!
        if (recipientAddressModel.selectedTabIndex == RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT) {
            recipientAddressModel.selectedTabIndex =
                RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN
            recipientAddressModel.isIgnoreSelectionAction = true
        } else if (recipientAddressModel.selectedTabIndex == RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN) {
            if (refreshCheckoutPageIfSuccess) {
                recipientAddressModel.locationDataModel = null
                recipientAddressModel.dropOffAddressDetail = ""
                recipientAddressModel.dropOffAddressName = ""
            } else {
                recipientAddressModel.selectedTabIndex =
                    RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT
                recipientAddressModel.isIgnoreSelectionAction = true
            }
        }
        onNeedUpdateViewItem(shipmentAdapter.recipientAddressModelPosition)
    }

    override fun generateNewCheckoutRequest(
        shipmentCartItemModelList: List<ShipmentCartItemModel>?,
        isAnalyticsPurpose: Boolean
    ): List<DataCheckoutRequest> {
        val requestData =
            shipmentAdapter.getRequestData(null, shipmentCartItemModelList, isAnalyticsPurpose)
        return requestData.checkoutRequestData
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        hideLoading()
        when (requestCode) {
            PaymentConstant.REQUEST_CODE -> {
                onResultFromPayment(resultCode, data)
            }
            LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY -> {
                onResultFromAddNewAddress(resultCode, data)
            }
            CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS -> {
                onResultFromRequestCodeAddressOptions(resultCode, data)
            }
            REQUEST_CODE_COURIER_PINPOINT -> {
                onResultFromCourierPinpoint(resultCode, data)
            }
            REQUEST_CODE_EDIT_ADDRESS -> {
                onResultFromEditAddress()
            }
            LogisticConstant.REQUEST_CODE_PICK_DROP_OFF_TRADE_IN -> {
                onResultFromSetTradeInPinpoint(data)
            }
            REQUEST_CODE_PROMO -> {
                onResultFromPromo(resultCode, data)
            }
            REQUEST_ADD_ON_PRODUCT_LEVEL_BOTTOMSHEET -> {
                onUpdateResultAddOnProductLevelBottomSheet(data)
            }
            REQUEST_ADD_ON_ORDER_LEVEL_BOTTOMSHEET -> {
                onUpdateResultAddOnOrderLevelBottomSheet(data)
            }
            REQUEST_CODE_UPLOAD_PRESCRIPTION -> {
                onUploadPrescriptionResult(data, false)
            }
            REQUEST_CODE_MINI_CONSULTATION -> {
                onMiniConsultationResult(resultCode, data)
            }
            REQUEST_CODE_UPSELL -> {
                onResultFromUpsell(data)
            }
        }
    }

    // Re-fetch rates to get promo mvc icon for all order, except already reloaded unique ids
    private fun reloadCourierForMvc(
        appliedMvcCartStrings: ArrayList<String>?,
        reloadedUniqueIds: ArrayList<String>?
    ) {
        val obj = shipmentAdapter.getShipmentDataList()
        if (obj.isNotEmpty()) {
            for (i in obj.indices) {
                if (obj[i] is ShipmentCartItemModel) {
                    val shipmentCartItemModel = obj[i] as ShipmentCartItemModel
                    if (appliedMvcCartStrings != null && appliedMvcCartStrings.contains(
                            shipmentCartItemModel.cartString
                        ) && !reloadedUniqueIds!!.contains(shipmentCartItemModel.cartString)
                    ) {
                        prepareReloadRates(i, false)
                    } else if (!reloadedUniqueIds!!.contains(shipmentCartItemModel.cartString)) {
                        prepareReloadRates(i, true)
                    }
                }
            }
        }
    }

    private fun onResultFromPromo(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (data!!.getStringExtra(ARGS_PROMO_ERROR) != null && data.getStringExtra(
                    ARGS_PROMO_ERROR
                ) == ARGS_FINISH_ERROR && activity != null
            ) {
                activity!!.finish()
            } else {
                shipmentPresenter.couponStateChanged = true
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
                                }
                            }
                        }
                    }
                    shipmentPresenter.setLatValidateUseRequest(validateUsePromoRequest)
                    if (!stillHasPromo) {
                        doResetButtonPromoCheckout()
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
                    shipmentPresenter.validateUsePromoRevampUiModel =
                        validateUsePromoRevampUiModel
                    doUpdateButtonPromoCheckout(validateUsePromoRevampUiModel.promoUiModel)
                    updatePromoTrackingData(validateUsePromoRevampUiModel.promoUiModel.trackingDetailUiModels)
                    sendEEStep3()
                    val validateBoResult =
                        shipmentPresenter.validateBoPromo(validateUsePromoRevampUiModel)
                    reloadedUniqueIds = validateBoResult.first
                    val unappliedUniqueIds = validateBoResult.second
                    if (messageInfo.isNotEmpty()) {
                        showToastNormal(messageInfo)
                    } else if (unappliedUniqueIds!!.size > 0) {
                        // when messageInfo is empty and has unapplied BO show hard coded toast
                        showToastNormal(getString(com.tokopedia.purchase_platform.common.R.string.pp_auto_unapply_bo_toaster_message))
                    }
                    doSetPromoBenefit(
                        validateUsePromoRevampUiModel.promoUiModel.benefitSummaryInfoUiModel.summaries,
                        true
                    )
                }
                val clearPromoUiModel =
                    data.getParcelableExtra<ClearPromoUiModel>(ARGS_CLEAR_PROMO_RESULT)
                if (clearPromoUiModel != null) {
                    val promoUiModel = PromoUiModel()
                    promoUiModel.titleDescription =
                        clearPromoUiModel.successDataModel.defaultEmptyPromoMessage
                    val tickerAnnouncementHolderData =
                        shipmentPresenter.tickerAnnouncementHolderData
                    if (!TextUtils.isEmpty(clearPromoUiModel.successDataModel.tickerMessage)) {
                        tickerAnnouncementHolderData.title = ""
                        tickerAnnouncementHolderData.message =
                            clearPromoUiModel.successDataModel.tickerMessage
                        updateTickerAnnouncementMessage()
                    }
                    doUpdateButtonPromoCheckout(promoUiModel)
                    shipmentPresenter.validateUsePromoRevampUiModel = null
                    shipmentPresenter.validateClearAllBoPromo()
                    shipmentAdapter.checkHasSelectAllCourier(false, -1, "", false, false)
                }
                val requiredToReloadRatesForMvcCourier =
                    data.getBooleanExtra(ARGS_PROMO_MVC_LOCK_COURIER_FLOW, false)
                val appliedMvcCartStrings =
                    data.getStringArrayListExtra(ARGS_APPLIED_MVC_CART_STRINGS)
                if (requiredToReloadRatesForMvcCourier) {
                    reloadCourierForMvc(appliedMvcCartStrings, reloadedUniqueIds)
                }
            }
        }
    }

    private fun onResultFromEditAddress() {
        shipmentPresenter.processInitialLoadCheckoutPage(
            true, isOneClickShipment, isTradeIn, true,
            false, null, deviceId, checkoutLeasingId,
            isPlusSelected()
        )
    }

    private fun onResultFromCourierPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data!!.extras != null) {
            val locationPass =
                data.extras!!.getParcelable<LocationPass>(LogisticConstant.EXTRA_EXISTING_LOCATION)
            if (locationPass != null) {
                val index = shipmentAdapter.lastChooseCourierItemPosition
                val shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(index)
                shipmentPresenter.editAddressPinpoint(
                    locationPass.latitude,
                    locationPass.longitude,
                    shipmentCartItemModel,
                    locationPass
                )
            }
        } else {
            shipmentAdapter.lastServiceId = 0
        }
    }

    private fun onResultFromPayment(resultCode: Int, data: Intent?) {
        when (resultCode) {
            PaymentConstant.PAYMENT_FAILED, PaymentConstant.PAYMENT_CANCELLED -> {
                if (data != null && data.getBooleanExtra(
                        PaymentConstant.EXTRA_HAS_CLEAR_RED_STATE_PROMO_BEFORE_CHECKOUT,
                        false
                    )
                ) {
                    shipmentPresenter.processInitialLoadCheckoutPage(
                        true, isOneClickShipment, isTradeIn, true,
                        false, null, deviceId, checkoutLeasingId,
                        isPlusSelected()
                    )
                }
                if (data != null && data.getBooleanExtra(
                        PaymentConstant.EXTRA_PAGE_TIME_OUT,
                        false
                    )
                ) {
                    showToastError(getString(R.string.checkout_label_payment_try_again))
                }
            }
            else -> {
                val activity: Activity? = activity
                activity?.finish()
            }
        }
    }

    private fun updatePromoTrackingData(trackingDetailsItemUiModels: List<TrackingDetailsItemUiModel>) {
        val dataList = shipmentAdapter.shipmentCartItemModelList ?: return
        for (shipmentCartItemModel in dataList) {
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                if (trackingDetailsItemUiModels.isNotEmpty()) {
                    for (trackingDetail in trackingDetailsItemUiModels) {
                        if (trackingDetail.productId == cartItemModel.productId) {
                            cartItemModel.analyticsProductCheckoutData.promoCode = trackingDetail.promoCodesTracking
                            cartItemModel.analyticsProductCheckoutData.promoDetails = trackingDetail.promoDetailsTracking
                        }
                    }
                }
            }
        }
    }

    private fun clearPromoTrackingData() {
        val dataList = shipmentAdapter.shipmentCartItemModelList ?: return
        for (shipmentCartItemModel in dataList) {
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                cartItemModel.analyticsProductCheckoutData.promoCode = ""
                cartItemModel.analyticsProductCheckoutData.promoDetails = ""
            }
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
                shipmentPresenter.processInitialLoadCheckoutPage(
                    false, isOneClickShipment, isTradeIn, false,
                    false, null, deviceId, checkoutLeasingId,
                    isPlusSelected()
                )
            }
        }
    }

    private fun onResultFromRequestCodeAddressOptions(resultCode: Int, data: Intent?) {
        when (resultCode) {
            CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS -> {
                val currentAddress = shipmentAdapter.addressShipmentData
                val chosenAddressModel =
                    data!!.getParcelableExtra<ChosenAddressModel>(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA)
                if (currentAddress != null && chosenAddressModel != null) {
                    shipmentPresenter.changeShippingAddress(
                        currentAddress,
                        chosenAddressModel,
                        isOneClickShipment,
                        false,
                        false,
                        true
                    )
                }
            }
            Activity.RESULT_CANCELED -> if (activity != null && data == null && shipmentPresenter.shipmentCartItemModelList == null) {
                activity!!.finish()
            }
            else -> shipmentPresenter.processInitialLoadCheckoutPage(
                false, isOneClickShipment, isTradeIn, false,
                false, null, deviceId, checkoutLeasingId,
                isPlusSelected()
            )
        }
    }

    override fun onChangeAddress() {
        sendAnalyticsOnClickChangeAddress()
        sendAnalyticsOnClickChooseOtherAddressShipment()
        if (isTradeIn) {
            checkoutTradeInAnalytics.eventTradeInClickChangeAddress()
        }
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
        intent.putExtra(EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS, true)
        intent.putExtra(PARAM_SOURCE, ManageAddressSource.CHECKOUT.source)
        startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS)
    }

    override fun getShipmentDetailData(
        shipmentCartItemModel: ShipmentCartItemModel?,
        recipientAddressModel: RecipientAddressModel?
    ): ShipmentDetailData {
        var oldShipmentDetailData: ShipmentDetailData? = null
        if (shipmentCartItemModel!!.selectedShipmentDetailData != null &&
            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null
        ) {
            oldShipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
        }
        val shipmentDetailData: ShipmentDetailData = ratesDataConverter.getShipmentDetailData(
            shipmentCartItemModel,
            recipientAddressModel!!
        )
        if (oldShipmentDetailData?.selectedCourier != null) {
            shipmentDetailData.selectedCourier = oldShipmentDetailData.selectedCourier
        }
        shipmentDetailData.isTradein = isTradeIn
        return shipmentDetailData
    }

    override fun sendAnalyticsOnClickSubtotal() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickSubtotal()
    }

    override fun sendAnalyticsOnClickChecklistShipmentRecommendationDuration(duration: String?) {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickChecklistPilihDurasiPengiriman(
            duration
        )
    }

    override fun sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(courier: String?) {
        checkoutAnalyticsCourierSelection.eventViewCourierCourierSelectionViewPreselectedCourierOption(
            courier
        )
    }

    override fun sendAnalyticsOnViewPreselectedCourierAfterPilihDurasi(shippingProductId: Int) {
        checkoutAnalyticsCourierSelection.eventViewPreselectedCourierOption(shippingProductId)
    }

    override fun sendAnalyticsOnDisplayDurationThatContainPromo(
        isCourierPromo: Boolean,
        duration: String?
    ) {
        checkoutAnalyticsCourierSelection.eventViewDuration(isCourierPromo, duration)
    }

    override fun sendAnalyticsOnDisplayLogisticThatContainPromo(
        isCourierPromo: Boolean,
        shippingProductId: Int
    ) {
        checkoutAnalyticsCourierSelection.eventViewCourierOption(
            isCourierPromo,
            shippingProductId
        )
    }

    override fun sendAnalyticsOnClickDurationThatContainPromo(
        isCourierPromo: Boolean,
        duration: String?,
        isCod: Boolean,
        shippingPriceMin: String?,
        shippingPriceHigh: String?
    ) {
        checkoutAnalyticsCourierSelection.eventClickChecklistPilihDurasiPengiriman(
            isCourierPromo,
            duration,
            isCod,
            shippingPriceMin!!,
            shippingPriceHigh!!
        )
    }

    override fun sendAnalyticsOnClickLogisticThatContainPromo(
        isCourierPromo: Boolean,
        shippingProductId: Int,
        isCod: Boolean
    ) {
        checkoutAnalyticsCourierSelection.eventClickChangeCourierOption(
            isCourierPromo,
            shippingProductId,
            isCod
        )
    }

    override fun sendAnalyticsOnClickChangeCourierShipmentRecommendation(shipmentCartItemModel: ShipmentCartItemModel?) {
        var label = ""
        if (shipmentCartItemModel!!.selectedShipmentDetailData != null && shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null && shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.ontimeDelivery != null) {
            val otdg =
                shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.ontimeDelivery
            if (otdg!!.available) {
                label = getString(R.string.otdg_gtm_label)
            }
        }
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickUbahKurir(label)
    }

    override fun sendAnalyticsOnClickChangeDurationShipmentRecommendation() {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickUbahDurasi()
    }

    override fun sendAnalyticsOnClickCheckBoxDropShipperOption() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickDropship()
    }

    override fun sendAnalyticsOnClickCheckBoxInsuranceOption() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionClickAsuransiPengiriman()
    }

    override fun sendAnalyticsScreenName(screenName: String?) {
        checkoutAnalyticsCourierSelection.sendScreenName(activity, screenName!!)
    }

    override fun sendAnalyticsCourierNotComplete() {
        checkoutAnalyticsCourierSelection.eventClickBuyCourierSelectionClickPilihMetodePembayaranCourierNotComplete()
    }

    override fun sendAnalyticsPromoRedState() {
        checkoutAnalyticsCourierSelection.eventClickBuyPromoRedState()
    }

    override fun sendAnalyticsDropshipperNotComplete() {
        checkoutAnalyticsCourierSelection.eventClickBuyCourierSelectionClickBayarFailedDropshipper()
    }

    override fun sendAnalyticsOnClickButtonCloseShipmentRecommendationDuration() {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickXPadaDurasiPengiriman()
    }

    override fun sendAnalyticsOnClickButtonCloseShipmentRecommendationCourier() {
        checkoutAnalyticsCourierSelection.eventClickCourierCourierSelectionClickXPadaKurirPengiriman()
    }

    override fun sendAnalyticsViewInformationAndWarningTickerInCheckout(tickerId: String?) {
        checkoutAnalyticsCourierSelection.eventViewInformationAndWarningTickerInCheckout(tickerId!!)
    }

    override fun sendAnalyticsViewPromoAfterAdjustItem(msg: String?) {
        checkoutAnalyticsCourierSelection.eventViewPromoAfterAdjustItem(msg!!)
    }

    override fun onTotalPaymentChange(totalPayment: String, enable: Boolean) {
        val shipmentButtonPaymentModel = shipmentPresenter.getShipmentButtonPaymentModel()
        shipmentButtonPaymentModel.totalPrice = totalPayment
        shipmentButtonPaymentModel.enable = enable
        onNeedUpdateViewItem(shipmentAdapter.itemCount - 1)
    }

    override fun onFinishChoosingShipment(
        lastSelectedCourierOrder: Int,
        lastSelectedCourierOrderCartString: String?,
        forceHitValidateUse: Boolean,
        skipValidateUse: Boolean
    ) {
        var stillHasPromo = false
        val lastValidateUseRequest = shipmentPresenter.lastValidateUseRequest
        if (lastValidateUseRequest != null) {
            if (lastValidateUseRequest.codes.size > 0) {
                stillHasPromo = true
            } else {
                for (order in lastValidateUseRequest.orders) {
                    if (order.codes.size > 0) {
                        stillHasPromo = true
                        break
                    }
                }
            }
        } else {
            val lastApplyUiModel = shipmentPresenter.lastApplyData
            if (lastApplyUiModel != null) {
                if (lastApplyUiModel.codes.isNotEmpty()) {
                    stillHasPromo = true
                } else {
                    if (lastApplyUiModel.voucherOrders.isNotEmpty()) {
                        for (voucherOrder in lastApplyUiModel.voucherOrders) {
                            if (voucherOrder.code.isNotEmpty()) {
                                stillHasPromo = true
                                break
                            }
                        }
                    }
                }
            }
        }
        if (stillHasPromo && !skipValidateUse) {
            shipmentPresenter.checkPromoCheckoutFinalShipment(
                generateValidateUsePromoRequest().copy(),
                lastSelectedCourierOrder,
                lastSelectedCourierOrderCartString
            )
        } else {
            clearPromoTrackingData()
            if (forceHitValidateUse) {
                shipmentPresenter.checkPromoCheckoutFinalShipment(
                    generateValidateUsePromoRequest().copy(),
                    lastSelectedCourierOrder,
                    lastSelectedCourierOrderCartString
                )
            } else {
                sendEEStep3()
            }
        }
    }

    private fun sendEEStep3() {
        val shipmentCartItemModels = shipmentAdapter.shipmentCartItemModelList ?: return

        // if one of courier reseted because of apply promo logistic (PSL) and eventually not eligible after hit validate use, don't send EE
        var courierHasReseted = false
        for (shipmentCartItemModel in shipmentCartItemModels) {
            val selectedShipmentDetailData = shipmentCartItemModel.selectedShipmentDetailData
            if (selectedShipmentDetailData == null) {
                courierHasReseted = true
                break
            }
            val selectedCourier = selectedShipmentDetailData.selectedCourier
            if (selectedCourier == null) {
                courierHasReseted = true
                break
            }
            val dataCheckoutRequests =
                shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerShippingData(
                    shipmentCartItemModel.cartString,
                    selectedCourier.selectedShipper.serviceId.toString(),
                    selectedCourier.selectedShipper.shipperPrice.toString(),
                    shipmentCartItemModel.spId.toString()
                )
            shipmentPresenter.setDataCheckoutRequestList(dataCheckoutRequests)
        }
        if (!courierHasReseted) {
            val dataCheckoutRequests =
                shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(
                    shipmentCartItemModels
                )
            shipmentPresenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
                dataCheckoutRequests,
                null,
                EnhancedECommerceActionField.STEP_3,
                ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION,
                ConstantTransactionAnalytics.EventAction.CLICK_ALL_COURIER_SELECTED,
                "",
                checkoutLeasingId,
                checkoutPageSource
            )
        }
    }

    override fun updateCheckoutRequest(checkoutRequestData: List<DataCheckoutRequest>?) {
        shipmentPresenter.setDataCheckoutRequestList(checkoutRequestData)
    }

    // Project ARMY, clear from promo BBO ticker
    override fun onCancelVoucherLogisticClicked(
        pslCode: String,
        position: Int,
        shipmentCartItemModel: ShipmentCartItemModel
    ) {
        checkoutAnalyticsCourierSelection.eventCancelPromoStackingLogistic()
        shipmentPresenter.cancelAutoApplyPromoStackLogistic(
            position,
            pslCode,
            shipmentCartItemModel
        )
        shipmentAdapter.cancelAllCourierPromo()
    }

    override fun onDataEnableToCheckout() {
        shipmentAdapter.updateCheckoutButtonData(null)
    }

    override fun onDataDisableToCheckout(message: String?) {
        shipmentAdapter.updateCheckoutButtonData(null)
    }

    override fun onCheckoutValidationResult(
        result: Boolean,
        shipmentData: Any?,
        errorPosition: Int,
        epharmacyError: Boolean
    ) {
        if (shipmentData == null && result) {
            if (shipmentPresenter.isIneligiblePromoDialogEnabled) {
                val notEligiblePromoHolderdataList = ArrayList<NotEligiblePromoHolderdata>()
                val validateUsePromoRevampUiModel =
                    shipmentPresenter.validateUsePromoRevampUiModel
                if (validateUsePromoRevampUiModel != null) {
                    if (validateUsePromoRevampUiModel.promoUiModel.messageUiModel.state == "red") {
                        val notEligiblePromoHolderdata = NotEligiblePromoHolderdata()
                        notEligiblePromoHolderdata.promoTitle =
                            validateUsePromoRevampUiModel.promoUiModel.titleDescription
                        if (validateUsePromoRevampUiModel.promoUiModel.codes.size > 0) {
                            notEligiblePromoHolderdata.promoCode =
                                validateUsePromoRevampUiModel.promoUiModel.codes[0]
                        }
                        notEligiblePromoHolderdata.shopName = "Kode promo"
                        notEligiblePromoHolderdata.iconType = TYPE_ICON_GLOBAL
                        notEligiblePromoHolderdata.showShopSection = true
                        notEligiblePromoHolderdata.errorMessage =
                            validateUsePromoRevampUiModel.promoUiModel.messageUiModel.text
                        notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata)
                    }
                    val voucherOrdersItemUiModels =
                        validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels
                    if (voucherOrdersItemUiModels.isNotEmpty()) {
                        for (i in voucherOrdersItemUiModels.indices) {
                            val voucherOrdersItemUiModel = voucherOrdersItemUiModels[i]
                            if (voucherOrdersItemUiModel.messageUiModel.state == "red") {
                                val notEligiblePromoHolderdata = NotEligiblePromoHolderdata()
                                notEligiblePromoHolderdata.promoTitle =
                                    voucherOrdersItemUiModel.titleDescription
                                notEligiblePromoHolderdata.promoCode = voucherOrdersItemUiModel.code
                                notEligiblePromoHolderdata.uniqueId =
                                    voucherOrdersItemUiModel.uniqueId
                                for (shipmentCartItemModel in shipmentAdapter.shipmentCartItemModelList!!) {
                                    if (shipmentCartItemModel.cartString == voucherOrdersItemUiModel.uniqueId) {
                                        notEligiblePromoHolderdata.shopName =
                                            shipmentCartItemModel.shopName!!
                                        notEligiblePromoHolderdata.shopBadge =
                                            shipmentCartItemModel.shopTypeInfoData.shopBadge
                                        break
                                    }
                                }
                                if (i <= 0) {
                                    notEligiblePromoHolderdata.showShopSection = true
                                } else if (voucherOrdersItemUiModels[i - 1].uniqueId.isNotEmpty() && voucherOrdersItemUiModel.uniqueId.isNotEmpty() && voucherOrdersItemUiModels[i - 1].uniqueId == voucherOrdersItemUiModel.uniqueId) {
                                    notEligiblePromoHolderdata.showShopSection = false
                                } else {
                                    notEligiblePromoHolderdata.showShopSection = true
                                }
                                notEligiblePromoHolderdata.errorMessage =
                                    validateUsePromoRevampUiModel.promoUiModel.messageUiModel.text
                                notEligiblePromoHolderdataList.add(notEligiblePromoHolderdata)
                            }
                        }
                    }
                }
                sendAnalyticsEpharmacyClickPembayaran()
                if (notEligiblePromoHolderdataList.size > 0) {
                    hasClearPromoBeforeCheckout = true
                    shipmentPresenter.cancelNotEligiblePromo(notEligiblePromoHolderdataList)
                } else {
                    hasClearPromoBeforeCheckout = false
                    if (shipmentPresenter.isUsingDynamicDataPassing()) {
                        shipmentPresenter.validateDynamicData()
                    } else {
                        doCheckout()
                    }
                }
            } else {
                var hasRedStatePromo = false
                var errorMessage = ""
                val validateUsePromoRevampUiModel =
                    shipmentPresenter.validateUsePromoRevampUiModel
                if (validateUsePromoRevampUiModel != null) {
                    if (validateUsePromoRevampUiModel.promoUiModel.messageUiModel.state == "red") {
                        hasRedStatePromo = true
                        errorMessage =
                            validateUsePromoRevampUiModel.promoUiModel.messageUiModel.text
                    } else {
                        val voucherOrdersItemUiModels =
                            validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels
                        if (validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels.isNotEmpty()) {
                            for (voucherOrder in voucherOrdersItemUiModels) {
                                if (voucherOrder.messageUiModel.state == "red") {
                                    hasRedStatePromo = true
                                    errorMessage = voucherOrder.messageUiModel.text
                                    break
                                }
                            }
                        }
                    }
                }
                if (hasRedStatePromo) {
                    hasRunningApiCall = false
                    hideLoading()
                    showToastError(errorMessage)
                    sendAnalyticsPromoRedState()
                } else {
                    if (shipmentPresenter.isUsingDynamicDataPassing()) {
                        shipmentPresenter.validateDynamicData()
                    } else {
                        sendAnalyticsEpharmacyClickPembayaran()
                        doCheckout()
                    }
                }
            }
        } else if (shipmentData != null && !result) {
            hasRunningApiCall = false
            hideLoading()
            sendAnalyticsDropshipperNotComplete()
            if (errorPosition != ShipmentAdapter.DEFAULT_ERROR_POSITION) {
                rvShipment!!.smoothScrollToPosition(errorPosition)
                onDataDisableToCheckout(null)
                val message = activity!!.getString(R.string.message_error_dropshipper_empty)
                showToastNormal(message)
                (shipmentData as ShipmentCartItemModel).isStateDropshipperHasError = true
                shipmentData.isStateAllItemViewExpanded = false
                onNeedUpdateViewItem(errorPosition)
            }
        } else if (shipmentData == null) {
            hasRunningApiCall = false
            hideLoading()
            if (isTradeIn) {
                checkoutTradeInAnalytics.eventClickBayarCourierNotComplete()
            }
            if (!epharmacyError) {
                sendAnalyticsCourierNotComplete()
            }
            checkShippingCompletion(true, epharmacyError)
        }
    }

    override fun doCheckout() {
        shipmentPresenter.processSaveShipmentState()
        shipmentPresenter.processCheckout(
            isOneClickShipment,
            isTradeIn,
            isTradeInByDropOff,
            deviceId,
            cornerId,
            checkoutLeasingId,
            isPlusSelected()
        )
    }

    private fun updateCheckboxDynamicData(newParam: DynamicDataParam, isChecked: Boolean) {
        val existingDdpParam = shipmentPresenter.getDynamicDataParam()
        var isAdded = false
        if (newParam.attribute.equals(ATTRIBUTE_DONATION, ignoreCase = true)) {
            for (existingParam in shipmentPresenter.getDynamicDataParam().data) {
                if (existingParam.attribute.equals(ATTRIBUTE_DONATION, ignoreCase = true)) {
                    isAdded = true
                    existingParam.donation = isChecked
                }
            }
        } else if (newParam.attribute.equals(ATTRIBUTE_ADDON_DETAILS, ignoreCase = true)) {
            if (isChecked) {
                for (existingParam in shipmentPresenter.getDynamicDataParam().data) {
                    if (existingParam.uniqueId.equals(newParam.uniqueId, ignoreCase = true)) {
                        isAdded = true
                        existingParam.addOn = newParam.addOn
                    }
                }
            } else {
                for (i in existingDdpParam.data.indices) {
                    val param = shipmentPresenter.getDynamicDataParam().data[i]
                    if (param.uniqueId.equals(newParam.uniqueId, ignoreCase = true)) {
                        existingDdpParam.data = existingDdpParam.data.toMutableList().apply { removeAt(i) }
                    }
                }
            }
        }
        if (!isAdded && isChecked) {
            existingDdpParam.data = existingDdpParam.data.toMutableList().apply { add(newParam) }
        }
        var source: String = SOURCE_NORMAL
        if (isOneClickShipment) source = SOURCE_OCS
        existingDdpParam.source = source
        shipmentPresenter.setDynamicDataParam(existingDdpParam)
        shipmentPresenter.updateDynamicData(existingDdpParam, true)
    }

    override fun onPriorityChecked(position: Int) {
        if (rvShipment?.isComputingLayout == true) {
            rvShipment?.post {
                shipmentAdapter.updateShipmentCostModel()
                shipmentAdapter.updateItemAndTotalCost(position)
            }
        } else {
            shipmentAdapter.updateShipmentCostModel()
            shipmentAdapter.updateItemAndTotalCost(position)
        }
    }

    override fun onInsuranceChecked(position: Int) {
        if (rvShipment?.isComputingLayout == true) {
            rvShipment?.post {
                shipmentAdapter.updateShipmentCostModel()
                shipmentAdapter.updateItemAndTotalCost(position)
                shipmentAdapter.updateInsuranceTncVisibility()
            }
        } else {
            shipmentAdapter.updateShipmentCostModel()
            shipmentAdapter.updateItemAndTotalCost(position)
            shipmentAdapter.updateInsuranceTncVisibility()
        }
    }

    override fun onNeedUpdateViewItem(position: Int) {
        if (rvShipment?.isComputingLayout == true) {
            rvShipment?.post { shipmentAdapter.notifyItemChanged(position) }
        } else {
            shipmentAdapter.notifyItemChanged(position)
        }
    }

    override fun onSubTotalItemClicked(position: Int) {
        sendAnalyticsOnClickSubtotal()
    }

    override fun onInsuranceTncClicked() {
        val intent = newInstance(
            context!!,
            CartConstant.TERM_AND_CONDITION_URL,
            getString(R.string.title_activity_checkout_tnc_webview)
        )
        startActivity(intent)
    }

    override fun onPriorityTncClicker() {
        val intent = newInstance(
            context!!,
            CartConstant.PRIORITY_TNC_URL,
            getString(R.string.title_activity_checkout_tnc_webview)
        )
        startActivity(intent)
    }

    override fun onOntimeDeliveryClicked(url: String?) {
        val intent = newInstance(
            context!!,
            url!!,
            getString(R.string.title_activity_checkout_tnc_webview)
        )
        startActivity(intent)
    }

    override fun onNeedUpdateRequestData() {
        shipmentAdapter.checkHasSelectAllCourier(true, -1, "", false, false)
    }

    override fun onDropshipCheckedForTrackingAnalytics() {
        sendAnalyticsOnClickCheckBoxDropShipperOption()
    }

    override fun onInsuranceCheckedForTrackingAnalytics() {
        sendAnalyticsOnClickCheckBoxInsuranceOption()
    }

    override fun onDonationChecked(checked: Boolean) {
        if (rvShipment?.isComputingLayout == true) {
            rvShipment?.post { shipmentAdapter.updateDonation(checked) }
        } else {
            shipmentAdapter.updateDonation(checked)
        }
        if (checked) sendAnalyticsOnClickTopDonation()
        checkoutAnalyticsCourierSelection.eventClickCheckboxDonation(checked)
        if (isTradeIn) {
            checkoutTradeInAnalytics.eventTradeInClickDonationOption(isTradeInByDropOff, checked)
        }
        if (shipmentPresenter.isUsingDynamicDataPassing()) {
            val dynamicDataParam = DynamicDataParam()
            dynamicDataParam.level = PAYMENT_LEVEL
            dynamicDataParam.uniqueId = ""
            dynamicDataParam.attribute = ATTRIBUTE_DONATION
            dynamicDataParam.donation = checked
            updateCheckboxDynamicData(dynamicDataParam, checked)
        }
    }

    override fun onCrossSellItemChecked(
        checked: Boolean,
        crossSellModel: CrossSellModel?,
        index: Int
    ) {
        if (rvShipment?.isComputingLayout == true) {
            rvShipment?.post { shipmentAdapter.updateCrossSell(checked, crossSellModel) }
        } else {
            shipmentAdapter.updateCrossSell(checked, crossSellModel)
        }
        val digitalCategoryName = crossSellModel!!.orderSummary.title
        val digitalProductId = crossSellModel.id
        val eventLabel = "$digitalCategoryName - $digitalProductId"
        val digitalProductName = crossSellModel.info.title
        val shipmentCartItemModels = shipmentAdapter.shipmentCartItemModelList
        checkoutAnalyticsCourierSelection.eventClickCheckboxCrossSell(
            checked,
            userSessionInterface.userId,
            index.toString(),
            eventLabel,
            digitalProductName,
            getCrossSellChildCategoryId(shipmentCartItemModels)
        )
    }

    override fun onEgoldChecked(checked: Boolean) {
        shipmentAdapter.updateEgold(checked)
        checkoutEgoldAnalytics.eventClickEgoldRoundup(checked)
        if (isTradeIn) {
            checkoutTradeInAnalytics.eventTradeInClickEgoldOption(isTradeInByDropOff, checked)
        }
    }

    override fun onNeedToSaveState(shipmentCartItemModel: ShipmentCartItemModel) {
        shipmentPresenter.processSaveShipmentState(shipmentCartItemModel)
    }

    override fun getScreenName(): String {
        return if (isOneClickShipment) {
            ConstantTransactionAnalytics.ScreenName.ONE_CLICK_SHIPMENT
        } else {
            ConstantTransactionAnalytics.ScreenName.CHECKOUT
        }
    }

    override fun onStart() {
        super.onStart()
        val activity: Activity? = activity
        if (activity != null) {
            sendAnalyticsScreenName(screenName)
            if (isTradeIn) {
                checkoutTradeInAnalytics.sendOpenScreenName(isTradeInByDropOff, activity)
            }
        }
    }

    override fun setStateLoadingCourierStateAtIndex(index: Int, isLoading: Boolean) {
        shipmentLoadingIndex = if (isLoading) index else -1
        val shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(index)
        shipmentCartItemModel?.isStateLoadingCourierState = isLoading
        onNeedUpdateViewItem(index)
    }

    override fun onLogisticPromoChosen(
        shippingCourierUiModels: List<ShippingCourierUiModel>?,
        courierData: ShippingCourierUiModel?,
        recipientAddressModel: RecipientAddressModel?,
        cartPosition: Int,
        serviceData: ServiceData?,
        flagNeedToSetPinpoint: Boolean,
        promoCode: String,
        selectedServiceId: Int,
        logisticPromo: LogisticPromoUiModel
    ) {
        checkoutAnalyticsCourierSelection.eventClickPromoLogisticTicker(promoCode)
        setStateLoadingCourierStateAtIndex(cartPosition, true)
        val courierItemData = shippingCourierConverter.convertToCourierItemDataWithPromo(
            courierData!!,
            logisticPromo
        )
        onShippingDurationChoosen(
            shippingCourierUiModels, courierItemData, recipientAddressModel,
            cartPosition, selectedServiceId, serviceData, flagNeedToSetPinpoint,
            false, false, false
        )
        val cartString = shipmentAdapter.getShipmentCartItemModelByIndex(cartPosition)!!.cartString
        if (!flagNeedToSetPinpoint) {
            val shipmentCartItemModel =
                shipmentAdapter.getShipmentCartItemModelByIndex(cartPosition)!!
            val validateUsePromoRequest = generateValidateUsePromoRequest()
            if (promoCode.isNotEmpty()) {
                for (order in validateUsePromoRequest.orders) {
                    if (order.uniqueId == shipmentCartItemModel.cartString && !order.codes.contains(promoCode)) {
                        if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                            // remove previous logistic promo code
                            order.codes.remove(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                        }
                        order.codes.add(promoCode)
                        break
                    }
                }
            }
            val shipmentCartItemModelLists = shipmentAdapter.shipmentCartItemModelList
            if (shipmentCartItemModelLists != null && shipmentCartItemModelLists.isNotEmpty() && !shipmentCartItemModel.isFreeShippingPlus) {
                for (tmpShipmentCartItemModel in shipmentCartItemModelLists) {
                    for (order in validateUsePromoRequest.orders) {
                        if (shipmentCartItemModel.cartString != tmpShipmentCartItemModel.cartString && tmpShipmentCartItemModel.cartString == order.uniqueId && tmpShipmentCartItemModel.voucherLogisticItemUiModel != null &&
                            !tmpShipmentCartItemModel.isFreeShippingPlus
                        ) {
                            order.codes.remove(tmpShipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                        }
                    }
                }
            }
            for (ordersItem in validateUsePromoRequest.orders) {
                if (ordersItem.uniqueId == shipmentCartItemModel.cartString) {
                    ordersItem.spId = courierItemData.shipperProductId
                    ordersItem.shippingId = courierItemData.shipperId
                    ordersItem.freeShippingMetadata = courierItemData.freeShippingMetadata
                    ordersItem.shopId = shipmentCartItemModel.shopId
                    ordersItem.isPo = shipmentCartItemModel.isProductIsPreorder
                    ordersItem.poDuration =
                        shipmentCartItemModel.cartItemModels[0].preOrderDurationDay
                    ordersItem.warehouseId = shipmentCartItemModel.fulfillmentId
                    ordersItem.boCampaignId = courierItemData.boCampaignId
                    ordersItem.shippingSubsidy = courierItemData.shippingSubsidy
                    ordersItem.benefitClass = courierItemData.benefitClass
                    ordersItem.shippingPrice = courierItemData.shippingRate.toDouble()
                    ordersItem.etaText = courierItemData.etaText!!
                    break
                }
            }
            shipmentPresenter.doValidateUseLogisticPromo(
                cartPosition,
                cartString,
                validateUsePromoRequest,
                promoCode,
                true
            )
        }
    }

    override fun onShippingDurationChoosen(
        shippingCourierUiModels: List<ShippingCourierUiModel>?,
        selectedCourier: ShippingCourierUiModel?,
        recipientAddressModel: RecipientAddressModel?,
        cartPosition: Int,
        selectedServiceId: Int,
        serviceData: ServiceData?,
        flagNeedToSetPinpoint: Boolean,
        isDurationClick: Boolean,
        isClearPromo: Boolean
    ) {
        var courierItemData: CourierItemData? = null
        if (selectedCourier != null) {
            courierItemData =
                shippingCourierConverter.convertToCourierItemData(selectedCourier, null)
        }
        onShippingDurationChoosen(
            shippingCourierUiModels, courierItemData, recipientAddressModel,
            cartPosition, selectedServiceId, serviceData,
            flagNeedToSetPinpoint, isDurationClick, isClearPromo, false
        )
    }

    private fun onShippingDurationChoosen(
        shippingCourierUiModels: List<ShippingCourierUiModel>?,
        recommendedCourier: CourierItemData?,
        recipientAddressModel: RecipientAddressModel?,
        cartItemPosition: Int,
        selectedServiceId: Int,
        serviceData: ServiceData?,
        flagNeedToSetPinpoint: Boolean,
        isDurationClick: Boolean,
        isClearPromo: Boolean,
        skipCheckAllCourier: Boolean
    ) {
        if (isTradeIn) {
            checkoutTradeInAnalytics.eventClickKurirTradeIn(serviceData!!.serviceName)
        }
        sendAnalyticsOnClickChecklistShipmentRecommendationDuration(serviceData!!.serviceName)
        // Has courier promo means that one of duration has promo, not always current selected duration.
        // It's for analytics purpose
        if (shippingCourierUiModels!!.isNotEmpty()) {
            val serviceDataTracker = shippingCourierUiModels[0].serviceData
            sendAnalyticsOnClickDurationThatContainPromo(
                serviceDataTracker.isPromo == 1,
                serviceDataTracker.serviceName,
                serviceDataTracker.codData.isCod == 1,
                removeDecimalSuffix(
                    convertPriceValueToIdrFormat(
                        serviceDataTracker.rangePrice.minPrice,
                        false
                    )
                ),
                removeDecimalSuffix(
                    convertPriceValueToIdrFormat(
                        serviceDataTracker.rangePrice.maxPrice,
                        false
                    )
                )
            )
        }
        if (flagNeedToSetPinpoint) {
            // If instant courier and has not set pinpoint
            shipmentAdapter.lastServiceId = selectedServiceId
            setPinpoint(cartItemPosition)
        } else if (recommendedCourier == null) {
            // If there's no recommendation, user choose courier manually
            val shipmentCartItemModel =
                shipmentAdapter.getShipmentCartItemModelByIndex(cartItemPosition)
            val shopShipments = shipmentCartItemModel?.shopShipmentList
            onChangeShippingCourier(
                recipientAddressModel,
                shipmentCartItemModel,
                cartItemPosition,
                shippingCourierUiModels
            )
        } else {
            if (recommendedCourier.isUsePinPoint &&
                (
                    recipientAddressModel!!.latitude == null ||
                        recipientAddressModel.latitude.equals(
                                "0",
                                ignoreCase = true
                            ) || recipientAddressModel.longitude == null ||
                        recipientAddressModel.longitude.equals("0", ignoreCase = true)
                    )
            ) {
                setPinpoint(cartItemPosition)
            } else {
                val shipmentCartItemModel =
                    shipmentAdapter.getShipmentCartItemModelByIndex(cartItemPosition)!!
                if (isTradeInByDropOff) {
                    shipmentAdapter.setSelectedCourierTradeInPickup(recommendedCourier)
                    shipmentPresenter.processSaveShipmentState(shipmentCartItemModel)
                } else {
                    sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(recommendedCourier.name)
                    sendAnalyticsOnViewPreselectedCourierAfterPilihDurasi(recommendedCourier.shipperProductId)

                    // Clear logistic voucher data when any duration is selected and voucher is not null
                    if (shipmentCartItemModel.voucherLogisticItemUiModel != null &&
                        !TextUtils.isEmpty(shipmentCartItemModel.voucherLogisticItemUiModel!!.code) && isClearPromo
                    ) {
                        val promoLogisticCode =
                            shipmentCartItemModel.voucherLogisticItemUiModel!!.code
                        shipmentPresenter.cancelAutoApplyPromoStackLogistic(
                            0,
                            promoLogisticCode,
                            shipmentCartItemModel
                        )
                        val validateUsePromoRequest = shipmentPresenter.lastValidateUseRequest
                        if (validateUsePromoRequest != null) {
                            if (shipmentCartItemModel.isFreeShippingPlus) {
                                for (ordersItem in validateUsePromoRequest.orders) {
                                    if (ordersItem.uniqueId == shipmentCartItemModel.cartString && ordersItem.codes.size > 0) {
                                        ordersItem.codes.remove(promoLogisticCode)
                                    }
                                }
                            } else {
                                for (ordersItem in validateUsePromoRequest.orders) {
                                    if (ordersItem.codes.size > 0) {
                                        ordersItem.codes.remove(promoLogisticCode)
                                    }
                                }
                            }
                        }
                        shipmentCartItemModel.voucherLogisticItemUiModel = null
                        benefitSummaryInfoUiModel = null
                        shipmentAdapter.clearTotalPromoStackAmount()
                        shipmentAdapter.updateShipmentCostModel()
                        shipmentAdapter.updateCheckoutButtonData(null)
                    }
                    shipmentAdapter.setSelectedCourier(
                        cartItemPosition,
                        recommendedCourier,
                        true,
                        false
                    )
                    shipmentPresenter.processSaveShipmentState(shipmentCartItemModel)
                    shipmentAdapter.setShippingCourierViewModels(
                        shippingCourierUiModels,
                        recommendedCourier,
                        cartItemPosition
                    )
                    if (!TextUtils.isEmpty(recommendedCourier.promoCode) && isDurationClick) {
                        checkCourierPromo(recommendedCourier, cartItemPosition)
                    }
                }
            }
        }
    }

    private fun checkCourierPromo(courierItemData: CourierItemData?, itemPosition: Int) {
        if (!TextUtils.isEmpty(courierItemData!!.promoCode)) {
            val promoCode = courierItemData.promoCode
            shipmentPresenter.processCheckPromoCheckoutCodeFromSelectedCourier(
                promoCode,
                itemPosition,
                false
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
                isTradeInByDropOff
            )
        }
    }

    override fun onShippingDurationButtonCloseClicked() {
        sendAnalyticsOnClickButtonCloseShipmentRecommendationDuration()
    }

    override fun onShowDurationListWithCourierPromo(isCourierPromo: Boolean, duration: String?) {
        sendAnalyticsOnDisplayDurationThatContainPromo(isCourierPromo, duration)
    }

    override fun onShowLogisticPromo(listLogisticPromo: List<LogisticPromoUiModel>) {
        for ((promoCode, _, _, _, _, _, _, _, _, _, _, _, _, _, _, disabled) in listLogisticPromo) {
            checkoutAnalyticsCourierSelection.eventViewPromoLogisticTicker(promoCode)
            if (disabled) {
                checkoutAnalyticsCourierSelection.eventViewPromoLogisticTickerDisable(promoCode)
            }
        }
    }

    override fun onCourierChoosen(
        shippingCourierUiModel: ShippingCourierUiModel,
        courierItemData: CourierItemData,
        recipientAddressModel: RecipientAddressModel?,
        cartItemPosition: Int,
        isCod: Boolean,
        isPromoCourier: Boolean,
        isNeedPinpoint: Boolean,
        shippingCourierList: List<ShippingCourierUiModel>
    ) {
        sendAnalyticsOnClickLogisticThatContainPromo(
            isPromoCourier,
            courierItemData.shipperProductId,
            isCod
        )
        if (isNeedPinpoint || courierItemData.isUsePinPoint && (
            recipientAddressModel!!.latitude == null ||
                recipientAddressModel.latitude.equals(
                        "0",
                        ignoreCase = true
                    ) || recipientAddressModel.longitude == null ||
                recipientAddressModel.longitude.equals("0", ignoreCase = true)
            )
        ) {
            setPinpoint(cartItemPosition)
        } else {
            val shipmentCartItemModel =
                shipmentAdapter.setSelectedCourier(cartItemPosition, courierItemData, true, false)!!
            if (shipmentCartItemModel.selectedShipmentDetailData != null) {
                shipmentCartItemModel.selectedShipmentDetailData!!.shippingCourierViewModels =
                    shippingCourierList
            }
            shipmentPresenter.processSaveShipmentState(shipmentCartItemModel)
            checkCourierPromo(courierItemData, cartItemPosition)
        }
    }

    override fun onCourierShipmentRecommendationCloseClicked() {
        sendAnalyticsOnClickButtonCloseShipmentRecommendationCourier()
    }

    private fun setPinpoint(cartItemPosition: Int) {
        shipmentAdapter.lastChooseCourierItemPosition = cartItemPosition
        val locationPass = LocationPass()
        val addressShipmentData = shipmentAdapter.addressShipmentData
        if (addressShipmentData != null) {
            locationPass.cityName = addressShipmentData.cityName
            locationPass.districtName =
                addressShipmentData.destinationDistrictName
            navigateToPinpointActivity(locationPass)
        }
    }

    private fun navigateToPinpointActivity(locationPass: LocationPass?) {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
        val bundle = Bundle()
        bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
        bundle.putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT)
    }

    override fun onChangeShippingDuration(
        shipmentCartItemModel: ShipmentCartItemModel?,
        recipientAddressModel: RecipientAddressModel?,
        cartPosition: Int
    ) {
        if (shipmentLoadingIndex == -1) {
            sendAnalyticsOnClickChangeDurationShipmentRecommendation()
            if (isTradeIn) {
                checkoutTradeInAnalytics.eventTradeInClickCourierOption(isTradeInByDropOff)
            }
            showShippingDurationBottomsheet(
                shipmentCartItemModel,
                recipientAddressModel,
                cartPosition
            )
        }
    }

    private fun showShippingDurationBottomsheet(
        shipmentCartItemModel: ShipmentCartItemModel?,
        recipientAddressModel: RecipientAddressModel?,
        cartPosition: Int
    ) {
        if (shipmentCartItemModel!!.shopShipmentList == null || shipmentCartItemModel.shopShipmentList!!.isEmpty()) {
            onNoCourierAvailable(getString(com.tokopedia.logisticcart.R.string.label_no_courier_bottomsheet_message))
        } else {
            val shipmentDetailData =
                getShipmentDetailData(shipmentCartItemModel, recipientAddressModel)
            var codHistory = -1
            if (shipmentPresenter.codData != null) {
                codHistory = shipmentPresenter.codData!!.counterCod
            }
            val activity: Activity? = activity
            if (activity != null) {
                val pslCode = getLogisticPromoCode(shipmentCartItemModel)
                val products = getProductForRatesRequest(shipmentCartItemModel)
                val shippingDurationBottomsheet = ShippingDurationBottomsheet()
                shippingDurationBottomsheet.show(
                    activity,
                    parentFragmentManager,
                    this,
                    shipmentDetailData,
                    shipmentAdapter.lastServiceId,
                    shipmentCartItemModel.shopShipmentList!!,
                    recipientAddressModel,
                    cartPosition,
                    codHistory,
                    shipmentCartItemModel.isLeasingProduct,
                    pslCode,
                    products,
                    shipmentCartItemModel.cartString,
                    shipmentCartItemModel.isOrderPrioritasDisable,
                    isTradeInByDropOff,
                    shipmentCartItemModel.isFulfillment,
                    shipmentCartItemModel.shipmentCartData!!.preOrderDuration,
                    shipmentPresenter.generateRatesMvcParam(
                        shipmentCartItemModel.cartString
                    ),
                    shipmentPresenter.cartDataForRates!!,
                    false,
                    shipmentCartItemModel.fulfillmentId.toString()
                )
            }
        }
    }

    private fun getProductForRatesRequest(shipmentCartItemModel: ShipmentCartItemModel?): ArrayList<Product> {
        val products = ArrayList<Product>()
        if (shipmentCartItemModel?.cartItemModels != null) {
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                if (!cartItemModel.isError) {
                    val product = Product()
                    product.productId = cartItemModel.productId
                    product.isFreeShipping = cartItemModel.isFreeShipping
                    product.isFreeShippingTc = cartItemModel.isFreeShippingExtra
                    products.add(product)
                }
            }
        }
        return products
    }

    override fun onChangeShippingCourier(
        recipientAddressModel: RecipientAddressModel?,
        shipmentCartItemModel: ShipmentCartItemModel?,
        cartPosition: Int,
        selectedShippingCourierUiModels: List<ShippingCourierUiModel>?
    ) {
        if (shipmentLoadingIndex == -1) {
            var shippingCourierUiModels: List<ShippingCourierUiModel>?
            shippingCourierUiModels = selectedShippingCourierUiModels
                ?: shipmentCartItemModel!!.selectedShipmentDetailData!!.shippingCourierViewModels
            sendAnalyticsOnClickChangeCourierShipmentRecommendation(shipmentCartItemModel)
            if (shippingCourierUiModels == null || shippingCourierUiModels.isEmpty() &&
                shipmentPresenter.getShippingCourierViewModelsState(shipmentCartItemModel!!.orderNumber) != null
            ) {
                shippingCourierUiModels = shipmentPresenter.getShippingCourierViewModelsState(
                    shipmentCartItemModel!!.orderNumber
                )
            }
            val activity: Activity? = activity
            if (activity != null) {
                shippingCourierBottomsheet = ShippingCourierBottomsheet()
                shippingCourierBottomsheet!!.show(
                    activity,
                    fragmentManager!!,
                    this,
                    shippingCourierUiModels,
                    recipientAddressModel,
                    cartPosition,
                    false
                )
                shippingCourierUiModels?.let { checkHasCourierPromo(it) }
            }
        }
    }

    private fun reloadCourier(
        shipmentCartItemModel: ShipmentCartItemModel?,
        cartPosition: Int,
        skipMvc: Boolean
    ) {
        if (shipmentCartItemModel?.selectedShipmentDetailData != null) {
            if (shipmentCartItemModel.selectedShipmentDetailData!!.shopId == null) {
                shipmentCartItemModel.selectedShipmentDetailData!!.shopId =
                    shipmentCartItemModel.shopId.toString()
            }
            shipmentCartItemModel.selectedShipmentDetailData!!.isTradein = isTradeIn
            if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null) {
                shipmentPresenter.processGetCourierRecommendation(
                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.shipperId,
                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.shipperProductId,
                    cartPosition,
                    shipmentCartItemModel.selectedShipmentDetailData,
                    shipmentCartItemModel,
                    shipmentCartItemModel.shopShipmentList!!,
                    false,
                    getProductForRatesRequest(shipmentCartItemModel),
                    shipmentCartItemModel.cartString,
                    isTradeInByDropOff,
                    shipmentAdapter.addressShipmentData,
                    cartPosition > -1,
                    skipMvc
                )
            }
        }
    }

    private fun checkHasCourierPromo(shippingCourierUiModels: List<ShippingCourierUiModel>) {
        var hasCourierPromo = false
        for (shippingCourierUiModel in shippingCourierUiModels) {
            if (!TextUtils.isEmpty(shippingCourierUiModel.productData.promoCode)) {
                hasCourierPromo = true
                break
            }
        }
        if (hasCourierPromo) {
            for (shippingCourierUiModel in shippingCourierUiModels) {
                sendAnalyticsOnDisplayLogisticThatContainPromo(
                    !TextUtils.isEmpty(shippingCourierUiModel.productData.promoCode),
                    shippingCourierUiModel.productData.shipperProductId
                )
            }
        }
    }

    override fun hideSoftKeyboard() {
        if (activity != null) {
            KeyboardHandler.hideSoftKeyboard(activity)
        }
    }

    override fun onLoadShippingState(
        shipperId: Int,
        spId: Int,
        itemPosition: Int,
        shipmentDetailData: ShipmentDetailData?,
        shipmentCartItemModel: ShipmentCartItemModel?,
        shopShipmentList: List<ShopShipment>?,
        isTradeInDropOff: Boolean
    ) {
        if (shopShipmentList != null && shopShipmentList.isNotEmpty()) {
            shipmentDetailData!!.isTradein = isTradeIn
            shipmentPresenter.processGetCourierRecommendation(
                shipperId, spId, itemPosition, shipmentDetailData,
                shipmentCartItemModel, shopShipmentList, true,
                getProductForRatesRequest(shipmentCartItemModel),
                shipmentCartItemModel!!.cartString, isTradeInDropOff,
                shipmentAdapter.addressShipmentData, false, false
            )
        }
    }

    override fun onCourierPromoCanceled(shipperName: String?, promoCode: String?) {
        if (shipmentAdapter.isCourierPromoStillExist) {
            showToastError(
                String.format(
                    getString(com.tokopedia.logisticcart.R.string.message_cannot_apply_courier_promo),
                    shipperName
                )
            )
        }
    }

    override fun onPurchaseProtectionLogicError() {
        val message = getString(R.string.error_dropshipper)
        showToastError(message)
    }

    override fun onPurchaseProtectionChangeListener(position: Int) {
        if (rvShipment?.isComputingLayout == true) {
            rvShipment?.post {
                shipmentAdapter.updateShipmentCostModel()
                shipmentAdapter.updateItemAndTotalCost(position)
                shipmentAdapter.updateInsuranceTncVisibility()
            }
        } else {
            shipmentAdapter.updateShipmentCostModel()
            shipmentAdapter.updateItemAndTotalCost(position)
            shipmentAdapter.updateInsuranceTncVisibility()
        }
    }

    override fun navigateToProtectionMore(cartItemModel: CartItemModel?) {
        val activity: Activity? = activity
        if (activity != null) {
            mTrackerPurchaseProtection.eventClickOnPelajari(
                userSessionInterface.userId,
                cartItemModel!!.protectionTitle,
                cartItemModel.protectionPricePerProduct,
                cartItemModel.analyticsProductCheckoutData.productCategoryId
            )
            val intent =
                RouteManager.getIntent(activity, ApplinkConstInternalFintech.INSURANCE_INFO)
            intent.putExtra(
                ApplinkConstInternalFintech.PARAM_INSURANCE_INFO_URL,
                cartItemModel.protectionLinkUrl
            )
            startActivity(intent)
        }
    }

    override fun onProcessToPayment() {
        showLoading()
        shipmentAdapter.checkDropshipperValidation()
    }

    private val resultCode: Int
        get() = if (shipmentPresenter.couponStateChanged) {
            RESULT_CODE_COUPON_STATE_CHANGED
        } else {
            Activity.RESULT_CANCELED
        }

    private fun releaseBookingIfAny() {
        if (cdLayout?.visibility == View.VISIBLE) {
            shipmentPresenter.releaseBooking()
        }
    }

    override fun updateCourierBottomsheetHasNoData(
        cartPosition: Int,
        shipmentCartItemModel: ShipmentCartItemModel?
    ) {
        shippingCourierBottomsheet?.setShippingCourierViewModels(null, cartPosition, null)
    }

    override fun updateCourierBottomssheetHasData(
        shippingCourierUiModels: List<ShippingCourierUiModel>?,
        cartPosition: Int,
        shipmentCartItemModel: ShipmentCartItemModel?,
        preOrderModel: PreOrderModel?
    ) {
        shippingCourierBottomsheet?.setShippingCourierViewModels(
            shippingCourierUiModels,
            cartPosition,
            preOrderModel
        )
    }

    override fun generateValidateUsePromoRequest(): ValidateUsePromoRequest {
        val bboPromoCodes = ArrayList<String>()
        var validateUsePromoRequest = shipmentPresenter.lastValidateUseRequest
        return if (validateUsePromoRequest != null) {
            // Update param if have done param data generation before
            val shipmentCartItemModelList = shipmentAdapter.shipmentCartItemModelList!!
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                for (ordersItem in validateUsePromoRequest.orders) {
                    if (shipmentCartItemModel.cartString == ordersItem.uniqueId) {
                        if (shipmentCartItemModel.selectedShipmentDetailData != null && shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                            if (!ordersItem.codes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
                                ordersItem.codes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                            }
                            if (!bboPromoCodes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
                                bboPromoCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                            }
                        }
                        setValidateUseSpIdParam(shipmentCartItemModel, ordersItem)
                    }
                }
            }
            if (isTradeIn) {
                validateUsePromoRequest.isTradeIn = 1
                validateUsePromoRequest.isTradeInDropOff = if (isTradeInByDropOff) 1 else 0
            }
            if (isOneClickShipment) {
                validateUsePromoRequest.cartType = "ocs"
            } else {
                validateUsePromoRequest.cartType = "default"
            }
            shipmentPresenter.setLatValidateUseRequest(validateUsePromoRequest)
            this.bboPromoCodes = bboPromoCodes
            validateUsePromoRequest
        } else {
            // First param data generation / initialization
            validateUsePromoRequest = ValidateUsePromoRequest()
            val listOrderItem = ArrayList<OrdersItem>()
            val shipmentCartItemModelList = shipmentAdapter.shipmentCartItemModelList
            val lastApplyUiModel = shipmentPresenter.lastApplyData
            if (shipmentCartItemModelList != null) {
                for (shipmentCartItemModel in shipmentCartItemModelList) {
                    val ordersItem = OrdersItem()
                    val productDetailsItems = ArrayList<ProductDetailsItem>()
                    for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                        if (!cartItemModel.isError) {
                            val productDetail = ProductDetailsItem()
                            productDetail.productId = cartItemModel.productId
                            productDetail.quantity = cartItemModel.quantity
                            var tmpBundleId: Long = 0
                            try {
                                tmpBundleId = cartItemModel.bundleId.toLong()
                            } catch (e: NumberFormatException) {
                            }
                            productDetail.bundleId = tmpBundleId
                            productDetailsItems.add(productDetail)
                        }
                    }
                    ordersItem.productDetails = productDetailsItems
                    val listOrderCodes = ArrayList<String>()
                    if (lastApplyUiModel != null) {
                        for (lastApplyVoucherOrdersItemUiModel in lastApplyUiModel.voucherOrders) {
                            if (shipmentCartItemModel.cartString.equals(
                                    lastApplyVoucherOrdersItemUiModel.uniqueId,
                                    ignoreCase = true
                                )
                            ) {
                                if (!listOrderCodes.contains(lastApplyVoucherOrdersItemUiModel.code) && !lastApplyVoucherOrdersItemUiModel.isTypeLogistic()) {
                                    listOrderCodes.add(lastApplyVoucherOrdersItemUiModel.code)
                                }
                                break
                            }
                        }
                    }
                    // Add data BBO
                    if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                        if (!listOrderCodes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
                            listOrderCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                        }
                        if (!bboPromoCodes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
                            bboPromoCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                        }
                    }
                    ordersItem.codes = listOrderCodes
                    ordersItem.uniqueId = shipmentCartItemModel.cartString
                    ordersItem.shopId = shipmentCartItemModel.shopId
                    ordersItem.boType = shipmentCartItemModel.shipmentCartData!!.boMetadata!!.boType
                    ordersItem.isPo = shipmentCartItemModel.isProductIsPreorder
                    ordersItem.poDuration =
                        shipmentCartItemModel.cartItemModels[0].preOrderDurationDay
                    ordersItem.warehouseId = shipmentCartItemModel.fulfillmentId
                    setValidateUseSpIdParam(shipmentCartItemModel, ordersItem)
                    listOrderItem.add(ordersItem)
                }
            }
            validateUsePromoRequest.orders = listOrderItem
            validateUsePromoRequest.state = PARAM_CHECKOUT
            validateUsePromoRequest.cartType = PARAM_DEFAULT
            validateUsePromoRequest.skipApply = 0
            if (isTradeIn) {
                validateUsePromoRequest.isTradeIn = 1
                validateUsePromoRequest.isTradeInDropOff = if (isTradeInByDropOff) 1 else 0
            }
            if (lastApplyUiModel != null) {
                val globalPromoCodes = ArrayList<String>()
                if (lastApplyUiModel.codes.isNotEmpty()) {
                    for (code in lastApplyUiModel.codes) {
                        if (code.isNotEmpty() && !globalPromoCodes.contains(code)) {
                            globalPromoCodes.add(code)
                        }
                    }
                }
                validateUsePromoRequest.codes = globalPromoCodes
            }
            if (isOneClickShipment) {
                validateUsePromoRequest.cartType = "ocs"
            } else {
                validateUsePromoRequest.cartType = "default"
            }
            shipmentPresenter.setLatValidateUseRequest(validateUsePromoRequest)
            this.bboPromoCodes = bboPromoCodes
            validateUsePromoRequest
        }
    }

    private fun setValidateUseSpIdParam(
        shipmentCartItemModel: ShipmentCartItemModel,
        ordersItem: OrdersItem
    ) {
        if (shipmentCartItemModel.selectedShipmentDetailData == null) {
            ordersItem.shippingId = 0
            ordersItem.spId = 0
            ordersItem.freeShippingMetadata = ""
            ordersItem.boCampaignId = 0
            ordersItem.benefitClass = ""
            ordersItem.shippingSubsidy = 0
            ordersItem.shippingPrice = 0.0
            ordersItem.etaText = ""
            ordersItem.validationMetadata = ""
        } else {
            if (isTradeInByDropOff) {
                if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null) {
                    ordersItem.shippingId =
                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperId
                    ordersItem.spId =
                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperProductId
                    if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                        ordersItem.freeShippingMetadata =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.freeShippingMetadata
                        ordersItem.benefitClass =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.benefitClass
                        ordersItem.shippingSubsidy =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shippingSubsidy
                        ordersItem.shippingPrice =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shippingRate.toDouble()
                        ordersItem.etaText =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.etaText!!
                        ordersItem.boCampaignId =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.boCampaignId
                    } else {
                        ordersItem.freeShippingMetadata = ""
                        ordersItem.boCampaignId = 0
                        ordersItem.benefitClass = ""
                        ordersItem.shippingSubsidy = 0
                        ordersItem.shippingPrice = 0.0
                        ordersItem.etaText = ""
                    }
                    ordersItem.validationMetadata = shipmentCartItemModel.validationMetadata
                } else {
                    ordersItem.shippingId = 0
                    ordersItem.spId = 0
                    ordersItem.freeShippingMetadata = ""
                    ordersItem.boCampaignId = 0
                    ordersItem.benefitClass = ""
                    ordersItem.shippingSubsidy = 0
                    ordersItem.shippingPrice = 0.0
                    ordersItem.etaText = ""
                    ordersItem.validationMetadata = ""
                }
            } else {
                if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null) {
                    ordersItem.shippingId =
                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shipperId
                    ordersItem.spId =
                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shipperProductId
                    if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                        ordersItem.freeShippingMetadata =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.freeShippingMetadata
                        ordersItem.benefitClass =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.benefitClass
                        ordersItem.shippingSubsidy =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shippingSubsidy
                        ordersItem.shippingPrice =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shippingRate.toDouble()
                        ordersItem.etaText =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.etaText!!
                        ordersItem.boCampaignId =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.boCampaignId
                    } else {
                        ordersItem.freeShippingMetadata = ""
                        ordersItem.boCampaignId = 0
                        ordersItem.benefitClass = ""
                        ordersItem.shippingSubsidy = 0
                        ordersItem.shippingPrice = 0.0
                        ordersItem.etaText = ""
                    }
                    ordersItem.validationMetadata = shipmentCartItemModel.validationMetadata
                } else {
                    ordersItem.shippingId = 0
                    ordersItem.spId = 0
                    ordersItem.freeShippingMetadata = ""
                    ordersItem.boCampaignId = 0
                    ordersItem.benefitClass = ""
                    ordersItem.shippingSubsidy = 0
                    ordersItem.shippingPrice = 0.0
                    ordersItem.etaText = ""
                    ordersItem.validationMetadata = ""
                }
            }
        }
    }

    override fun generateCouponListRecommendationRequest(): PromoRequest {
        val promoRequest = PromoRequest()
        val listOrderItem = ArrayList<Order>()
        val shipmentCartItemModelList = shipmentAdapter.shipmentCartItemModelList
        if (shipmentCartItemModelList != null) {
            for (shipmentCartItemModel in shipmentCartItemModelList) {
                val ordersItem = Order()
                val productDetailsItems = ArrayList<ProductDetail>()
                for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                    if (!cartItemModel.isError) {
                        val productDetail = ProductDetail()
                        productDetail.productId = cartItemModel.productId
                        productDetail.quantity = cartItemModel.quantity
                        var tmpBundleId: Long = 0
                        try {
                            tmpBundleId = cartItemModel.bundleId.toLong()
                        } catch (e: NumberFormatException) {
                        }
                        productDetail.bundleId = tmpBundleId
                        productDetailsItems.add(productDetail)
                    }
                }
                ordersItem.product_details = productDetailsItems
                ordersItem.isChecked = true
                val listCodes = ArrayList<String>()
                if (shipmentCartItemModel.listPromoCodes != null) {
                    for (code in shipmentCartItemModel.listPromoCodes!!) {
                        listCodes.add(code)
                    }
                }
                ordersItem.codes = listCodes
                ordersItem.uniqueId = shipmentCartItemModel.cartString
                ordersItem.shopId = shipmentCartItemModel.shopId
                ordersItem.boType = shipmentCartItemModel.shipmentCartData!!.boMetadata!!.boType
                ordersItem.isInsurancePrice = if (shipmentCartItemModel.isInsurance) 1 else 0
                if (shipmentCartItemModel.selectedShipmentDetailData == null) {
                    ordersItem.shippingId = 0
                    ordersItem.spId = 0
                    ordersItem.freeShippingMetadata = ""
                    ordersItem.validationMetadata = ""
                } else {
                    if (isTradeInByDropOff) {
                        if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null) {
                            ordersItem.shippingId =
                                shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperId
                            ordersItem.spId =
                                shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperProductId
                            if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                                ordersItem.freeShippingMetadata =
                                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.freeShippingMetadata
                            } else {
                                ordersItem.freeShippingMetadata = ""
                            }
                            ordersItem.validationMetadata = shipmentCartItemModel.validationMetadata
                        } else {
                            ordersItem.shippingId = 0
                            ordersItem.spId = 0
                            ordersItem.freeShippingMetadata = ""
                            ordersItem.validationMetadata = ""
                        }
                    } else {
                        if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null) {
                            ordersItem.shippingId =
                                shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shipperId
                            ordersItem.spId =
                                shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.shipperProductId
                            if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                                ordersItem.freeShippingMetadata =
                                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.freeShippingMetadata
                            } else {
                                ordersItem.freeShippingMetadata = ""
                            }
                            ordersItem.validationMetadata = shipmentCartItemModel.validationMetadata
                        } else {
                            ordersItem.shippingId = 0
                            ordersItem.spId = 0
                            ordersItem.freeShippingMetadata = ""
                            ordersItem.validationMetadata = ""
                        }
                    }
                }
                listOrderItem.add(ordersItem)
            }
        }
        promoRequest.orders = listOrderItem
        promoRequest.state = PARAM_CHECKOUT
        if (isOneClickShipment) {
            promoRequest.cartType = "ocs"
        } else {
            promoRequest.cartType = PARAM_DEFAULT
        }
        if (isTradeIn) {
            promoRequest.isTradeIn = 1
            promoRequest.isTradeInDropOff = if (isTradeInByDropOff) 1 else 0
        }
        val lastApplyUiModel = shipmentPresenter.lastApplyData
        if (lastApplyUiModel != null) {
            val globalPromoCodes = ArrayList<String>()
            if (lastApplyUiModel.codes.isNotEmpty()) {
                globalPromoCodes.addAll(lastApplyUiModel.codes)
            }
            promoRequest.codes = globalPromoCodes
        }
        return promoRequest
    }

    override fun onSuccessClearPromoLogistic(position: Int, isLastAppliedPromo: Boolean) {
        if (position != 0) {
            val shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(position)!!
            shipmentCartItemModel.voucherLogisticItemUiModel = null
            onNeedUpdateViewItem(position)
        }
        if (isLastAppliedPromo) {
            doResetButtonPromoCheckout()
        }
        shipmentAdapter.checkHasSelectAllCourier(false, -1, "", true, false)
    }

    override fun triggerSendEnhancedEcommerceCheckoutAnalyticAfterCheckoutSuccess(
        transactionId: String?,
        deviceModel: String?,
        devicePrice: Long,
        diagnosticId: String?
    ) {
        val dataCheckoutRequests =
            shipmentPresenter.updateEnhancedEcommerceCheckoutAnalyticsDataLayerPromoData(
                shipmentAdapter.shipmentCartItemModelList!!
            )
        var eventCategory = ConstantTransactionAnalytics.EventCategory.COURIER_SELECTION
        var eventAction = ConstantTransactionAnalytics.EventAction.CLICK_PILIH_METODE_PEMBAYARAN
        var eventLabel = ConstantTransactionAnalytics.EventLabel.SUCCESS
        val tradeInCustomDimension: MutableMap<String, String> = HashMap()
        if (isTradeIn) {
            eventCategory =
                CheckoutTradeInAnalytics.EVENT_CATEGORY_SELF_PICKUP_ADDRESS_SELECTION_TRADE_IN
            eventLabel = String.format(
                Locale.getDefault(),
                CheckoutTradeInAnalytics.EVENT_LABEL_TRADE_IN_CHECKOUT_EE,
                deviceModel,
                devicePrice,
                diagnosticId
            )
            tradeInCustomDimension[CheckoutTradeInAnalytics.KEY_USER_ID] =
                userSessionInterface.userId
            tradeInCustomDimension[CheckoutTradeInAnalytics.KEY_BUSINESS_UNIT] =
                CheckoutTradeInAnalytics.VALUE_TRADE_IN
            if (isTradeInByDropOff) {
                eventAction = CheckoutTradeInAnalytics.EVENT_ACTION_PILIH_PEMBAYARAN_INDOMARET
                tradeInCustomDimension[CheckoutTradeInAnalytics.KEY_SCREEN_NAME] =
                    CheckoutTradeInAnalytics.SCREEN_NAME_DROP_OFF_ADDRESS
            } else {
                eventAction = CheckoutTradeInAnalytics.EVENT_ACTION_PILIH_PEMBAYARAN_NORMAL
                tradeInCustomDimension[CheckoutTradeInAnalytics.KEY_SCREEN_NAME] =
                    CheckoutTradeInAnalytics.SCREEN_NAME_NORMAL_ADDRESS
            }
        }
        shipmentPresenter.triggerSendEnhancedEcommerceCheckoutAnalytics(
            dataCheckoutRequests,
            tradeInCustomDimension,
            EnhancedECommerceActionField.STEP_4,
            eventCategory,
            eventAction,
            eventLabel,
            checkoutLeasingId,
            checkoutPageSource
        )
    }

    override fun resetCourier(position: Int) {
        val shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(position)!!
        resetCourier(shipmentCartItemModel)
    }

    override fun clearTotalBenefitPromoStacking() {
        shipmentAdapter.clearTotalPromoStackAmount()
        shipmentAdapter.updateShipmentCostModel()
    }

    // Keep this method
    private fun showPromoNotEligibleDialog(
        notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>,
        requestCode: Int
    ) {
        val activity = activity
        if (activity != null) {
            if (promoNotEligibleBottomsheet == null) {
                promoNotEligibleBottomsheet =
                    PromoNotEligibleBottomSheet(notEligiblePromoHolderdataList, this)
            }
            promoNotEligibleBottomsheet!!.dismissListener = {
                checkoutAnalyticsCourierSelection.eventClickBatalOnErrorPromoConfirmation()
            }
            promoNotEligibleBottomsheet!!.notEligiblePromoHolderDataList =
                notEligiblePromoHolderdataList
            promoNotEligibleBottomsheet!!.show(activity, parentFragmentManager)
            checkoutAnalyticsCourierSelection.eventViewPopupErrorPromoConfirmation()
            eventCheckoutViewBottomsheetPromoError()
        }
    }

    override fun onButtonContinueClicked() {
        if (promoNotEligibleBottomsheet != null) {
            checkoutAnalyticsCourierSelection.eventClickLanjutkanOnErrorPromoConfirmation()
            val notEligiblePromoHolderdataArrayList: ArrayList<NotEligiblePromoHolderdata> =
                promoNotEligibleBottomsheet!!.notEligiblePromoHolderDataList
            promoNotEligibleBottomsheet!!.dismiss()
            shipmentPresenter.cancelNotEligiblePromo(notEligiblePromoHolderdataArrayList)
        }
    }

    override fun onButtonChooseOtherPromo() {
        if (promoNotEligibleBottomsheet != null) {
            promoNotEligibleBottomsheet!!.dismiss()
            eventCheckoutClickPilihPromoLainOnBottomsheetPromoError()
            val validateUseRequestParam = generateValidateUsePromoRequest()
            val promoRequestParam = generateCouponListRecommendationRequest()
            val intent = RouteManager.getIntent(
                activity,
                ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE
            )
            intent.putExtra(ARGS_PAGE_SOURCE, PAGE_CHECKOUT)
            intent.putExtra(ARGS_PROMO_REQUEST, promoRequestParam)
            intent.putExtra(ARGS_VALIDATE_USE_REQUEST, validateUseRequestParam)
            intent.putStringArrayListExtra(ARGS_BBO_PROMO_CODES, bboPromoCodes)
            setChosenAddressForTradeInDropOff(intent)
            setPromoExtraMvcLockCourierFlow(intent)
            startActivityForResult(intent, REQUEST_CODE_PROMO)
        }
    }

    override fun onShow() {
        // no op
    }

    override fun removeIneligiblePromo(notEligiblePromoHolderdataArrayList: ArrayList<NotEligiblePromoHolderdata>) {
        val validateUsePromoRevampUiModel = shipmentPresenter.validateUsePromoRevampUiModel
        if (validateUsePromoRevampUiModel != null) {
            if (validateUsePromoRevampUiModel.promoUiModel.messageUiModel.state == "red") {
                validateUsePromoRevampUiModel.promoUiModel.codes = listOf()
            }
            val deletedVoucherOrder = ArrayList<PromoCheckoutVoucherOrdersItemUiModel>()
            val voucherOrderUiModels = validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels.toMutableList()
            for (voucherOrdersItemUiModel in voucherOrderUiModels) {
                if (voucherOrdersItemUiModel.messageUiModel.state == "red") {
                    deletedVoucherOrder.add(voucherOrdersItemUiModel)
                }
            }
            if (deletedVoucherOrder.size > 0) {
                for (voucherOrdersItemUiModel in deletedVoucherOrder) {
                    voucherOrderUiModels.remove(
                        voucherOrdersItemUiModel
                    )
                }
                validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels = voucherOrderUiModels
            }
        }
        if (shipmentPresenter.isUsingDynamicDataPassing()) {
            shipmentPresenter.validateDynamicData()
        } else {
            doCheckout()
        }
    }

    override fun updateTickerAnnouncementMessage() {
        val index = shipmentAdapter.tickerAnnouncementHolderDataIndex
        if (index != RecyclerView.NO_POSITION) {
            // If ticker exist, update the view
            onNeedUpdateViewItem(index)
        } else {
            // If ticker not exist, add ticker to list, then update the list
            val tickerAnnouncementHolderData = shipmentPresenter.tickerAnnouncementHolderData
            shipmentAdapter.addTickerAnnouncementData(tickerAnnouncementHolderData)
            if (rvShipment?.isComputingLayout == true) {
                rvShipment?.post { onAddTickerAnnouncementMessage() }
            } else {
                onAddTickerAnnouncementMessage()
            }
        }
    }

    private fun onAddTickerAnnouncementMessage() {
        if (rvShipment?.canScrollVertically(-1) == false) {
            shipmentAdapter.notifyItemInserted(ShipmentAdapter.SECOND_HEADER_POSITION)
            rvShipment?.scrollToPosition(0)
        } else {
            shipmentAdapter.notifyItemInserted(ShipmentAdapter.SECOND_HEADER_POSITION)
        }
    }

    override fun setPromoBenefit(summariesUiModels: List<SummariesItemUiModel>) {
        shipmentAdapter.setPromoBenefit(summariesUiModels)
        onNeedUpdateViewItem(shipmentAdapter.shipmentCostPosition)
    }

    override fun resetPromoBenefit() {
        shipmentAdapter.resetPromoBenefit()
        onNeedUpdateViewItem(shipmentAdapter.shipmentCostPosition)
        shipmentAdapter.updateShipmentCostModel()
    }

    override fun onChangeTradeInDropOffClicked(latitude: String?, longitude: String?) {
        checkoutTradeInAnalytics.eventTradeInClickPilihIndomaret()
        val dropOffIntent =
            RouteManager.getIntent(activity, ApplinkConstInternalLogistic.DROPOFF_PICKER)
        dropOffIntent.putExtra(EXTRA_DROPOFF_LATITUDE, latitude)
        dropOffIntent.putExtra(EXTRA_DROPOFF_LONGITUDE, longitude)
        startActivityForResult(dropOffIntent, LogisticConstant.REQUEST_CODE_PICK_DROP_OFF_TRADE_IN)
    }

    /*
     * This method is to solve expired dialog not shown up after time expired in background
     * Little caveat: what if device's time is tempered and not synchronized with server?
     * Later: consider serverTimeOffset, need more time
     * */
    private fun checkCampaignTimer() {
        val timer = shipmentPresenter.getCampaignTimer()
        if (timer != null && timer.showTimer) {
            val diff = timeSinceNow(timer.timerExpired)
            showCampaignTimerExpiredDialog(timer, diff, checkoutAnalyticsCourierSelection)
        }
    }

    private fun showCampaignTimerExpiredDialog(
        timer: CampaignTimerUi?,
        diff: Long,
        checkoutAnalyticsCourierSelection: CheckoutAnalyticsCourierSelection
    ) {
        if (isAdded) {
            val fragmentManager = parentFragmentManager
            if (diff <= 0) {
                val dialog = newInstance(timer!!, checkoutAnalyticsCourierSelection, this)
                dialog.show(fragmentManager, "expired dialog")
            }
        }
    }

    private fun setCampaignTimer() {
        val timer = shipmentPresenter.getCampaignTimer()
        if (timer != null && timer.showTimer) {
            val diff = timeBetweenRFC3339(timer.timerServer, timer.timerExpired)
            cdLayout?.visibility = View.VISIBLE
            cdText?.text = timer.timerDescription
            cdView?.remainingMilliseconds = diff
            cdView?.onFinish = {
                val dialog =
                    newInstance(timer, checkoutAnalyticsCourierSelection, this@ShipmentFragment)
                dialog.show(fragmentManager!!, "expired dialog")
            }
        }
    }

    private fun onResultFromSetTradeInPinpoint(data: Intent?) {
        if (data != null) {
            val locationDataModel =
                data.getParcelableExtra<LocationDataModel>(LogisticConstant.RESULT_DATA_STORE_LOCATION)
            val recipientAddressModel = shipmentAdapter.addressShipmentData
            if (recipientAddressModel != null) {
                recipientAddressModel.locationDataModel = locationDataModel
                recipientAddressModel.dropOffAddressName = locationDataModel!!.addrName
                recipientAddressModel.dropOffAddressDetail = locationDataModel.address1
                shipmentPresenter.changeShippingAddress(
                    recipientAddressModel,
                    null,
                    true,
                    true,
                    true,
                    true
                )
            }
        }
    }

    private fun onUpdateResultAddOnProductLevelBottomSheet(data: Intent?) {
        if (data != null) {
            val saveAddOnStateResult =
                data.getParcelableExtra<SaveAddOnStateResult>(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA_RESULT)
            shipmentPresenter.updateAddOnProductLevelDataBottomSheet(saveAddOnStateResult)
        }
    }

    private fun onUpdateResultAddOnOrderLevelBottomSheet(data: Intent?) {
        if (data != null) {
            val saveAddOnStateResult =
                data.getParcelableExtra<SaveAddOnStateResult>(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA_RESULT)
            shipmentPresenter.updateAddOnOrderLevelDataBottomSheet(saveAddOnStateResult)
        }
    }

    override val isTradeInByDropOff: Boolean
        get() {
            val recipientAddressModel = shipmentAdapter.addressShipmentData ?: return false
            return recipientAddressModel.selectedTabIndex == 1
        }

    override fun hasSelectTradeInLocation(): Boolean {
        val recipientAddressModel = shipmentAdapter.addressShipmentData ?: return false
        return recipientAddressModel.locationDataModel != null
    }

    override fun onTradeInAddressTabChanged(addressPosition: Int) {
        val cartItemPosition = addressPosition + 1
        onNeedUpdateViewItem(cartItemPosition)
        onNeedUpdateViewItem(addressPosition)
        val recipientAddressModel = shipmentAdapter.addressShipmentData!!
        if (recipientAddressModel.selectedTabIndex == RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT) {
            checkoutTradeInAnalytics.eventClickJemputTab()
            if (recipientAddressModel.locationDataModel != null) {
                shipmentPresenter.changeShippingAddress(
                    recipientAddressModel,
                    null,
                    true,
                    false,
                    true,
                    true
                )
            }
        } else {
            checkoutTradeInAnalytics.eventClickDropOffTab()
            if (recipientAddressModel.locationDataModel != null) {
                shipmentPresenter.changeShippingAddress(
                    recipientAddressModel,
                    null,
                    true,
                    true,
                    true,
                    true
                )
            }
        }
    }

    override fun onClickPromoCheckout(lastApplyUiModel: LastApplyUiModel?) {
        if (lastApplyUiModel == null) return
        val validateUseRequestParam = generateValidateUsePromoRequest()
        val promoRequestParam = generateCouponListRecommendationRequest()
        val intent =
            RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_CHECKOUT_MARKETPLACE)
        intent.putExtra(ARGS_PAGE_SOURCE, PAGE_CHECKOUT)
        intent.putExtra(ARGS_PROMO_REQUEST, promoRequestParam)
        intent.putExtra(ARGS_VALIDATE_USE_REQUEST, validateUseRequestParam)
        intent.putStringArrayListExtra(ARGS_BBO_PROMO_CODES, bboPromoCodes)
        setChosenAddressForTradeInDropOff(intent)
        setPromoExtraMvcLockCourierFlow(intent)
        startActivityForResult(intent, REQUEST_CODE_PROMO)
        if (isTradeIn) {
            checkoutTradeInAnalytics.eventTradeInClickPromo(isTradeInByDropOff)
        }
    }

    private fun setChosenAddressForTradeInDropOff(intent: Intent) {
        val activity: Activity? = activity
        val recipientAddressModel = shipmentPresenter.recipientAddressModel
        if (activity != null && isTradeInByDropOff && recipientAddressModel != null) {
            val lca = getLocalizingAddressData(
                activity.applicationContext
            )
            val locationDataModel = recipientAddressModel.locationDataModel
            val chosenAddress: ChosenAddress = if (locationDataModel != null) {
                ChosenAddress(
                    ChosenAddress.MODE_ADDRESS,
                    locationDataModel.addrId,
                    locationDataModel.district,
                    locationDataModel.postalCode,
                    if (!TextUtils.isEmpty(locationDataModel.latitude) && !TextUtils.isEmpty(
                            locationDataModel.longitude
                        )
                    ) {
                        locationDataModel.latitude + "," + locationDataModel.longitude
                    } else {
                        ""
                    },
                    ChosenAddressTokonow(
                        lca.shop_id,
                        lca.warehouse_id,
                        lca.warehouses,
                        lca.service_type
                    )
                )
            } else {
                ChosenAddress(
                    ChosenAddress.MODE_ADDRESS,
                    recipientAddressModel.id,
                    recipientAddressModel.destinationDistrictId,
                    recipientAddressModel.postalCode,
                    if (!TextUtils.isEmpty(recipientAddressModel.latitude) && !TextUtils.isEmpty(
                            recipientAddressModel.longitude
                        )
                    ) {
                        recipientAddressModel.latitude + "," + recipientAddressModel.longitude
                    } else {
                        ""
                    },
                    ChosenAddressTokonow(
                        lca.shop_id,
                        lca.warehouse_id,
                        lca.warehouses,
                        lca.service_type
                    )
                )
            }
            intent.putExtra(ARGS_CHOSEN_ADDRESS, chosenAddress)
        }
    }

    private fun setPromoExtraMvcLockCourierFlow(intent: Intent) {
        var promoMvcLockCourierFlow = false
        if (shipmentPresenter.validateUsePromoRevampUiModel != null) {
            if (shipmentPresenter.validateUsePromoRevampUiModel!!.promoUiModel.additionalInfoUiModel.promoSpIds.isNotEmpty()) {
                promoMvcLockCourierFlow = true
            }
        } else if (shipmentPresenter.lastApplyData != null) {
            if (shipmentPresenter.lastApplyData!!.additionalInfo.promoSpIds.isNotEmpty()) {
                promoMvcLockCourierFlow = true
            }
        }
        intent.putExtra(ARGS_PROMO_MVC_LOCK_COURIER_FLOW, promoMvcLockCourierFlow)
    }

    override fun updateButtonPromoCheckout(
        promoUiModel: PromoUiModel,
        isNeedToHitValidateFinal: Boolean
    ) {
        doUpdateButtonPromoCheckout(promoUiModel)
        updatePromoTrackingData(promoUiModel.trackingDetailUiModels)
        sendEEStep3()
        updateLogisticPromoData(promoUiModel)
        val hasSetAllCourier =
            doSetPromoBenefit(promoUiModel.benefitSummaryInfoUiModel.summaries, true)
        if (hasSetAllCourier) {
            // Check if need to hit validate final, if so then hit validate final by checking is all courier have been selected
            if (isNeedToHitValidateFinal) {
                shipmentAdapter.checkHasSelectAllCourier(false, -1, "", false, false)
            }
        }
    }

    private fun doSetPromoBenefit(
        summariesUiModels: List<SummariesItemUiModel>,
        forceSetPromoBenefit: Boolean
    ): Boolean {
        val hasSetAllCourier = shipmentAdapter.hasSetAllCourier()
        if (hasSetAllCourier || forceSetPromoBenefit) {
            resetPromoBenefit()
            setPromoBenefit(summariesUiModels)
            shipmentAdapter.updateShipmentCostModel()
        }
        return hasSetAllCourier
    }

    private fun doUpdateButtonPromoCheckout(promoUiModel: PromoUiModel) {
        shipmentAdapter.updatePromoCheckoutData(promoUiModel)
        onNeedUpdateViewItem(shipmentAdapter.promoCheckoutPosition)
    }

    override fun doResetButtonPromoCheckout() {
        shipmentAdapter.resetPromoCheckoutData()
        onNeedUpdateViewItem(shipmentAdapter.promoCheckoutPosition)
        resetPromoBenefit()
        clearPromoTrackingData()
    }

    override fun onSendAnalyticsClickPromoCheckout(
        isApplied: Boolean?,
        listAllPromoCodes: List<String>
    ) {
        eventCheckoutClickPromoSection(
            listAllPromoCodes,
            isApplied!!,
            userSessionInterface.userId
        )
    }

    override fun onSendAnalyticsViewPromoCheckoutApplied() {
        eventCheckoutViewPromoAlreadyApplied()
    }

    private fun updateLogisticPromoData(promoUiModel: PromoUiModel?) {
        val shipmentCartItemModels = shipmentAdapter.shipmentCartItemModelList ?: return
        val voucherOrdersItemUiModels = promoUiModel!!.voucherOrderUiModels
        for (voucherOrder in voucherOrdersItemUiModels) {
            if (voucherOrder.success && voucherOrder.type == "logistic") {
                for (shipmentCartItemModel in shipmentCartItemModels) {
                    if (shipmentCartItemModel.cartString == voucherOrder.uniqueId) {
                        val log = VoucherLogisticItemUiModel()
                        log.code = voucherOrder.code
                        log.couponDesc = voucherOrder.titleDescription
                        log.couponAmount = getFormattedCurrency(voucherOrder.discountAmount)
                        log.couponAmountRaw = voucherOrder.discountAmount
                        val messageUiModel = MessageUiModel()
                        messageUiModel.color = voucherOrder.messageUiModel.color
                        messageUiModel.state = voucherOrder.messageUiModel.state
                        messageUiModel.text = voucherOrder.messageUiModel.text
                        log.message = messageUiModel
                        shipmentCartItemModel.voucherLogisticItemUiModel = log
                        onNeedUpdateViewItem(
                            shipmentAdapter.getShipmentCartItemModelPosition(
                                shipmentCartItemModel
                            )
                        )
                    }
                }
            } else if (!voucherOrder.success && isNullOrEmpty(voucherOrder.type)) {
                for (shipmentCartItemModel in shipmentCartItemModels) {
                    if (shipmentCartItemModel.cartString == voucherOrder.uniqueId && shipmentCartItemModel.selectedShipmentDetailData != null && shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null && shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.logPromoCode == voucherOrder.code) {
                        resetCourier(shipmentCartItemModel)
                    }
                }
            }
        }
    }

    private fun getFormattedCurrency(price: Long): String {
        return if (price == 0L) {
            ""
        } else {
            getThousandSeparatorString(
                price.toDouble(),
                false,
                0
            ).formattedString
        }
    }

    override fun resetCourier(shipmentCartItemModel: ShipmentCartItemModel) {
        val index = shipmentAdapter.getShipmentDataList().indexOf(shipmentCartItemModel)
        if (index != -1) {
            val validateUsePromoRequest = shipmentPresenter.lastValidateUseRequest
            if (validateUsePromoRequest != null) {
                for (order in validateUsePromoRequest.orders) {
                    if (order.uniqueId == shipmentCartItemModel.cartString && shipmentCartItemModel.selectedShipmentDetailData != null && shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null) {
                        val redStateBBOCode =
                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier!!.selectedShipper.logPromoCode
                        order.codes.remove(redStateBBOCode)
                    }
                }
            }
            shipmentAdapter.resetCourier(index)
            addShippingCompletionTicker(shipmentCartItemModel.isEligibleNewShippingExperience)
        }
    }

    override fun resetAllCourier() {
        shipmentAdapter.resetAllCourier()
    }

    override fun onCheckShippingCompletionClicked() {
        checkoutAnalyticsCourierSelection.clickCekOnSummaryTransactionTickerCourierNotComplete(
            userSessionInterface.userId
        )
        checkShippingCompletion(false, false)
    }

    private fun checkShippingCompletion(
        isTriggeredByPaymentButton: Boolean,
        epharmacyError: Boolean
    ) {
        if (activity != null) {
            val shipmentDataList = shipmentAdapter.getShipmentDataList()
            if (isTradeInByDropOff) {
                var position = 0
                for (i in shipmentDataList.indices) {
                    if (shipmentDataList[i] is ShipmentCartItemModel) {
                        position = i
                        break
                    }
                }
                if (isTriggeredByPaymentButton) {
                    showToastNormal(activity!!.getString(R.string.message_error_courier_not_selected))
                }
                rvShipment!!.smoothScrollToPosition(position)
            } else {
                var notSelectCourierCount = 0
                var firstFoundPosition = 0
                for (i in shipmentDataList.indices) {
                    if (shipmentDataList[i] is ShipmentCartItemModel) {
                        val shipmentCartItemModel = shipmentDataList[i] as ShipmentCartItemModel
                        if (!shipmentCartItemModel.isError && (shipmentCartItemModel.selectedShipmentDetailData == null || shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier == null)) {
                            if (firstFoundPosition == 0) {
                                firstFoundPosition = i
                            }
                            shipmentCartItemModel.isTriggerShippingVibrationAnimation = true
                            shipmentCartItemModel.isStateAllItemViewExpanded = false
                            shipmentCartItemModel.isShippingBorderRed = isTriggeredByPaymentButton
                            onNeedUpdateViewItem(i)
                            notSelectCourierCount++
                        }
                    } else if (shipmentDataList[i] is UploadPrescriptionUiModel) {
                        val uploadPrescriptionUiModel =
                            shipmentDataList[i] as UploadPrescriptionUiModel
                        val viewHolder = rvShipment!!.findViewHolderForAdapterPosition(i)
                        if (viewHolder is UploadPrescriptionViewHolder) {
                            if (epharmacyError) {
                                val toasterMessage: String = if (uploadPrescriptionUiModel.consultationFlow) {
                                    activity!!.getString(com.tokopedia.purchase_platform.common.R.string.pp_epharmacy_message_error_prescription_or_consultation_not_found)
                                } else {
                                    activity!!.getString(com.tokopedia.purchase_platform.common.R.string.pp_epharmacy_message_error_prescription_not_found)
                                }
                                if (firstFoundPosition == 0) {
                                    showToastError(toasterMessage)
                                    firstFoundPosition = i
                                }
                                uploadPrescriptionUiModel.isError = true
                                onNeedUpdateViewItem(i)
                                if (uploadPrescriptionUiModel.consultationFlow) {
                                    ePharmacyAnalytics.clickPilihPembayaran(
                                        viewHolder.getButtonNotes(),
                                        uploadPrescriptionUiModel.epharmacyGroupIds,
                                        false,
                                        toasterMessage
                                    )
                                }
                            }
                        }
                    }
                }
                if (isTriggeredByPaymentButton && notSelectCourierCount > 0) {
                    if (notSelectCourierCount == 1) {
                        showToastNormal(activity!!.getString(R.string.message_error_courier_not_selected))
                    } else {
                        showToastNormal(
                            String.format(
                                getString(R.string.message_error_multiple_courier_not_selected),
                                notSelectCourierCount
                            )
                        )
                    }
                }
                rvShipment?.smoothScrollToPosition(firstFoundPosition)
            }
        }
    }

    override fun onShowTickerShippingCompletion() {
        checkoutAnalyticsCourierSelection.eventViewSummaryTransactionTickerCourierNotComplete(
            userSessionInterface.userId
        )
    }

    override fun onCashbackUpdated(amount: Int) {
        // No-op
    }

    override fun onPrimaryCTAClicked() {
        releaseBookingIfAny()
    }

    override fun onClickTradeInInfo() {
        checkoutTradeInAnalytics.eventTradeInClickInformation(isTradeInByDropOff)
        val fragmentManager = fragmentManager
        val context = context
        if (fragmentManager != null && context != null) {
            showTradeInInfoBottomsheet(fragmentManager, context)
        }
    }

    override fun onClickSwapInIndomaret() {
        checkoutTradeInAnalytics.eventTradeInClickTukarDiIndomaret()
    }

    override fun onSwapInUserAddress() {
        checkoutTradeInAnalytics.eventTradeInClickTukarDiAlamatmu()
    }

    override val currentFragmentManager: FragmentManager
        get() = parentFragmentManager

    override fun scrollToPositionWithOffset(position: Int, dy: Float) {
        val layoutManager = rvShipment!!.layoutManager
        if (layoutManager != null) {
            (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, dy.toInt())
        }
    }

    private fun onViewTickerProductError(shopId: String, errorMessage: String) {
        checkoutAnalyticsCourierSelection.eventViewTickerProductLevelErrorInCheckoutPage(
            shopId,
            errorMessage
        )
    }

    private fun onViewTickerOrderError(shopId: String, errorMessage: String?) {
        checkoutAnalyticsCourierSelection.eventViewTickerOrderLevelErrorInCheckoutPage(
            shopId,
            errorMessage!!
        )
    }

    private fun onViewTickerPaymentError(errorMessage: String) {
        val shipmentCartItemModelList = shipmentPresenter.shipmentCartItemModelList
        for (shipmentCartItemModel in shipmentCartItemModelList!!) {
            checkoutAnalyticsCourierSelection.eventViewTickerPaymentLevelErrorInCheckoutPage(
                shipmentCartItemModel.shopId.toString(),
                errorMessage
            )
        }
    }

    override fun onClickLihatOnTickerOrderError(shopId: String?, errorMessage: String?) {
        checkoutAnalyticsCourierSelection.eventClickLihatOnTickerErrorOrderLevelErrorInCheckoutPage(
            shopId!!,
            errorMessage!!
        )
    }

    override fun onClickRefreshErrorLoadCourier() {
        checkoutAnalyticsCourierSelection.eventClickRefreshWhenErrorLoadCourier()
    }

    override fun onViewErrorInCourierSection(errorMessage: String?) {
        checkoutAnalyticsCourierSelection.eventViewErrorInCourierSection(errorMessage!!)
    }

    override fun onClickSetPinpoint(position: Int) {
        setPinpoint(position)
    }

    override fun prepareReloadRates(lastSelectedCourierOrder: Int, skipMvc: Boolean) {
        val shipmentCartItemModel =
            shipmentAdapter.getShipmentCartItemModelByIndex(lastSelectedCourierOrder)
        shipmentCartItemModel?.let { reloadCourier(it, lastSelectedCourierOrder, skipMvc) }
    }

    override fun updateLocalCacheAddressData(userAddress: UserAddress?) {
        val activity: Activity? = activity
        if (activity != null) {
            val lca = getLocalizingAddressData(
                activity
            )
            val tokonow = userAddress!!.tokoNow
            if (userAddress.state == UserAddress.STATE_ADDRESS_ID_NOT_MATCH || lca.address_id.isEmpty() || lca.address_id == "0") {
                updateLocalizingAddressDataFromOther(
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
                        mapWarehousesAddAddressModelToLocal(
                            tokonow.warehouses
                        )
                    } else {
                        lca.warehouses
                    },
                    if (tokonow.isModified) tokonow.serviceType else lca.service_type,
                    ""
                )
            } else if (tokonow.isModified) {
                updateTokoNowData(
                    activity,
                    tokonow.warehouseId,
                    tokonow.shopId,
                    mapWarehousesAddAddressModelToLocal(tokonow.warehouses),
                    tokonow.serviceType
                )
            }
        }
    }

    override fun openAddOnProductLevelBottomSheet(
        cartItemModel: CartItemModel?,
        addOnWordingModel: AddOnWordingModel?
    ) {
        if (activity != null) {
            val addOnsDataModel = cartItemModel!!.addOnProductLevelModel
            val addOnBottomSheetModel = addOnsDataModel.addOnsBottomSheetModel

            // No need to open add on bottom sheet if action = 0
            if (addOnsDataModel.addOnsButtonModel.action == 0) return
            var availableBottomSheetData = AvailableBottomSheetData()
            var unavailableBottomSheetData = UnavailableBottomSheetData()
            if (addOnsDataModel.status == ADD_ON_STATUS_DISABLE) {
                unavailableBottomSheetData =
                    mapUnavailableBottomSheetProductLevelData(addOnBottomSheetModel, cartItemModel)
            }
            if (cartItemModel.addOnProductLevelModel.status == ADD_ON_STATUS_ACTIVE) {
                availableBottomSheetData = mapAvailableBottomSheetProductLevelData(
                    addOnWordingModel!!,
                    cartItemModel
                )
            }
            val addOnProductData = mapAddOnBottomSheetParam(
                addOnsDataModel,
                availableBottomSheetData,
                unavailableBottomSheetData
            )
            val intent =
                RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.ADD_ON_GIFTING)
            intent.putExtra(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA, addOnProductData)
            intent.putExtra(AddOnConstant.EXTRA_ADD_ON_SOURCE, AddOnConstant.ADD_ON_SOURCE_CHECKOUT)
            startActivityForResult(intent, REQUEST_ADD_ON_PRODUCT_LEVEL_BOTTOMSHEET)
            checkoutAnalyticsCourierSelection.eventClickAddOnsDetail(cartItemModel.productId.toString())
        }
    }

    override fun openAddOnOrderLevelBottomSheet(
        shipmentCartItemModel: ShipmentCartItemModel?,
        addOnWordingModel: AddOnWordingModel?
    ) {
        if (activity != null && shipmentCartItemModel!!.addOnsOrderLevelModel != null) {
            val addOnsDataModel = shipmentCartItemModel.addOnsOrderLevelModel
            val addOnBottomSheetModel = addOnsDataModel!!.addOnsBottomSheetModel

            // No need to open add on bottom sheet if action = 0
            if (addOnsDataModel.addOnsButtonModel.action == 0) return
            var availableBottomSheetData = AvailableBottomSheetData()
            var unavailableBottomSheetData = UnavailableBottomSheetData()
            if (addOnsDataModel.status == ADD_ON_STATUS_DISABLE) {
                unavailableBottomSheetData = mapUnavailableBottomSheetOrderLevelData(
                    addOnBottomSheetModel,
                    shipmentCartItemModel
                )
            }
            if (addOnsDataModel.status == ADD_ON_STATUS_ACTIVE) {
                availableBottomSheetData = mapAvailableBottomSheetOrderLevelData(
                    addOnWordingModel!!,
                    shipmentCartItemModel
                )
            }
            val addOnProductData = mapAddOnBottomSheetParam(
                addOnsDataModel,
                availableBottomSheetData,
                unavailableBottomSheetData
            )
            val intent =
                RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.ADD_ON_GIFTING)
            intent.putExtra(AddOnConstant.EXTRA_ADD_ON_PRODUCT_DATA, addOnProductData)
            startActivityForResult(intent, REQUEST_ADD_ON_ORDER_LEVEL_BOTTOMSHEET)
            checkoutAnalyticsCourierSelection.eventClickAddOnsDetail(shipmentCartItemModel.cartString)
        }
    }

    override fun addOnProductLevelImpression(productId: String?) {
        checkoutAnalyticsCourierSelection.eventViewAddOnsWidget(productId!!)
    }

    override fun addOnOrderLevelImpression(cartItemModelList: List<CartItemModel>?) {
        val listCartString = ArrayList<String>()
        for (cartItemModel in cartItemModelList!!) {
            listCartString.add(cartItemModel.cartString)
        }
        checkoutAnalyticsCourierSelection.eventViewAddOnsWidget(listCartString.toString())
    }

    override fun uploadPrescriptionAction(
        uploadPrescriptionUiModel: UploadPrescriptionUiModel,
        buttonText: String,
        buttonNotes: String
    ) {
        if (!uploadPrescriptionUiModel.consultationFlow) {
            ePharmacyAnalytics.sendPrescriptionWidgetClick(uploadPrescriptionUiModel.checkoutId)
            val uploadPrescriptionIntent = RouteManager.getIntent(
                activityContext,
                UploadPrescriptionViewHolder.EPharmacyAppLink
            )
            uploadPrescriptionIntent.putExtra(
                EXTRA_CHECKOUT_ID_STRING,
                uploadPrescriptionUiModel.checkoutId
            )
            startActivityForResult(uploadPrescriptionIntent, REQUEST_CODE_UPLOAD_PRESCRIPTION)
        } else {
            val uploadPrescriptionIntent = RouteManager.getIntent(
                activityContext,
                UploadPrescriptionViewHolder.EPharmacyMiniConsultationAppLink
            )
            startActivityForResult(uploadPrescriptionIntent, REQUEST_CODE_MINI_CONSULTATION)
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

    private fun onUploadPrescriptionResult(data: Intent?, isApi: Boolean) {
        if (data != null && data.extras != null &&
            data.extras!!.containsKey(KEY_UPLOAD_PRESCRIPTION_IDS_EXTRA) && activity != null
        ) {
            val uploadModel = shipmentPresenter.uploadPrescriptionUiModel
            val prescriptions = data.extras!!.getStringArrayList(
                KEY_UPLOAD_PRESCRIPTION_IDS_EXTRA
            )
            uploadModel!!.isError = false
            if (!isApi || (prescriptions != null && prescriptions.isNotEmpty())) {
                shipmentPresenter.setPrescriptionIds(prescriptions!!)
            }
            if (!isApi) {
                showToastNormal(activity!!.getString(com.tokopedia.purchase_platform.common.R.string.pp_epharmacy_upload_success_text))
            }
            updateUploadPrescription(uploadModel)
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
                shipmentPresenter.setMiniConsultationResult(results)
            }
        }
    }

    override fun updateUploadPrescription(uploadPrescriptionUiModel: UploadPrescriptionUiModel?) {
        shipmentAdapter.updateUploadPrescription(uploadPrescriptionUiModel)
    }

    override fun onViewUpsellCard(shipmentUpsellModel: ShipmentUpsellModel?) {
        checkoutAnalyticsCourierSelection.eventViewGotoplusUpsellTicker()
    }

    override fun onClickUpsellCard(shipmentUpsellModel: ShipmentUpsellModel?) {
        if (context != null) {
            checkoutAnalyticsCourierSelection.eventClickGotoplusUpsellTicker()
            RouteManager.route(context, shipmentUpsellModel!!.appLink)
        }
    }

    override fun onViewNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel?) {
        checkoutAnalyticsCourierSelection.eventViewNewUpsell(shipmentUpsellModel!!.isSelected)
    }

    override fun onClickApplyNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel?) {
        startActivityForResult(
            getStartIntent(
                requireContext(),
                shipmentUpsellModel!!.appLink,
                true,
                true,
                false,
                ""
            ),
            REQUEST_CODE_UPSELL
        )
        checkoutAnalyticsCourierSelection.eventClickNewUpsell(shipmentUpsellModel.isSelected)
    }

    override fun onClickCancelNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel?) {
        isPlusSelected = false
        shipmentPresenter.cancelUpsell(
            true, isOneClickShipment, isTradeIn, true,
            false, null, deviceId, checkoutLeasingId,
            isPlusSelected()
        )
        checkoutAnalyticsCourierSelection.eventClickNewUpsell(shipmentUpsellModel!!.isSelected)
    }

    override fun onViewFreeShippingPlusBadge() {
        checkoutAnalyticsCourierSelection.eventViewGotoplusTicker()
    }

    override fun onChangeScheduleDelivery(
        scheduleDeliveryUiModel: ScheduleDeliveryUiModel?,
        position: Int,
        donePublisher: PublishSubject<Boolean>
    ) {
        if (view != null) {
            val shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(position)!!
            if (shipmentCartItemModel.selectedShipmentDetailData != null &&
                shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier != null
            ) {
                val courierItemData =
                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier
                val newCourierItemData = clone(courierItemData!!, scheduleDeliveryUiModel!!)
                val hasNoPromo =
                    TextUtils.isEmpty(courierItemData.selectedShipper.logPromoCode) && TextUtils.isEmpty(
                        newCourierItemData.selectedShipper.logPromoCode
                    )
                if (scheduleDeliveryUiModel.isSelected) {
                    shipmentCartItemModel.scheduleDate = scheduleDeliveryUiModel.scheduleDate
                    shipmentCartItemModel.timeslotId = scheduleDeliveryUiModel.timeslotId
                    shipmentCartItemModel.validationMetadata =
                        scheduleDeliveryUiModel.deliveryProduct.validationMetadata
                } else {
                    shipmentCartItemModel.scheduleDate = ""
                    shipmentCartItemModel.timeslotId = 0
                    shipmentCartItemModel.validationMetadata = ""
                }
                val selectedShipper = newCourierItemData.selectedShipper
                val shouldValidateUse = selectedShipper.logPromoCode != null && selectedShipper.logPromoCode!!.isNotEmpty()
                val hasCheckAllCourier =
                    shipmentAdapter.checkHasSelectAllCourier(true, -1, "", false, false)
                val haveToClearCache = shipmentCartItemModel.voucherLogisticItemUiModel != null &&
                    !TextUtils.isEmpty(shipmentCartItemModel.voucherLogisticItemUiModel!!.code) &&
                    TextUtils.isEmpty(newCourierItemData.selectedShipper.logPromoCode)
                val shouldStopInClearCache = haveToClearCache && !hasCheckAllCourier
                val shouldStopInDoValidateUseLogistic = shouldValidateUse && !hasCheckAllCourier
                shipmentPresenter.setScheduleDeliveryMapData(
                    shipmentCartItemModel.cartString,
                    ShipmentScheduleDeliveryMapData(
                        donePublisher,
                        shouldStopInClearCache,
                        shouldStopInDoValidateUseLogistic
                    )
                )
                if (haveToClearCache) {
                    val promoLogisticCode = shipmentCartItemModel.voucherLogisticItemUiModel!!.code
                    shipmentPresenter.cancelAutoApplyPromoStackLogistic(
                        0,
                        promoLogisticCode,
                        shipmentCartItemModel
                    )
                    val validateUsePromoRequest = shipmentPresenter.lastValidateUseRequest
                    if (validateUsePromoRequest != null) {
                        for (ordersItem in validateUsePromoRequest.orders) {
                            if (ordersItem.codes.size > 0) {
                                ordersItem.codes.remove(promoLogisticCode)
                            }
                        }
                    }
                    shipmentCartItemModel.voucherLogisticItemUiModel = null
                    benefitSummaryInfoUiModel = null
                    shipmentAdapter.clearTotalPromoStackAmount()
                    shipmentAdapter.updateShipmentCostModel()
                    shipmentAdapter.updateCheckoutButtonData(null)
                }
                shipmentAdapter.setSelectedCourier(
                    position,
                    newCourierItemData,
                    true,
                    shouldValidateUse
                )
                shipmentPresenter.processSaveShipmentState(shipmentCartItemModel)
                if (shouldValidateUse) {
                    val validateUsePromoRequest = generateValidateUsePromoRequest()
                    if (selectedShipper.logPromoCode != null && selectedShipper.logPromoCode!!.isNotEmpty()) {
                        for (order in validateUsePromoRequest.orders) {
                            if (order.uniqueId == shipmentCartItemModel.cartString && !order.codes.contains(
                                    newCourierItemData.selectedShipper.logPromoCode
                                )
                            ) {
                                if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                                    // remove previous logistic promo code
                                    order.codes.remove(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                                }
                                order.codes.add(selectedShipper.logPromoCode!!)
                                break
                            }
                        }
                    }
                    val shipmentCartItemModelLists = shipmentAdapter.shipmentCartItemModelList
                    if (shipmentCartItemModelLists != null && shipmentCartItemModelLists.isNotEmpty()) {
                        for (tmpShipmentCartItemModel in shipmentCartItemModelLists) {
                            for (order in validateUsePromoRequest.orders) {
                                if (shipmentCartItemModel.cartString != tmpShipmentCartItemModel.cartString && tmpShipmentCartItemModel.cartString == order.uniqueId && tmpShipmentCartItemModel.voucherLogisticItemUiModel != null) {
                                    order.codes.remove(tmpShipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                                }
                            }
                        }
                    }
                    for (ordersItem in validateUsePromoRequest.orders) {
                        if (ordersItem.uniqueId == shipmentCartItemModel.cartString) {
                            ordersItem.spId = selectedShipper.shipperProductId
                            ordersItem.shippingId = selectedShipper.shipperId
                            ordersItem.freeShippingMetadata = selectedShipper.freeShippingMetadata
                            ordersItem.shopId = shipmentCartItemModel.shopId
                            ordersItem.isPo = shipmentCartItemModel.isProductIsPreorder
                            ordersItem.poDuration =
                                shipmentCartItemModel.cartItemModels[0].preOrderDurationDay
                            ordersItem.warehouseId = shipmentCartItemModel.fulfillmentId
                            ordersItem.boCampaignId = selectedShipper.boCampaignId
                            ordersItem.shippingSubsidy = selectedShipper.shippingSubsidy
                            ordersItem.benefitClass = selectedShipper.benefitClass
                            ordersItem.shippingPrice = selectedShipper.shippingRate.toDouble()
                            ordersItem.etaText = selectedShipper.etaText!!
                            ordersItem.validationMetadata = shipmentCartItemModel.validationMetadata
                            break
                        }
                    }
                    shipmentPresenter.doValidateUseLogisticPromo(
                        position,
                        shipmentCartItemModel.cartString,
                        validateUsePromoRequest,
                        selectedShipper.logPromoCode!!,
                        false
                    )
                } else if (!shouldStopInClearCache && !shouldStopInDoValidateUseLogistic && !hasCheckAllCourier || hasNoPromo) {
                    donePublisher.onCompleted()
                }
            }
        }
    }

    override fun onInsuranceInfoTooltipClickedTrackingAnalytics() {
        checkoutAnalyticsCourierSelection.eventClickAtcCourierSelectionInsuranceInfoTooltip(
            userSessionInterface.userId
        )
    }

    private fun updateLocalCacheAddressData(saveAddressDataModel: SaveAddressDataModel) {
        val activity: Activity? = activity
        if (activity != null) {
            updateLocalizingAddressDataFromOther(
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
                mapWarehousesAddAddressModelToLocal(saveAddressDataModel.warehouses),
                saveAddressDataModel.serviceType,
                ""
            )
        }
    }

    override fun logOnErrorLoadCheckoutPage(throwable: Throwable?) {
        logOnErrorLoadCheckoutPage(throwable!!, isOneClickShipment, isTradeIn, isTradeInByDropOff)
    }

    override fun logOnErrorLoadCourier(
        throwable: Throwable?,
        itemPosition: Int,
        boPromoCode: String?
    ) {
        val shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition)
        if (shipmentCartItemModel != null) {
            logOnErrorLoadCourier(
                throwable!!,
                shipmentCartItemModel,
                isOneClickShipment,
                isTradeIn,
                isTradeInByDropOff,
                boPromoCode!!
            )
        }
    }

    override fun logOnErrorApplyBo(throwable: Throwable?, itemPosition: Int, boPromoCode: String?) {
        val shipmentCartItemModel = shipmentAdapter.getShipmentCartItemModelByIndex(itemPosition)
        if (shipmentCartItemModel != null) {
            logOnErrorApplyBo(
                throwable!!,
                shipmentCartItemModel,
                isOneClickShipment,
                isTradeIn,
                isTradeInByDropOff,
                boPromoCode!!
            )
        }
    }

    override fun logOnErrorApplyBo(
        throwable: Throwable,
        shipmentCartItemModel: ShipmentCartItemModel,
        boPromoCode: String
    ) {
        logOnErrorApplyBo(
            throwable,
            shipmentCartItemModel,
            isOneClickShipment,
            isTradeIn,
            isTradeInByDropOff,
            boPromoCode
        )
    }

    override fun logOnErrorCheckout(throwable: Throwable, request: String) {
        logOnErrorCheckout(
            throwable,
            request,
            isOneClickShipment,
            isTradeIn,
            isTradeInByDropOff
        )
    }

    override fun showPopUp(popUpData: PopUpData) {
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

    override fun updateAddOnsData(addOnsDataModel: AddOnsDataModel?, identifier: Int) {
        // identifier : 0 = product level, 1  = order level
        if (identifier == 0) {
            shipmentAdapter.notifyItemChanged(shipmentAdapter.addOnProductLevelPosition)
        } else {
            shipmentAdapter.notifyItemChanged(shipmentAdapter.addOnOrderLevelPosition)
        }
        shipmentAdapter.updateShipmentCostModel()
        onNeedUpdateViewItem(shipmentAdapter.shipmentCostPosition)
    }

    override fun updateAddOnsDynamicDataPassing(
        addOnsDataModel: AddOnsDataModel?,
        addOnResult: AddOnResult?,
        identifier: Int,
        cartString: String?,
        cartId: Long?
    ) {
        // identifier : 0 = product level, 1  = order level
        if (addOnResult!!.addOnData.isEmpty()) {
            // unchecked
            val uncheckedDynamicDataParam = DynamicDataParam()
            uncheckedDynamicDataParam.attribute = ATTRIBUTE_ADDON_DETAILS
            if (identifier == 1) {
                uncheckedDynamicDataParam.level = ORDER_LEVEL
                uncheckedDynamicDataParam.uniqueId = cartString!!
            } else if (identifier == 0) {
                uncheckedDynamicDataParam.level = PRODUCT_LEVEL
                uncheckedDynamicDataParam.parentUniqueId = cartString!!
                uncheckedDynamicDataParam.uniqueId = cartId.toString()
            }
            updateCheckboxDynamicData(uncheckedDynamicDataParam, false)
        } else {
            val dynamicDataParam = DynamicDataParam()
            dynamicDataParam.attribute = ATTRIBUTE_ADDON_DETAILS
            dynamicDataParam.addOn = getAddOn(addOnResult, isOneClickShipment)
            if (identifier == 1) {
                // order level
                dynamicDataParam.level = ORDER_LEVEL
                dynamicDataParam.uniqueId = cartString!!
            } else if (identifier == 0) {
                // product level
                dynamicDataParam.level = PRODUCT_LEVEL
                dynamicDataParam.parentUniqueId = cartString!!
                dynamicDataParam.uniqueId = cartId.toString()
            }
            updateCheckboxDynamicData(dynamicDataParam, true)
        }
    }

    override fun renderUnapplyBoIncompleteShipment(unappliedBoPromoUniqueIds: List<String>?) {
        if (activity != null) {
            val shipmentDataList = shipmentAdapter.getShipmentDataList()
            var firstFoundPosition = 0
            shipment_loop@ for (i in shipmentDataList.indices) {
                if (shipmentDataList[i] is ShipmentCartItemModel) {
                    val shipmentCartItemModel = shipmentDataList[i] as ShipmentCartItemModel
                    for (uniqueId in unappliedBoPromoUniqueIds!!) {
                        if (shipmentCartItemModel.cartString == uniqueId &&
                            (shipmentCartItemModel.selectedShipmentDetailData == null || shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourier == null)
                        ) {
                            firstFoundPosition = i
                            shipmentCartItemModel.isTriggerShippingVibrationAnimation = true
                            shipmentCartItemModel.isStateAllItemViewExpanded = false
                            shipmentCartItemModel.isShippingBorderRed = true
                            onNeedUpdateViewItem(i)
                            break@shipment_loop
                        }
                    }
                }
            }
            rvShipment?.smoothScrollToPosition(firstFoundPosition)
        }
    }

    override fun getShipmentCartItemModelAdapterPositionByUniqueId(uniqueId: String?): Int {
        for (i in shipmentAdapter.getShipmentDataList().indices) {
            val adapterItem = shipmentAdapter.getShipmentDataList()[i]
            if (adapterItem is ShipmentCartItemModel && adapterItem.cartString == uniqueId) {
                return i
            }
        }
        return -1
    }

    override fun getShipmentCartItemModel(adapterPosition: Int): ShipmentCartItemModel? {
        return shipmentAdapter.getShipmentCartItemModelByIndex(adapterPosition)
    }

    private fun onResultFromUpsell(data: Intent?) {
        if (data != null && data.hasExtra(CartConstant.CHECKOUT_IS_PLUS_SELECTED)) {
            isPlusSelected = data.getBooleanExtra(CartConstant.CHECKOUT_IS_PLUS_SELECTED, false)
            shipmentPresenter.processInitialLoadCheckoutPage(
                true, isOneClickShipment, isTradeIn, true,
                false, null, deviceId, checkoutLeasingId,
                isPlusSelected()
            )
        }
    }

    override fun showPrescriptionReminderDialog(uploadPrescriptionUiModel: UploadPrescriptionUiModel?) {
        val epharmacyGroupIds = uploadPrescriptionUiModel!!.epharmacyGroupIds
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

    private fun sendAnalyticsEpharmacyClickPembayaran() {
        val shipmentDataList = shipmentAdapter.getShipmentDataList()
        for (i in shipmentDataList.indices.reversed()) {
            if (shipmentDataList[i] is UploadPrescriptionUiModel) {
                val uploadPrescriptionUiModel = shipmentDataList[i] as UploadPrescriptionUiModel
                if (uploadPrescriptionUiModel.consultationFlow && uploadPrescriptionUiModel.showImageUpload) {
                    val viewHolder = rvShipment!!.findViewHolderForAdapterPosition(i)
                    if (viewHolder is UploadPrescriptionViewHolder) {
                        ePharmacyAnalytics.clickPilihPembayaran(
                            viewHolder.getButtonNotes(),
                            uploadPrescriptionUiModel.epharmacyGroupIds,
                            false,
                            "success"
                        )
                    }
                }
                break
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_EDIT_ADDRESS = 11
        private const val REQUEST_CODE_COURIER_PINPOINT = 13
        private const val REQUEST_CODE_PROMO = 954
        const val REQUEST_CODE_UPLOAD_PRESCRIPTION = 10021
        const val REQUEST_CODE_MINI_CONSULTATION = 10022
        private const val REQUEST_CODE_UPSELL = 777
        private const val ADD_ON_STATUS_ACTIVE = 1
        private const val ADD_ON_STATUS_DISABLE = 2
        private const val SHIPMENT_TRACE = "mp_shipment"
        private const val KEY_UPLOAD_PRESCRIPTION_IDS_EXTRA = "epharmacy_prescription_ids"
        const val ARG_IS_ONE_CLICK_SHIPMENT = "ARG_IS_ONE_CLICK_SHIPMENT"
        const val ARG_CHECKOUT_LEASING_ID = "ARG_CHECKOUT_LEASING_ID"
        const val ARG_CHECKOUT_PAGE_SOURCE = "ARG_CHECKOUT_PAGE_SOURCE"
        const val ARG_IS_PLUS_SELECTED = "ARG_IS_PLUS_SELECTED"
        private const val DATA_STATE_LAST_CHOOSE_COURIER_ITEM_POSITION =
            "LAST_CHOOSE_COURIER_ITEM_POSITION"
        private const val DATA_STATE_LAST_CHOOSEN_SERVICE_ID = "DATA_STATE_LAST_CHOOSEN_SERVICE_ID"
        var EXTRA_CHECKOUT_ID_STRING = "extra_checkout_id_string"
        private const val KEY_PREFERENCE_COACHMARK_EPHARMACY = "has_seen_epharmacy_coachmark"
        private const val TOASTER_THROTTLE: Long = 2000

        fun newInstance(
            isOneClickShipment: Boolean,
            leasingId: String,
            pageSource: String,
            isPlusSelected: Boolean,
            bundle: Bundle?
        ): ShipmentFragment {
            val b = bundle ?: Bundle()
            b.putString(ARG_CHECKOUT_LEASING_ID, leasingId)
            if (leasingId.isNotEmpty()) {
                b.putBoolean(ARG_IS_ONE_CLICK_SHIPMENT, true)
            } else {
                b.putBoolean(ARG_IS_ONE_CLICK_SHIPMENT, isOneClickShipment)
            }
            b.putString(ARG_CHECKOUT_PAGE_SOURCE, pageSource)
            b.putBoolean(ARG_IS_PLUS_SELECTED, isPlusSelected)
            val shipmentFragment = ShipmentFragment()
            shipmentFragment.arguments = b
            return shipmentFragment
        }
    }
}
