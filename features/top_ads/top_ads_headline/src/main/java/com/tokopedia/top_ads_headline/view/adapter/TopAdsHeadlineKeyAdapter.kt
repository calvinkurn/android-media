package com.tokopedia.top_ads_headline.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.top_ads_headline.R
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.util.Utils.convertToCurrency
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.topads_headline_keyword_item.view.*

/**
 * Created by Pika on 6/10/20.
 */

class TopAdsHeadlineKeyAdapter(private var onCheck: (pos: Int) -> Unit,
                               private val onError: (enable: Boolean) -> Unit,
                               private val selectedKeywords: MutableList<KeywordDataItem>?) : RecyclerView.Adapter<TopAdsHeadlineKeyAdapter.ViewHolder>() {

    var items: MutableList<KeywordDataItem> = mutableListOf()
    private var minimumBid: Int = 0
    private var maxBid:Int = 0

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_headline_keyword_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.checkBox.setOnClickListener(null)
        holder.view.keywordName.text = items[holder.adapterPosition].keyword
        holder.view.keywordDesc.text = String.format(holder.view.context.getString(R.string.topads_headline_keyword_desc), convertToCurrency(items[holder.adapterPosition].totalSearch.toLong()))
        holder.view.keywordBid.textFieldInput.setText(convertToCurrency(items[holder.adapterPosition].bidSuggest.toLong()))
        if (selectedKeywords?.isEmpty() == true) {
            holder.view.checkBox.isChecked = items[holder.adapterPosition].onChecked
            holder.view.keywordBid.textFieldInput.setText(convertToCurrency(items[holder.adapterPosition].bidSuggest.toLong()))
        }
        else {
            holder.view.checkBox.isChecked = false
            selectedKeywords?.forEach {
                if(it.keyword == items[holder.adapterPosition].keyword){
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
                onCheck(position)
        }
        setBidInfo(holder, items[holder.adapterPosition].bidSuggest)
    }

    private fun setBidInfo(holder: ViewHolder, bidSuggest: Int) {
        holder.view.keywordBid.textFieldInput.addTextChangedListener(object : NumberTextWatcher(holder.view.keywordBid.textFieldInput, "0") {

            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                items[holder.adapterPosition].bidSuggest = result
                when {
                    result < minimumBid -> {
                        holder.view.keywordBid.setError(true)
                        holder.view.keywordBid.setMessage(String.format(holder.view.context.getString(R.string.topads_common_min_bid), minimumBid))
                        onError(false)
                    }
                    result < bidSuggest -> {
                        holder.view.keywordBid.setError(false)
                        holder.view.keywordBid.setMessage(String.format(holder.view.context.getString(R.string.topads_common_recom_bid), bidSuggest))
                        onError(true)
                    }
                    result > maxBid -> {
                        holder.view.keywordBid.setError(true)
                        holder.view.keywordBid.setMessage(String.format(holder.view.context.getString(R.string.topads_common_max_bid), maxBid))
                        onError(false)
                    }
                    else -> {
                        holder.view.keywordBid.setError(false)
                        holder.view.keywordBid.setMessage("")
                        onError(true)
                    }
                }
            }
        })
    }

    fun setList(list: List<KeywordData>, minBid: Int) {
        items.clear()
        minimumBid = minBid
        list.forEach {
            it.keywordData.forEach { data ->
                data.onChecked = true
                items.add(data)
            }
        }
        notifyDataSetChanged()
    }

    fun setMax(max:Int){
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