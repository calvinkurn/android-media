package com.tokopedia.feedplus.detail

import androidx.annotation.StringRes

/**
 * Created by meyta.taliti on 26/09/23.
 */
interface FeedDetailBottomBarActionListener {

    fun setBottomBarAction(
        @StringRes stringRes: Int,
        onClickListener: () -> Unit
    )

    fun hideBottomBar()
}
