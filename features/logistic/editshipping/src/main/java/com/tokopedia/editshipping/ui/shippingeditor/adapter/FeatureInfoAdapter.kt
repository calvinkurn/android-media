package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.ItemFeatureDetailBinding
import com.tokopedia.editshipping.domain.model.shippingEditor.FeatureInfoModel

class FeatureInfoAdapter : RecyclerView.Adapter<FeatureInfoAdapter.FeatureInfoViewHolder>() {

    private val featureData = mutableListOf<FeatureInfoModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureInfoViewHolder {
        return FeatureInfoViewHolder(
            ItemFeatureDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return featureData.size
    }

    override fun onBindViewHolder(holder: FeatureInfoViewHolder, position: Int) {
        holder.binData(featureData[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<FeatureInfoModel>) {
        featureData.clear()
        featureData.addAll(data)
        notifyDataSetChanged()
    }

    inner class FeatureInfoViewHolder(private val binding: ItemFeatureDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binData(data: FeatureInfoModel) {
            binding.tvFeatureName.text = data.header
            binding.tvFeatureDesc.text = data.body
        }
    }
}
