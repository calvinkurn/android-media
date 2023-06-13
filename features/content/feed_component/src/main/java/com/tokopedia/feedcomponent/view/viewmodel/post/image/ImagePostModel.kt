package com.tokopedia.feedcomponent.view.viewmodel.post.image

import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel

/**
 * @author by milhamj on 04/12/18.
 */
data class ImagePostModel (
    val image: String = "",
    val redirectLink: String = "",
    val trackingList: MutableList<TrackingModel> = ArrayList(),
    override var postId: String = "0",
    override var positionInFeed: Int = 0
) : BasePostModel
