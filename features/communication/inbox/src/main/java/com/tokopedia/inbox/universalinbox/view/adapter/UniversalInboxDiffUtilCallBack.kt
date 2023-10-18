package com.tokopedia.inbox.universalinbox.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopadsHeadlineUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel

class UniversalInboxDiffUtilCallBack(
    private val oldList: List<Visitable<in UniversalInboxTypeFactory>>,
    private val newList: List<Visitable<in UniversalInboxTypeFactory>>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newItem = newList[newItemPosition]
        val oldItem = oldList[oldItemPosition]
        return when {
            // Menu items name are the same
            (newItem is UniversalInboxMenuUiModel && oldItem is UniversalInboxMenuUiModel) ->
                UniversalInboxMenuUiModel.areItemsTheSame(oldItem, newItem)

            // Only one separator should exist
            (
                newItem is UniversalInboxMenuSeparatorUiModel &&
                    oldItem is UniversalInboxMenuSeparatorUiModel
                ) -> true

            // TopAds banner ads could be changed
            (
                newItem is UniversalInboxTopAdsBannerUiModel &&
                    oldItem is UniversalInboxTopAdsBannerUiModel
                ) -> newItem.requested == oldItem.requested && newItem.ads == oldItem.ads

            // TopAds headline won't change without refresh
            (
                newItem is UniversalInboxTopadsHeadlineUiModel &&
                    oldItem is UniversalInboxTopadsHeadlineUiModel
                ) -> true
            (
                newItem is UniversalInboxWidgetMetaUiModel &&
                    oldItem is UniversalInboxWidgetMetaUiModel
                ) -> true

            // Loader is always the same
            (
                newItem is LoadingMoreModel &&
                    oldItem is LoadingMoreModel
                ) -> true

            // Recommendation title is always the same
            (
                newItem is UniversalInboxRecommendationTitleUiModel &&
                    oldItem is UniversalInboxRecommendationTitleUiModel
                ) -> UniversalInboxRecommendationTitleUiModel.areItemsTheSame(oldItem, newItem)

            // Recommendation Item has product id
            (
                newItem is UniversalInboxRecommendationUiModel &&
                    oldItem is UniversalInboxRecommendationUiModel
                ) -> newItem.recommendationItem == oldItem.recommendationItem

            else -> newItem == oldItem
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val newItem = newList[newItemPosition]
        val oldItem = oldList[oldItemPosition]
        return when {
            // Menu items contents are the same
            (newItem is UniversalInboxMenuUiModel && oldItem is UniversalInboxMenuUiModel) ->
                UniversalInboxMenuUiModel.areContentsTheSame(oldItem, newItem)

            // Widget items contents are the same
            (
                newItem is UniversalInboxWidgetMetaUiModel &&
                    oldItem is UniversalInboxWidgetMetaUiModel
                ) ->
                UniversalInboxWidgetMetaUiModel.areContentsTheSame(oldItem, newItem)

            // Product recommendation has its own checker
            (
                newItem is UniversalInboxRecommendationUiModel &&
                    oldItem is UniversalInboxRecommendationUiModel
                ) -> newItem.recommendationItem == oldItem.recommendationItem

            // TopAds contents mark as different to trigger render
            (
                newItem is UniversalInboxTopAdsBannerUiModel &&
                    oldItem is UniversalInboxTopAdsBannerUiModel
                ) -> false

            // Put the contents check above, else default true to skip it
            else -> true
        }
    }
}
