package com.tokopedia.logisticCommon.data.model

import android.annotation.SuppressLint

data class CustomProductLogisticModel(
    var shipperList: List<ShipperListCPLModel> = listOf(),
    var shouldShowOnBoarding: Boolean = false
) {
    fun getActivatedSpIds(): List<Long> {
        val shipperProductIds = mutableListOf<Long>()
        shipperList.forEach { shipperGroup ->
            shipperGroup.shipper.filter { s -> s.isActive }.forEach { s ->
                shipperProductIds.addAll(s.shipperProduct.filter { sp -> sp.isActive }
                    .map { sp -> sp.shipperProductId })

            }
        }
        return shipperProductIds
    }
}

data class ShipperListCPLModel(
    var header: String = "",
    var description: String = "",
    var shipper: List<ShipperCPLModel> = listOf()
) {
    @SuppressLint("PII Data Exposure")
    fun getActiveServiceName(): String {
        val activeService = mutableListOf<String>()
        shipper.forEach { s ->
            s.shipperProduct.filter { it.isActive }.forEach { sp ->
                if (sp.shipperServiceName !in activeService) {
                    activeService.add(sp.shipperServiceName)
                }
            }
        }
        return activeService.joinToString()
    }
}

data class ShipperCPLModel(
    var shipperId: Long = 0,
    var shipperName: String = "",
    var logo: String = "",
    var description: String = "",
    var shipperProduct: List<ShipperProductCPLModel> = listOf(),
    var isActive: Boolean = false,
    var isWhitelabel: Boolean = false,
)

data class ShipperProductCPLModel(
    var shipperProductId: Long = 0,
    var shipperProductName: String = "",
    var uiHidden: Boolean = false,
    var isActive: Boolean = false,
    var shipperServiceName: String = "",
)

