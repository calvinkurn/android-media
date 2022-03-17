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

    override fun updateData(attribute: Any?) {
        if (attribute is ProductBundlingPojo) {
            val result = builder.mapToSingleProductBundling(attribute.listProductBundling)
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

        override fun build(): MultipleProductBundlingUiModel {
            return MultipleProductBundlingUiModel(this)
        }

        fun withProductBundlingResponse(listProductBundling: List<ProductBundlingData>): Builder {
            val result = mapToSingleProductBundling(listProductBundling)
            this.listProductBundling.addAll(result)
            return self()
        }

        fun mapToSingleProductBundling(
            listProductBundling: List<ProductBundlingData>
        ): List<ProductBundlingUiModel> {
            val listResult = arrayListOf<ProductBundlingUiModel>()
            for (i in listProductBundling.indices) {
                val singleProductBundling = ProductBundlingUiModel.Builder()
                    .withProductBundling(listProductBundling[i])
                    .build()
                listResult.add(singleProductBundling)
            }
            return listResult
        }
    }
}