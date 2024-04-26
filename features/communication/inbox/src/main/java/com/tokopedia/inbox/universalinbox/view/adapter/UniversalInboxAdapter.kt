package com.tokopedia.inbox.universalinbox.view.adapter

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
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationTitleUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsBannerUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxTopAdsVerticalBannerUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxWidgetMetaUiModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import timber.log.Timber

class UniversalInboxAdapter(
    private val typeFactory: UniversalInboxTypeFactory
) : BaseListAdapter<Visitable<in UniversalInboxTypeFactory>, UniversalInboxTypeFactory>(
    typeFactory
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        val holder = typeFactory.createViewHolder(view, viewType)
        val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when (viewType) {
            UniversalInboxMenuItemViewHolder.LAYOUT -> layout.isFullSpan = true
            UniversalInboxMenuSeparatorViewHolder.LAYOUT -> layout.isFullSpan = true
            UniversalInboxWidgetMetaViewHolder.LAYOUT -> layout.isFullSpan = true
            UniversalInboxRecommendationWidgetViewHolder.LAYOUT -> layout.isFullSpan = true
            UniversalInboxTopAdsBannerViewHolder.LAYOUT -> layout.isFullSpan = true
            UniversalInboxTopAdsHeadlineViewHolder.LAYOUT -> layout.isFullSpan = true
            UniversalInboxRecommendationLoaderViewHolder.LAYOUT -> layout.isFullSpan = true
            UniversalInboxRecommendationTitleViewHolder.LAYOUT -> layout.isFullSpan = true
        }
        return holder
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        try {
            @Suppress("UNCHECKED_CAST")
            (holder as AbstractViewHolder<Visitable<*>>).bind(visitables[position])
        } catch (throwable: Throwable) {
            Timber.d(throwable)
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
    private var menuSeparatorPosition: Int? = null
    private var recommendationFirstPosition: Int? = null
    private var recommendationTitlePosition: Int? = null
    private var topAdsBannerFirstPosition: Int? = null

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

    fun getProductRecommendationFirstPosition(
        list: List<Visitable<in UniversalInboxTypeFactory>> = visitables
    ): Int? {
        return if (checkCachedRecommendationFirstPosition()) {
            recommendationFirstPosition
        } else {
            // get first index or -1
            list.indexOfFirst {
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

    fun removeProductRecommendationLoader() {
        getFirstLoadingPosition()?.let {
            tryRemoveItemAtPosition(it)
        }
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

    fun getMenuSeparatorPosition(): Int? {
        return if (checkCachedMenuSeparatorPosition()) {
            menuSeparatorPosition
        } else {
            // get first index or -1
            visitables.indexOfFirst {
                it is UniversalInboxMenuSeparatorUiModel
            }.takeIf { it >= 0 }?.also { // get result when it not -1 (found)
                menuSeparatorPosition = it
            }
        }
    }

    private fun checkCachedMenuSeparatorPosition(): Boolean {
        return menuSeparatorPosition?.let {
            it < visitables.size && visitables[it] is UniversalInboxMenuSeparatorUiModel
        } ?: false
    }

    private fun getTopAdsBannerFirstPosition(): Int? {
        return if (checkCachedTopAdsBannerFirstPosition()) {
            topAdsBannerFirstPosition
        } else {
            // get first index or -1
            visitables.indexOfFirst {
                it is UniversalInboxTopAdsBannerUiModel
            }.takeIf { it >= 0 }?.also { // get result when it not -1 (found)
                topAdsBannerFirstPosition = it
            }
        }
    }

    private fun checkCachedTopAdsBannerFirstPosition(): Boolean {
        return topAdsBannerFirstPosition?.let {
            it < visitables.size && visitables[it] is UniversalInboxTopAdsBannerUiModel
        } ?: false
    }

    fun updateFirstTopAdsBanner(listAds: List<TopAdsImageUiModel>) {
        getTopAdsBannerFirstPosition()?.let {
            if (visitables[it] is UniversalInboxTopAdsBannerUiModel) {
                (visitables[it] as UniversalInboxTopAdsBannerUiModel).ads = listAds
                updateItems(visitables)
            }
        }
    }

    fun isWidgetMetaAdded(): Boolean {
        return visitables.firstOrNull() is UniversalInboxWidgetMetaUiModel
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

    fun tryAddProductRecommendation(
        title: String,
        newList: List<Visitable<in UniversalInboxTypeFactory>>
    ) {
        try {
            val editedList = visitables.toMutableList()
            // Add title if not exist yet
            if (getRecommendationTitlePosition() == null && title.isNotBlank()) {
                editedList.add(UniversalInboxRecommendationTitleUiModel(title))
            }
            // Add Vertical Banner to the list
            val productListWithVerticalBanner = addVerticalBannerToProductList(newList)
            // Update RV
            editedList.addAll(productListWithVerticalBanner)
            updateItems(editedList)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun addVerticalBannerToProductList(
        productList: List<Visitable<in UniversalInboxTypeFactory>>
    ): List<Visitable<in UniversalInboxTypeFactory>> {
        val interval = 7 // For every 8th position, add vertical banner
        val listWithVerticalBanner = mutableListOf<Visitable<in UniversalInboxTypeFactory>>()

        for ((index, item) in productList.withIndex()) {
            if (index == interval) {
                listWithVerticalBanner.add(UniversalInboxTopAdsVerticalBannerUiModel())
            }
            // Add the current item
            listWithVerticalBanner.add(item)
        }
        return listWithVerticalBanner
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

    fun tryRemoveProductRecommendation() {
        try {
            if (visitables.isEmpty()) return
            getProductRecommendationFirstPosition()?.let { startPosition ->
                val editedList = visitables.slice(0 until startPosition)
                updateItems(editedList)
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    fun getInboxItem(position: Int): Visitable<in UniversalInboxTypeFactory> {
        return visitables[position]
    }
}
