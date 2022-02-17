package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.databinding.ItemRvRingkasanBinding

class TopAdsBerandsKataKunciChipsDetailRvAdapter() :
    RecyclerView.Adapter<TopAdsBerandsKataKunciChipsDetailRvAdapter.ViewHolder>() {

    private val list = mutableListOf<Pair<String,String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRvRingkasanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[holder.adapterPosition])
    }

    fun addItems(items: List<Pair<String,String>>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    class ViewHolder(private val binding: ItemRvRingkasanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Pair<String,String>) {

        }
    }
}

