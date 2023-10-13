package com.tokopedia.feedplus.browse.presentation

import android.util.Log
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 13/10/23
 */
internal interface FeedBrowseModelFetcher {

    fun isSupported(model: FeedBrowseModel): Boolean

    suspend fun call(model: FeedBrowseModel): FeedBrowseModel
}

internal class FeedContentSlotFetcher @Inject constructor(
    private val repo: FeedBrowseRepository,
) : FeedBrowseModelFetcher {

    override fun isSupported(model: FeedBrowseModel): Boolean {
        return model is FeedBrowseModel.ChannelsWithMenus
    }

    override suspend fun call(model: FeedBrowseModel): FeedBrowseModel {
        return when (model) {
            is FeedBrowseModel.ChannelsWithMenus -> model.call()
            else -> error("Model $model is not supported")
        }
    }

    private suspend fun FeedBrowseModel.ChannelsWithMenus.call(): FeedBrowseModel {
        val menuKeys = menus.keys
        val selectedMenu = menuKeys.firstOrNull { it.isSelected }

        val requestModel = if (menus.isEmpty()) {
            Log.d("FeedBrowse", "Requesting: Slot $slotId, Group: $group")
            WidgetRequestModel(group = group)
        } else {
            val menu = selectedMenu ?: menuKeys.first()
            WidgetRequestModel(
                group = menu.group,
                sourceType = menu.sourceType,
                sourceId = menu.sourceId,
            )
        }

        return try {
            when (val response = repo.getWidgetContentSlot(requestModel)) {
                is ContentSlotModel.TabMenus -> {
                    copy(menus = response.menu.associateWith { emptyList() })
                }
                is ContentSlotModel.ChannelBlock -> {
                    if (menuKeys.isEmpty()) {
                        copy(menus = mapOf(WidgetMenuModel.Default to response.channels))
                    }
                    else {
                        val menu = selectedMenu ?: menuKeys.first()
                        copy(menus = menus + (menu to response.channels))
                    }
                }
            }
        } catch (e: IllegalStateException) { this }
    }
}

internal class FeedWidgetRecommendationFetcher @Inject constructor(
    private val repo: FeedBrowseRepository
) : FeedBrowseModelFetcher {

    override fun isSupported(model: FeedBrowseModel): Boolean {
        return model is FeedBrowseModel.InspirationBanner
    }

    override suspend fun call(model: FeedBrowseModel): FeedBrowseModel {
        return when (model) {
            is FeedBrowseModel.InspirationBanner -> model.call()
            else -> error("Model $model is not supported")
        }
    }

    private suspend fun FeedBrowseModel.InspirationBanner.call(): FeedBrowseModel {
        return when (val response = repo.getWidgetRecommendation(identifier)) {
            is WidgetRecommendationModel.Banners -> copy(bannerList = response.banners)
            else -> this
        }
    }
}

internal class FeedBrowseModelFetchers @Inject constructor(
    private val modelFetchers: Set<@JvmSuppressWildcards FeedBrowseModelFetcher>,
) {

    suspend fun call(model: FeedBrowseModel): FeedBrowseModel {
        val fetcher = modelFetchers.first { it.isSupported(model) }
        return fetcher.call(model)
    }
}
