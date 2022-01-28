package com.tokopedia.play.view.uimodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.play.view.type.PlayMoreActionType

/**
 * Created by jegul on 10/12/19
 */
data class PlayMoreActionUiModel(
        val type: PlayMoreActionType,
        @DrawableRes val iconRes: Int,
        @StringRes val subtitleRes: Int,
        val isIconAvailable: Boolean,
        val onClick: (PlayMoreActionUiModel) -> Unit
)