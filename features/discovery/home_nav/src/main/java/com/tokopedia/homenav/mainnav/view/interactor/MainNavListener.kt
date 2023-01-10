package com.tokopedia.homenav.mainnav.view.interactor

import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.base.diffutil.HomeNavListener
import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistModel
import com.tokopedia.trackingoptimizer.TrackingQueue

interface MainNavListener : HomeNavListener{

    fun onProfileLoginClicked()

    fun onProfileRegisterClicked()

    fun onProfileSectionClicked()

    fun onErrorProfileRefreshClicked(position: Int)

    fun onErrorShopInfoRefreshClicked(position: Int)

    fun onErrorBuListClicked(position: Int)

    fun onErrorTransactionListClicked(position: Int)

    fun getTrackingQueueObj(): TrackingQueue?

    fun putEEToTrackingQueue(data: HashMap<String, Any>)

    fun onErrorAffiliateInfoRefreshClicked(position: Int)

    fun onTitleClicked(homeNavTitleDataModel: HomeNavTitleDataModel)

    fun onErrorWishlistClicked()

    fun onWishlistCollectionClicked(wishlistModel: NavWishlistModel, position: Int)

    fun onErrorFavoriteShopClicked()

    fun onFavoriteShopItemClicked(favoriteShopModel: NavFavoriteShopModel, position: Int)

    fun showReviewProduct(uriReviewProduct: String)

    fun onErrorReviewClicked()
}
