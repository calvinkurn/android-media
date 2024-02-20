package com.tokopedia.autocompletecomponent.util

import com.tokopedia.discovery.common.model.SearchParameter

data class AutoCompleteNavigate(
    val applink: String? = null,
    val searchParameter: SearchParameter? = null
)
