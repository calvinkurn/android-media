package com.tokopedia.broadcaster.chucker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.broadcaster.chucker.ui.uimodel.ChuckerLogUIModel
import com.tokopedia.broadcaster.chucker.util.dateFormat
import com.tokopedia.broadcaster.databinding.ItemChuckerLogBinding

class ChuckerAdapter constructor(
    private val logs: MutableList<ChuckerLogUIModel> = mutableListOf()
) : RecyclerView.Adapter<ChuckerAdapter.ChuckerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChuckerViewHolder {
        return ChuckerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ChuckerViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount() = logs.size

    fun addItem(model: ChuckerLogUIModel) {
        logs.add(model)
        notifyDataSetChanged()
    }

    fun addItems(models: List<ChuckerLogUIModel>) {
        logs.clear()
        logs.addAll(models)
        notifyDataSetChanged()
    }

    class ChuckerViewHolder(
        private val binding: ItemChuckerLogBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChuckerLogUIModel) {
            binding.txtUrl.text = model.url
            binding.txtStartDate.text = model.startTime.dateFormat()
        }

        companion object {
            fun create(parent: ViewGroup): ChuckerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val inflate = ItemChuckerLogBinding.inflate(layoutInflater, parent, false)
                return ChuckerViewHolder(inflate)
            }
        }

    }

}