package com.tokopedia.inbox.universalinbox.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxMenuItemViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxMenuSeparatorViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxRecommendationLoaderViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxRecommendationTitleViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxRecommendationWidgetViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxTopAdsBannerViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxTopAdsHeadlineViewHolder
import com.tokopedia.inbox.universalinbox.view.adapter.viewholder.UniversalInboxWidgetMetaViewHolder
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import timber.log.Timber

class UniversalInboxAdapter(
    private val typeFactory: UniversalInboxTypeFactory
) : BaseListAdapter<Visitable<in UniversalInboxTypeFactory>, UniversalInboxTypeFactory>(
    typeFactory
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        try {
            val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            when (getItemViewType(position)) {
                UniversalInboxMenuItemViewHolder.LAYOUT -> layout.isFullSpan = true
                UniversalInboxMenuSeparatorViewHolder.LAYOUT -> layout.isFullSpan = true
                UniversalInboxWidgetMetaViewHolder.LAYOUT -> layout.isFullSpan = true
                UniversalInboxRecommendationWidgetViewHolder.LAYOUT -> layout.isFullSpan = true
                UniversalInboxTopAdsBannerViewHolder.LAYOUT -> layout.isFullSpan = true
                UniversalInboxTopAdsHeadlineViewHolder.LAYOUT -> layout.isFullSpan = true
                UniversalInboxRecommendationLoaderViewHolder.LAYOUT -> layout.isFullSpan = true
                UniversalInboxRecommendationTitleViewHolder.LAYOUT -> layout.isFullSpan = true
            }
            (holder as AbstractViewHolder<Visitable<*>>).bind(visitables[position])
        } catch (throwable: Throwable) {
            Timber.e(throwable)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < 0 || position >= itemCount) {
            HideViewHolder.LAYOUT
        } else {
            visitables[position].type(typeFactory)
        }
    }

    override fun onViewRecycled(holder: AbstractViewHolder<*>) {
        holder.onViewRecycled()
    }

    private var recommendationFirstPosition: Int? = null
    private var recommendationTitlePosition: Int? = null

    private val loaderUiModel by lazy {
        LoadingMoreModel()
    }
    private fun updateItems(newList: List<Visitable<in UniversalInboxTypeFactory>>) {
        try {
            val diffResult = DiffUtil.calculateDiff(
                UniversalInboxDiffUtilCallBack(visitables, newList)
            )
            visitables = newList
            diffResult.dispatchUpdatesTo(this)
        } catch (throwable: Throwable) {
            Timber.e(throwable)
        }
    }

    fun getProductRecommendationFirstPosition(): Int? {
        return if (checkCachedRecommendationFirstPosition()) {
            recommendationFirstPosition
        } else {
            // get first index or -1
            visitables.indexOfFirst {
                it is UniversalInboxRecommendationUiModel
            }.takeIf { it >= 0 }?.also { // get result when it not -1 (found)
                recommendationFirstPosition = it
            }
        }
    }

    private fun checkCachedRecommendationFirstPosition(): Boolean {
        return recommendationFirstPosition?.let {
            it < visitables.size && visitables[it] is UniversalInboxRecommendationUiModel
        } ?: false
    }

    private fun isRecommendationLoader(position: Int): Boolean {
        return visitables[position]::class == LoadingMoreModel::class
    }

    private fun getFirstLoadingPosition(): Int? {
        return if (visitables.isEmpty()) {
            null
        } else if (isRecommendationLoader(visitables.lastIndex)) {
            visitables.lastIndex
        } else {
            // get first index or -1
            visitables.indexOfFirst {
                it is LoadingMoreModel
            }.takeIf { it >= 0 } // get result when it not -1 (found)
        }
    }

    fun addProductRecommendationLoader() {
        if (getFirstLoadingPosition() != null) return
        tryAddItemAtPosition(itemCount, loaderUiModel)
    }

    private fun getRecommendationTitlePosition(): Int? {
        return if (checkCachedRecommendationTitlePosition()) {
            recommendationTitlePosition
        } else {
            // get first index or -1
            visitables.indexOfFirst {
                it is UniversalInboxRecommendationTitleUiModel
            }.takeIf { it >= 0 }?.also { // get result when it not -1 (found)
                recommendationTitlePosition = it
            }
        }
    }

    private fun checkCachedRecommendationTitlePosition(): Boolean {
        return recommendationTitlePosition?.let {
            it < visitables.size && visitables[it] is UniversalInboxRecommendationTitleUiModel
        } ?: false
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFirstTopAdsBanner(listAds: List<TopAdsImageViewModel>) {
        visitables.forEach loop@{ item ->
            if (item is UniversalInboxTopAdsBannerUiModel) {
                item.ads = listAds
                return@loop
            }
        }
        // Need to use notify data set changed, because need to trigger TDN's onAttachedToWindow
        // onAttachedToWindow is needed to be recalled after we get TDN response
        notifyDataSetChanged()
    }

    private fun isWidgetMetaAdded(): Boolean {
        return visitables.firstOrNull() is UniversalInboxWidgetMetaUiModel
    }

    fun getWidgetPosition(widgetType: Int): Int {
        var position = -1
        try {
            if (isWidgetMetaAdded()) {
                val widgetMetaUiModel = visitables.firstOrNull() as? UniversalInboxWidgetMetaUiModel
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

    fun tryUpdateMenuItemsAtPosition(newList: List<Visitable<in UniversalInboxTypeFactory>>) {
        try {
            val editedList = visitables.toMutableList()
            val fromIndex = if (isWidgetMetaAdded()) 1 else 0
            editedList.subList(
                fromIndex = fromIndex,
                // toIndex matched with item count
                toIndex = getPositionBeforeProductRecommendation() ?: itemCount
            ).apply {
                clear()
                addAll(newList) // replace the sublist
            }
            updateItems(editedList)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun getPositionBeforeProductRecommendation(): Int? {
        return getRecommendationTitlePosition() ?: // Get title product recom position
            getProductRecommendationFirstPosition() ?: // Get product recom first position
            getFirstLoadingPosition()
    }

    fun tryUpdateProductRecommendations(
        title: String,
        newList: List<Visitable<in UniversalInboxTypeFactory>>
    ) {
        try {
            val editedList = visitables.toMutableList()
            if (getRecommendationTitlePosition() == null && title.isNotBlank()) {
                editedList.add(
                    UniversalInboxRecommendationTitleUiModel(title)
                )
            }
            val productRecommendationFirstPosition = getProductRecommendationFirstPosition()
            if (productRecommendationFirstPosition != null) {
                editedList.subList(
                    fromIndex = productRecommendationFirstPosition,
                    toIndex = itemCount
                ).apply {
                    clear()
                    addAll(newList) // replace the sublist
                }
            } else {
                editedList.addAll(newList)
            }
            editedList.remove(loaderUiModel) // Remove loader if any
            updateItems(editedList)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    fun tryUpdateWidgetMeta(item: UniversalInboxWidgetMetaUiModel) {
        try {
            val widgetMetaUiModel = visitables.firstOrNull() as? UniversalInboxWidgetMetaUiModel
            if (widgetMetaUiModel == null) {
                tryAddItemAtPosition(0, item)
            } else {
                // Replace item in the list
                val editedList = visitables.toMutableList()
                editedList[0] = item
                updateItems(editedList)
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun tryAddItemAtPosition(position: Int, item: Visitable<in UniversalInboxTypeFactory>) {
        try {
            // Add to the list
            val editedList = visitables.toMutableList()
            editedList.add(position, item)
            updateItems(editedList)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    fun tryRemoveItemAtPosition(position: Int) {
        try {
            if (visitables.isEmpty()) return
            val editedList = visitables.toMutableList()
            editedList.removeAt(position)
            updateItems(editedList)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }
}
