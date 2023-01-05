package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.datamodel.*
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.ErrorStateFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.ShimmerFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ShimmerReviewDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ErrorStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ShimmerWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistDataModel

interface MainNavTypeFactory {

    fun type(accountHeaderDataModel: AccountHeaderDataModel): Int

    fun type(separatorDataModel: SeparatorDataModel) : Int

    fun type(transactionListItemDataModel: TransactionListItemDataModel) : Int

    fun type(wishlistDataModel: WishlistDataModel): Int

    fun type(favoriteShopListDataModel: FavoriteShopListDataModel): Int

    fun type(reviewListDataModel: ReviewListDataModel): Int

    fun type(initialShimmerDataModel: InitialShimmerDataModel) : Int

    fun type(initialShimmerProfileDataModel: InitialShimmerProfileDataModel) : Int

    fun type(initialShimmerTransactionRevampDataModel: InitialShimmerTransactionRevampDataModel) : Int

    fun type(initialShimmerTransactionDataModel: InitialShimmerTransactionDataModel) : Int

    fun type(shimmerFavoriteShopDataModel: ShimmerFavoriteShopDataModel) : Int

    fun type(shimmerWishlistDataModel: ShimmerWishlistDataModel) : Int

    fun type(shimmerReviewDataModel: ShimmerReviewDataModel) : Int

    fun type(errorStateBuDataModel: ErrorStateBuDataModel): Int

    fun type(errorStateOngoingTransactionModel: ErrorStateOngoingTransactionModel): Int

    fun type(errorStateFavoriteShopDataModel: ErrorStateFavoriteShopDataModel): Int

    fun type(errorStateWishlistDataModel: ErrorStateWishlistDataModel): Int

    fun createViewHolder(view: View, viewType: Int) : AbstractViewHolder<*>
}
