package com.tokopedia.feedcomponent.view.viewmodel.post.image

import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel

/**
 * @author by milhamj on 04/12/18.
 */
data class ImagePostViewModel (
        val image: String = "",
        val redirectLink: String = "",
        override var postId: Int = 0,
        override var positionInFeed: Int = 0
) : BasePostViewModel