package com.tokopedia.topchat.chatroom.view.uimodel.product_bundling

import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingData

class ProductBundlingUiModel constructor(
    builder: Builder
) : SendableUiModel(builder), DeferredAttachment {

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String = attachmentId

    var productBundling: ProductBundlingData = builder.productBundling
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

    open class Builder : SendableUiModel.Builder<Builder, ProductBundlingUiModel>() {

        internal var productBundling: ProductBundlingData = ProductBundlingData()

        override fun build(): ProductBundlingUiModel {
            return ProductBundlingUiModel(this)
        }

        fun withProductBundling(productBundling: ProductBundlingData): Builder {
            this.productBundling = productBundling
            return self()
        }
    }
}