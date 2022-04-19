package com.tokopedia.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.favorite.domain.model.FavoriteShopItem
import com.tokopedia.favorite.domain.model.TopAdsShopItem
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/* Copyright 2019 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */
fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

fun dummyFavoriteShopItemList(size: Int): List<FavoriteShopItem> {
    val items = ArrayList<FavoriteShopItem>()
    for (i in 0 until size) {
        items.add(FavoriteShopItem(name = randomString(20), isFav = false))
    }
    return items
}

fun randomString(length: Int = 10): String {
    val alphanum = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return List(length) { alphanum.random() }.joinToString("")
}

fun dummyTopAdsShopItem(): TopAdsShopItem {
    return TopAdsShopItem(
            shopId = randomString(10),
            shopDomain = randomString(10),
            shopName = randomString(10),
            adRefKey = randomString(10),
            shopClickUrl = randomString(10),
            shopImageCover = randomString(10),
            shopImageCoverEcs = randomString(10),
            shopImageUrl = randomString(10),
            shopImageEcs = randomString(10),
            shopLocation = randomString(10),
            isSelected = true
    )
}

fun dummyTopAdsShopItemList(size: Int): ArrayList<TopAdsShopItem> {
    val topAdsShopItemList = ArrayList<TopAdsShopItem>()
    for (i in 0 until size) {
        topAdsShopItemList.add(dummyTopAdsShopItem())
    }
    return topAdsShopItemList
}
