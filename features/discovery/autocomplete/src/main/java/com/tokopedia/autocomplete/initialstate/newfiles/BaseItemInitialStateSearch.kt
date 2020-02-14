package com.tokopedia.autocomplete.initialstate.newfiles

import com.tokopedia.discovery.common.utils.URLParser

import java.util.HashMap

class BaseItemInitialStateSearch(
        var template: String = "",
        var imageUrl: String = "",
        var applink: String = "",
        var url: String = "",
        var title: String = "",
        var subtitle: String = "",
        var iconTitle: String = "",
        var iconSubtitle: String = "",
        var label: String = "",
        var shortcutUrl: String = "",
        var shortcutImage: String = "",
        var productId: String = "",
        var productPrice: String = ""
) {
    val applinkParameterHashmap: HashMap<String, String>
        get() = URLParser(this.applink).paramKeyValueMap
}
