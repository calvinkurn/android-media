package com.tokopedia.onboarding.view.component.uimodel

data class TitleUiModel(
        var componentLevel: Int = 0,
        var componentName: String = "",
        var text: String = "",
        var textColor: String = "",
        var typographyType: String = "",
        var visibility: Boolean = false
): OnboardingUiModel {
    override fun position() = componentLevel
}