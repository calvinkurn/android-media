package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.FeatureInfoModel
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.unifyprinciples.Typography

class FeatureInfoAdapter: RecyclerView.Adapter<FeatureInfoAdapter.FeatureInfoViewHolder>() {

    private val featureData = mutableListOf<FeatureInfoModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureInfoViewHolder {
        return FeatureInfoViewHolder(parent.inflateLayout(R.layout.item_feature_detail))
    }

    override fun getItemCount(): Int {
        return featureData.size
    }

    override fun onBindViewHolder(holder: FeatureInfoViewHolder, position: Int) {
        holder.binData(featureData[position])
    }

    fun setData(data: List<FeatureInfoModel>) {
        featureData.clear()
        featureData.addAll(data)
        notifyDataSetChanged()
    }

    inner class FeatureInfoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvFeatureName = itemView.findViewById<Typography>(R.id.tv_feature_name)
        private val tvFeatureDesc = itemView.findViewById<Typography>(R.id.tv_feature_desc)

        fun binData(data: FeatureInfoModel) {
            tvFeatureName.text = data.header
            tvFeatureDesc.text = data.body
        }
    }

}