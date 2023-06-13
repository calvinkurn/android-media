package com.tokopedia.shop.product.view.viewholder

import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopProductSortFilterBinding
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopProductSortFilterViewHolder(
    itemView: View,
    private val shopProductSortFilterViewHolderListener: ShopProductSortFilterViewHolderListener?
) : AbstractViewHolder<ShopProductSortFilterUiModel>(itemView) {

    companion object {
        @JvmStatic
        @LayoutRes
        val LAYOUT = R.layout.item_shop_product_sort_filter
    }

    interface ShopProductSortFilterViewHolderListener {
        fun onEtalaseFilterClicked()
        fun onSortFilterClicked()
        fun onClearFilterClicked()
        fun setSortFilterMeasureHeight(measureHeight: Int)
        fun onFilterClicked()
    }

    private val viewBinding: ItemShopProductSortFilterBinding? by viewBinding()
    private var shopProductSortFilterUiModel: ShopProductSortFilterUiModel? = null
    private val sortFilterWidget: SortFilter? = viewBinding?.sortFilter

    private val scrollListener = ViewTreeObserver.OnScrollChangedListener {
        shopProductSortFilterUiModel?.scrollX = sortFilterWidget?.sortFilterHorizontalScrollView?.scrollX
            ?: 0
    }

    override fun bind(data: ShopProductSortFilterUiModel) {
        removeOnScrollChangedListener()
        this.shopProductSortFilterUiModel = data
        itemView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                addScrollListener()
                itemView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        sortFilterWidget?.sortFilterItems?.removeAllViews()
        if (data.isShowSortFilter) {
            sortFilterWidget?.filterType = SortFilter.TYPE_ADVANCED
            sortFilterWidget?.filterRelationship = SortFilter.RELATIONSHIP_AND
            sortFilterWidget?.parentListener = {
                shopProductSortFilterViewHolderListener?.onFilterClicked()
            }
            sortFilterWidget?.textView?.text = itemView.context.getString(R.string.shop_sort_filter_chips_name)
            sortFilterWidget?.indicatorCounter = shopProductSortFilterUiModel?.filterIndicatorCounter.orZero()
        } else {
            sortFilterWidget?.filterType = SortFilter.TYPE_QUICK
            sortFilterWidget?.filterRelationship = SortFilter.RELATIONSHIP_OR
        }
        val filterData = ArrayList<SortFilterItem>()
        var sortFilter: SortFilterItem? = null
        if (data.isShowSortFilter) {
            sortFilter = if (data.selectedSortName.isNotEmpty()) {
                SortFilterItem(data.selectedSortName).apply {
                    type = ChipsUnify.TYPE_SELECTED
                }
            } else {
                SortFilterItem(
                    itemView.resources.getString(
                        R.string.shop_sort_filter_default_label
                    )
                ).apply {
                    type = ChipsUnify.TYPE_NORMAL
                }
            }
            sortFilter.typeUpdated = false
            sortFilter.listener = {
                shopProductSortFilterViewHolderListener?.onSortFilterClicked()
            }
            filterData.add(sortFilter)
        }

        val etalaseFilter = if (data.selectedEtalaseName.isNotEmpty()) {
            SortFilterItem(data.selectedEtalaseName).apply {
                type = ChipsUnify.TYPE_SELECTED
            }
        } else {
            SortFilterItem(
                itemView.resources.getString(
                    R.string.shop_etalase_filter_default_label
                )
            ).apply {
                type = ChipsUnify.TYPE_NORMAL
            }
        }
        etalaseFilter.typeUpdated = false
        etalaseFilter.listener = {
            shopProductSortFilterViewHolderListener?.onEtalaseFilterClicked()
        }
        filterData.add(etalaseFilter)
        sortFilterWidget?.addItem(filterData)
        if (data.isShowSortFilter) {
            sortFilter?.refChipUnify?.setChevronClickListener {
                shopProductSortFilterViewHolderListener?.onSortFilterClicked()
            }
        }
        etalaseFilter.refChipUnify.setChevronClickListener {
            shopProductSortFilterViewHolderListener?.onEtalaseFilterClicked()
        }
        itemView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        shopProductSortFilterViewHolderListener?.setSortFilterMeasureHeight(itemView.measuredHeight)
    }

    private fun addScrollListener() {
        sortFilterWidget?.sortFilterHorizontalScrollView?.viewTreeObserver?.addOnScrollChangedListener(scrollListener)
    }

    private fun removeOnScrollChangedListener() {
        sortFilterWidget?.sortFilterHorizontalScrollView?.viewTreeObserver?.removeOnScrollChangedListener(scrollListener)
    }
}
