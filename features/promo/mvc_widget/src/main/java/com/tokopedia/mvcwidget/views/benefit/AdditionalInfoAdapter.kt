package com.tokopedia.mvcwidget.views.benefit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.mvcwidget.databinding.LayoutItemInfoBinding

class AdditionalInfoAdapter : RecyclerView.Adapter<AdditionalInfoAdapter.InfoViewHolder>() {

    private var list: List<String> = listOf()

    fun submitList(list: List<String>) {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val b = LayoutItemInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoViewHolder(b)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class InfoViewHolder(private val binding: LayoutItemInfoBinding) : ViewHolder(binding.root) {
        fun bind(s: String) {
            binding.text.text = s
        }
    }
}
