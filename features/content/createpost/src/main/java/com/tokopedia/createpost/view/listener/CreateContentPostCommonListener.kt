package com.tokopedia.createpost.view.listener

import com.tokopedia.createpost.view.viewmodel.HeaderViewModel
import com.tokopedia.createpost.common.data.feedrevamp.FeedXMediaTagging
import com.tokopedia.imagepicker_insta.common.ui.model.FeedAccountUiModel

interface CreateContentPostCommonListener {
    fun deleteItemFromProductTagList(
        position: Int,
        productId: String,
        isDeletedFromBubble: Boolean,
        mediaType: String,
    )
    fun setFeedAccountList(feedAccountList: List<FeedAccountUiModel>)
    fun openProductTaggingPageOnPreviewMediaClick(position: Int)
    fun clickProductTagBubbleAnalytics(mediaType: String, productId: String)
    fun updateTaggingInfoInViewModel(
        feedXMediaTagging: FeedXMediaTagging
    )
    fun clickContinueOnTaggingPage()
    fun postFeed()
}