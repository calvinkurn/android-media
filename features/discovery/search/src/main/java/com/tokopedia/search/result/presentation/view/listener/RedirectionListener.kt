package com.tokopedia.search.result.presentation.view.listener

interface RedirectionListener {
    fun showSearchInputView()
    fun setAutocompleteApplink(autocompleteApplink: String?)
    fun startActivityWithApplink(applink: String?, vararg parameter: String?)
    fun startActivityWithUrl(url: String?, vararg parameter: String?)
}