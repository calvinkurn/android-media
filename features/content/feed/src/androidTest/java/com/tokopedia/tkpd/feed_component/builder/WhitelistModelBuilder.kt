package com.tokopedia.tkpd.feed_component.builder

import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist

/**
 * Created By : Jonathan Darwin on September 23, 2022
 */
class WhitelistModelBuilder {

    fun buildUgcOnly() = WhitelistQuery(
        whitelist = Whitelist(
            authors = listOf(
                getAuthorUgc()
            )
        )
    )

    fun buildSellerOnly() = WhitelistQuery(
        whitelist = Whitelist(
            authors = listOf(
                getAuthorSeller()
            )
        )
    )

    fun buildComplete() = WhitelistQuery(
        whitelist = Whitelist(
            authors = listOf(
                getAuthorUgc(),
                getAuthorSeller()
            )
        )
    )

    private fun getAuthorUgc() = Author(
        type = "content-user",
        thumbnail = "https://images.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-24.jpg"
    )

    private fun getAuthorSeller() = Author(
        type = "content-shop",
        thumbnail = "https://images.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-24.jpg"
    )
}
