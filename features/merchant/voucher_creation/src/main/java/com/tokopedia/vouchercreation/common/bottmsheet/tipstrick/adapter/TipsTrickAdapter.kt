package com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.model.TipsTrickModel
import com.tokopedia.vouchercreation.databinding.ItemMvcTipsTrickBinding

/**
 * Created By @ilhamsuaib on 08/05/20
 */

class TipsTrickAdapter : RecyclerView.Adapter<TipsTrickAdapter.TipsTrickViewHolder>() {

    private var items: List<TipsTrickModel> = emptyList()

    fun setItems(items: List<TipsTrickModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsTrickViewHolder {
        return TipsTrickViewHolder(ItemMvcTipsTrickBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TipsTrickViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class TipsTrickViewHolder(private val binding: ItemMvcTipsTrickBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TipsTrickModel) {
            binding.apply {
                tvMvcTipsTrickNumber.text = adapterPosition.plus(1).toString()
                tvMvcTipsTrickTitle.text = item.title
                tvMvcTipsTrickDescription.text = item.description
            }
        }
    }
}