package com.tokopedia.wishlistcollection.util

import com.tokopedia.wishlist.data.model.*
import com.tokopedia.wishlist.util.WishlistV2Consts
import com.tokopedia.wishlistcollection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_CREATE
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_DIVIDER
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_EMPTY_CAROUSEL
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlistcollection.util.WishlistCollectionConsts.TYPE_COLLECTION_TICKER

object WishlistCollectionUtils {
    fun mapCollection(data: GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData,
                      recomm: WishlistV2RecommendationDataModel): List<WishlistCollectionTypeLayoutData> {
        val listCollection = arrayListOf<WishlistCollectionTypeLayoutData>()
        if (data.ticker.title.isNotEmpty() && data.ticker.description.isNotEmpty()) {
            val tickerObject = WishlistCollectionTypeLayoutData(
                data.ticker,
                TYPE_COLLECTION_TICKER
            )
            listCollection.add(tickerObject)
        }



        if (data.isEmptyState) {
            mapToEmptyState(data.emptyState, listCollection, recomm)
        } else {
            data.collections.forEach { item ->
                val collectionItemObject = WishlistCollectionTypeLayoutData(
                    item,
                    TYPE_COLLECTION_ITEM
                )
                listCollection.add(collectionItemObject)
            }

            val createNewItem =
                WishlistCollectionTypeLayoutData(
                    data.placeholder,
                    TYPE_COLLECTION_CREATE
                )
            listCollection.add(createNewItem)

            listCollection.add(
                WishlistCollectionTypeLayoutData("", TYPE_COLLECTION_DIVIDER))

            listCollection.add(
                WishlistCollectionTypeLayoutData(
                    recomm.title,
                    WishlistV2Consts.TYPE_RECOMMENDATION_TITLE
                )
            )
            recomm.recommendationProductCardModelData.forEachIndexed { index, productCardModel ->
                if (recomm.listRecommendationItem.isNotEmpty()) {
                    listCollection.add(
                        WishlistCollectionTypeLayoutData(
                            productCardModel,
                            WishlistV2Consts.TYPE_RECOMMENDATION_LIST,
                            recommItem = recomm.listRecommendationItem[index]
                        )
                    )
                }
            }
        }

        return listCollection
    }

    private fun mapToEmptyState(
        collectionEmptyState: GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.EmptyState,
        listData: ArrayList<WishlistCollectionTypeLayoutData>,
        recomm: WishlistV2RecommendationDataModel
    ): ArrayList<WishlistCollectionTypeLayoutData> {
        listData.add(
            WishlistCollectionTypeLayoutData(
                collectionEmptyState,
                TYPE_COLLECTION_EMPTY_CAROUSEL
            )
        )

        listData.add(
            WishlistCollectionTypeLayoutData(
                recomm.title,
                WishlistV2Consts.TYPE_RECOMMENDATION_TITLE
            )
        )
        recomm.recommendationProductCardModelData.forEachIndexed { index, productCardModel ->
            if (recomm.listRecommendationItem.isNotEmpty()) {
                listData.add(
                    WishlistCollectionTypeLayoutData(
                        productCardModel,
                        WishlistV2Consts.TYPE_RECOMMENDATION_LIST,
                        recommItem = recomm.listRecommendationItem[index]
                    )
                )
            }
        }
        return listData
    }
}