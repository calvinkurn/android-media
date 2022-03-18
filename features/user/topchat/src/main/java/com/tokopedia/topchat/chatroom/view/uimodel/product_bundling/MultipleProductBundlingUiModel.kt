package com.tokopedia.topchat.chatroom.view.uimodel.product_bundling

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingData
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingPojo
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class MultipleProductBundlingUiModel constructor(
    private val builder: Builder
) : SendableUiModel(builder), Visitable<TopChatTypeFactory>, DeferredAttachment {

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String = attachmentId

    var listBundling: ArrayList<ProductBundlingUiModel> = builder.listProductBundling
        private set

    init {
        if (!builder.needSync) {
            finishLoading()
        }
    }

    override fun updateData(attribute: Any?) {
        if (attribute is ProductBundlingPojo) {
            val result = builder.mapToListProductBundling(attribute.listProductBundling)
            this.listBundling.addAll(result)
        }
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

    open class Builder : SendableUiModel.Builder<Builder, MultipleProductBundlingUiModel>() {
        internal var listProductBundling: ArrayList<ProductBundlingUiModel> = arrayListOf()
        internal var needSync: Boolean = false

        override fun build(): MultipleProductBundlingUiModel {
            return MultipleProductBundlingUiModel(this)
        }

        fun withProductBundlingResponse(listProductBundling: List<ProductBundlingData>): Builder {
            val result = mapToListProductBundling(listProductBundling)
            this.listProductBundling.addAll(result)
            return self()
        }

        fun withNeedSync(needSync: Boolean): Builder {
            this.needSync = needSync
            return self()
        }

        fun mapToListProductBundling(listProductBundling: List<ProductBundlingData>): List<ProductBundlingUiModel> {
            val listResult = arrayListOf<ProductBundlingUiModel>()
            for (i in listProductBundling.indices) {
                val productBundling = ProductBundlingUiModel.Builder()
                    .withProductBundling(listProductBundling[i])
                    .build()
                listResult.add(productBundling)
            }
            return listResult
        }
    }
}