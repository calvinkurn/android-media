package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.data.model.beranda.KataKunciHomePageBase
import com.tokopedia.topads.dashboard.data.model.beranda.RecommendationStatistics
import com.tokopedia.topads.dashboard.data.model.beranda.KataKunciSimpleButton
import com.tokopedia.topads.dashboard.databinding.ItemChipsRvKataKunciBinding
import com.tokopedia.unifycomponents.ChipsUnify

class TopAdsBerandsKataKunciChipsRvAdapter(private val itemClick: (KataKunciHomePageBase) -> Unit) :
    RecyclerView.Adapter<TopAdsBerandsKataKunciChipsRvAdapter.ViewHolder>() {

    private var selectedPosition = 0
    private val list = mutableListOf<KataKunciHomePageBase>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemChipsRvKataKunciBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[holder.adapterPosition]
        holder.bind(item) {
            if (item is RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup) {
                val selectedItem = getSelectedItem()
                if (selectedItem is RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup) {
                    selectedItem.isSelected = false
                    notifyItemChanged(selectedPosition)
                    selectedPosition = holder.adapterPosition
                }
            }
            itemClick(item)
        }
    }

    fun getSelectedItem() = list[selectedPosition]

    fun addItems(items: List<KataKunciHomePageBase>, lastItem: String) {
        if (items.isEmpty()) return
        list.clear()
        (items[0] as RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup).isSelected =
            true
        itemClick(items[0])
        list.addAll(items)
        list.add(KataKunciSimpleButton(lastItem))
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(
        private val binding: ItemChipsRvKataKunciBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: KataKunciHomePageBase, itemClick: (KataKunciHomePageBase) -> Unit) {
            if (item is RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup) {
                binding.chip.show()
                binding.textView.hide()
                binding.chip.chipText = item.groupName
                updateChipType(item)
                binding.chip.setOnClickListener {
                    if (!item.isSelected) {
                        item.isSelected = true
                        updateChipType(item)
                        itemClick(item)
                    }
                }
            } else if (item is KataKunciSimpleButton) {
                binding.textView.show()
                binding.chip.hide()
                binding.textView.text = item.label
                binding.textView.setOnClickListener { itemClick(item) }
            }
        }

        private fun updateChipType(item: RecommendationStatistics.Statistics.Data.KeywordRecommendationStats.TopGroup) {
            binding.chip.chipType =
                if (item.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
        }
    }
}

