package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.purchase_platform.common.utils.Utils
import javax.inject.Inject


class RatesResponseStateConverter @Inject constructor() {

    fun fillState(response: ShippingRecommendationData, shopShipments: List<ShopShipment>,
                  selectedSpId: Int, selectedServiceId: Int): ShippingRecommendationData {
        val isPromoApplied = isPromoStackingApplied(response.logisticPromo)
        response.shippingDurationViewModels?.forEach { duration ->
            duration.shippingCourierViewModelList?.forEach { courier ->
                if (selectedSpId != 0 && !isPromoApplied) {
                    if (selectedSpId == courier.productData.shipperProductId) {
                        courier.isSelected = true
                        duration.isSelected = true;
                    }
                } else if (selectedServiceId != 0 && !isPromoApplied) {
                    if (!(duration.serviceData.error != null && !(duration.serviceData.error.errorId).isNullOrBlank()) &&
                            selectedServiceId == duration.serviceData.serviceId) {
                        duration.isSelected = true;
                    }
                } else {
                    courier.isSelected = courier.productData.isRecommend;
                    duration.isSelected = false;
                }
                courier.additionalFee = getAdditionalFee(courier.productData, shopShipments)
                courier.isAllowDropshipper = isAllowDropshipper(courier.productData, shopShipments)
            }
        }
        return response
    }

    private fun getAdditionalFee(productData: ProductData, shopShipmentList: List<ShopShipment>)
            : Int {
        for (shopShipment in shopShipmentList) {
            if (shopShipment.shipProds != null) {
                for (shipProd in shopShipment.shipProds) {
                    if (shipProd.shipProdId == productData.shipperProductId) {
                        return shipProd.additionalFee
                    }
                }
            }
        }
        return 0
    }

    private fun isAllowDropshipper(productData: ProductData, shopShipmentList: List<ShopShipment>)
            : Boolean {
        for (shopShipment in shopShipmentList) {
            if (shopShipment.shipId == productData.shipperId) {
                return shopShipment.isDropshipEnabled
            }
        }
        return false
    }

    private fun isPromoStackingApplied(logisticPromo: LogisticPromoUiModel?): Boolean =
            when (logisticPromo) {
                null -> false
                else -> logisticPromo.isApplied
            }

}