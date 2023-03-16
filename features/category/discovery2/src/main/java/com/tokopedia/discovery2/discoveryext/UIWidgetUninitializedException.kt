package com.tokopedia.discovery2.discoveryext

class UIWidgetUninitializedException : Exception() {
    override val message: String?
        get() = "UI widget component must be initialized before bindView."
}
