package com.tokopedia.checkout.revamp.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.mapper.ShipmentAddOnProductServiceMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentPlatformFeeData
import com.tokopedia.checkout.revamp.view.converter.CheckoutDataConverter
import com.tokopedia.checkout.revamp.view.processor.CheckoutAddOnProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutCartProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutLogisticProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutPaymentProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutPromoProcessor
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.view.CheckoutMutableLiveData
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSummaryModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckoutViewModel @Inject constructor(
    private val cartProcessor: CheckoutCartProcessor,
    private val logisticProcessor: CheckoutLogisticProcessor,
    private val promoProcessor: CheckoutPromoProcessor,
    private val addOnProcessor: CheckoutAddOnProcessor,
    private val paymentProcessor: CheckoutPaymentProcessor,
    private val checkoutProcessor: CheckoutProcessor,
    private val dataConverter: CheckoutDataConverter,
    private val shippingCourierConverter: ShippingCourierConverter,
    private val stateConverter: RatesResponseStateConverter,
    private val shipmentDataRequestConverter: ShipmentDataRequestConverter,
    private val mTrackerShipment: CheckoutAnalyticsCourierSelection,
    private val mTrackerPurchaseProtection: CheckoutAnalyticsPurchaseProtection,
    private val userSessionInterface: UserSessionInterface,
    private val gson: Gson,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    var currentJob: Job? = null
    var counter: Int = 0

    var ms = MutableStateFlow(true)

    var sm = flow<Boolean> { }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)

    val listData: CheckoutMutableLiveData<List<CheckoutItem>> = CheckoutMutableLiveData(emptyList())

    val pageState: CheckoutMutableLiveData<CheckoutPageState> = CheckoutMutableLiveData(CheckoutPageState.Loading)

    var isOneClickShipment: Boolean = false

    var checkoutLeasingId: String = "0"

    var isTradeIn: Boolean = false

    var deviceId: String = ""

    var isPlusSelected: Boolean = false

    val cornerId: String?
        get() = recipientAddressModel.cornerId

    val recipientAddressModel: RecipientAddressModel
        get() = (listData.value.firstOrNull { it is CheckoutAddressModel } as? CheckoutAddressModel)?.recipientAddressModel ?: RecipientAddressModel()

    private var isUsingDdp = false

    private var dynamicDataParam: DynamicDataPassingParamRequest = DynamicDataPassingParamRequest()

    private var shipmentPlatformFeeData: ShipmentPlatformFeeData = ShipmentPlatformFeeData()

    var dynamicData = ""

    // add ons product
    // list summary add on - ready to render
    private var listSummaryAddOnModel: List<ShipmentAddOnSummaryModel> = emptyList()

    // list summary default
    private var summariesAddOnUiModel: HashMap<Int, String> = hashMapOf()

    fun test() {
        currentJob = viewModelScope.launch {
            Log.i("qwertyuiop", "start launch")
            doSomething(++counter)
            Log.i("qwertyuiop", "done launch")
        }
    }

    private suspend fun doSomething(count: Int) {
        currentJob?.join()
        withContext(dispatchers.io) {
            Log.i("qwertyuiop", "start delay $count")
            delay(5_000)
            Log.i("qwertyuiop", "done delay $count")
        }
    }

    fun loadSAF(
        isReloadData: Boolean,
        skipUpdateOnboardingState: Boolean,
        isReloadAfterPriceChangeHigher: Boolean
    ) {
        viewModelScope.launch(dispatchers.io) {
            val saf = cartProcessor.hitSAF(
                isOneClickShipment,
                isTradeIn,
                skipUpdateOnboardingState,
                cornerId,
                deviceId,
                checkoutLeasingId,
                isPlusSelected,
                isReloadData,
                isReloadAfterPriceChangeHigher
            )
            if (saf is CheckoutPageState.Success) {
                val tickerData = saf.cartShipmentAddressFormData.tickerData
                var ticker = CheckoutTickerModel(ticker = TickerAnnouncementHolderData())
                if (tickerData != null) {
                    ticker = CheckoutTickerModel(
                        ticker = TickerAnnouncementHolderData(
                            tickerData.id,
                            tickerData.title,
                            tickerData.message
                        )
                    )
//                    analyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
//                        tickerData.id
//                    )
                }
                val address = CheckoutAddressModel(recipientAddressModel = dataConverter.getRecipientAddressModel(saf.cartShipmentAddressFormData))

                val upsell = dataConverter.getUpsellModel(saf.cartShipmentAddressFormData.newUpsell)

                isUsingDdp = saf.cartShipmentAddressFormData.isUsingDdp
                dynamicData = saf.cartShipmentAddressFormData.dynamicData
                shipmentPlatformFeeData = saf.cartShipmentAddressFormData.shipmentPlatformFee
                listSummaryAddOnModel =
                    ShipmentAddOnProductServiceMapper.mapSummaryAddOns(saf.cartShipmentAddressFormData)

                val items = dataConverter.getCheckoutItems(
                    saf.cartShipmentAddressFormData,
                    address.recipientAddressModel.locationDataModel != null,
                    userSessionInterface.name
                )

                val epharmacy = CheckoutEpharmacyModel(
                    epharmacy = UploadPrescriptionUiModel(
                        showImageUpload = saf.cartShipmentAddressFormData.epharmacyData.showImageUpload,
                        uploadImageText = saf.cartShipmentAddressFormData.epharmacyData.uploadText,
                        leftIconUrl = saf.cartShipmentAddressFormData.epharmacyData.leftIconUrl,
                        checkoutId = saf.cartShipmentAddressFormData.epharmacyData.checkoutId,
                        frontEndValidation = saf.cartShipmentAddressFormData.epharmacyData.frontEndValidation,
                        consultationFlow = saf.cartShipmentAddressFormData.epharmacyData.consultationFlow,
                        rejectedWording = saf.cartShipmentAddressFormData.epharmacyData.rejectedWording
                    )
                )

                withContext(dispatchers.main) {
                    listData.value = listOf(ticker, address, upsell) + items + epharmacy
                    pageState.value = saf
                }
            } else {
                withContext(dispatchers.main) {
                    pageState.value = saf
                }
            }
            Log.i("qwertyuiop", "saf $saf")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("qwertyuiop", "done clear")
        Log.i("qwertyuiop", "job: ${currentJob?.isCompleted}")
        Log.i(
            "qwertyuiop",
            "parent job: ${viewModelScope.coroutineContext.job?.children?.find { !it.isCompleted }}"
        )
    }
}
