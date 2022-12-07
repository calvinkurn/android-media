package com.tokopedia.feedcomponent.view.viewmodel.post.youtube

import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel

/**
 * @author by milhamj on 14/12/18.
 */
data class YoutubeModel (
    val youtubeId: String = " ",
    val trackingList: MutableList<TrackingModel> = ArrayList(),
    override var postId: String = "0",
    override var positionInFeed: Int = 0
) : BasePostModel
