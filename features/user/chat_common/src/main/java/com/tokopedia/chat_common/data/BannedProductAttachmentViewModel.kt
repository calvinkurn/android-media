package com.tokopedia.chat_common.data

import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

class BannedProductAttachmentViewModel(
    builder: ProductAttachmentViewModel.Builder
) : ProductAttachmentViewModel(builder) {

    val liteUrl get() = playStoreData.redirectUrl

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getBannedWarningMessage(): String {
        return playStoreData.message
    }

    class Builder : ProductAttachmentViewModel.Builder() {
        override fun build(): ProductAttachmentViewModel {
            return BannedProductAttachmentViewModel(this)
        }
    }
}