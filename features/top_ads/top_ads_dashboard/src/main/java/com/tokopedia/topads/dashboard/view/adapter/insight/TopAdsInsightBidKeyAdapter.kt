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
import com.tokopedia.topads.dashboard.data.model.insightkey.Bid
import com.tokopedia.topads.dashboard.data.model.insightkey.MutationData
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 22/7/20.
 */

class TopAdsInsightBidKeyAdapter(var onButtonClick: ((MutationData) -> Unit?)) :
    RecyclerView.Adapter<TopAdsInsightBidKeyAdapter.ViewHolder>() {

    var items: MutableList<Bid> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_dash_insight_pos_key_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.keywordName?.text = items[position].data?.get(INDEX_1)?.value.toString()
        holder.txtSearch?.text = holder.view.resources.getString(R.string.topads_insight_item_bid_1)
        holder.textSearchValue?.text =
            Utils.convertToCurrencyString((items[position].data?.get(INDEX_2)?.value as Double).toLong())
        holder.txtSavings?.text =
            holder.view.resources.getString(R.string.topads_insight_item_bid_2)
        holder.txtSavingsValue?.text =
            Utils.convertToCurrencyString((items[position].data?.get(INDEX_3)?.value as Double).toLong())
        val draw = ContextCompat.getDrawable(holder.view.context, R.drawable.topads_text_shadow)
        draw?.let {
            holder.txtPotential?.setBackgroundDrawable(draw)
        }
        holder.txtPotential?.text =
            String.format(holder.view.resources.getString(R.string.topads_insight_item_bid_3),
                Utils.convertToCurrencyString((items[position].data?.get(INDEX_4)?.value as Double).toLong()))
        holder.btnTambah?.text =
            holder.view.resources.getString(R.string.topads_insight_btn_terpakan)
        holder.btnTambah?.setOnClickListener {
            onButtonClick.invoke(items[position].mutationData)
            holder.btnTambah.isEnabled = false
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val keywordName: Typography? = view.findViewById(R.id.keywordName)
        val txtSearch: Typography? = view.findViewById(R.id.txtSearch)
        val textSearchValue: Typography? = view.findViewById(R.id.textSearchValue)
        val txtSavings: Typography? = view.findViewById(R.id.txtSavings)
        val txtSavingsValue: Typography? = view.findViewById(R.id.txtSavingsValue)
        val btnTambah: UnifyButton? = view.findViewById(R.id.btnTambah)
        val txtPotential: Typography? = view.findViewById(R.id.txtpotential)

        fun viewHolder(itemView: View) {
            super.itemView
        }
    }
}