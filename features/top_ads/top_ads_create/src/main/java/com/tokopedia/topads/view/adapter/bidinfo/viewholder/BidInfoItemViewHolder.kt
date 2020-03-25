package com.tokopedia.topads.view.adapter.bidinfo.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import kotlinx.android.synthetic.main.topads_create_layout_budget_list_item.view.*

class BidInfoItemViewHolder(val view: View, var selectedKeywords: MutableList<String>, var selectedSuggestBid: MutableList<Int>, var actionClose: ((pos: Int) -> Unit)?, private val actionClick: (() -> MutableMap<String, Int>)?, var actionEnable: ((flag: Boolean) -> Unit)?) : BidInfoViewHolder<BidInfoItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_budget_list_item
        private var bidMap = mutableMapOf<String, Int>()
    }

    override fun bind(item: BidInfoItemViewModel) {
        bidMap = actionClick!!.invoke()
        item.let {
            if (selectedKeywords.size != 0) {
                view.title.text = selectedKeywords[adapterPosition]
                if (selectedSuggestBid[adapterPosition] != 0) {
                    view.budget.setText(selectedSuggestBid[adapterPosition].toString())
                    view.recom_txt.text = String.format(view.resources.getString(R.string.recommendated_bid_message), selectedSuggestBid[adapterPosition])
                } else {
                    view.budget.setText(bidMap["min"].toString())
                    view.recom_txt.text = String.format(view.resources.getString(R.string.recommendated_bid_message), bidMap["min"])

                }
            }

            view.budget.addTextChangedListener(object : NumberTextWatcher(view.budget, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    val result = number.toInt()
                    selectedSuggestBid[adapterPosition] = result
                    when {
                        result < bidMap["min"]!! -> {
                            actionEnable!!.invoke(false)
                            errorTextVisibility(true)
                            view.error_text.text = String.format(view.resources.getString(R.string.min_bid_error), bidMap["min"])
                        }
                        result > bidMap["max"]!! -> {
                            actionEnable!!.invoke(false)
                            errorTextVisibility(true)
                            view.error_text.text = String.format(view.resources.getString(R.string.max_bid_error), bidMap["max"])
                        }
                        else -> {
                            actionEnable!!.invoke(true)
                            errorTextVisibility(false)
                        }
                    }
                }
            })
            view.close_button.setOnClickListener {
                actionClose!!.invoke(adapterPosition)
            }
        }
    }

    fun errorTextVisibility(visible: Boolean) {
        if (visible) {
            view.error_text.visibility = View.VISIBLE
            view.recom_txt.visibility = View.GONE
        } else {
            view.error_text.visibility = View.GONE
            view.recom_txt.visibility = View.VISIBLE
        }
    }
}