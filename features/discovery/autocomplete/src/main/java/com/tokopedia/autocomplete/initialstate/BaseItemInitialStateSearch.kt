package com.tokopedia.autocomplete.initialstate

import com.tokopedia.discovery.common.utils.URLParser

import java.util.HashMap

class BaseItemInitialStateSearch(
        val template: String = "",
        val imageUrl: String = "",
        val applink: String = "",
        val url: String = "",
        val title: String = "",
        val subtitle: String = "",
        val iconTitle: String = "",
        val iconSubtitle: String = "",
        val label: String = "",
        val labelType: String = "",
        val shortcutImage: String = "",
        val productId: String = ""
) {
    val applinkParameterHashmap: HashMap<String, String>
        get() = URLParser(this.applink).paramKeyValueMap
}
