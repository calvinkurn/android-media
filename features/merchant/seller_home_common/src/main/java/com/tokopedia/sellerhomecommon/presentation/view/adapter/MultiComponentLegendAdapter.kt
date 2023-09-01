package com.tokopedia.sellerhomecommon.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.databinding.ShcItemStackedBarLegendBinding
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.BarMultiComponentLegendModel

class MultiComponentLegendAdapter(
    private val uiModels: List<BarMultiComponentLegendModel>
): RecyclerView.Adapter<MultiComponentLegendAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ShcItemStackedBarLegendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(uiModels[position])
    }

    override fun getItemCount(): Int {
        return uiModels.size
    }

    inner class ViewHolder(
        private val binding: ShcItemStackedBarLegendBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(model: BarMultiComponentLegendModel) {
            binding.ivShcStackedBarLegend.background.setTint(model.color)
            binding.tvShcStackedBarLegendTitle.text = model.title
            binding.tvShcStackedBarLegendValue.text = model.value
        }

    }

}
