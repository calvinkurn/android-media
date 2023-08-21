package com.tokopedia.stories.internal

import android.content.Intent

/**
 * Created by kenny.hadisaputra on 21/08/23
 */
private const val EXTRA_ALL_STORIES_SEEN = "all_stories_seen"
private const val EXTRA_STORIES_SHOP_ID = "stories_shop_id"

fun Intent.putAllStoriesSeenExtra(allStoriesSeen: Boolean) =
    putExtra(EXTRA_ALL_STORIES_SEEN, allStoriesSeen)

fun Intent.getAllStoriesSeenExtra(): Boolean = getBooleanExtra(EXTRA_ALL_STORIES_SEEN, false)

fun Intent.putStoriesShopId(shopId: String) =
    putExtra(EXTRA_STORIES_SHOP_ID, shopId)

fun Intent.getStoriesShopId(): String = getStringExtra(EXTRA_STORIES_SHOP_ID).orEmpty()
