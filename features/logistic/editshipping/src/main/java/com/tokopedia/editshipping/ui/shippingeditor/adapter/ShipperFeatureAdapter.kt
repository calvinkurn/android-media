package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.databinding.LabelFeatureItemsBinding
import com.tokopedia.editshipping.domain.model.shippingEditor.FeatureInfoModel

class ShipperFeatureAdapter : RecyclerView.Adapter<ShipperFeatureAdapter.ShipperLabelViewHolder>() {

    private val featureData = mutableListOf<FeatureInfoModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipperLabelViewHolder {
        return ShipperLabelViewHolder(
            LabelFeatureItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return featureData.size
    }

    override fun onBindViewHolder(holder: ShipperLabelViewHolder, position: Int) {
        holder.bindData(featureData[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<FeatureInfoModel>) {
        featureData.clear()
        featureData.addAll(data)
        notifyDataSetChanged()
    }

    inner class ShipperLabelViewHolder(private val binding: LabelFeatureItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: FeatureInfoModel) {
            binding.lblFeatureItem.text = data.header
        }
    }
}
