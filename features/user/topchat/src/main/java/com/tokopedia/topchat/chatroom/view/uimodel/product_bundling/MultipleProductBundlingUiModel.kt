package com.tokopedia.topchat.chatroom.view.uimodel.product_bundling

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingData
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class MultipleProductBundlingUiModel constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatTypeFactory> {

    var listBundling: ArrayList<ProductBundlingUiModel> = builder.listProductBundling
        private set

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    open class Builder : SendableUiModel.Builder<Builder, MultipleProductBundlingUiModel>() {
        internal var listProductBundling: ArrayList<ProductBundlingUiModel> = arrayListOf()

        override fun build(): MultipleProductBundlingUiModel {
            return MultipleProductBundlingUiModel(this)
        }

        fun withProductBundlingResponse(listProductBundling: List<ProductBundlingData>): Builder {
            val result = mapToListProductBundling(listProductBundling)
            this.listProductBundling.addAll(result)
            return self()
        }

        private fun mapToListProductBundling(
            listProductBundling: List<ProductBundlingData>
        ): List<ProductBundlingUiModel> {
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