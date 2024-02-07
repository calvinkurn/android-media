package com.tokopedia.logisticcart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.dummy.ShippingWidgetDummyType
import com.tokopedia.logisticcart.dummy.toDummyData
import com.tokopedia.logisticcart.dummy.toDummyType
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheet
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierBottomsheetListener
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheet
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationBottomsheetListener
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingCheckoutRevampWidget
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.InsuranceWidgetUiModel
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetState
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetUiModel
import com.tokopedia.logisticcart.utils.ShippingWidgetUtils.toShippingWidgetUiModel
import com.tokopedia.logisticcart.test.R as logisticcarttestR

class ShippingWidgetCheckoutActivity :
    FragmentActivity(),
    ShippingCheckoutRevampWidget.ShippingWidgetListener,
    ShippingDurationBottomsheetListener,
    ShippingCourierBottomsheetListener {

    companion object {
        fun newInstance(context: Context, type: ShippingWidgetDummyType): Intent {
            return Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
                putExtra(WIDGET_INITIAL_KEY, type.name)
            }
        }

        private const val WIDGET_INITIAL_KEY = "WIDGET_UI_MODEL_KEY"
    }

    var model: ShippingWidgetUiModel = ShippingWidgetUiModel()
    var courierList: List<ShippingCourierUiModel> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(logisticcarttestR.layout.activity_shipping_widget_checkout_test)
        val modelType = intent.getStringExtra("WIDGET_UI_MODEL_KEY")!!.toDummyType()
        model = modelType.toDummyData()
        findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget).run {
            setupListener(this@ShippingWidgetCheckoutActivity)
        }
        render()
    }

    private fun render() {
        findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget).render(
            model
        )
    }

    override fun onChangeDurationClickListener() {
        ShippingDurationBottomsheet.show(
            fragmentManager = getHostFragmentManager(),
            shippingDurationBottomsheetListener = this@ShippingWidgetCheckoutActivity,
            cartPosition = 0,
            isDisableOrderPrioritas = false,
            isOcc = false,
            isRatesTradeInApi = false,
            ratesParam = RatesParam.Builder(listOf(), ShippingParam()).build(),
            recipientAddressModel = RecipientAddressModel(),
            selectedSpId = 0,
            selectedServiceId = 0
        )
    }

    override fun onChangeCourierClickListener() {
        ShippingCourierBottomsheet.show(
            fragmentManager = getHostFragmentManager(),
            cartPosition = 0,
            isOcc = false,
            recipientAddressModel = null,
            shippingCourierUiModels = courierList,
            shippingCourierBottomsheetListener = this@ShippingWidgetCheckoutActivity

        )
    }

    override fun onOnTimeDeliveryClicked(url: String) {
    }

    override fun onClickSetPinpoint() {
    }

    override fun onClickLayoutFailedShipping() {
    }

    override fun onViewErrorInCourierSection(logPromoDesc: String) {
    }

    override fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel) {
    }

    override fun getHostFragmentManager(): FragmentManager {
        return this@ShippingWidgetCheckoutActivity.supportFragmentManager
    }

    override fun onInsuranceCheckedForTrackingAnalytics() {
    }

    override fun onInsuranceChecked(insuranceData: InsuranceWidgetUiModel?) {
    }

    override fun onInsuranceInfoTooltipClickedTrackingAnalytics() {
    }

    override fun showInsuranceBottomSheet(description: String) {
    }

    override fun onViewCartErrorState(error: ShippingWidgetState.CartError) {
    }

    override fun onRenderVibrationAnimation(shippingWidgetUiModel: ShippingWidgetUiModel) {
    }

    override fun onRenderNoSelectedShippingLayout() {
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
        val courierItemData =
            ShippingCourierConverter().convertToCourierItemDataNew(selectedCourier)
        model = courierItemData.toShippingWidgetUiModel(
            cartError = false,
            cartErrorTitle = "",
            needPinpoint = false,
            isDisableChangeCourier = false,
            hasGeolocation = false,
            isLoading = false,
            isHasShownCourierError = false,
            isCheckInsurance = false,
            isInsurance = false,
            isShippingBorderRed = false,
            isTriggerShippingVibrationAnimation = false
        )
        courierList = shippingCourierUiModels
        render()
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
        val courierItemData = ShippingCourierConverter().convertToCourierItemDataWithPromo(
            courierData,
            logisticPromo
        )
        model = courierItemData.toShippingWidgetUiModel(
            cartError = false,
            cartErrorTitle = "",
            needPinpoint = false,
            isDisableChangeCourier = false,
            hasGeolocation = false,
            isLoading = false,
            isHasShownCourierError = false,
            isCheckInsurance = false,
            isInsurance = false,
            isShippingBorderRed = false,
            isTriggerShippingVibrationAnimation = false
        )
        render()
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
        model = courierItemData.toShippingWidgetUiModel(
            cartError = false,
            cartErrorTitle = "",
            needPinpoint = false,
            isDisableChangeCourier = false,
            hasGeolocation = false,
            isLoading = false,
            isHasShownCourierError = false,
            isCheckInsurance = false,
            isInsurance = false,
            isShippingBorderRed = false,
            isTriggerShippingVibrationAnimation = false
        )
        render()
    }

    override fun onCourierShipmentRecommendationCloseClicked() {
        //
    }
}
