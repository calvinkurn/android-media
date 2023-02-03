package com.tokopedia.kol.feature.postdetail.view.datamodel

import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Share

/**
 * @author by nisie on 28/03/19.
 */
data class PostDetailFooterModel(
        var contentId: String = "",
        var isLiked: Boolean = false,
        var totalLike: Int = 0,
        var totalComment: Int = 0,
        var shareData : Share = Share()
)
