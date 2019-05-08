package com.tokopedia.shop.feed.domain

import com.tokopedia.kolcommon.data.pojo.Author

import java.util.ArrayList

/**
 * @author by yfsx on 20/06/18.
 */
class WhitelistDomain {

    var error: String? = null
    var isWhitelist: Boolean = false
    var url: String? = null
    var title: String? = null
    var titleIdentifier: String? = null
    var postSuccessMessage: String? = null
    var desc: String? = null
    var image: String? = null
    var authors: ArrayList<Author>? = null
}
