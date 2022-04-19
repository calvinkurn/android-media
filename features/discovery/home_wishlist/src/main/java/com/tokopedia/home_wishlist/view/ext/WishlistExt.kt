package com.tokopedia.home_wishlist.view.ext

import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.util.TypeAction
import com.tokopedia.home_wishlist.util.WishlistAction
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

fun WishlistAction.addToCart(callback: (Boolean, String) -> Unit) {
    if (typeAction == TypeAction.ADD_TO_CART) {
        callback.invoke(isSuccess, message)
    }
}
fun WishlistAction.bulkWishlist(callback: (Boolean, String) -> Unit) {
    if (typeAction == TypeAction.BULK_DELETE_WISHLIST) {
        callback.invoke(isSuccess, message)
    }
}
fun WishlistAction.addWishlist(callback: (Boolean, String) -> Unit) {
    if (typeAction == TypeAction.ADD_WISHLIST) {
        callback.invoke(isSuccess, message)
    }
}
fun WishlistAction.removeWishlist(callback: (Boolean, String) -> Unit) {
    if (typeAction == TypeAction.REMOVE_WISHLIST) {
        callback.invoke(isSuccess, message)
    }
}

fun List<WishlistItem>.mappingWishlistToVisitable(isInBulkMode: Boolean): MutableList<WishlistDataModel>{
    return map{ WishlistItemDataModel( productItem = it,isOnBulkRemoveProgress = isInBulkMode) }.toMutableList()
}

fun List<RecommendationWidget>.mappingRecommendationToWishlist(
        currentPage: Int,
        wishlistVisitable: List<WishlistDataModel>,
        listRecommendationCarouselOnMarked: HashMap<Int, WishlistDataModel>,
        recommendationPositionInPage: Int,
        maxItemInPage: Int,
        isInBulkMode: Boolean): List<WishlistDataModel>{
    val list = mutableListOf<WishlistDataModel>()

    val recommendationPositionInThisPage = ((currentPage-1) * maxItemInPage) + recommendationPositionInPage
    list.addAll(wishlistVisitable)
    if (isInBulkMode) {
        listRecommendationCarouselOnMarked[recommendationPositionInThisPage] =
                RecommendationCarouselDataModel(
                        id = first().tid,
                        title = first().title,
                        list = first().recommendationItemList.map {
                            RecommendationCarouselItemDataModel(it, first().title, getRecommendationParentPosition(
                                    maxItemInPage,
                                    recommendationPositionInPage,
                                    currentPage)) } as MutableList<RecommendationCarouselItemDataModel>,
                        isOnBulkRemoveProgress = isInBulkMode,
                        seeMoreAppLink = first().seeMoreAppLink)
    } else {
        list.add(recommendationPositionInThisPage,
                RecommendationCarouselDataModel(
                        id = first().tid,
                        title = first().title,
                        list = first().recommendationItemList.map {
                            RecommendationCarouselItemDataModel(it, first().title, getRecommendationParentPosition(
                                    maxItemInPage,
                                    recommendationPositionInPage,
                                    currentPage)) } as MutableList<RecommendationCarouselItemDataModel>,
                        isOnBulkRemoveProgress = isInBulkMode,
                        seeMoreAppLink = first().seeMoreAppLink))
    }
    return list
}

private fun getRecommendationParentPosition(maxItemInPage: Int,
                                            recommendationPositionInPage: Int,
                                            currentPage: Int): Int {
    return (currentPage - 1) * maxItemInPage + recommendationPositionInPage
}

fun List<WishlistDataModel>.combineVisitable(secondList: List<WishlistDataModel>): MutableList<WishlistDataModel>{
    val newList = ArrayList(this)
    newList.addAll(secondList)
    return newList
}

fun List<WishlistDataModel>.mappingTopadsBannerToWishlist(
        topadsBanner: TopAdsImageViewModel,
        listRecommendationCarouselOnMarked: HashMap<Int, WishlistDataModel>,
        recommendationPositionInPage: Int,
        currentPage: Int,
        isInBulkMode: Boolean,
        maxItemInPage: Int): List<WishlistDataModel>{
    val recommendationPositionInThisPage = ((currentPage-1) * maxItemInPage) + recommendationPositionInPage
    val list = mutableListOf<WishlistDataModel>()
    list.addAll(this)
    if (isInBulkMode) {
        listRecommendationCarouselOnMarked[recommendationPositionInThisPage] =
                BannerTopAdsDataModel(
                        topAdsDataModel = topadsBanner,
                        isOnBulkRemoveProgress = isInBulkMode)
    } else {
        list.add(recommendationPositionInThisPage,
                BannerTopAdsDataModel(
                        topAdsDataModel = topadsBanner,
                        isOnBulkRemoveProgress = isInBulkMode))
    }
    return list
}

fun mappingTopadsBannerWithRecommendationToWishlist(
        topadsBanner: TopAdsImageViewModel,
        wishlistVisitable: List<WishlistDataModel>,
        listRecommendation: List<RecommendationWidget>,
        listRecommendationCarouselOnMarked: HashMap<Int, WishlistDataModel>,
        recommendationPositionInPage: Int,
        currentPage: Int,
        isInBulkMode: Boolean,
        maxItemInPage: Int,
        recommendationIndex: Int): List<WishlistDataModel> {
    val recommendationPositionInThisPage = ((currentPage-1) * maxItemInPage) + recommendationPositionInPage
    val list = mutableListOf<WishlistDataModel>()
    list.addAll(wishlistVisitable)
    if (isInBulkMode) {
        listRecommendationCarouselOnMarked[recommendationPositionInThisPage] =
                BannerTopAdsDataModel(
                        topAdsDataModel = topadsBanner,
                        isOnBulkRemoveProgress = isInBulkMode)
    } else {
        list.add(recommendationPositionInThisPage,
                BannerTopAdsDataModel(
                        topAdsDataModel = topadsBanner,
                        isOnBulkRemoveProgress = isInBulkMode))
    }

    if (isInBulkMode) {
        listRecommendationCarouselOnMarked[recommendationIndex] =
                RecommendationCarouselDataModel(
                        id = listRecommendation.first().tid,
                        title = listRecommendation.first().title,
                        list = listRecommendation.first().recommendationItemList.map {
                            RecommendationCarouselItemDataModel(it, listRecommendation.first().title, getRecommendationParentPosition(
                                    maxItemInPage,
                                    recommendationPositionInPage,
                                    currentPage)) } as MutableList<RecommendationCarouselItemDataModel>,
                        isOnBulkRemoveProgress = isInBulkMode,
                        seeMoreAppLink = listRecommendation.first().seeMoreAppLink)
    } else {
        list.add(recommendationIndex,
                RecommendationCarouselDataModel(
                        id = listRecommendation.first().tid,
                        title = listRecommendation.first().title,
                        list = listRecommendation.first().recommendationItemList.map {
                            RecommendationCarouselItemDataModel(it, listRecommendation.first().title, getRecommendationParentPosition(
                                    maxItemInPage,
                                    recommendationPositionInPage,
                                    currentPage)) } as MutableList<RecommendationCarouselItemDataModel>,
                        isOnBulkRemoveProgress = isInBulkMode,
                        seeMoreAppLink = listRecommendation.first().seeMoreAppLink))
    }
    return list
}