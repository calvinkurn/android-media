package com.tokopedia.feedcomponent.view.viewmodel.post.poll

import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel

/**
 * @author by milhamj on 10/12/18.
 */
data class PollContentModel (
    val pollId: String = "",
    var totalVoter: String = "",
    var totalVoterNumber: Int = 0,
    var voted: Boolean = false,
    val optionList: MutableList<PollContentOptionModel>,
    val trackingList: MutableList<TrackingModel> = ArrayList(),
    override var postId: String = "0",
    override var positionInFeed: Int = 0
) : BasePostModel
