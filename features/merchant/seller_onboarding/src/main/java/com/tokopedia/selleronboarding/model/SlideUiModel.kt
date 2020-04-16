package com.tokopedia.selleronboarding.model

import androidx.annotation.DrawableRes

/**
 * Created By @ilhamsuaib on 11/04/20
 */

data class SlideUiModel(
        val headerText: String,
        @DrawableRes val vectorDrawableRes: Int,
        @DrawableRes val pngDrawableRes: Int
)