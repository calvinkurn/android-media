package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.ItemOwocSectionGroupBinding
import com.tokopedia.buyerorderdetail.presentation.adapter.OwocProductListAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocProductListHeaderListener
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocProductListListener
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocRecyclerviewPoolListener
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocProductListTypeFactoryImpl
import com.tokopedia.buyerorderdetail.presentation.model.BaseOwocVisitableUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocSectionGroupUiModel

class OwocSectionGroupViewHolder(
    view: View?,
    navigator: BuyerOrderDetailNavigator?,
    private val owocProductListHeaderListener: OwocProductListHeaderListener,
    private val recyclerviewPoolListener: OwocRecyclerviewPoolListener
) : AbstractViewHolder<OwocSectionGroupUiModel>(view), OwocProductListListener {

    companion object {
        val LAYOUT = R.layout.item_owoc_section_group
    }

    private val binding = ItemOwocSectionGroupBinding.bind(itemView)

    private val typeFactory: OwocProductListTypeFactoryImpl by lazy(LazyThreadSafetyMode.NONE) {
        OwocProductListTypeFactoryImpl(navigator, this, owocProductListHeaderListener, recyclerviewPoolListener)
    }

    private val owocSectionGroupAdapter: OwocProductListAdapter by lazy {
        OwocProductListAdapter(typeFactory)
    }

    override fun bind(element: OwocSectionGroupUiModel) {
        with(binding) {
            setupRecyclerView(element)
        }
    }

    override fun bind(element: OwocSectionGroupUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OwocSectionGroupUiModel && newItem is OwocSectionGroupUiModel) {
                    if (oldItem.baseOwocProductListUiModel != newItem.baseOwocProductListUiModel) {
                        setupOwocProductList(newItem.baseOwocProductListUiModel)
                    }
                    return
                }
            }
        }
    }

    private fun ItemOwocSectionGroupBinding.setupRecyclerView(item: OwocSectionGroupUiModel) {
        with(rvOwocProductList) {
            if (adapter != owocSectionGroupAdapter) {
                layoutManager = LinearLayoutManager(context)
                itemAnimator = null
                adapter = owocSectionGroupAdapter
                isNestedScrollingEnabled = false
                setRecycledViewPool(recyclerviewPoolListener.parentPool)
            }
            setupOwocProductList(item.baseOwocProductListUiModel)
        }
    }

    private fun setupOwocProductList(newItems: List<BaseOwocVisitableUiModel>) {
        owocSectionGroupAdapter.updateItems(newItems)
    }

    override fun onCollapseProductList(
        expandedProducts: List<BaseOwocVisitableUiModel>,
        isExpanded: Boolean
    ) {
        owocSectionGroupAdapter.collapseOwocProduct(
            expandedProducts,
            isExpanded
        )
    }

    override fun onExpandProductList(
        expandedProducts: List<BaseOwocVisitableUiModel>,
        isExpanded: Boolean
    ) {
        owocSectionGroupAdapter.expandOwocProduct(
            expandedProducts,
            isExpanded
        )
    }
}
