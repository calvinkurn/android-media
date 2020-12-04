package com.tokopedia.shop.extension

import com.tokopedia.shop.common.data.model.ShopShipmentData
import com.tokopedia.shop.info.view.model.ShopInfoLogisticUiModel
import java.text.DecimalFormat

const val THOUSAND = 1000
const val MILLION = 1000000
val df = DecimalFormat("###.##")

fun ShopShipmentData.transformToVisitable(): ShopInfoLogisticUiModel {
    return ShopInfoLogisticUiModel().also {
        it.shipmentImage = this.image
        it.shipmentName = this.name
        it.shipmentPackage = this.product
                .map { shipmentPackage -> shipmentPackage.name }
                .joinToString(separator = ", ")
    }
}

fun Double.formatToSimpleNumber():String {
    return when {
        containsIn(this, THOUSAND, MILLION) -> {
            "${df.format(this/THOUSAND)}rb"
        }
        containsIn(this, MILLION, Int.MAX_VALUE) -> {
            "${df.format(this/ MILLION)}jt"
        }
        else -> {
            df.format(this)
        }
    }.replace(".",",")
}

private fun containsIn(value: Double, start: Int, end: Int): Boolean {
    return value.toIntExactOrNull().let { if (it != null) contains(it, start, end) else false }
}

private fun Double.toIntExactOrNull(): Int? {
    return if (this in Int.MIN_VALUE.toDouble()..Int.MAX_VALUE.toDouble()) this.toInt() else null
}

private fun contains(value: Int, start: Int, end: Int): Boolean = value in start..end