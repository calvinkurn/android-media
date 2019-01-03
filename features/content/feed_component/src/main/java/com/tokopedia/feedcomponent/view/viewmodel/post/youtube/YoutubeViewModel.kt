package com.tokopedia.feedcomponent.view.viewmodel.post.youtube

import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel

/**
 * @author by milhamj on 14/12/18.
 */
data class YoutubeViewModel (
        val youtubeId: String = " ",
        override var rowNumber: Int = 0
) : BasePostViewModel