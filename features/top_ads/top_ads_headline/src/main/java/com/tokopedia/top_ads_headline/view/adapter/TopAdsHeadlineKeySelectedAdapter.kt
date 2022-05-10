package com.tokopedia.top_ads_headline.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.top_ads_headline.R
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.utils.text.currency.NumberTextWatcher
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 6/10/20.
 */

class TopAdsHeadlineKeySelectedAdapter(
    private val onCheck: ((position: Int, keywordDataItem: KeywordDataItem) -> Unit),
    private val onBidChange: ((enable: Boolean, keywordDataItem: KeywordDataItem) -> Unit),
) : RecyclerView.Adapter<TopAdsHeadlineKeySelectedAdapter.ViewHolder>() {

    var items: MutableList<KeywordDataItem> = mutableListOf()
    private var minBid = "0"
    private var maxBid = "0"
    private var suggestedBid = "0"

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val keywordName: Typography? = view.findViewById(R.id.keywordName)
        val keywordDesc: Typography? = view.findViewById(R.id.keywordDesc)
        val checkBox: CheckboxUnify? = view.findViewById(R.id.checkBox)
        val keywordBid: TextFieldUnify? = view.findViewById(R.id.keywordBid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_headline_keyword_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBox?.isChecked = true
        holder.keywordName?.text = items[holder.adapterPosition].keyword
        holder.keywordBid?.textFieldInput?.setText(Utils.convertToCurrency(items[holder.adapterPosition].bidSuggest.toLong()))
        holder.keywordDesc?.visibility = View.GONE
        holder.view.setOnClickListener {
            holder.checkBox?.isChecked = holder.checkBox?.isChecked == false
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                items[holder.adapterPosition].onChecked = holder.checkBox?.isChecked == true
                onCheck(holder.adapterPosition, items[holder.adapterPosition])
            }
        }
        setBidInfo(holder)
    }

    private fun setBidInfo(holder: ViewHolder) {
        holder.keywordBid?.textFieldInput?.addTextChangedListener(object :
            NumberTextWatcher(holder.keywordBid.textFieldInput, "0") {

            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                items[holder.adapterPosition].bidSuggest = result.toString()
                when {
                    result < minBid.toDouble() -> {
                        holder.keywordBid.setError(true)
                        holder.keywordBid.setMessage(String.format(holder.view.context.getString(
                            com.tokopedia.topads.common.R.string.topads_common_min_bid), minBid))
                        onBidChange(false, items[holder.adapterPosition])
                    }
                    result < suggestedBid.toDouble() -> {
                        holder.keywordBid.setError(false)
                        holder.keywordBid.setMessage(String.format(holder.view.context.getString(
                            com.tokopedia.topads.common.R.string.topads_common_recom_bid), suggestedBid))
                        onBidChange(true, items[holder.adapterPosition])
                    }
                    result > maxBid.toDouble() -> {
                        holder.keywordBid.setError(true)
                        holder.keywordBid.setMessage(String.format(holder.view.context.getString(
                            com.tokopedia.topads.common.R.string.topads_common_max_bid), maxBid))
                        onBidChange(false, items[holder.adapterPosition])
                    }
                    result % 50 != 0 -> {
                        holder.keywordBid.setError(true)
                        holder.keywordBid.setMessage(holder.view.context.getString(com.tokopedia.topads.common.R.string.error_bid_multiple_50))
                        onBidChange(false, items[holder.adapterPosition])
                    }
                    else -> {
                        holder.keywordBid.setError(false)
                        holder.keywordBid.setMessage("")
                        onBidChange(true, items[holder.adapterPosition])
                    }
                }
            }
        })
    }

    fun setDefaultValues(maxBid: String?, minBid: String?, suggestionBid: String?) {
        this.maxBid = maxBid ?: "0"
        this.minBid = minBid ?: "0"
        this.suggestedBid = suggestionBid ?: "0"
        notifyDataSetChanged()
    }
}