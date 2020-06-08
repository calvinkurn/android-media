package com.tokopedia.onboarding.view.component.uimodel

data class ImageUiModel(
        var animationUrl: String = "",
        var componentLevel: Int = 0,
        var componentName: String = "",
        var imageUrl: String = "",
        var visibility: Boolean = false
) : OnboardingUiModel {
    override fun position() = componentLevel
}