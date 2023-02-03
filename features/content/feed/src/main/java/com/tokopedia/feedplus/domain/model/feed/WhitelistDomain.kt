package com.tokopedia.feedplus.domain.model.feed

import com.tokopedia.content.common.model.GetCheckWhitelistResponse
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER


/**
 * Created By : Jonathan Darwin on June 09, 2022
 */
data class WhitelistDomain(
    val error: String,
    val isWhitelist: Boolean,
    val url: String,
    val title: String,
    val titleIdentifier: String,
    val postSuccessMessage: String,
    val desc: String,
    val image: String,
    val authors: List<GetCheckWhitelistResponse.Author>
) {

    val isShopAccountExists: Boolean
        get() = authors.find { it.type == TYPE_SHOP } != null

    // for buyer/non-seller/ugc enable was meant for accepting tnc
    val isBuyerAccountPostEligible: Boolean
        get() = authors.find{ it.type == TYPE_USER && it.post.enable } != null

    val isBuyerAccountExists: Boolean
        get() = authors.find{ it.type == TYPE_USER } != null

    val isShopAccountShortsEligible: Boolean
        get() = authors.find { it.type == TYPE_SHOP && it.shortVideo.enable } != null

    val userAccount: GetCheckWhitelistResponse.Author?
        get() = authors.find { it.type == TYPE_USER }

    companion object {
        val Empty: WhitelistDomain
            get() = WhitelistDomain(
                error = "",
                isWhitelist = false,
                url = "",
                title = "",
                titleIdentifier = "",
                postSuccessMessage = "",
                desc = "",
                image = "",
                authors = emptyList()
            )
    }
}
