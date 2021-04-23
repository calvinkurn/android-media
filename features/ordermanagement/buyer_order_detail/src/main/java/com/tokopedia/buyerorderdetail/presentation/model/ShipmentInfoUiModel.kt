package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero

data class ShipmentInfoUiModel(
        val headerUiModel: PlainHeaderUiModel,
        val courierInfoUiModel: CourierInfoUiModel,
        val ticker: TickerUiModel,
        val courierDriverInfoUiModel: CourierDriverInfoUiModel,
        val awbInfoUiModel: AwbInfoUiModel,
        val receiverReceiverAddressInfoUiModel: ReceiverAddressInfoUiModel
) {
    data class ReceiverAddressInfoUiModel(
            val receiverName: String,
            val receiverPhoneNumber: String,
            val receiverAddress: String,
            val receiverAddressNote: String
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
            val courierNameAndProductName: String,
            val isFreeShipping: Boolean,
            val arrivalEstimation: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }

    data class CourierDriverInfoUiModel(
            val photoUrl: String,
            val name: String,
            val phoneNumber: String,
            val plateNumber: String
    ) : Visitable<BuyerOrderDetailTypeFactory> {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }
}