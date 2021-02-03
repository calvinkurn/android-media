package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INDEX_1
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INDEX_2
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INDEX_3
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INDEX_4
import com.tokopedia.topads.dashboard.data.model.insightkey.Negative
import com.tokopedia.topads.dashboard.data.utils.Utils.convertToCurrencyString
import kotlinx.android.synthetic.main.topads_dash_insight_pos_key_item_layout.view.*

/**
 * Created by Pika on 22/7/20.
 */

class TopAdsInsightNegKeyAdapter (var onButtonClick: ((position:Int) -> Unit?)) : RecyclerView.Adapter<TopAdsInsightNegKeyAdapter.ViewHolder>() {

    var items: MutableList<Negative> = mutableListOf()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun viewHolder(itemView: View) {
            super.itemView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_dash_insight_pos_key_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.keywordName.text = items[position].data?.get(INDEX_1)?.value.toString()
        holder.view.txtSearch.text = holder.view.resources.getString(R.string.topads_insight_item_neg_1)
        holder.view.textSearchValue.text = convertToCurrencyString((items[position].data?.get(INDEX_2)?.value as Double).toLong())
        holder.view.txtSavings.text = holder.view.resources.getString(R.string.topads_insight_item_neg_2)
        holder.view.txtSavingsValue.text = convertToCurrencyString((items[position].data?.get(INDEX_3)?.value as Double).toLong())
        holder.view.txtpotential.text = String.format(holder.view.resources.getString(R.string.topads_insight_item_neg_3),  convertToCurrencyString((items[position].data?.get(INDEX_4)?.value as Double).toLong()))
        val draw = ContextCompat.getDrawable(holder.view.context, R.drawable.topads_text_shadow)
        draw?.let {
            holder.view.txtpotential.setBackgroundDrawable(draw)
        }
        holder.view.btnTambah.text = holder.view.resources.getString(R.string.topads_insight_btn_terpakan)
        holder.view.btnTambah.setOnClickListener {
            onButtonClick.invoke(position)
            holder.view.btnTambah.isEnabled = false
        }
    }
}