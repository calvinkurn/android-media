package com.tokopedia.shop.extension

import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoShipment
import com.tokopedia.shop.info.view.model.ShopInfoLogisticViewModel
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote
import com.tokopedia.shop.note.view.model.ShopNoteViewModel

const val THOUSAND = 1000
const val MILLION = 1000000

fun ShopInfoShipment.transformToVisitable(): ShopInfoLogisticViewModel {
    return ShopInfoLogisticViewModel().also {
        it.shipmentImage = this.shipmentImage
        it.shipmentName = this.shipmentName
        it.shipmentPackage = this.shipmentPackage
                .mapNotNull { shipmentPackage -> shipmentPackage.productName }
                .joinToString(separator = ", ")
    }
}

fun ShopNote.transformToVisitable():ShopNoteViewModel {
    return ShopNoteViewModel().also {
        it.shopNoteId = this.shopNoteId
        it.title = this.title
        it.lastUpdate = this.lastUpdate
        it.position = this.position
    }
}

fun Long.formatToSimpleNumber():String {
    return when(this) {
        in THOUSAND..MILLION-1 -> "${this/THOUSAND}rb"
        in MILLION..Long.MAX_VALUE -> "${this/ MILLION}jt"
        else -> this.toString()
    }
}