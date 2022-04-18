package com.tokopedia.tokofood.feature.merchant.presentation.model

import androidx.annotation.DrawableRes
import com.tokopedia.tokofood.R

data class CarouselData(
        val dataType: DataType,
        val title: String = "",
        val information: String = "",
        val isWarning: Boolean = false
) {
    @DrawableRes
    var imageResource = when (dataType) {
        DataType.RATING -> R.drawable.ic_rating_star
        else -> {
            null
        }
    }
}
