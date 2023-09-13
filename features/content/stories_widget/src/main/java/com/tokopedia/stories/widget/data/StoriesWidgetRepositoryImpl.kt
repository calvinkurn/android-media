package com.tokopedia.stories.widget.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.gson.GsonSingleton
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.MAINAPP_DISABLED_STORIES_ENTRY_POINTS
import com.tokopedia.remoteconfig.RemoteConfigKey.SELLERAPP_DISABLED_STORIES_ENTRY_POINTS
import com.tokopedia.stories.internal.StoriesPreferenceUtil
import com.tokopedia.stories.internal.storage.StoriesSeenStorage
import com.tokopedia.stories.widget.StoriesStatus
import com.tokopedia.stories.widget.domain.GetShopStoriesStatusUseCase
import com.tokopedia.stories.widget.domain.StoriesEntrySource
import com.tokopedia.stories.widget.domain.StoriesWidgetInfo
import com.tokopedia.stories.widget.domain.StoriesWidgetRepository
import com.tokopedia.stories.widget.domain.StoriesWidgetState
import com.tokopedia.stories.widget.domain.TimeMillis
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 27/07/23
 */
internal class StoriesWidgetRepositoryImpl @Inject constructor(
    private val prefUtil: StoriesPreferenceUtil,
    private val storiesSeenStorage: StoriesSeenStorage,
    private val getShopStoriesUseCase: GetShopStoriesStatusUseCase,
    private val remoteConfig: RemoteConfig,
    private val dispatchers: CoroutineDispatchers
) : StoriesWidgetRepository {

    override suspend fun setHasSeenCoachMark() {
        prefUtil.setHasAckStoriesFeature()
    }

    override suspend fun hasSeenCoachMark(): Boolean {
        return prefUtil.hasAckStoriesFeature()
    }

    override suspend fun getUpdatedSeenStatus(
        shopId: String,
        lastUpdated: TimeMillis
    ): Boolean = withContext(dispatchers.io) {
        return@withContext storiesSeenStorage.hasSeenAllAuthorStories(
            StoriesSeenStorage.Author.Shop(shopId),
            lastUpdated.time
        )
    }

    override suspend fun getStoriesWidgetInfo(
        entryPoint: StoriesEntrySource,
        shopIds: List<String>
    ): StoriesWidgetInfo = withContext(dispatchers.io) {
        val isEntryPointAllowed = isEntryPointAllowed(entryPoint)
        if (!isEntryPointAllowed) return@withContext StoriesWidgetInfo.Default

        val response = getShopStoriesUseCase(
            params = GetShopStoriesStatusUseCase.Request.create(
                entryPoint,
                shopIds.map {
                    GetShopStoriesStatusUseCase.Request.Author.create(
                        it,
                        GetShopStoriesStatusUseCase.Request.Author.Type.Shop
                    )
                }
            )
        )

        val result = response.response.data.map {
            StoriesWidgetState(
                shopId = it.id,
                status = getStoriesStatus(anyStoryExists = it.hasStory, hasUnseenStories = it.isUnseenStoryExist),
                appLink = it.appLink,
                updatedAt = TimeMillis.now(),
            )
        }

        result.forEach {
            if (it.status != StoriesStatus.AllStoriesSeen) return@forEach
            storiesSeenStorage.setSeenAllAuthorStories(StoriesSeenStorage.Author.Shop(it.shopId))
        }
        return@withContext StoriesWidgetInfo(
            result.associateBy { it.shopId },
            "Test CoachMark"
        )
    }

    private fun getStoriesStatus(anyStoryExists: Boolean, hasUnseenStories: Boolean): StoriesStatus {
        return when {
            !anyStoryExists -> StoriesStatus.NoStories
            hasUnseenStories -> StoriesStatus.HasUnseenStories
            else -> StoriesStatus.AllStoriesSeen
        }
    }

    private fun isEntryPointAllowed(key: StoriesEntrySource): Boolean {
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
