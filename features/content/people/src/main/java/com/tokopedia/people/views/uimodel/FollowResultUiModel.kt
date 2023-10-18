package com.tokopedia.people.views.uimodel

/**
 * Created by meyta.taliti on 07/03/23.
 */
sealed class FollowResultUiModel {
    data class Success(val message: String): FollowResultUiModel()
    data class Fail(
        val message: String,
        val isFollowed: Boolean,
        val itemPosition: Int
    ): FollowResultUiModel()
}
