package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
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
        list.add(
            KataKunciDetail(
                resources.getString(R.string.topads_dashboard_kata_kunci_baru),
                item.newKeywordCount,
                String.format(
                    resources.getString(R.string.topads_dashboard_potensi_tampil_str),
                    item.newKeywordTotalImpression
                )
            )
        )
        list.add(
            KataKunciDetail(
                resources.getString(R.string.new_keyword_subtitle2),
                item.bidCount,
                String.format(
                    resources.getString(R.string.topads_dashboard_potensi_tampil_str),
                    item.bidTotalImpression
                )
            )
        )
        list.add(
            KataKunciDetail(
                resources.getString(R.string.topads_dashboard_kata_kunci_neg),
                item.negativeKeywordCount,
                String.format(
                    resources.getString(R.string.topads_dashboard_potensi_tampil_str),
                    item.newKeywordTotalImpression
                )
            )
        )
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    class ViewHolder(private val binding: ItemRvRingkasanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: KataKunciDetail) {
            binding.bottomView.hide()
            binding.ivInformation.hide()
            binding.txtTitle.text = item.title
            binding.txtSubTitle.text = item.count.toString()
            binding.txtPercentageChange.text = item.footer
            binding.txtFooterRingkasan.hide()
        }
    }

    data class KataKunciDetail(
        val title: String,
        val count: Int,
        val footer: String
    )
}

