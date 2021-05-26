package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class ShipmentInfoUiModel(
        val awbInfoUiModel: AwbInfoUiModel,
        val courierDriverInfoUiModel: CourierDriverInfoUiModel,
        val courierInfoUiModel: CourierInfoUiModel,
        val headerUiModel: PlainHeaderUiModel,
        val receiverAddressInfoUiModel: ReceiverAddressInfoUiModel,
        val ticker: TickerUiModel
) {
    data class ReceiverAddressInfoUiModel(
            val receiverAddress: String,
            val receiverName: String,
            val receiverPhoneNumber: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    data class AwbInfoUiModel(
            val awbNumber: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
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