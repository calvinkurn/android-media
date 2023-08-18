package com.tokopedia.stories.common.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.MAINAPP_DISABLED_STORIES_ENTRY_POINTS
import com.tokopedia.remoteconfig.RemoteConfigKey.SELLERAPP_DISABLED_STORIES_ENTRY_POINTS
import com.tokopedia.stories.common.domain.GetShopStoriesStatusUseCase
import com.tokopedia.stories.common.domain.StoriesKey
import com.tokopedia.stories.common.domain.ShopStoriesState
import com.tokopedia.stories.common.domain.StoriesAvatarRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
internal class StoriesAvatarRepositoryImpl @Inject constructor(
    private val prefUtil: StoriesAvatarPreferenceUtil,
    private val getShopStoriesUseCase: GetShopStoriesStatusUseCase,
    private val remoteConfig: RemoteConfig,
    private val dispatchers: CoroutineDispatchers
) : StoriesAvatarRepository {

    private var mHasSeen = false

    override suspend fun setHasSeenCoachMark() {
        prefUtil.setHasSeenCoachMark()
    }

    override suspend fun hasSeenCoachMark(): Boolean {
        return prefUtil.hasSeenCoachMark()
    }

    override suspend fun getShopStoriesState(
        key: StoriesKey,
        shopIds: List<String>
    ): List<ShopStoriesState> = withContext(dispatchers.io) {
        val isEntryPointAllowed = isEntryPointAllowed(key)
        if (!isEntryPointAllowed) return@withContext emptyList()

        delay(1000)
        shopIds.map { shopId ->
            val shopInt = shopId.toInt()
            ShopStoriesState(
                shopId,
                anyStoryExisted = true,
                hasUnseenStories = true,
                "tokopedia://play/12669"
            )
        }
//        val response = getShopStoriesUseCase(
//            params = GetShopStoriesStatusUseCase.Request.create(
//                key,
//                shopIds.map {
//                    GetShopStoriesStatusUseCase.Request.Author.create(
//                        it,
//                        GetShopStoriesStatusUseCase.Request.Author.Type.Shop
//                    )
//                }
//            )
//        )

//        return@withContext response.response.data.map {
//            ShopStoriesState(
//                shopId = it.id,
//                anyStoryExisted = it.hasStory,
//                hasUnseenStories = it.isUnseenStoryExist,
//                appLink = it.appLink,
//            )
//        }
    }

    private fun isEntryPointAllowed(key: StoriesKey): Boolean {
        val disabledEntryPoints = getDisabledEntryPoints()
        return !disabledEntryPoints.contains(key.key)
    }

    private fun getDisabledEntryPoints(): List<String> {
        val key = getRemoteConfigKey()

        return try {
            val rawConfig = remoteConfig.getString(key)
            val mappedConfig = GsonSingleton.instance.fromJson(
                rawConfig,
                StoriesEntryPointRemoteConfigResponse::class.java
            )
            mappedConfig.disabledEntryPoints
        } catch (e: NullPointerException) {
            emptyList()
        }
    }

    private fun getRemoteConfigKey(): String {
        return if (GlobalConfig.isSellerApp()) {
            SELLERAPP_DISABLED_STORIES_ENTRY_POINTS
        } else {
            MAINAPP_DISABLED_STORIES_ENTRY_POINTS
        }
    }
}
