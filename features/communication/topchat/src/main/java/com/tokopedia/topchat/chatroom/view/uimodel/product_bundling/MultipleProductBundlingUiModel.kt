package com.tokopedia.topchat.chatroom.view.uimodel.product_bundling

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingData
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingPojo
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class MultipleProductBundlingUiModel constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatTypeFactory>, DeferredAttachment {

    var listBundling: ArrayList<ProductBundlingUiModel> = builder.listProductBundling
        private set

    override var isLoading: Boolean = true
    override var isError: Boolean = false
    override val id: String = attachmentId

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun updateData(attribute: Any?) {
        if (attribute is ProductBundlingPojo) {
            listBundling.forEach {
                it.updateData(attribute)
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

    open class Builder : SendableUiModel.Builder<Builder, MultipleProductBundlingUiModel>() {

        internal var listProductBundling: ArrayList<ProductBundlingUiModel> = arrayListOf()
        internal var needSync: Boolean = true
        private var chatPojo: ChatSocketPojo? = null
        private var reply: Reply? = null

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

        override fun withResponseFromWs(reply: ChatSocketPojo): Builder {
            this.chatPojo = reply
            return super.withResponseFromWs(reply)
        }

        override fun withResponseFromGQL(reply: Reply): Builder {
            this.reply = reply
            return super.withResponseFromGQL(reply)
        }

        private fun mapToListProductBundling(
            listProductBundling: List<ProductBundlingData>
        ): List<ProductBundlingUiModel> {
            val listResult = arrayListOf<ProductBundlingUiModel>()
            for (i in listProductBundling.indices) {
                val productBundlingBuilder = ProductBundlingUiModel.Builder()
                    .withIsSender(isSender)
                    .withProductBundling(listProductBundling[i])
                    .withNeedSync(needSync)
                chatPojo?.let {
                    productBundlingBuilder.withResponseFromWs(it)
                }
                reply?.let {
                    productBundlingBuilder.withResponseFromGQL(it)
                }
                val productBundling = productBundlingBuilder.build()
                listResult.add(productBundling)
            }
            return listResult
        }
    }
}