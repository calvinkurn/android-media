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

    override suspend fun setHasSeenCoachMark() {
        prefUtil.setHasSeenCoachMark()
    }

    override suspend fun hasSeenCoachMark(): Boolean {
        return prefUtil.hasSeenCoachMark()
    }

    override suspend fun getShopStoriesState(shopId: String): ShopStoriesState =
        withContext(dispatchers.io) {
            delay(2000)
            ShopStoriesState(
                shopId,
                anyStoryExisted = true,
                hasUnseenStories = true,
                "tokopedia://play/12669"
            )
        }
}
