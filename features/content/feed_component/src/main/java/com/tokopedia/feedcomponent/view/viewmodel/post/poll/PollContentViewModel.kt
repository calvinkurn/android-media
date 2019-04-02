package com.tokopedia.feedcomponent.view.viewmodel.post.poll

import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel

/**
 * @author by milhamj on 10/12/18.
 */
data class PollContentViewModel (
        val pollId: String = "",
        var totalVoter: String = "",
        var totalVoterNumber: Int = 0,
        var voted: Boolean = false,
        val optionList: MutableList<PollContentOptionViewModel>,
        val trackingList: MutableList<TrackingViewModel> = ArrayList(),
        override var postId: Int = 0,
        override var positionInFeed: Int = 0
) : BasePostViewModel