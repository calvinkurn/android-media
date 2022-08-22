package com.tokopedia.createpost.view.listener

import com.tokopedia.createpost.view.viewmodel.HeaderViewModel
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.content.common.ui.model.ContentAccountUiModel

interface CreateContentPostCommonListener {
    fun deleteItemFromProductTagList(
        position: Int,
        productId: String,
        isDeletedFromBubble: Boolean,
        mediaType: String,
    )
    fun setFeedAccountList(contentAccountList: List<ContentAccountUiModel>)
    fun openProductTaggingPageOnPreviewMediaClick(position: Int)
    fun clickProductTagBubbleAnalytics(mediaType: String, productId: String)
    fun updateTaggingInfoInViewModel(
        feedXMediaTagging: FeedXMediaTagging
    )
    fun clickContinueOnTaggingPage()
    fun postFeed()
}