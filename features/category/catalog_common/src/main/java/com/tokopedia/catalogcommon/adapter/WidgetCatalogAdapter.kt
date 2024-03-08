package com.tokopedia.catalogcommon.adapter

import android.os.Handler
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.catalogcommon.OnStickySingleHeaderListener
import com.tokopedia.catalogcommon.StickySingleHeaderView
import com.tokopedia.catalogcommon.uimodel.BaseCatalogUiModel
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.catalogcommon.uimodel.SellerOfferingUiModel
import com.tokopedia.catalogcommon.uimodel.StickyNavigationUiModel
import com.tokopedia.catalogcommon.viewholder.StickyTabNavigationViewHolder
import com.tokopedia.kotlin.extensions.orFalse

class WidgetCatalogAdapter(
    private val baseListAdapterTypeFactory: CatalogAdapterFactoryImpl
) : BaseAdapter<CatalogAdapterFactoryImpl>(
    baseListAdapterTypeFactory
),
    StickySingleHeaderView.OnStickySingleHeaderAdapter {

    private var recyclerView: RecyclerView? = null
    private val differ = CatalogDifferImpl()
    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null

    fun addWidget(itemList: List<Visitable<*>>) {
        val diffUtilCallback = differ.create(visitables, itemList)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(itemList)
        result.dispatchUpdatesTo(this)
    }

    fun changeComparison(comparison: ComparisonUiModel) {
        visitables.forEachIndexed { index, visitable ->
            if (visitable is ComparisonUiModel) {
                visitables[index] = comparison
                notifyItemChanged(index)
            }
        }
    }

    fun displayVariantTextToSellerOffering(variantText: String) {
        visitables.forEachIndexed { index, visitable ->
            if (visitable is SellerOfferingUiModel) {
                (visitables[index] as SellerOfferingUiModel).variantsName = variantText
                notifyItemChanged(index)
            }
        }
    }

    fun isVisitableEmpty() = visitables.isEmpty()

    private fun refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView?.post { onStickySingleHeaderViewListener?.refreshSticky() }
        }
    }

    override val stickyHeaderPosition: Int
        get() = findPositionNavigation()

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return baseListAdapterTypeFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is StickyTabNavigationViewHolder) {
            val navigation = visitables.find {
                it is StickyNavigationUiModel
            } as? StickyNavigationUiModel
            navigation?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener
    }

    override fun updateEtalaseListViewHolderData() {
        if (recyclerView?.isComputingLayout == false) {
            Handler().post {
                val positionNavigation = findPositionNavigation()
                if (positionNavigation != -1) {
                    notifyItemChanged(positionNavigation)
                }
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
        super.onDetachedFromRecyclerView(recyclerView)
    }

    fun autoSelectNavigation(position: Int) {
        val indexNavigation = visitables.indexOfFirst {
            it is StickyNavigationUiModel
        }

        val navigation = visitables.getOrNull(indexNavigation) as? StickyNavigationUiModel

        val currentWidget = visitables.getOrNull(position) as? BaseCatalogUiModel

        navigation?.let { stickyNav ->
            val indexPartOfNavigation = stickyNav.content.indexOfFirst {
                it.anchorWidgets.contains(currentWidget?.widgetName.orEmpty())
            }

            changeNavigationTabActive(indexPartOfNavigation)
        }
    }

    fun autoSelectNavigation(currentWidgetName: String) {
        val indexNavigation = visitables.indexOfFirst {
            it is StickyNavigationUiModel
        }

        val navigation = visitables.getOrNull(indexNavigation) as? StickyNavigationUiModel

        navigation?.let { stickyNav ->
            val indexPartOfNavigation = stickyNav.content.indexOfFirst {
                it.anchorWidgets.contains(currentWidgetName)
            }

            changeNavigationTabActive(indexPartOfNavigation)
        }
    }

    fun changeNavigationTabActive(
        tabPosition: Int
    ) {
        val indexNavigation = visitables.indexOfFirst {
            it is StickyNavigationUiModel
        }

        val navigation = visitables.getOrNull(indexNavigation) as? StickyNavigationUiModel

        if (tabPosition != navigation?.currentSelectTab) {
            navigation?.currentSelectTab = tabPosition
            visitables[indexNavigation] = navigation
            if (!onStickySingleHeaderViewListener?.isStickyShowed.orFalse()) {
                notifyItemChanged(indexNavigation)
            } else {
                Handler().post {
                    notifyItemChanged(indexNavigation)
                }
                refreshSticky()
            }
        }
    }

    fun findPositionWidget(widgetName: String): Int {
        return visitables.indexOfFirst {
            val uiModel = it as BaseCatalogUiModel
            uiModel.widgetName == widgetName
        }
    }

    fun findPositionNavigation(): Int {
        val index = visitables.indexOfFirst {
            it is StickyNavigationUiModel
        }
        return index
    }

    fun findNavigationCount(): Int {
        val index = visitables.indexOfFirst {
            it is StickyNavigationUiModel
        }
        if (index != -1) {
            val nav = visitables[index] as StickyNavigationUiModel
            return nav.content.size
        }
        return index
    }

    fun findNavigationCurrentTab(): Int {
        return (visitables.getOrNull(findPositionNavigation()) as? StickyNavigationUiModel)?.currentSelectTab
            ?: -1
    }
}
