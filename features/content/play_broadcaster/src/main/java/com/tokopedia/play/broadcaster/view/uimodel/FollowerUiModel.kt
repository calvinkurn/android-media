package com.tokopedia.play.broadcaster.view.uimodel

/**
 * Created by jegul on 20/05/20
 */
sealed class FollowerUiModel {
    object Unknown : FollowerUiModel()
    data class User(val imageUrl: String?) : FollowerUiModel()
}