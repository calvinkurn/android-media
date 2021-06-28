package com.tokopedia.logisticaddaddress.features.district_recommendation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.databinding.ItemDistrictRecommendationRevampBinding
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.district_recommendation.viewholder.DiscomAdapterViewHolder

class DiscomAdapterRevamp(private var listener: ActionListener): RecyclerView.Adapter<DiscomAdapterViewHolder>() {

    private var districtData = mutableListOf<Address>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscomAdapterViewHolder {
        val binding = ItemDistrictRecommendationRevampBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = DiscomAdapterViewHolder.getViewHolder(binding)

        return viewHolder.apply {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onDistrictItemRevampClicked(districtData[adapterPosition])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return districtData.size
    }

    override fun onBindViewHolder(holder: DiscomAdapterViewHolder, position: Int) {
        holder.bindData(districtData[position])
    }

    fun setData(data: List<Address>) {
        districtData.clear()
        districtData.addAll(data)
        notifyDataSetChanged()
    }

    fun appendData(data: List<Address>) {
        districtData.addAll(data)
        notifyDataSetChanged()
    }


    interface ActionListener {
        fun onDistrictItemRevampClicked(districtModel: Address)
    }

}