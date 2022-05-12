package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.databinding.ItemRvAnggaranHarianBinding
import com.tokopedia.topads.dashboard.databinding.LayoutRecommendasiRoundedSubViewTopadsDashboardBinding

class TopAdsBerandaAnggarnHarianAdapter :
    RecyclerView.Adapter<TopAdsBerandaAnggarnHarianAdapter.ViewHolder>() {

    private val list = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRvAnggaranHarianBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun addItems(items: List<String>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(private val binding: ItemRvAnggaranHarianBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(it: String) {
            binding.txtTitle.text = it
        }
    }
}