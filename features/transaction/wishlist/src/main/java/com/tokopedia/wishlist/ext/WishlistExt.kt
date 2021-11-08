package com.tokopedia.wishlist.ext

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.wishlist.data.model.*

fun List<WishlistV2Response.Data.WishlistV2.Item>.mappingWishlistToVisitable(): MutableList<WishlistV2DataModel>{
    return map{ WishlistV2DataModel( item = it ) }.toMutableList()
}

fun List<RecommendationWidget>.mappingRecommendationToWishlist(
    currentPage: Int,
    wishlistVisitable: List<WishlistV2Data>,
    recommendationPositionInPage: Int,
    maxItemInPage: Int): List<WishlistV2Data>{
    val list = mutableListOf<WishlistV2Data>()

    val recommendationPositionInThisPage = ((currentPage-1) * maxItemInPage) + recommendationPositionInPage
    list.addAll(wishlistVisitable)
    list.add(recommendationPositionInThisPage,
        WishlistV2RecommendationWrapper(this, isCarousel = true))

    return list
}

private fun getRecommendationParentPosition(maxItemInPage: Int,
                                            recommendationPositionInPage: Int,
                                            currentPage: Int): Int {
    return (currentPage - 1) * maxItemInPage + recommendationPositionInPage
}

fun List<WishlistV2DataModel>.combineVisitable(secondList: List<WishlistV2DataModel>): MutableList<WishlistV2DataModel>{
    val newList = ArrayList(this)
    newList.addAll(secondList)
    return newList
}

fun List<WishlistV2Data>.mappingTopadsBannerToWishlist(
    topadsBanner: TopAdsImageViewModel,
    recommendationPositionInPage: Int,
    currentPage: Int,
    maxItemInPage: Int): List<WishlistV2Data>{
    val recommendationPositionInThisPage = ((currentPage-1) * maxItemInPage) + recommendationPositionInPage
    val list = mutableListOf<WishlistV2Data>()
    list.addAll(this)
    list.add(recommendationPositionInThisPage, WishlistV2TopAdsWrapper(topAdsData = topadsBanner))
    return list
}

fun mappingTopadsBannerWithRecommendationToWishlist(
    topadsBanner: TopAdsImageViewModel,
    wishlistVisitable: List<WishlistV2Data>,
    listRecommendation: List<RecommendationWidget>,
    recommendationPositionInPage: Int,
    currentPage: Int,
    isInBulkMode: Boolean,
    maxItemInPage: Int,
    recommendationIndex: Int): List<WishlistV2Data> {
    val recommendationPositionInThisPage = ((currentPage-1) * maxItemInPage) + recommendationPositionInPage
    val list = mutableListOf<WishlistV2Data>()
    list.addAll(wishlistVisitable)

        list.add(recommendationPositionInThisPage, WishlistV2TopAdsWrapper(topAdsData = topadsBanner))

        list.add(recommendationIndex,
            WishlistV2RecommendationWrapper(
                listRecommendation, true), )

    return list
}