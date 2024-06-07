package com.tokopedia.wishlist.collection.util

import android.os.SystemClock
import android.view.View
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.wishlist.collection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.collection.data.response.GetWishlistCollectionResponse
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.COLLECTION_PRIVATE
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.COLLECTION_PUBLIC
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_CREATE
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_DIVIDER
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_EMPTY_CAROUSEL
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_ITEM
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_PRIVATE_SELF
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_PUBLIC_OTHERS
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_PUBLIC_SELF
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_SHARE
import com.tokopedia.wishlist.collection.util.WishlistCollectionConsts.TYPE_COLLECTION_TICKER
import com.tokopedia.wishlist.detail.data.model.WishlistRecommendationDataModel
import com.tokopedia.wishlist.detail.util.WishlistConsts

object WishlistCollectionUtils {
    fun isAffiliateTickerEnabled() = RemoteConfigInstance.getInstance().abTestPlatform.getString(
        RollenceKey.WISHLIST_AFFILIATE_TICKER,
        ""
    ) == RollenceKey.WISHLIST_AFFILIATE_TICKER

    fun mapCollection(
        data: GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData,
        recomm: WishlistRecommendationDataModel = WishlistRecommendationDataModel(),
        tickerHasBeenClosed: Boolean
    ): List<WishlistCollectionTypeLayoutData> {
        val listCollection = arrayListOf<WishlistCollectionTypeLayoutData>()
        if ((data.ticker.title.isNotEmpty() && data.ticker.description.isNotEmpty()) && !tickerHasBeenClosed || isAffiliateTickerEnabled()) {
            val tickerObject = WishlistCollectionTypeLayoutData(
                TYPE_COLLECTION_TICKER,
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
                    "${TYPE_COLLECTION_ITEM}_${item.id}",
                    item,
                    TYPE_COLLECTION_ITEM
                )
                listCollection.add(collectionItemObject)
            }

            val createNewItem =
                WishlistCollectionTypeLayoutData(
                    TYPE_COLLECTION_CREATE,
                    data.placeholder,
                    TYPE_COLLECTION_CREATE
                )
            listCollection.add(createNewItem)

            if (recomm.listRecommendationItem.isNotEmpty()) {
                listCollection.add(
                    WishlistCollectionTypeLayoutData(
                        TYPE_COLLECTION_DIVIDER,
                        "",
                        TYPE_COLLECTION_DIVIDER
                    )
                )
                listCollection.add(
                    WishlistCollectionTypeLayoutData(
                        WishlistConsts.TYPE_RECOMMENDATION_TITLE,
                        recomm.title,
                        WishlistConsts.TYPE_RECOMMENDATION_TITLE
                    )
                )
                recomm.recommendationProductCardModelData.forEachIndexed { index, productCardModel ->
                    if (recomm.listRecommendationItem.isNotEmpty()) {
                        val productId = recomm.listRecommendationItem[index].productId
                        listCollection.add(
                            WishlistCollectionTypeLayoutData(
                                "${WishlistConsts.TYPE_RECOMMENDATION_LIST}_$productId",
                                productCardModel,
                                WishlistConsts.TYPE_RECOMMENDATION_LIST,
                                recommItem = recomm.listRecommendationItem[index]
                            )
                        )
                    }
                }
            }
        }

        return listCollection
    }

    private fun mapToEmptyState(
        collectionEmptyState: GetWishlistCollectionResponse.GetWishlistCollections.WishlistCollectionResponseData.EmptyState,
        listData: ArrayList<WishlistCollectionTypeLayoutData>,
        recomm: WishlistRecommendationDataModel
    ): ArrayList<WishlistCollectionTypeLayoutData> {
        listData.add(
            WishlistCollectionTypeLayoutData(
                TYPE_COLLECTION_EMPTY_CAROUSEL,
                collectionEmptyState,
                TYPE_COLLECTION_EMPTY_CAROUSEL
            )
        )

        listData.add(
            WishlistCollectionTypeLayoutData(
                WishlistConsts.TYPE_RECOMMENDATION_TITLE,
                recomm.title,
                WishlistConsts.TYPE_RECOMMENDATION_TITLE
            )
        )
        recomm.recommendationProductCardModelData.forEachIndexed { index, productCardModel ->
            if (recomm.listRecommendationItem.isNotEmpty()) {
                val productId = recomm.listRecommendationItem[index].productId
                listData.add(
                    WishlistCollectionTypeLayoutData(
                        "${WishlistConsts.TYPE_RECOMMENDATION_LIST}_$productId",
                        productCardModel,
                        WishlistConsts.TYPE_RECOMMENDATION_LIST,
                        recommItem = recomm.listRecommendationItem[index]
                    )
                )
            }
        }
        return listData
    }

    fun View.clickWithDebounce(debounceTime: Long = 600L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0

            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                    return
                } else {
                    action()
                }

                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }

    fun Int.getStringCollectionType(): String {
        return when (this) {
            TYPE_COLLECTION_PRIVATE_SELF -> COLLECTION_PRIVATE
            TYPE_COLLECTION_PUBLIC_SELF -> COLLECTION_PUBLIC
            TYPE_COLLECTION_PUBLIC_OTHERS -> COLLECTION_PUBLIC
            TYPE_COLLECTION_SHARE -> COLLECTION_PUBLIC
            else -> COLLECTION_PRIVATE
        }
    }
}
