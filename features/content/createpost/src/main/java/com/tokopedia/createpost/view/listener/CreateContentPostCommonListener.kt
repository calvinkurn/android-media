package com.tokopedia.createpost.view.listener

import com.tokopedia.content.common.ui.model.ContentAccountUiModel

interface CreateContentPostCommonListener {
    fun deleteItemFromProductTagList(
        position: Int,
        productId: String,
        isDeletedFromBubble: Boolean,
        mediaType: String,
    )
    fun setContentAccountList(contentAccountList: List<ContentAccountUiModel>)
    fun openProductTaggingPageOnPreviewMediaClick(position: Int)
    fun clickContinueOnTaggingPage()
    fun postFeed()
}
