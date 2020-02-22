package com.tokopedia.topads.view.adapter.bidinfo.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import kotlinx.android.synthetic.main.topads_create_layout_budget_list_item.view.*
import java.lang.NumberFormatException

class BidInfoItemViewHolder(val view: View, var selectedKeywords: MutableList<String>, var selectedSuggestBid: MutableList<Int>, var actionClose: ((pos: Int) -> Unit)?, private val actionClick: (() -> Int)?) : BidInfoViewHolder<BidInfoItemViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_create_layout_budget_list_item
        var minBid = 0
    }

    override fun bind(item: BidInfoItemViewModel) {
        minBid = actionClick!!.invoke()
        item?.let {
            if (selectedKeywords.size != 0) {
                view.title.text = selectedKeywords[adapterPosition]
                view.budget.setText(selectedSuggestBid[adapterPosition].toString())
                view.recom_txt.text = String.format(view.resources.getString(R.string.recommendated_bid_message), selectedSuggestBid[adapterPosition])

            }

            view.budget.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    try {
                        val result = Integer.parseInt(view.budget.text.toString())
                        selectedSuggestBid[adapterPosition] = result
                        if (result < minBid!!) {
                            view.error_text.visibility = View.VISIBLE
                            view.recom_txt.visibility = View.GONE
                            view.error_text.text = String.format(view.resources.getString(R.string.min_bid_error), minBid)
                        } else {
                            view.error_text.visibility = View.GONE
                            view.recom_txt.visibility = View.VISIBLE

                        }
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                }
            })
            view.close_button.setOnClickListener {
                actionClose!!.invoke(adapterPosition)
            }

        }
    }
}