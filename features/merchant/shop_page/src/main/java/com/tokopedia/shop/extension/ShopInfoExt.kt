package com.tokopedia.shop.extension

import com.tokopedia.shop.common.graphql.data.shopinfo.ShopShipment
import com.tokopedia.shop.info.view.model.ShopInfoLogisticViewModel
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote
import com.tokopedia.shop.note.view.model.ShopNoteViewModel
import java.text.DecimalFormat

const val THOUSAND = 1000
const val MILLION = 1000000
val df = DecimalFormat("###.##")

fun ShopShipment.transformToVisitable(): ShopInfoLogisticViewModel {
    return ShopInfoLogisticViewModel().also {
        it.shipmentImage = this.image
        it.shipmentName = this.name
        it.shipmentPackage = this.product
                .map { shipmentPackage -> shipmentPackage.name }
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

fun Double.formatToSimpleNumber():String {
    return when(this) {
        in THOUSAND until MILLION -> "${df.format(this/THOUSAND)}rb"
        in MILLION..Long.MAX_VALUE -> "${df.format(this/ MILLION)}jt"
        else -> df.format(this)
    }.replace(".",",")
}