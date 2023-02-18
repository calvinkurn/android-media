package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.app.Activity
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.PreOrderModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShopShipment

/**
 * Created by Irfan Khoirul on 07/08/18.
 */
interface ShippingDurationContract {
    interface View : CustomerView {
        fun showLoading()
        fun hideLoading()
        fun showErrorPage(message: String)
        fun showData(uiModelList: MutableList<RatesViewModelType>)
        fun showNoCourierAvailable(message: String?)
        fun stopTrace()
        fun getActivity(): Activity
        fun sendAnalyticCourierPromo(shippingDurationUiModelList: List<ShippingDurationUiModel>)
        fun sendAnalyticPromoLogistic(promoViewModelList: List<LogisticPromoUiModel>)
        fun isToogleYearEndPromotionOn(): Boolean
        fun onShippingDurationAndRecommendCourierChosen(
            shippingCourierUiModelList: List<ShippingCourierUiModel>,
            courierData: ShippingCourierUiModel?,
            cartPosition: Int,
            selectedServiceId: Int,
            serviceData: ServiceData,
            flagNeedToSetPinpoint: Boolean
        )

        fun onLogisticPromoChosen(
            shippingCourierViewModelList: List<ShippingCourierUiModel>,
            courierData: ShippingCourierUiModel,
            serviceData: ServiceData,
            needToSetPinpoint: Boolean,
            promoCode: String,
            serviceId: Int,
            data: LogisticPromoUiModel
        )

        fun showPromoCourierNotAvailable()
    }

    interface Presenter : CustomerPresenter<View> {
        fun loadCourierRecommendation(
            shipmentDetailData: ShipmentDetailData,
            selectedServiceId: Int,
            shopShipmentList: List<ShopShipment>,
            codHistory: Int,
            isCorner: Boolean,
            isLeasing: Boolean,
            pslCode: String,
            products: List<Product>,
            cartString: String,
            isTradeInDropOff: Boolean,
            recipientAddressModel: RecipientAddressModel?,
            isFulfillment: Boolean,
            preOrderTime: Int,
            mvc: String,
            cartData: String,
            isOcc: Boolean,
            isDisableCourierPromo: Boolean
        )

        fun getCourierItemDataById(
            spId: Int,
            shippingCourierUiModels: List<ShippingCourierUiModel>
        ): ShippingCourierUiModel?

        fun convertServiceListToUiModel(
            shippingDurationUiModels: List<ShippingDurationUiModel>,
            promoUiModel: List<LogisticPromoUiModel>,
            preOrderModel: PreOrderModel?,
            isOcc: Boolean
        ): MutableList<RatesViewModelType>

        fun onChooseDuration(
            shippingCourierUiModelList: List<ShippingCourierUiModel>,
            cartPosition: Int,
            serviceData: ServiceData,
            isOcc: Boolean
        )

        fun onLogisticPromoClicked(data: LogisticPromoUiModel)
    }
}
