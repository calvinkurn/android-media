package com.tokopedia.wishlist.ext

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.wishlist.data.model.*

fun List<WishlistV2Response.Data.WishlistV2.Item>.mappingWishlistToVisitable(): MutableList<WishlistV2Data>{
    return map{ WishlistV2DataModel( item = it ) }.toMutableList()
}

fun List<WishlistV2Data>.combineVisitable(secondList: List<WishlistV2Data>): MutableList<WishlistV2Data>{
    val newList = ArrayList(this)
    newList.addAll(secondList)
    return newList
}
