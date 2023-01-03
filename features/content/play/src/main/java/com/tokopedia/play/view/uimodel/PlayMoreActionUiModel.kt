package com.tokopedia.play.view.uimodel

import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
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
)
