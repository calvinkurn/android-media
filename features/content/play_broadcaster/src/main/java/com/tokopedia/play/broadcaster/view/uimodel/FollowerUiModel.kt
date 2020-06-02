package com.tokopedia.play.broadcaster.view.uimodel

import androidx.annotation.ColorRes

/**
 * Created by jegul on 20/05/20
 */
sealed class FollowerUiModel {
    data class Unknown(@ColorRes val colorRes: Int) : FollowerUiModel()
    data class User(val imageUrl: String?) : FollowerUiModel()
}