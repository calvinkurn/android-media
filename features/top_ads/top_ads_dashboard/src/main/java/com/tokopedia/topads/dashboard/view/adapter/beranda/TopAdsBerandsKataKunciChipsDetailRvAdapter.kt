package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.beranda.KataKunciDetail
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.data.utils.TopAdsDashboardBerandaUtils
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.databinding.ItemRvRingkasanBinding

class TopAdsBerandsKataKunciChipsDetailRvAdapter() :
    RecyclerView.Adapter<TopAdsBerandsKataKunciChipsDetailRvAdapter.ViewHolder>() {

    private val list = mutableListOf<KataKunciDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRvRingkasanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[holder.adapterPosition])
    }

    fun addItems(
        item: RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup,
        resources: Resources
    ) {
        list.clear()
        list.addAll(TopAdsDashboardBerandaUtils.mapChipDetailToKataKunci(item, resources))
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    class ViewHolder(private val binding: ItemRvRingkasanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: KataKunciDetail) {
            binding.ivInformation.hide()
            binding.txtTitle.text = item.title
            binding.txtSubTitle.text = item.count.toString()
            binding.txtPercentageChange.text =
                HtmlCompat.fromHtml(item.footer, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }
}

