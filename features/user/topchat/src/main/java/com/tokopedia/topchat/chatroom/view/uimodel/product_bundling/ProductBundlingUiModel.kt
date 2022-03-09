package com.tokopedia.topchat.chatroom.view.uimodel.product_bundling

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class ProductBundlingUiModel protected constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatTypeFactory>, DeferredAttachment {

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String = attachmentId

    var bundlingId: String = builder.bundlingId
        private set
    var bundlingName: String = builder.bundlingName
        private set
    var labelDesc: String = builder.labelDesc
        private set
    var discountText: String = builder.discountText
        private set
    var discountPrice: String = builder.discountPrice
        private set
    var discountAmount: String = builder.discountAmount
        private set
    var productPrice: String = builder.productPrice
        private set
    var imageUrl: String = builder.imageUrl
        private set
    var applink: String = builder.applink
        private set

    override fun updateData(attribute: Any?) {
        TODO("Not yet implemented")
    }

    override fun syncError() {
        isLoading = false
        isError = true
    }

    override fun finishLoading() {
        isLoading = false
        isError = false
    }

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    open class Builder : SendableUiModel.Builder<Builder, ProductBundlingUiModel>() {
        internal var bundlingId: String = "0"
        internal var bundlingName: String = ""
        internal var labelDesc: String = ""
        internal var discountText: String = ""
        internal var discountPrice: String = ""
        internal var discountAmount: String = ""
        internal var productPrice: String = ""
        internal var imageUrl: String = ""
        internal var applink: String = ""

        override fun build(): ProductBundlingUiModel {
            return ProductBundlingUiModel(this)
        }

        fun withProductBundlingResponse(): Builder {
            //TODO Here
            return self()
        }

        fun withBundlingId(bundlingId: String): Builder {
            this.bundlingId = bundlingId
            return self()
        }

        fun withProductName(productName: String): Builder {
            this.bundlingName = productName
            return self()
        }

        fun withLabelDesc(labelDesc: String): Builder {
            this.labelDesc = labelDesc
            return self()
        }

        fun withDiscountText(discountText: String): Builder {
            this.discountText = discountText
            return self()
        }

        fun withDiscountPrice(discountPrice: String): Builder {
            this.discountPrice = discountPrice
            return self()
        }

        fun withDiscountAmount(discountAmount: String): Builder {
            this.discountAmount = discountAmount
            return self()
        }

        fun withProductPrice(productPrice: String): Builder {
            this.productPrice = productPrice
            return self()
        }

        fun withImageUrl(imageUrl: String): Builder {
            this.imageUrl = imageUrl
            return self()
        }

        fun withApplink(applink: String): Builder {
            this.applink = applink
            return self()
        }
    }
}