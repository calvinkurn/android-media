package com.tokopedia.discovery2.discoveryext

class UIWidgetUninitializedException : Exception() {

    var componentName = ""
    override val message: String?
        get() = "UI widget component must be initialized before bindView $componentName."
}
