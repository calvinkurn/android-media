package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.ComparisonBpcListModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcItemModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcSeeMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactory
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.util.ComparisonBpcDiffUtil

/**
 * Created by Frenzel
 */
class ComparisonBpcWidgetAdapter(
    var adapterTypeFactory: ComparisonBpcTypeFactory
) : RecyclerView.Adapter<AbstractViewHolder<Visitable<ComparisonBpcTypeFactory>>>() {
    private var comparisonBpcListModel = ComparisonBpcListModel()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<ComparisonBpcTypeFactory>> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return adapterTypeFactory.createViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<ComparisonBpcTypeFactory>>, position: Int) {
        holder.bind(comparisonBpcListModel.getListItemsToShow()[position])
    }

    override fun getItemCount(): Int {
        return comparisonBpcListModel.getListItemsToShow().size
    }

    override fun getItemViewType(position: Int): Int {
        return comparisonBpcListModel.getListItemsToShow()[position].type(adapterTypeFactory)
    }

    fun submitList(model: ComparisonBpcListModel) {
        updateListAndCalculateDiff(model)
    }

    fun showNextPage() {
        val remainingItems = comparisonBpcListModel.listData.countData() - comparisonBpcListModel.itemsToShow
        val nextItemsToShow = comparisonBpcListModel.itemsToShow + remainingItems.coerceAtMost(
            ComparisonBpcListModel.DEFAULT_ITEM_TO_SHOW
        )
        val newModel = comparisonBpcListModel.copy(itemsToShow = nextItemsToShow)
        updateListAndCalculateDiff(newModel)
    }

    private fun List<Visitable<ComparisonBpcTypeFactory>>.countData(): Int {
        return filterIsInstance(ComparisonBpcItemModel::class.java).size
    }

    private fun List<Visitable<ComparisonBpcTypeFactory>>.appendViewAllIfNeeded(model: ComparisonBpcListModel): List<Visitable<ComparisonBpcTypeFactory>> {
        return this.toMutableList().apply {
            if (model.isShowViewAllCard()) {
                add(ComparisonBpcSeeMoreDataModel(model.trackingModel, model.productAnchor))
            }
        }
    }

    private fun ComparisonBpcListModel.getListItemsToShow(): List<Visitable<ComparisonBpcTypeFactory>> {
        return listData.take(itemsToShow).appendViewAllIfNeeded(this)
    }

    private fun updateListAndCalculateDiff(newModel: ComparisonBpcListModel) {
        val diffCallback = ComparisonBpcDiffUtil(
            comparisonBpcListModel.getListItemsToShow(),
            newModel.getListItemsToShow()
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        comparisonBpcListModel = newModel
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getProductAnchor(model: ComparisonBpcListModel) {
        model.listData.filterIsInstance<ComparisonBpcItemModel>()
    }
}
