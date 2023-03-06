package com.tokopedia.feedplus.presentation.model

import android.os.Parcelable
import com.tokopedia.feedplus.oldFeed.domain.model.feed.WhitelistDomain
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
data class FeedTabsModel(
    val data: List<FeedDataModel>,
    val meta: MetaModel
)

@Parcelize
data class FeedDataModel(
    val title: String,
    val key: String,
    val type: String,
    val position: Int,
    val isActive: Boolean
) : Parcelable

data class MetaModel(
    val selectedIndex: Int,
    val profileApplink: String,
    val profileWeblink: String,
    val profilePhotoUrl: String,
    val isLoggedIn: Boolean,
    val showMyProfile: Boolean,
    val whiteListDomain: WhitelistDomain,
) {

//    val shouldShowProfile = !isLoggedIn || (showMyProfile && whiteListDomain.userAccount != null)
    val shouldShowProfile = true

    val createPostAllowed = whiteListDomain.isShopAccountExists ||
        whiteListDomain.isBuyerAccountPostEligible

    val createLiveAllowed = whiteListDomain.authors.isNotEmpty()

    val createShortsAllowed = whiteListDomain.isShopAccountShortsEligible ||
        whiteListDomain.isBuyerAccountExists

    val createContentAllowed = createPostAllowed || createLiveAllowed || createShortsAllowed

    fun login(isLoggedIn: Boolean): MetaModel {
        return copy(isLoggedIn = isLoggedIn)
    }
}
