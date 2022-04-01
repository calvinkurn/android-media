package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.BuyerOrderDetailTypeFactory
import com.tokopedia.buyerorderdetail.presentation.coachmark.BuyerOrderDetailCoachMarkItemManager
import com.tokopedia.buyerorderdetail.presentation.coachmark.DriverTippingCoachMarkManager
import com.tokopedia.kotlin.extensions.view.orZero

data class ShipmentInfoUiModel(
        val awbInfoUiModel: AwbInfoUiModel,
        val courierDriverInfoUiModel: CourierDriverInfoUiModel,
        val driverTippingInfoUiModel: DriverTippingInfoUiModel,
        val courierInfoUiModel: CourierInfoUiModel,
        val dropShipperInfoUiModel: CopyableKeyValueUiModel,
        val headerUiModel: PlainHeaderUiModel,
        val receiverAddressInfoUiModel: CopyableKeyValueUiModel,
        val ticker: TickerUiModel
) {

    data class AwbInfoUiModel(
        val orderId: String,
        val orderStatusId: String,
        override val copyMessage: StringRes,
        override val copyableText: String,
        override val label: StringRes,
        override val copyLabel: StringRes
    ) : CopyableKeyValueUiModel() {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }
    }

    data class CourierInfoUiModel(
            val arrivalEstimation: String,
            val courierNameAndProductName: String,
            val isFreeShipping: Boolean,
            val boBadgeUrl: String,
            val etaChanged: Boolean,
            val etaUserInfo: String
    ) : BaseVisitableUiModel {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        override fun shouldShow(context: Context?): Boolean {
            return courierNameAndProductName.isNotBlank()
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }
    }

    data class CourierDriverInfoUiModel(
            val name: String,
            val phoneNumber: String,
            val photoUrl: String,
            val plateNumber: String
    ) : BaseVisitableUiModel {
        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        override fun shouldShow(context: Context?): Boolean {
            return name.isNotBlank()
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return null
        }
    }

    data class DriverTippingInfoUiModel(
        val imageUrl: String,
        val title: String,
        val description: String
    ): BaseVisitableUiModel {
        override fun shouldShow(context: Context?): Boolean {
            return imageUrl.isNotBlank() && title.isNotBlank() && description.isNotBlank()
        }

        override fun type(typeFactory: BuyerOrderDetailTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }

        override fun getCoachMarkItemManager(): BuyerOrderDetailCoachMarkItemManager? {
            return DriverTippingCoachMarkManager(uiModel = this)
        }
    }
}