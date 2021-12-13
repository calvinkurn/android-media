package com.tokopedia.recharge_component.digital_card.presentation.model

enum class MediaType(val value: String, val ratio: String) {
    SQUARE("square", "H,1:1"),
    RECTANGLE("rectangle", "H,2:1")
}

enum class RatingType(val value: String) {
    SQUARE("square"),
    STAR("star")
}

enum class CTAButtonType(val value: String) {
    ENABLE("enable"),
    DISABLE("disable")
}