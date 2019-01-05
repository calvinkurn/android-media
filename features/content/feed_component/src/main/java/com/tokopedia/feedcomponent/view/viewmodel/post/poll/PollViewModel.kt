package com.tokopedia.feedcomponent.view.viewmodel.post.poll

import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel

/**
 * @author by milhamj on 10/12/18.
 */
data class PollViewModel (
        val pollId: String = "",
        val totalVoter: String = "",
        val voted: Boolean = false,
        val optionList: MutableList<PollOptionViewModel>,
        override var postId: Int = 0,
        override var positionInFeed: Int = 0
) : BasePostViewModel