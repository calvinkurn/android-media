package com.tokopedia.talk


/**
 * @author by Steven.
 */

data class ProductTalkItemViewModel(
        var avatar: String? = "",
        var name: String? = "",
        var timestamp: String? = "",
        var comment: String? = "",
        var menu: List<String>) {

}
