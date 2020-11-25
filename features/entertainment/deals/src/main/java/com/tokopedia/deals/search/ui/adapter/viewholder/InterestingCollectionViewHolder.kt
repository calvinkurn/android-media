package com.tokopedia.deals.search.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.search.listener.DealsSearchListener
import com.tokopedia.deals.search.model.response.Category
import com.tokopedia.deals.search.model.visitor.CuratedModel
import com.tokopedia.deals.search.mapper.DealsSearchMapper
import com.tokopedia.deals.search.ui.adapter.DealsCuratedAdapter

class InterestingCollectionViewHolder(itemView: View, dealsSearchListener: DealsSearchListener): AbstractViewHolder<CuratedModel>(itemView) {

    private val curatedRv: RecyclerView = itemView.findViewById(R.id.rv_curated)
    private val curatedAdapter = DealsCuratedAdapter(dealsSearchListener)

    init {
        curatedRv.adapter = curatedAdapter
        curatedRv.layoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
    }

    override fun bind(element: CuratedModel?) {
        element?.let {
            val curatedList = arrayListOf<Category>()
            val categoryList = element.categories
            for (i in categoryList.indices) {
                if(categoryList[i].isCard == 1 && categoryList[i].isHidden == 0 && categoryList[i].url != TOPDEALS &&
                        curatedList.size < DealsSearchMapper.MAX_NUM_CURATED) {
                    curatedList.add(categoryList[i])
                }
            }
            curatedAdapter.curatedList = curatedList
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_deals_curated_list_search
        const val TOPDEALS = "topdeals"
    }
}