package com.tokopedia.shop.feed.domain

import com.tokopedia.kolcommon.data.pojo.Author

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
    var authors: ArrayList<Author>? = ArrayList()
){}
