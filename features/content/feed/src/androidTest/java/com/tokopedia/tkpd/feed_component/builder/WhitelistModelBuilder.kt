package com.tokopedia.tkpd.feed_component.builder

import com.tokopedia.content.common.model.GetCheckWhitelistResponse

/**
 * Created By : Jonathan Darwin on September 23, 2022
 */
class WhitelistModelBuilder {

    fun buildUgcOnly() = GetCheckWhitelistResponse(
        whitelist = GetCheckWhitelistResponse.Whitelist(
            authors = listOf(
                getAuthorUgc()
            )
        )
    )

    fun buildSellerOnly() = GetCheckWhitelistResponse(
        whitelist = GetCheckWhitelistResponse.Whitelist(
            authors = listOf(
                getAuthorSeller()
            )
        )
    )

    fun buildComplete() = GetCheckWhitelistResponse(
        whitelist = GetCheckWhitelistResponse.Whitelist(
            authors = listOf(
                getAuthorUgc(),
                getAuthorSeller()
            )
        )
    )

    private fun getAuthorUgc() = GetCheckWhitelistResponse.Author(
        type = "content-user",
        thumbnail = "https://images.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-24.jpg"
    )

    private fun getAuthorSeller() = GetCheckWhitelistResponse.Author(
        type = "content-shop",
        thumbnail = "https://images.tokopedia.net/img/cache/100-square/default_picture_user/default_toped-24.jpg"
    )
}
