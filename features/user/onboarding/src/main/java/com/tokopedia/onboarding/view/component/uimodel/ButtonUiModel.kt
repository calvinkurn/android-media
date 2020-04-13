package com.tokopedia.onboarding.view.component.uimodel

data class ButtonUiModel(
        var appLink: String = "",
        var color: String = "",
        var componentLevel: Int = 0,
        var componentName: String = "",
        var text: String = "",
        var textColor: String = "",
        var visibility: Boolean = false
) : OnboardingUiModel {
    override fun position() = componentLevel
}