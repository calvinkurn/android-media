package com.tokopedia.topads.view.adapter.bidinfo.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import com.tokopedia.topads.view.fragment.BudgetingAdsFragment.Companion.BROAD_POSITIVE
import com.tokopedia.topads.view.fragment.BudgetingAdsFragment.Companion.BROAD_TYPE
import com.tokopedia.topads.view.fragment.BudgetingAdsFragment.Companion.SPECIFIC_TYPE
import kotlinx.android.synthetic.main.topads_create_layout_budget_list_item.view.*

class BidInfoItemViewHolder(val view: View, var selectedKeywords: MutableList<String>, var selectedSuggestBid: MutableList<Int>, var itemClicked: ((pos: Int) -> Unit)?) : BidInfoViewHolder<BidInfoItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_budget_list_item
    }

    override fun bind(item: BidInfoItemViewModel, typeList: MutableList<Int>) {
        view.setOnClickListener {
            itemClicked?.invoke(adapterPosition)
        }
        item.let {
            if (selectedKeywords.size != 0) {
                view.keywordName.text = selectedKeywords[adapterPosition]
                if(adapterPosition < typeList.size){
                    if (typeList[adapterPosition] == BROAD_POSITIVE)
                        view.keywordType.text = BROAD_TYPE
                    else
                        view.keywordType.text = SPECIFIC_TYPE
                }

                if (selectedSuggestBid[adapterPosition] != 0) {
                    view.keywordBudget.text = "Rp "+ selectedSuggestBid[adapterPosition].toString()
                }
            }
        }
    }

}