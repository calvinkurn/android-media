package com.tokopedia.shop.info.view.mapper

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoShipment
import com.tokopedia.shop.info.view.model.ShopInfoLogisticUiModel
import java.util.*

/**
 * Created by alvarisi on 12/14/17.
 */
class ShopInfoLogisticViewModelMapper {
    fun transform(shopInfoShipmentList: List<ShopInfoShipment>): List<Visitable<*>> {
        val visitableList: MutableList<Visitable<*>> = ArrayList()
        for (shopInfoShipment in shopInfoShipmentList) {
            val logisticViewModel = ShopInfoLogisticUiModel()
            logisticViewModel.shipmentImage = shopInfoShipment.shipmentImage
            logisticViewModel.shipmentName = shopInfoShipment.shipmentName
            var packagName = ""
            for (shipmentPackage in shopInfoShipment.shipmentPackage) {
                if (!TextUtils.isEmpty(packagName)) {
                    packagName += ", "
                }
                packagName += shipmentPackage.productName
            }
            logisticViewModel.shipmentPackage = packagName
            visitableList.add(logisticViewModel)
        }
        return visitableList
    }
}