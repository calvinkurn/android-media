package com.tokopedia.topchat.chatroom.domain.pojo.param

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.chat_common.data.ProductAttachmentUiModel

data class AddToCartParam(
    var productId: String = "",
    var productName: String = "",
    var shopId: String = "0",
    var category: String = "",
    var minOrder: Int = 0,
    var price: Double = 0.0,
    var productImage: String = "",
    var freeShipping: Boolean = false,
    var productUrl: String = "",
    var blastId: String = "0",
    var source: String = "",
    var action: String = "",
    var dataModel: DataModel? = null
) {
    fun getAtcDimension40(sourcePage: String): String {
        return when (sourcePage) {
            ApplinkConst.Chat.SOURCE_CHAT_SEARCH -> "search chat"
            else -> getField()
        }
    }

    private fun getField(): String {
        return if (blastId != "0") {
            FIELD_BC
        } else if (source.contains("smart", true) &&
            source.contains("reply", true)
        ) {
            // If source is smart reply in any form, return smart_reply
            FIELD_SMART_REPLY
        } else {
            FIELD_CHAT
        }
    }

    fun getAtcEventAction(): String {
        return EVENT_ACTION_ATC
    }

    fun getBuyEventAction(): String {
        return EVENT_ACTION_BUY
    }

    companion object {
        const val ACTION_ATC = "atc"
        const val ACTION_BUY = "buy"
        const val EVENT_ACTION_ATC = "click atc on product thumbnail"
        const val EVENT_ACTION_BUY = "click buy on product thumbnail"
        private const val FIELD_BC = "/broadcast"
        private const val FIELD_CHAT = "/chat"
        private const val FIELD_SMART_REPLY = "/smart_reply"

        fun mapUiModelToParam(uiModel: ProductAttachmentUiModel, action: String): AddToCartParam {
            return AddToCartParam(
                productId = uiModel.productId,
                productName = uiModel.productName,
                shopId = uiModel.shopId,
                category = uiModel.category,
                minOrder = uiModel.minOrder,
                price = uiModel.priceNumber,
                productImage = uiModel.productImage,
                freeShipping = uiModel.hasFreeShipping(),
                productUrl = uiModel.productUrl,
                blastId = uiModel.blastId,
                source = uiModel.source,
                action = action
            )
        }
    }
}
