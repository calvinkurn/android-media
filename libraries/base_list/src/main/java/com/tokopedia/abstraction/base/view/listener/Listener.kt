package com.tokopedia.abstraction.base.view.listener

interface Listener {
    fun onSearchSubmitted(text: String)
    fun onSearchTextChanged(text: String)
}
