package com.tokopedia.tokofood.feature.merchant.presentation.model

import androidx.annotation.DrawableRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CarouselDataType

data class CarouselData(
        val carouselDataType: CarouselDataType,
        val title: String = "",
        val information: String = "",
        val isWarning: Boolean = false
) {
    @DrawableRes
    var imageResource = when (carouselDataType) {
        CarouselDataType.RATING -> R.drawable.ic_rating_star
        else -> {
            null
        }
    }
}
