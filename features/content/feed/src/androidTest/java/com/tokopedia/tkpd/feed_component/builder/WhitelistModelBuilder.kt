package com.tokopedia.tkpd.feed_component.builder

import com.tokopedia.content.common.model.GetCheckWhitelist

/**
 * Created By : Jonathan Darwin on September 23, 2022
 */
class WhitelistModelBuilder {

    fun buildUgcOnly() = GetCheckWhitelist(
        whitelist = GetCheckWhitelist.Whitelist(
            authors = listOf(
                getAuthorUgc()
            )
        )
    )

    fun buildSellerOnly() = GetCheckWhitelist(
        whitelist = GetCheckWhitelist.Whitelist(
            authors = listOf(
                getAuthorSeller()
            )
        )
    )

    fun buildComplete() = GetCheckWhitelist(
        whitelist = GetCheckWhitelist.Whitelist(
            authors = listOf(
                getAuthorUgc(),
                getAuthorSeller()
            )
        )
    )

    private fun getAuthorUgc() = GetCheckWhitelist.Author(
        type = "content-user",
        thumbnail = "https://images.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-24.jpg"
    )

    private fun getAuthorSeller() = GetCheckWhitelist.Author(
        type = "content-shop",
        thumbnail = "https://images.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-24.jpg"
    )
}
