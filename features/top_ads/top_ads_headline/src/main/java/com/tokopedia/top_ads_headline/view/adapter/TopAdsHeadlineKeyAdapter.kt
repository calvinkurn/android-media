package com.tokopedia.top_ads_headline.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.R
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.topads_headline_keyword_item.view.*

/**
 * Created by Pika on 6/10/20.
 */

class TopAdsHeadlineKeyAdapter(private var onCheck: (keywordDataItem: KeywordDataItem) -> Unit,
                               private val onBidChange: (enable: Boolean, keywordDataItem: KeywordDataItem) -> Unit,
                               private val selectedKeywords: MutableList<KeywordDataItem>?) : RecyclerView.Adapter<TopAdsHeadlineKeyAdapter.ViewHolder>() {

    var items: MutableList<KeywordDataItem> = mutableListOf()
    private var minimumBid: Int = 0
    private var maxBid: String = "0"
    private var stateRestore: Boolean = false

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_headline_keyword_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.keywordName.text = items[holder.adapterPosition].keyword
        holder.view.keywordDesc.text = String.format(holder.view.context.getString(R.string.topads_headline_keyword_desc), convertToCurrency(items[holder.adapterPosition].totalSearch.toLong()))
        holder.view.keywordBid.textFieldInput.setText(convertToCurrency(items[holder.adapterPosition].bidSuggest.toLong()))
        if (selectedKeywords?.isEmpty() == true && !stateRestore) {
            holder.view.checkBox.isChecked = true
            items[holder.adapterPosition].onChecked = true
            holder.view.keywordBid.textFieldInput.setText(convertToCurrency(items[holder.adapterPosition].bidSuggest.toLong()))
        } else {
            holder.view.checkBox.isChecked = false
            selectedKeywords?.forEach {
                if (it.keyword == items[holder.adapterPosition].keyword) {
                    holder.view.checkBox.isChecked = true
                    holder.view.keywordBid.textFieldInput.setText(convertToCurrency(it.bidSuggest.toLong()))
                    return@forEach
                }
            }
        }
        holder.view.setOnClickListener {
            holder.view.checkBox.isChecked = !holder.view.checkBox.isChecked
            items[holder.adapterPosition].onChecked = holder.view.checkBox.isChecked
            if (holder.adapterPosition != RecyclerView.NO_POSITION)
                onCheck(items[holder.adapterPosition])
        }
        setBidInfo(holder, items[holder.adapterPosition].bidSuggest.toIntOrZero())
    }

    private fun setBidInfo(holder: ViewHolder, bidSuggest: Int) {
        holder.view.keywordBid.textFieldInput.addTextChangedListener(object : NumberTextWatcher(holder.view.keywordBid.textFieldInput, "0") {

            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                items[holder.adapterPosition].bidSuggest = result.toString()
                when {
                    result < minimumBid -> {
                        holder.view.keywordBid.setError(true)
                        holder.view.keywordBid.setMessage(String.format(holder.view.context.getString(R.string.topads_common_min_bid), minimumBid))
                        onBidChange(false, items[holder.adapterPosition])
                    }
                    result < bidSuggest -> {
                        holder.view.keywordBid.setError(false)
                        holder.view.keywordBid.setMessage(String.format(holder.view.context.getString(R.string.topads_common_recom_bid), bidSuggest))
                        onBidChange(true, items[holder.adapterPosition])
                    }
                    result > maxBid.toDouble() -> {
                        holder.view.keywordBid.setError(true)
                        holder.view.keywordBid.setMessage(String.format(holder.view.context.getString(R.string.topads_common_max_bid), maxBid))
                        onBidChange(false, items[holder.adapterPosition])
                    }
                    else -> {
                        holder.view.keywordBid.setError(false)
                        holder.view.keywordBid.setMessage("")
                        onBidChange(true, items[holder.adapterPosition])
                    }
                }
            }
        })
    }

    fun setList(list: List<KeywordData>, minBid: Int, selectedKeywords: MutableList<KeywordDataItem>?, stateRestore: Boolean?) {
        items.clear()
        this.stateRestore = stateRestore ?: false
        minimumBid = minBid
        list.forEach {
            it.keywordData.forEach { data ->
                if ((stateRestore != true) || selectedKeywords?.find { it -> it.keyword == data.keyword } != null)
                    data.onChecked = true
                items.add(data)
            }
        }
        notifyDataSetChanged()
    }

    fun setMax(max: String) {
        maxBid = max
        notifyDataSetChanged()
    }

    fun getSelectItems(): MutableList<KeywordDataItem> {
        val list: MutableList<KeywordDataItem> = mutableListOf()
        items.forEach {
            if (it.onChecked)
                list.add(it)
        }
        return list
    }
}