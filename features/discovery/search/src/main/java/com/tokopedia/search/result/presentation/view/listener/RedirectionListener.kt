package com.tokopedia.search.result.presentation.view.listener

interface RedirectionListener {
    fun showSearchInputView()
    fun setAutocompleteApplink(autocompleteApplink: String?)

    @Deprecated("Please call RouteManager.route directly")
    fun startActivityWithApplink(applink: String?, vararg parameter: String?)

    @Deprecated("Please call RouteManager.route directly")
    fun startActivityWithUrl(url: String?, vararg parameter: String?)
}