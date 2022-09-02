package com.tokopedia.shop.flashsale.presentation.creation.information

import android.annotation.SuppressLint
import com.tokopedia.shop.flashsale.domain.entity.Gradient

@SuppressLint("UnsupportedDarkModeColor")
val defaultGradientColor =  Gradient("#26A116", "#60BB55", true)

@SuppressLint("UnsupportedDarkModeColor")
val campaignGradientColors = listOf(
    defaultGradientColor,
    Gradient("#019751", "#00AA5B", false),
    Gradient("#00615B", "#04837E", false),
    Gradient("#2B4A62", "#3E6786", false),
    Gradient("#2059A1", "#2F89FC", false),
    Gradient("#1C829E", "#28B9E1", false),

    Gradient("#EF4C60", "#E96E7D", false),
    Gradient("#AE1720", "#D74049", false),
    Gradient("#850623", "#B31D40", false),
    Gradient("#B8266C", "#CD5D99", false),
    Gradient("#4F2DC0", "#7F66D1", false),
    Gradient("#58197E", "#854C9E", false),

    Gradient("#685447", "#836857", false),
    Gradient("#DD8F00", "#F8A400", false),
)