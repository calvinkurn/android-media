package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory

/**
 * Created By : Muhammad Furqan on 02/03/23
 */
data class FeedCardLivePreviewContentModel(
    val id: String,
    val author: FeedAuthorModel,
    val title: String,
    val subtitle: String,
    val text: String,
    val applink: String,
    val media: List<FeedMediaModel>,
    val hashtagApplinkFmt: String,
    val hashtagWeblinkFmt: String,
    val playChannelId: String,
) : Visitable<FeedAdapterTypeFactory> {
    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)
}
