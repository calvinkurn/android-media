package com.tokopedia.feed_shop.shop.domain

import com.tokopedia.feedcomponent.data.pojo.whitelist.Author

import java.util.ArrayList

/**
 * @author by yfsx on 20/06/18.
 */
data class WhitelistDomain(
    var error: String = "",
    var isWhitelist: Boolean = false,
    var url: String = "",
    var title: String = "",
    var titleIdentifier: String = "",
    var postSuccessMessage: String = "",
    var desc: String = "",
    var image: String = "",
    var authors: List<Author> = ArrayList()
)
