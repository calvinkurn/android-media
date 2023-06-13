package com.tokopedia.feedcomponent.view.viewmodel.post.video

import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostModel
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * @author by yfsx on 20/03/19.
 */
data class VideoModel(
        var id: String = "",
        var thumbnail: String = "",
        var url: String = "",
        var canPlayVideo: Boolean = false,
        var redirectLink: String = "",
        override var postId: String = "0",
        override var positionInFeed: Int = 0
) : ImpressHolder(), BasePostModel {
}
