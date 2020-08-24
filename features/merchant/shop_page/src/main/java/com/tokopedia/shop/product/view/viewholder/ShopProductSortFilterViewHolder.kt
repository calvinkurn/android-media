package com.tokopedia.shop.product.view.viewholder

import androidx.annotation.LayoutRes
import android.view.View
import android.view.ViewTreeObserver

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_shop_product_sort_filter.view.*

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopProductSortFilterViewHolder(
        itemView: View,
        private val shopProductEtalaseChipListViewHolderListener: ShopProductEtalaseChipListViewHolderListener?
) : AbstractViewHolder<ShopProductSortFilterUiModel>(itemView) {

    companion object {
        @JvmStatic
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_sort_filter
    }

    interface ShopProductEtalaseChipListViewHolderListener {
        fun onEtalaseFilterClicked()
        fun onSortFilterClicked()
        fun onClearFilterClicked()
    }

    private var shopProductSortFilterUiModel: ShopProductSortFilterUiModel? = null

    private val scrollListener = ViewTreeObserver.OnScrollChangedListener {
        shopProductSortFilterUiModel?.scrollX = itemView.sort_filter?.sortFilterHorizontalScrollView?.scrollX
                ?: 0
    }

    override fun bind(data: ShopProductSortFilterUiModel) {
        removeOnScrollChangedListener()
        this.shopProductSortFilterUiModel = data
        itemView.sort_filter?.sortFilterItems?.removeAllViews()
        val scrollX = shopProductSortFilterUiModel?.scrollX ?: 0
        itemView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                itemView.sort_filter?.sortFilterHorizontalScrollView?.scrollX = scrollX
                itemView.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
        itemView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                addScrollListener()
                itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        val filterData = ArrayList<SortFilterItem>()
        var sortFilter: SortFilterItem? = null
        if(data.isShowSortFilter) {
            sortFilter = if (data.selectedSortName.isNotEmpty()) {
                SortFilterItem(data.selectedSortName).apply {
                    type = ChipsUnify.TYPE_SELECTED
                }
            } else {
                SortFilterItem(itemView.resources.getString(
                        R.string.shop_sort_filter_default_label)
                ).apply {
                    type = ChipsUnify.TYPE_NORMAL
                }
            }
            sortFilter.listener = {
                shopProductEtalaseChipListViewHolderListener?.onSortFilterClicked()
            }
            filterData.add(sortFilter)
        }

        val etalaseFilter = if (data.selectedEtalaseName.isNotEmpty()) {
            SortFilterItem(data.selectedEtalaseName).apply {
                type = ChipsUnify.TYPE_SELECTED
            }
        } else {
            SortFilterItem(itemView.resources.getString(
                    R.string.shop_etalase_filter_default_label)
            ).apply {
                type = ChipsUnify.TYPE_NORMAL
            }
        }
        etalaseFilter.listener = {
            shopProductEtalaseChipListViewHolderListener?.onEtalaseFilterClicked()
        }
        filterData.add(etalaseFilter)
        itemView.sort_filter?.addItem(filterData)
        if(data.isShowSortFilter) {
            sortFilter?.refChipUnify?.setChevronClickListener {
                shopProductEtalaseChipListViewHolderListener?.onSortFilterClicked()
            }
        }
        etalaseFilter.refChipUnify.setChevronClickListener {
            shopProductEtalaseChipListViewHolderListener?.onEtalaseFilterClicked()
        }
        itemView.sort_filter?.filterType = SortFilter.TYPE_QUICK
        itemView.sort_filter?.filterRelationship = SortFilter.RELATIONSHIP_AND
        itemView.sort_filter?.dismissListener = {
            shopProductEtalaseChipListViewHolderListener?.onClearFilterClicked()
        }
    }

    private fun addScrollListener() {
        itemView.sort_filter?.sortFilterHorizontalScrollView?.viewTreeObserver?.addOnScrollChangedListener(scrollListener)
    }

    private fun removeOnScrollChangedListener() {
        itemView.sort_filter?.sortFilterHorizontalScrollView?.viewTreeObserver?.removeOnScrollChangedListener(scrollListener)
    }
}