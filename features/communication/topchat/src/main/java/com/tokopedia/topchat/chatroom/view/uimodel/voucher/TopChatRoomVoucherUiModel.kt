package com.tokopedia.topchat.chatroom.view.uimodel.voucher

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherAmount
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherBanner
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherModel
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherOwner
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherStatus
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherType
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.promousage.domain.entity.list.PromoSimpleItem
import com.tokopedia.topchat.chatroom.domain.pojo.voucher.TopChatRoomVoucherAttachmentDto
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.TopChatRoomTypeFactory
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TopChatRoomVoucherUiModel private constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatRoomTypeFactory> {

    private val isPublic: Int = builder.isPublic
    private val isLockToProduct: Int = builder.isLockToProduct
    private val merchantVoucherModel: MerchantVoucherModel = builder.merchantVoucherModel
    val voucher: MerchantVoucherViewModel = MerchantVoucherViewModel(merchantVoucherModel).apply {
        this.isPublic = isPublic()
        this.isLockToProduct = isLockToProduct()
    }

    private fun isPublic(): Boolean {
        return isPublic == 1
    }

    fun isLockToProduct(): Boolean {
        return isLockToProduct == 1
    }

    // New Voucher Section
    val appLink: String = builder.appLink
    val header: String = builder.header
    val description: String = builder.description
    val voucherUi: PromoSimpleItem? = builder.voucherUi
    val impressHolder = ImpressHolder()

    override fun type(typeFactory: TopChatRoomTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Builder : SendableUiModel.Builder<Builder, TopChatRoomVoucherUiModel>() {

        internal var merchantVoucherModel = MerchantVoucherModel(0, merchantVoucherOwner = MerchantVoucherOwner())
        internal var isPublic: Int = 1
        internal var isLockToProduct: Int = 0
        internal var appLink: String = ""
        internal var voucherUi: PromoSimpleItem? = null
        internal var header: String = ""
        internal var description: String = ""

        fun withVoucherData(voucherDataDto: TopChatRoomVoucherAttachmentDto) = apply {
            val voucherType = MerchantVoucherType(voucherDataDto.voucherType, "")
            val voucherAmount = MerchantVoucherAmount(voucherDataDto.amountType, voucherDataDto.amount)
            val voucherOwner = MerchantVoucherOwner(
                identifier = voucherDataDto.identifier,
                ownerId = voucherDataDto.ownerId.toIntOrZero()
            )
            val voucherBanner = MerchantVoucherBanner(mobileUrl = voucherDataDto.mobileUrl)
            val merchantVoucherModel = MerchantVoucherModel(
                voucherId = voucherDataDto.voucherId.toIntOrZero(),
                voucherName = voucherDataDto.voucherName,
                voucherCode = voucherDataDto.voucherCode,
                merchantVoucherType = voucherType,
                merchantVoucherAmount = voucherAmount,
                minimumSpend = voucherDataDto.minimumSpend.toIntOrZero(),
                merchantVoucherOwner = voucherOwner,
                validThru = voucherDataDto.validThru.toString(),
                tnc = voucherDataDto.tnc,
                merchantVoucherBanner = voucherBanner,
                merchantVoucherStatus = MerchantVoucherStatus()
            )
            this.merchantVoucherModel = merchantVoucherModel
            this.isPublic = voucherDataDto.isPublic
            this.isLockToProduct = voucherDataDto.isLockToProduct.orZero()
            return self()
        }

        fun withAppLink(appLink: String): Builder {
            this.appLink = appLink
            return self()
        }

        fun withVoucherUi(displayDto: TopChatRoomVoucherAttachmentDto) = apply {
            this.header = displayDto.voucherHeader
            this.description = displayDto.voucherDescription
            this.voucherUi = PromoSimpleItem(
                title = displayDto.voucherAmountString,
                type = displayDto.voucherTypeString,
                typeColor = displayDto.voucherTypeColor,
                typeColorDark = displayDto.voucherTypeColorDark,
                desc = displayDto.voucherMinimumString,
                backgroundUrl = displayDto.voucherBackgroundUrl,
                backgroundUrlDark = displayDto.voucherBackgroundUrl,
                iconUrl = displayDto.voucherIconUrl,
                iconUrlDark = displayDto.voucherIconUrl,
                curveColor = unifyprinciplesR.color.Unify_NN50,
                curveColorDark = unifyprinciplesR.color.Unify_NN0,
                curveAlpha = 128,
                curveAlphaDark = 200
            )
        }

        fun withMerchantVoucherModel(
            merchantVoucherModel: MerchantVoucherModel,
            isLockToProduct: Int,
            isPublic: Int
        ): Builder {
            this.merchantVoucherModel = merchantVoucherModel
            this.isLockToProduct = isLockToProduct
            this.isPublic = isPublic
            return self()
        }

        override fun build(): TopChatRoomVoucherUiModel {
            return TopChatRoomVoucherUiModel(this)
        }
    }
}
