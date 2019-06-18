package com.tokopedia.kol.feature.post.view.viewmodel

import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Share

/**
 * @author by nisie on 28/03/19.
 */
data class PostDetailFooterModel(
        var contentId: Int = 0,
        var isLiked: Boolean = false,
        var totalLike: Int = 0,
        var totalComment: Int = 0,
        var shareData : Share = Share()
) {

}