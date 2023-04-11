package com.tokopedia.topchat.chatroom.view.uimodel.product_bundling

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingData
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingPojo
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class ProductBundlingUiModel constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatTypeFactory>, DeferredAttachment {

    var productBundling: ProductBundlingData = builder.productBundling
        private set

    fun isBroadcast(): Boolean {
        return blastId != NOT_BROADCAST
    }

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String = attachmentId

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun updateData(attribute: Any?) {
        if (attribute is ProductBundlingPojo) {
            attribute.listProductBundling.forEach {
                if (it.bundleId == productBundling.bundleId) {
                    productBundling = it
                    return@forEach
                }
            }
        }
        this.isLoading = false
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

    fun getBundleTypeMapped(): String {
        if (productBundling.bundleType != null) {
            return when (productBundling.bundleType) {
                BUNDLE_TYPE_SINGLE -> "single"
                else -> "multiple"
            }
        }
        return ""
    }

    companion object {
        const val BUNDLE_TYPE_MULTIPLE = 2
        private const val BUNDLE_TYPE_SINGLE = 1
        private const val NOT_BROADCAST = "0"
    }
}