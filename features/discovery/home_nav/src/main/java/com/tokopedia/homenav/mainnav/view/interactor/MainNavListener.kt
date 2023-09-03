package com.tokopedia.homenav.mainnav.view.interactor

import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.mainnav.domain.model.NavReviewModel
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.trackingoptimizer.TrackingQueue

interface MainNavListener : HomeNavListener {

    fun onProfileLoginClicked()

    fun onProfileRegisterClicked()

    fun onProfileSectionClicked(eventLabel: String, applink: String)

    fun onErrorProfileRefreshClicked(position: Int)

    fun onErrorShopInfoRefreshClicked(position: Int)

    fun onErrorBuListClicked(position: Int)

    fun onErrorTransactionListClicked(position: Int)

    fun getTrackingQueueObj(): TrackingQueue?

    fun putEEToTrackingQueue(data: HashMap<String, Any>)

    fun onErrorAffiliateInfoRefreshClicked(position: Int)

    fun onTitleClicked(homeNavTitleDataModel: HomeNavTitleDataModel)

    fun onErrorWishlistClicked()

    fun onWishlistCardClicked(wishlistModel: NavWishlistModel, position: Int)

    fun onWishlistCardImpressed(wishlistModel: NavWishlistModel, position: Int)

    fun onReviewCardClicked(element: NavReviewModel, position: Int, ratingValue: String, uri: String)

    fun onReviewCardImpressed(element: NavReviewModel, position: Int)

    fun onErrorReviewClicked()

    fun onOrderCardClicked(
        applink: String,
        trackingLabel: String? = null,
        orderId: String,
        position: Int,
    )

    fun onOrderCardImpressed(trackingLabel: String, orderId: String, position: Int)

    fun onViewAllCardClicked(sectionId: Int, applink: String)
}
