package com.tokopedia.inbox.universalinbox.view.adapter

import com.tokopedia.adapterdelegate.BaseCommonAdapter
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxMenuItemDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxMenuSeparatorDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxRecommendationDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxRecommendationLoaderDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxRecommendationTitleDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxRecommendationWidgetDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxTopAdsBannerDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxTopAdsHeadlineDelegate
import com.tokopedia.inbox.universalinbox.view.adapter.delegate.UniversalInboxWidgetMetaDelegate
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxMenuListener
import com.tokopedia.inbox.universalinbox.view.listener.UniversalInboxWidgetListener
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationLoaderUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopadsHeadlineUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetModel
import com.tokopedia.topads.sdk.listener.TdnBannerResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber

class UniversalInboxAdapter(
    userSession: UserSessionInterface,
    widgetListener: UniversalInboxWidgetListener,
    menuListener: UniversalInboxMenuListener,
    tdnBannerResponseListener: TdnBannerResponseListener,
    topAdsClickListener: TopAdsImageViewClickListener,
    recommendationListener: RecommendationListener
) : BaseCommonAdapter() {

    init {
        delegatesManager.addDelegate(UniversalInboxWidgetMetaDelegate(widgetListener))
        delegatesManager.addDelegate(UniversalInboxMenuItemDelegate(menuListener))
        delegatesManager.addDelegate(
            UniversalInboxTopAdsBannerDelegate(tdnBannerResponseListener, topAdsClickListener)
        )
        delegatesManager.addDelegate(UniversalInboxRecommendationWidgetDelegate())
        delegatesManager.addDelegate(UniversalInboxRecommendationTitleDelegate())
        delegatesManager.addDelegate(UniversalInboxRecommendationDelegate(recommendationListener))
        delegatesManager.addDelegate(UniversalInboxRecommendationLoaderDelegate())
        delegatesManager.addDelegate(UniversalInboxMenuSeparatorDelegate())
        delegatesManager.addDelegate(UniversalInboxTopAdsHeadlineDelegate(userSession))
    }

    private var recommendationViewType: Int? = null
    private var recommendationFirstPosition: Int? = null
    private var recommendationTitlePosition: Int? = null

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            // Menu items name are the same
            (newItem is UniversalInboxMenuUiModel && oldItem is UniversalInboxMenuUiModel) ->
                UniversalInboxMenuUiModel.areItemsTheSame(oldItem, newItem)

            // Only one separator should exist
            (
                newItem is UniversalInboxMenuSeparatorUiModel &&
                    oldItem is UniversalInboxMenuSeparatorUiModel
                ) -> true

            // Only one banner should exist
            (
                newItem is UniversalInboxTopAdsBannerUiModel &&
                    oldItem is UniversalInboxTopAdsBannerUiModel
                ) -> true

            // Only one recommendation widget should exist
            (
                newItem is RecommendationWidgetModel &&
                    oldItem is RecommendationWidgetModel
                ) -> true

            (
                newItem is UniversalInboxWidgetMetaUiModel &&
                    oldItem is UniversalInboxWidgetMetaUiModel
                ) -> true

            else -> newItem == oldItem
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
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

            // Put the contents check above, else default true to skip it
            else -> true
        }
    }

    fun getProductRecommendationViewType(): Int? {
        recommendationViewType?.let { return it }
        // get first index or -1
        return itemList.indexOfFirst {
            it is RecommendationItem
        }.takeIf {
            it >= 0 // get result when it not -1 (found)
        }?.let { index ->
            getItemViewType(index).also {
                recommendationViewType = it
            }
        }
    }

    fun getProductRecommendationFirstPosition(): Int? {
        return if (checkCachedRecommendationFirstPosition()) {
            recommendationFirstPosition
        } else {
            // get first index or -1
            itemList.indexOfFirst {
                it is RecommendationItem
            }.takeIf { it >= 0 }?.also { // get result when it not -1 (found)
                recommendationFirstPosition = it
            }
        }
    }

    private fun checkCachedRecommendationFirstPosition(): Boolean {
        return recommendationFirstPosition?.let {
            it < itemList.size && itemList[it] is RecommendationItem
        } ?: false
    }

    private fun isRecommendationLoader(position: Int): Boolean {
        return itemList[position]::class == UniversalInboxRecommendationLoaderUiModel::class
    }

    private fun getFirstLoadingPosition(): Int? {
        return if (itemList.isEmpty()) {
            null
        } else if (isRecommendationLoader(itemList.lastIndex)) {
            itemList.lastIndex
        } else {
            // get first index or -1
            itemList.indexOfFirst {
                it is UniversalInboxRecommendationLoaderUiModel
            }.takeIf { it >= 0 } // get result when it not -1 (found)
        }
    }

    fun addProductRecommendationLoader() {
        if (getFirstLoadingPosition() != null) return
        addItemAndAnimateChanges(UniversalInboxRecommendationLoaderUiModel())
    }

    fun removeProductRecommendationLoader() {
        getFirstLoadingPosition()?.let {
            removeItemAt(it)
            notifyItemRemoved(it)
        }
    }

    private fun getRecommendationTitlePosition(): Int? {
        return if (checkCachedRecommendationTitlePosition()) {
            recommendationTitlePosition
        } else {
            // get first index or -1
            itemList.indexOfFirst {
                it is UniversalInboxRecommendationTitleUiModel
            }.takeIf { it >= 0 }?.also { // get result when it not -1 (found)
                recommendationTitlePosition = it
            }
        }
    }

    private fun checkCachedRecommendationTitlePosition(): Boolean {
        return recommendationTitlePosition?.let {
            it < itemList.size && itemList[it] is UniversalInboxRecommendationTitleUiModel
        } ?: false
    }

    fun getFirstTopAdsBannerPositionPair(): Pair<Int, UniversalInboxTopAdsBannerUiModel>? {
        itemList.forEachIndexed { index, item ->
            if (item is UniversalInboxTopAdsBannerUiModel) {
                return Pair(index, item)
            }
        }
        return null
    }

    fun getTopAdsHeadlineCount(): Int {
        return itemList.count {
            it is UniversalInboxTopadsHeadlineUiModel
        }
    }

    private fun isWidgetMetaAdded(): Boolean {
        return itemList.firstOrNull() is UniversalInboxWidgetMetaUiModel
    }

    fun getWidgetPosition(widgetType: Int): Int {
        var position = -1
        try {
            if (isWidgetMetaAdded()) {
                val widgetMetaUiModel = itemList.firstOrNull() as? UniversalInboxWidgetMetaUiModel
                widgetMetaUiModel?.widgetList?.forEachIndexed { index, uiModel ->
                    if (uiModel.type == widgetType) {
                        position = index
                    }
                }
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
        return position
    }

    fun updateWidgetCounter(
        position: Int,
        counter: Int,
        additionalAction: (UniversalInboxWidgetUiModel) -> Unit = {}
    ) {
        (itemList.firstOrNull() as? UniversalInboxWidgetMetaUiModel)?.let {
            it.widgetList.getOrNull(position)?.let { uiModel ->
                uiModel.counter = counter
                additionalAction(uiModel)
            }
            notifyItemChanged(Int.ZERO)
        }
    }

    fun removeWidget(position: Int) {
        (itemList.firstOrNull() as? UniversalInboxWidgetMetaUiModel)
            ?.widgetList?.removeAt(position)
        notifyItemChanged(Int.ZERO) // notify item changed in page rv, first item
    }

    fun removeAllProductRecommendation() {
        getProductRecommendationFirstPosition()?.let {
            var itemCountToDrop = itemList.size - it
            if (it < itemList.size && // out of bound
                it > Int.ZERO && // not -1
                isRecommendationTitle(it - Int.ONE)
            ) {
                itemCountToDrop++
            }
            val result = itemList.dropLast(itemCountToDrop)
            clearAllItems() // clear all
            addItemsAndAnimateChanges(result) // add the widget & menu
        }
    }

    private fun isRecommendationTitle(position: Int): Boolean {
        return itemList[position]::class == UniversalInboxRecommendationTitleUiModel::class
    }

    fun tryUpdateMenuItemsAtPosition(newList: List<Any>) {
        try {
            val editedList = itemList.toMutableList()
            val fromIndex = if (isWidgetMetaAdded()) 1 else 0
            editedList.subList(
                fromIndex = fromIndex,
                toIndex = getPositionBeforeProductRecommendation() // toIndex matched with item count
            ).apply {
                clear()
                addAll(newList) // replace the sublist
            }
            setItemsAndAnimateChanges(editedList)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun getPositionBeforeProductRecommendation(): Int {
        val calculatedPosition = getRecommendationTitlePosition() ?: // Get title product recom position
            getProductRecommendationFirstPosition() ?: // Get product recom first position
            getFirstLoadingPosition() // Get first loading position
        return calculatedPosition ?: itemCount
    }

    fun tryUpdateProductRecommendations(title: String, newList: List<Any>) {
        try {
            val editedList = itemList.toMutableList()
            if (getRecommendationTitlePosition() == null && title.isNotBlank()) {
                editedList.add(UniversalInboxRecommendationTitleUiModel(title))
            }
            if (getProductRecommendationFirstPosition() != null) {
                editedList.subList(
                    fromIndex = getProductRecommendationFirstPosition() ?: lastIndex, // First recom or last index
                    toIndex = itemCount
                ).apply {
                    clear()
                    addAll(newList) // replace the sublist
                }
            } else {
                editedList.addAll(newList)
            }
            setItemsAndAnimateChanges(editedList)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    fun tryUpdateWidgetMeta(item: UniversalInboxWidgetMetaUiModel) {
        try {
            val widgetMetaUiModel = itemList.firstOrNull() as? UniversalInboxWidgetMetaUiModel
            if (widgetMetaUiModel == null) {
                tryAddItemAtPosition(0, item)
            } else {
                // Replace item in the list
                val editedList = itemList.toMutableList()
                editedList[0] = item
                setItemsAndAnimateChanges(editedList)
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    fun tryAddItemAtPosition(position: Int, item: Any) {
        try {
            // Add to the list
            val editedList = itemList.toMutableList()
            editedList.add(position, item)
            setItemsAndAnimateChanges(editedList)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }
}
