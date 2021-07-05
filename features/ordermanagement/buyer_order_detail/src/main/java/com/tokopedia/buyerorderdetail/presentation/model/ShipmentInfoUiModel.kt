package com.tokopedia.buyerorderdetail.presentation.model

import android.text.Spannable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class ShipmentInfoUiModel(
        val awbInfoUiModel: AwbInfoUiModel,
        val courierDriverInfoUiModel: CourierDriverInfoUiModel,
        val courierInfoUiModel: CourierInfoUiModel,
        val dropShipperInfoUiModel: CopyableKeyValueUiModel,
        val headerUiModel: PlainHeaderUiModel,
        val receiverAddressInfoUiModel: CopyableKeyValueUiModel,
        val ticker: TickerUiModel
) {

    data class AwbInfoUiModel(
            val orderId: String,
            val orderStatusId: String,
            override val copyMessage: String,
            override val copyableText: Spannable,
            override val label: String,
            override val copyLabel: String
    ) : CopyableKeyValueUiModel() {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    data class CourierInfoUiModel(
            val arrivalEstimation: String,
            val courierNameAndProductName: String,
            val isFreeShipping: Boolean,
            val boBadgeUrl: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    data class CourierDriverInfoUiModel(
            val name: String,
            val phoneNumber: String,
            val photoUrl: String,
            val plateNumber: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }
}