package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.FeatureInfoModel
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.unifycomponents.Label

class ShipperFeatureAdapter: RecyclerView.Adapter<ShipperFeatureAdapter.ShipperLabelViewHolder>() {

    private val featureData = mutableListOf<FeatureInfoModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipperLabelViewHolder {
        return ShipperLabelViewHolder(parent.inflateLayout(R.layout.label_feature_items))
    }

    override fun getItemCount(): Int {
        return featureData.size
    }

    override fun onBindViewHolder(holder: ShipperLabelViewHolder, position: Int) {
        holder.bindData(featureData[position])
    }

    fun setData(data: List<FeatureInfoModel>) {
        featureData.clear()
        featureData.addAll(data)
        notifyDataSetChanged()
    }


    inner class ShipperLabelViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val labelFeature = itemView.findViewById<Label>(R.id.lbl_feature_item)

        fun bindData(data: FeatureInfoModel) {
            labelFeature.text = data.header
        }
    }
}