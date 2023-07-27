package com.tokopedia.stories.common.domain

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
interface StoriesAvatarRepository {

    suspend fun setHasSeenCoachMark()

    suspend fun hasSeenCoachMark(): Boolean

    suspend fun getShopStoriesState(shopIds: List<String>): List<ShopStoriesState>
}
