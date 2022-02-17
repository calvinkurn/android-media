package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.databinding.ItemChipsRvKataKunciBinding

class TopAdsBerandsKataKunciChipsRvAdapter(private val itemClick: (Any) -> Unit) :
    RecyclerView.Adapter<TopAdsBerandsKataKunciChipsRvAdapter.ViewHolder>() {

    private val list = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemChipsRvKataKunciBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ViewHolder(binding)
        binding.root.setOnClickListener { itemClick(list[holder.adapterPosition]) }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[holder.adapterPosition])
    }

    fun addItems(items: List<Any>, lastItem: String) {
        list.clear()
        list.addAll(items)
        list.add(lastItem)
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    class ViewHolder(private val binding: ItemChipsRvKataKunciBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Any) {
            if (item is RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup) {
                binding.chip.show()
                binding.textView.hide()
                binding.chip.chipText = item.groupName
            } else if (item is String) {
                binding.textView.show()
                binding.chip.hide()
                binding.textView.text = item
            }
        }
    }
}

