package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.app.Activity
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.*

/**
 * Created by Irfan Khoirul on 07/08/18.
 */
interface ShippingDurationContract {
    interface View : CustomerView {
        fun showLoading()
        fun hideLoading()
        fun showErrorPage(message: String)
        fun showData(serviceDataList: List<ShippingDurationUiModel>, promoViewModelList: List<LogisticPromoUiModel>, preOrderModel: PreOrderModel?)
        fun showNoCourierAvailable(message: String?)
        fun stopTrace()
        fun isDisableCourierPromo(): Boolean
        fun getActivity(): Activity
    }

    interface Presenter : CustomerPresenter<View> {
        fun loadCourierRecommendation(shipmentDetailData: ShipmentDetailData, selectedServiceId: Int,
                                      shopShipmentList: List<ShopShipment>, codHistory: Int,
                                      isCorner: Boolean, isLeasing: Boolean, pslCode: String,
                                      products: List<Product>, cartString: String, isTradeInDropOff: Boolean,
                                      recipientAddressModel: RecipientAddressModel, isFulfillment: Boolean, preOrderTime: Int, mvc: String)

        fun getCourierItemData(shippingCourierUiModels: List<ShippingCourierUiModel>): CourierItemData?
        fun getCourierItemDataById(spId: Int, shippingCourierUiModels: List<ShippingCourierUiModel>): CourierItemData?
    }
}