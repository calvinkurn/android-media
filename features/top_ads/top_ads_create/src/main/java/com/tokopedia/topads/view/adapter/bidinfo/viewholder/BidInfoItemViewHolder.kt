package com.tokopedia.topads.view.adapter.bidinfo.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.bidinfo.viewModel.BidInfoItemViewModel
import kotlinx.android.synthetic.main.topads_create_layout_budget_list_item.view.*

class BidInfoItemViewHolder(val view: View, var selectedKeywords: MutableList<String>, var selectedSuggestBid: MutableList<Int>,private var suggestBidInitial: List<Int>, var actionClose: ((pos: Int) -> Unit)?, private val actionClick: (() -> MutableMap<String, Int>)?, var actionEnable: (() -> Unit)?) : BidInfoViewHolder<BidInfoItemViewModel>(view) {

    companion object {
        private const val FACTOR = 50
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
                    view.budget.textFieldInput.setText(selectedSuggestBid[adapterPosition].toString())
                    setMessageErrorField((view.resources.getString(R.string.recommendated_bid_message)), selectedSuggestBid[adapterPosition], false)
                } else {
                    view.budget.textFieldInput.setText(bidMap["min"].toString())
                    setMessageErrorField((view.resources.getString(R.string.recommendated_bid_message)), bidMap["min"]!!, false)


                }
            }

            view.budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(view.budget.textFieldInput, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    val result = number.toInt()
                    selectedSuggestBid[adapterPosition] = result
                    when {
                        result < bidMap["min"]!! -> {
                            item.isError = true
                            setMessageErrorField(view.resources.getString(R.string.min_bid_error), bidMap["min"]!!, true)
                        }
                        result > bidMap["max"]!! -> {
                            item.isError = true
                            setMessageErrorField(view.resources.getString(R.string.max_bid_error), bidMap["max"]!!, true)
                        }
                        result % FACTOR != 0 ->{
                            item.isError = true
                            setMessageErrorField(view.resources.getString(R.string.error_multiple_50),FACTOR ,true)
                        }
                        else -> {
                            item.isError = false
                            if (suggestBidInitial[adapterPosition] != 0) {
                                setMessageErrorField((view.resources.getString(R.string.recommendated_bid_message)), suggestBidInitial[adapterPosition], false)
                            } else {
                                setMessageErrorField((view.resources.getString(R.string.recommendated_bid_message)), bidMap["min"]!!, false)
                            }
                        }
                    }
                    actionEnable!!.invoke()
                }
            })
            view.close_button.setOnClickListener {
                actionClose!!.invoke(adapterPosition)
            }
        }
    }

    private fun setMessageErrorField(error: String, bid: Int, bool: Boolean) {
        view.budget.setError(bool)
        view.budget.setMessage(String.format(error, bid))

    }
}