package com.tokopedia.stories.common.domain

import com.tokopedia.stories.common.StoriesKey

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
interface StoriesAvatarRepository {

    suspend fun setHasSeenCoachMark()

    suspend fun hasSeenCoachMark(): Boolean

    suspend fun getShopStoriesState(
        key: StoriesKey,
        shopIds: List<String>
    ): List<ShopStoriesState>
}
