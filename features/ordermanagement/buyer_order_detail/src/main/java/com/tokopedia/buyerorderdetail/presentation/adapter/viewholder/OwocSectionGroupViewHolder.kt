package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.ItemOwocSectionGroupBinding
import com.tokopedia.buyerorderdetail.presentation.adapter.OwocProductListAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocProductListListener
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocProductListTypeFactoryImpl
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel

class OwocSectionGroupViewHolder(view: View?,
                                 navigator: BuyerOrderDetailNavigator
): AbstractViewHolder<OwocProductListUiModel>(view), OwocProductListListener {

    companion object {
        val LAYOUT = R.layout.item_owoc_section_group
    }

    private val binding = ItemOwocSectionGroupBinding.bind(itemView)

    private val typeFactory: OwocProductListTypeFactoryImpl by lazy(LazyThreadSafetyMode.NONE) {
        OwocProductListTypeFactoryImpl(navigator, this)
    }

    private val owocSectionGroupAdapter: OwocProductListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        OwocProductListAdapter(typeFactory)
    }

    override fun bind(element: OwocProductListUiModel) {
        with(binding) {
            setupRecyclerView(element)
        }
    }

    private fun ItemOwocSectionGroupBinding.setupRecyclerView(item: OwocProductListUiModel) {
        if (rvOwocProductList.adapter != owocSectionGroupAdapter) {
            rvOwocProductList.layoutManager = LinearLayoutManager(root.context)
            rvOwocProductList.adapter = owocSectionGroupAdapter
            owocSectionGroupAdapter.updateItems(root.context, item)
        }
    }

    override fun onCollapseProductList() {
        TODO("Not yet implemented")
    }

    override fun onExpandProductList() {
        TODO("Not yet implemented")
    }

}
