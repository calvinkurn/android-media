package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.pdplayout.Component
import com.tokopedia.product.detail.data.model.datamodel.DynamicPDPDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductShopInfoDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSnapshotDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSocialProofDataModel

object DynamicProductDetailMapper {

    fun mapIntoVisitable(data: List<Component>): List<DynamicPDPDataModel> {
        val listOfComponent: MutableList<DynamicPDPDataModel> = mutableListOf()
        data.forEachIndexed { index, component ->
            when (component.type) {
                "product_snapshot" -> {
                    listOfComponent.add(ProductSnapshotDataModel(dataLayout = component.componentData, type = component.type))
                }
//                "shop_voucher" -> {
//                    listOfComponent.add(ProductShopVoucherDataModel(dataLayout = component.componentData))
//                }
//                "shipping_info" -> {
//                    listOfComponent.add(ProductShippingInfoDataModel(dataLayout = component.componentData))
//                }
                "shop_info" -> {
                    listOfComponent.add(ProductShopInfoDataModel(dataLayout = component.componentData, type = component.type))
                }
                "social_proof" -> {
                    listOfComponent.add(ProductSocialProofDataModel(dataLayout = component.componentData, type = component.type))
                }


            }
        }
        return listOfComponent
    }

    fun hashMapLayout(data: List<DynamicPDPDataModel>): Map<String, DynamicPDPDataModel> {
        return data.associateBy({
            it.type()
        }, {
            it
        })
    }
}