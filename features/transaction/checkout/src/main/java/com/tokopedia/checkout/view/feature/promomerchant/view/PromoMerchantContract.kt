package com.tokopedia.checkout.view.feature.promomerchant.view

import android.app.Activity
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * Created by fwidjaja on 01/03/19.
 */
interface PromoMerchantContract {

    interface View : CustomerView {

        fun showLoading()

        fun hideLoading()

        fun showErrorPage(message: String)

        fun showData()

        fun stopTrace()
    }

    interface Presenter : CustomerPresenter<View> {

        /*val shippingDurationViewModels: List<ShippingDurationViewModel>

        var recipientAddressModel: RecipientAddressModel
        fun loadCourierRecommendation(shipmentDetailData: ShipmentDetailData, selectedServiceId: Int,
                                      shopShipmentList: List<ShopShipment>, codHistory: Int, cornerId: String)

        fun loadCourierRecommendation(shippingParam: ShippingParam, selectedServiceId: Int, shopShipmentList: List<ShopShipment>, codHistory: Int, cornerId: String)

        fun getCourierItemData(shippingCourierViewModels: List<ShippingCourierViewModel>): CourierItemData*/

        fun loadPromoMerchantList()
    }

}