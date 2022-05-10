package com.tokopedia.topchat.chatroom.view.uimodel.product_bundling

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingData
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class ProductBundlingUiModel constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatTypeFactory>, DeferredAttachment {

    var productBundling: ProductBundlingData = builder.productBundling
        private set

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String = attachmentId

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun updateData(attribute: Any?) {
        if (attribute is ProductBundlingData) {
            productBundling = attribute
        }
    }

    override fun syncError() {
        this.isLoading = false
        this.isError = true
    }

    override fun finishLoading() {
        this.isLoading = false
        this.isError = false
    }

    init {
        if (!builder.needSync) {
            finishLoading()
        }
    }

    open class Builder : SendableUiModel.Builder<Builder, ProductBundlingUiModel>() {

        internal var productBundling: ProductBundlingData = ProductBundlingData()
        internal var needSync: Boolean = true

        override fun build(): ProductBundlingUiModel {
            return ProductBundlingUiModel(this)
        }

        fun withProductBundling(productBundling: ProductBundlingData): Builder {
            this.productBundling = productBundling
            return self()
        }

        fun withNeedSync(needSync: Boolean): Builder {
            this.needSync = needSync
            return self()
        }
    }

    companion object {
        const val BUNDLE_TYPE_MULTIPLE = 2
    }
}