package com.tokopedia.feedplus.domain.model.feed

import com.tokopedia.feedcomponent.data.pojo.whitelist.Author
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author.Companion.TYPE_SHOP
import com.tokopedia.feedcomponent.data.pojo.whitelist.Author.Companion.TYPE_USER

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
    val authors: List<Author>
) {

    val isShopAccountExists: Boolean
        get() = authors.find { it.type == TYPE_SHOP } != null

    val isUserAccountPostEligible: Boolean
        get() = authors.find{ it.type == TYPE_USER && it.post.hasAcceptTnc } != null

    val userAccount: Author?
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
