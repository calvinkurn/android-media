package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 03/08/20
 */
class LikeCountViewComponent(
    container: ViewGroup,
) : ViewComponent(container, R.id.view_like_count) {

    private val tvTotalLikes = rootView as Typography

    fun setTotalLikes(totalLikes: String) {
        tvTotalLikes.text = totalLikes
    }
}