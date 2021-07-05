package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.*
import com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder.FeatureDetailsViewHolder
import com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder.ServiceDetailsViewHolder
import com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder.ShipperDetailsViewHolder

class ShippingEditorDetailsAdapter : RecyclerView.Adapter<ShippingEditorDetailsAdapter.BaseViewHolder<*>>() {

    val data = mutableListOf<ShipperDetailVisitable>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_shipper_detail -> ShipperDetailsViewHolder(view)
            R.layout.item_service_detail -> ServiceDetailsViewHolder(view)
            R.layout.item_feature_detail -> FeatureDetailsViewHolder(view)
            else -> NoResultViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int = when(data[position]) {
        is ShipperDetailsModel -> R.layout.item_shipper_detail
        is ServiceDetailsModel -> R.layout.item_service_detail
        is FeatureDetailsModel -> R.layout.item_feature_detail
        is DividerModelFeature -> R.layout.item_divider_feature
        is DividerServiceFeature -> R.layout.item_divider_service
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    fun setShippingEditorDetailsData(model: ShipperDetailModel) {
        data.clear()
        data.addAll(model.shipperDetails)
        data.add(model.shipperDetails.size, DividerModelFeature())
        data.addAll(model.featureDetails)
        data.add(model.shipperDetails.size + model.featureDetails.size, DividerServiceFeature())
        data.addAll(model.serviceDetails)
        notifyDataSetChanged()
    }

    private inner class NoResultViewHolder(itemView: View) : ShippingEditorDetailsAdapter.BaseViewHolder<ShipperDetailVisitable>(itemView) {
        override fun bind(item: ShipperDetailVisitable, position: Int) {
            //no-op
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val data = data[position]
        when (holder) {
            is ShipperDetailsViewHolder -> {
                holder.bind(data, holder.adapterPosition)
            }
            is FeatureDetailsViewHolder -> {
                holder.bind(data, holder.adapterPosition)
            }
            is ServiceDetailsViewHolder -> {
                holder.bind(data, holder.adapterPosition)
            }
        }
    }

}