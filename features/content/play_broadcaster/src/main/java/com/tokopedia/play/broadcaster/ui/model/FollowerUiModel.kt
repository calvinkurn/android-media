package com.tokopedia.play.broadcaster.ui.model

import androidx.annotation.ColorRes
import com.tokopedia.play.broadcaster.R

/**
 * Created by jegul on 20/05/20
 */
sealed class FollowerUiModel {
    data class Unknown(@ColorRes val colorRes: Int) : FollowerUiModel() {

        companion object {
            fun fromIndex(index: Int) = Unknown(getColorList(index))

            private fun getColorList(index: Int) = when (index) {
                0 -> com.tokopedia.play_common.R.color.play_dms_Y500
                1 -> com.tokopedia.play_common.R.color.play_dms_B600
                else -> com.tokopedia.play_common.R.color.play_dms_Y300
            }
        }
    }
    data class User(val imageUrl: String) : FollowerUiModel()
}

data class FollowerDataUiModel(
        val followersList: List<FollowerUiModel>,
        val totalFollowers: Int
) {

    companion object {
        fun init(count: Int) = FollowerDataUiModel(
                followersList = List(count) { FollowerUiModel.Unknown.fromIndex(it) },
                totalFollowers = 0
        )
    }
}