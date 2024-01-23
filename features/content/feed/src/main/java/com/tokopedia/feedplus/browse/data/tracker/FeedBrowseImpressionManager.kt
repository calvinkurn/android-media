package com.tokopedia.feedplus.browse.data.tracker

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.content.analytic.impression.OneTimeImpressionManager
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationMap
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 06/11/23
 */
@ActivityScope
internal class FeedBrowseImpressionManager @Inject constructor() {

    private val channelImpressionManagerMap = mutableMapOf<String, OneTimeImpressionManager<String>>()
    private val menuImpressionManagerMap = mutableMapOf<String, OneTimeImpressionManager<String>>()
    private val inspirationBannerImpressionManagerMap = mutableMapOf<String, OneTimeImpressionManager<String>>()

    private val oldWidgetsMap = mutableMapOf<String, FeedBrowseSlotUiModel>()

    private var oldCategoryInspirationData: CategoryInspirationDataContainer =
        CategoryInspirationDataContainer(emptyMap(), "")

    fun impress(slotId: String, key: PlayWidgetChannelUiModel, onImpress: () -> Unit) {
        getChannelImpressionManager(slotId)
            .impress(key.channelId, onImpress)
    }

    fun impress(slotId: String, menu: WidgetMenuModel, onImpress: () -> Unit) {
        getMenuImpressionManager(slotId)
            .impress(menu.id, onImpress)
    }

    fun impress(slotId: String, banner: BannerWidgetModel, onImpress: () -> Unit) {
        getMenuImpressionManager(slotId)
            .impress(banner.title, onImpress)
    }

    fun impress(slotId: String, author: AuthorWidgetModel, onImpress: () -> Unit) {
        getChannelImpressionManager(slotId)
            .impress(author.id, onImpress)
    }

    fun onNewWidgets(widgets: List<FeedBrowseSlotUiModel>) {
        val newWidgetsMap = widgets.associateBy { it.slotId }
        doCompare(oldWidgetsMap, newWidgetsMap)
        oldWidgetsMap.clear()
        oldWidgetsMap.putAll(newWidgetsMap)
    }

    fun onNewWidgets(data: CategoryInspirationMap, selectedMenuId: String) {
        val newData = CategoryInspirationDataContainer(data, selectedMenuId)
        doCompare(oldCategoryInspirationData, newData)
        oldCategoryInspirationData = newData
    }

    private fun doCompare(
        oldWidgets: Map<String, FeedBrowseSlotUiModel>,
        newWidgets: Map<String, FeedBrowseSlotUiModel>
    ) {
        newWidgets.forEach { entry ->
            val oldValue = oldWidgets[entry.key] ?: return@forEach
            when (val value = entry.value) {
                is FeedBrowseSlotUiModel.ChannelsWithMenus -> {
                    value.doCompare(entry.key, oldValue)
                }
                is FeedBrowseSlotUiModel.InspirationBanner -> {
                    value.doCompare(entry.key, oldValue)
                }
                else -> {}
            }
        }
    }

    private fun doCompare(
        oldData: CategoryInspirationDataContainer,
        newData: CategoryInspirationDataContainer,
    ) {
        if (oldData.data.keys != newData.data.keys) {
            resetMenuImpression("")
        }

        if (oldData.selectedMenuId != newData.selectedMenuId) {
            resetChannelImpression("")
        }
    }

    private fun FeedBrowseSlotUiModel.ChannelsWithMenus.doCompare(slotId: String, oldValue: FeedBrowseSlotUiModel) {
        if (oldValue !is FeedBrowseSlotUiModel.ChannelsWithMenus) {
            resetMenuImpression(slotId)
            resetChannelImpression(slotId)
            return
        }
        if (menus.keys != oldValue.menus.keys) {
            resetMenuImpression(slotId)
        }
        if (selectedMenuId != oldValue.selectedMenuId) {
            resetChannelImpression(slotId)
        }
        val newSelectedMenu = menus.keys.firstOrNull { it.id == selectedMenuId } ?: return
        val oldSelectedMenu = oldValue.menus.keys.firstOrNull { it.id == oldValue.selectedMenuId } ?: return
        val newSelectedMenuChannelIds = menus[newSelectedMenu]!!.items.map { it.channelId }
        val oldSelectedMenuChannelIds = oldValue.menus[oldSelectedMenu]!!.items.map { it.channelId }
        val impressionManager = getChannelImpressionManager(slotId)
        (oldSelectedMenuChannelIds - newSelectedMenuChannelIds.toSet()).forEach {
            impressionManager.clear(it)
        }
    }

    private fun FeedBrowseSlotUiModel.InspirationBanner.doCompare(slotId: String, oldValue: FeedBrowseSlotUiModel) {
        if (oldValue !is FeedBrowseSlotUiModel.InspirationBanner) {
            resetInspirationBannerImpression(slotId)
            return
        }
        val impressionManager = getInspirationBannerImpressionManager(slotId)
        (oldValue.bannerList - bannerList.toSet()).forEach {
            impressionManager.clear(it.title)
        }
    }

    private fun resetMenuImpression(slotId: String) = getMenuImpressionManager(slotId).reset()

    private fun resetChannelImpression(slotId: String) = getChannelImpressionManager(slotId).reset()

    private fun resetInspirationBannerImpression(slotId: String) = getInspirationBannerImpressionManager(slotId).reset()

    private fun getChannelImpressionManager(slotId: String): OneTimeImpressionManager<String> {
        return channelImpressionManagerMap[slotId] ?: OneTimeImpressionManager<String>().also {
            channelImpressionManagerMap[slotId] = it
        }
    }

    private fun getMenuImpressionManager(slotId: String): OneTimeImpressionManager<String> {
        return menuImpressionManagerMap[slotId] ?: OneTimeImpressionManager<String>().also {
            menuImpressionManagerMap[slotId] = it
        }
    }

    private fun getInspirationBannerImpressionManager(slotId: String): OneTimeImpressionManager<String> {
        return inspirationBannerImpressionManagerMap[slotId] ?: OneTimeImpressionManager<String>().also {
            inspirationBannerImpressionManagerMap[slotId] = it
        }
    }

    private data class CategoryInspirationDataContainer(
        val data: CategoryInspirationMap,
        val selectedMenuId: String,
    )
}
