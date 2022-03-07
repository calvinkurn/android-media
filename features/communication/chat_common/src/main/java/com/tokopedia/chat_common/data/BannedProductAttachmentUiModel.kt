package com.tokopedia.chat_common.data

import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

class BannedProductAttachmentUiModel(
    builder: ProductAttachmentUiModel.Builder
) : ProductAttachmentUiModel(builder) {

    val liteUrl get() = playStoreData.redirectUrl

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getBannedWarningMessage(): String {
        return playStoreData.message
    }

    class Builder : ProductAttachmentUiModel.Builder() {
        override fun build(): ProductAttachmentUiModel {
            return BannedProductAttachmentUiModel(this)
        }
    }
}