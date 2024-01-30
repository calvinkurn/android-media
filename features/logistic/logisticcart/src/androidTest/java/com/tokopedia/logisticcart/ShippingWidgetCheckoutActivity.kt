package com.tokopedia.logisticcart

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tokopedia.logisticcart.dummy.toDummyData
import com.tokopedia.logisticcart.dummy.toDummyType
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingCheckoutRevampWidget
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingWidgetUiModel
import com.tokopedia.logisticcart.test.R as logisticcarttestR

class ShippingWidgetCheckoutActivity : FragmentActivity(),
    ShippingCheckoutRevampWidget.ShippingWidgetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(logisticcarttestR.layout.activity_shipping_widget_checkout_test)
        val modelType = intent.getStringExtra("WIDGET_UI_MODEL_KEY")!!.toDummyType()
        val model = modelType.toDummyData()
        findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget).render(
            model
        )
    }

    override fun onChangeDurationClickListener() {
        //
    }

    override fun onChangeCourierClickListener() {
        //
    }

    override fun onOnTimeDeliveryClicked(url: String) {
        //
    }

    override fun onClickSetPinpoint() {
//        TODO("Not yet implemented")
    }

    override fun onClickLayoutFailedShipping() {
//        TODO("Not yet implemented")
    }

    override fun onViewErrorInCourierSection(logPromoDesc: String) {
//        TODO("Not yet implemented")
    }

    override fun onChangeScheduleDelivery(scheduleDeliveryUiModel: ScheduleDeliveryUiModel) {
//        TODO("Not yet implemented")
    }

    override fun getHostFragmentManager(): FragmentManager {
        return this@ShippingWidgetCheckoutActivity.getHostFragmentManager()
    }

    override fun onInsuranceCheckedForTrackingAnalytics() {
//        TODO("Not yet implemented")
    }

    override fun onInsuranceChecked(shippingWidgetUiModel: ShippingWidgetUiModel) {
//        TODO("Not yet implemented")
    }

    override fun onInsuranceInfoTooltipClickedTrackingAnalytics() {
//        TODO("Not yet implemented")
    }

    override fun showInsuranceBottomSheet(description: String) {
//        TODO("Not yet implemented")
    }

    override fun onViewCartErrorState(shippingWidgetUiModel: ShippingWidgetUiModel) {
//        TODO("Not yet implemented")
    }

    override fun onRenderVibrationAnimation(shippingWidgetUiModel: ShippingWidgetUiModel) {
//        TODO("Not yet implemented")
    }

    override fun onRenderNoSelectedShippingLayout() {
//        TODO("Not yet implemented")
    }
}
