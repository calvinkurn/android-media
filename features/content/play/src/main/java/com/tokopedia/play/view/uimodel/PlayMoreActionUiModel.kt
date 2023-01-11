package com.tokopedia.play.view.uimodel

import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import com.tokopedia.play.R
import com.tokopedia.play.view.type.PlayMoreActionType

/**
 * Created by jegul on 10/12/19
 */
data class PlayMoreActionUiModel(
    val type: PlayMoreActionType,
    val icon: Drawable?,
    @StringRes val subtitleRes: Int,
    val onClick: (PlayMoreActionUiModel) -> Unit,
    val priority: Int,
) {
    companion object {
        val Empty : PlayMoreActionUiModel
        get () = PlayMoreActionUiModel(
            type = PlayMoreActionType.PiP,
            icon = null,
            subtitleRes = R.string.play_product_empty_title,
            onClick = {},
            priority = -1,
        )
    }
}
