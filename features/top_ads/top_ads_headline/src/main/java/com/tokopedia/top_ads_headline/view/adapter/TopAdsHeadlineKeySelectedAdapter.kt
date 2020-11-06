package com.tokopedia.top_ads_headline.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.top_ads_headline.R
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.topads_headline_keyword_item.view.*

/**
 * Created by Pika on 6/10/20.
 */

class TopAdsHeadlineKeySelectedAdapter(private val onCheck: ((position: Int) -> Unit)) : RecyclerView.Adapter<TopAdsHeadlineKeySelectedAdapter.ViewHolder>() {

    var items: MutableList<KeywordDataItem> = mutableListOf()

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_headline_keyword_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.checkBox.setOnClickListener(null)
        holder.view.checkBox.isChecked = true
        holder.view.keywordName.text = items[holder.adapterPosition].keyword
        holder.view.keywordBid.textFieldInput.setText(items[holder.adapterPosition].bidSuggest.toString())
        if (items[holder.adapterPosition].totalSearch == "-1")
            holder.view.keywordDesc.visibility = View.GONE
        else
            holder.view.keywordDesc.visibility = View.VISIBLE
        holder.view.setOnClickListener {
            items[holder.adapterPosition].onChecked = !holder.view.checkBox.isChecked
            if (holder.adapterPosition != RecyclerView.NO_POSITION)
                onCheck(holder.adapterPosition)
        }
        setBidInfo(holder, position)
    }

    private fun setBidInfo(holder: ViewHolder, position: Int) {
        holder.view.keywordBid.textFieldInput.addTextChangedListener(object : NumberTextWatcher(holder.view.keywordBid.textFieldInput, "0") {

            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                //todo set recom and minbid
                if (result < 200) {
                    holder.view.keywordBid.setError(true)
                    holder.view.keywordBid.setMessage(String.format(holder.view.context.getString(R.string.topads_common_min_bid), "200"))
                }
                else if (result < 400) {
                    holder.view.keywordBid.setError(true)
                    holder.view.keywordBid.setMessage(String.format(holder.view.context.getString(R.string.topads_common_recom_bid), "400"))

                }


            }
        })
    }

}