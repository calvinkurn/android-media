package com.tokopedia.topads.dashboard.view.adapter.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.INDEX_4
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 21/7/20.
 */

class TopAdsKeywordInsightAdapter(private var onCheck: ((pos: Int) -> Unit)) :
    RecyclerView.Adapter<TopAdsKeywordInsightAdapter.ViewHolder>() {

    var items: MutableList<KeywordInsightDataMain> = mutableListOf()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val img: com.tokopedia.unifycomponents.ImageUnify = view.findViewById(R.id.img)
        val keywordName: Typography = view.findViewById(R.id.keywordName)
        val txtPotential: Typography = view.findViewById(R.id.txtPotential)
        val txtSavings: Typography = view.findViewById(R.id.txtSavings)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_dash_keyword_insight_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.img.setImageDrawable(holder.view.context.getResDrawable(R.drawable.topads_dashboard_folder))
        val name = items[position].name + "(" + items[position].count + ")"
        holder.keywordName.text = name
        holder.txtPotential.text =
            String.format(holder.view.resources.getString(R.string.topads_dash_insight_key_item_tampil_potensi),
                Utils.convertToCurrencyString(setPotentialValue(position).toLong()))
        holder.txtSavings.text =
            String.format(holder.view.resources.getString(R.string.topads_dash_insight_key_item_hemat_potensi),
                Utils.convertToCurrencyString(setSavingsValue(position).toLong()))
        holder.view.setOnClickListener {
            onCheck.invoke(position)
        }
    }

    private fun setPotentialValue(pos: Int): Double {
        var totalPotential = 0.0
        items[pos].keyword.forEach { key ->
            totalPotential += (key.data?.get(INDEX_4)?.value) as Double
        }
        items[pos].bid.forEach { key ->
            totalPotential += (key.data?.get(INDEX_4)?.value) as Double
        }
        return totalPotential
    }

    private fun setSavingsValue(pos: Int): Double {
        var totalSaving = 0.0
        items[pos].negative.forEach { key ->
            totalSaving += (key.data?.get(INDEX_4)?.value) as Double
        }
        return totalSaving
    }
}