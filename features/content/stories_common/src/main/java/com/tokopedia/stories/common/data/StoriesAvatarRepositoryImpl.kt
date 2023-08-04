package com.tokopedia.stories.common.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.stories.common.domain.ShopStoriesState
import com.tokopedia.stories.common.domain.StoriesAvatarRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
class StoriesAvatarRepositoryImpl @Inject constructor(
    private val prefUtil: StoriesAvatarPreferenceUtil,
    private val dispatchers: CoroutineDispatchers
) : StoriesAvatarRepository {

    private var mHasSeen = false

    override suspend fun setHasSeenCoachMark() {
//        prefUtil.setHasSeenCoachMark()
        mHasSeen = true
    }

    override suspend fun hasSeenCoachMark(): Boolean {
//        return prefUtil.hasSeenCoachMark()
        return mHasSeen
    }

    override suspend fun getShopStoriesState(shopIds: List<String>): List<ShopStoriesState> =
        withContext(dispatchers.io) {
            delay(500)
            shopIds.map { shopId ->
                val shopInt = shopId.toInt()
                ShopStoriesState(
                    shopId,
//                    anyStoryExisted = shopInt % 2 == 0,
//                    anyStoryExisted = shopInt % 2 == 0,
                    anyStoryExisted = true,
                    hasUnseenStories = true,
                    "tokopedia://play/12669"
                )
            }
        }
}
