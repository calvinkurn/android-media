package com.tokopedia.logisticaddaddress.features.district_recommendation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meituan.robust.patch.annotaion.Add
import com.tokopedia.logisticaddaddress.databinding.BottomsheetDistrictRecommendationItemBinding
import com.tokopedia.logisticaddaddress.databinding.ItemDistrictRecommendationRevampBinding
import com.tokopedia.logisticaddaddress.domain.model.Address

class DiscomAdapterRevamp(private var listener: ActionListener): RecyclerView.Adapter<DiscomAdapterRevamp.DiscomAdapterViewHolder>() {

    private var districtData = mutableListOf<Address>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscomAdapterViewHolder {
        val binding = ItemDistrictRecommendationRevampBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscomAdapterViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return districtData.size
    }

    override fun onBindViewHolder(holder: DiscomAdapterViewHolder, position: Int) {
        holder.bindData(districtData[position])
    }


    interface ActionListener {
        fun onDistrictItemClicked(districtModel: Address)
    }

    inner class DiscomAdapterViewHolder (binding: ItemDistrictRecommendationRevampBinding, private val listener: ActionListener): RecyclerView.ViewHolder(binding.root) {

        val tvDistrictName = binding.searchPlaceName

        fun bindData(data: Address) {
            val districtSelected = "${data.provinceName}, ${data.cityName}, ${data.districtName}"
            itemView.apply {
                tvDistrictName.text = districtSelected
                tvDistrictName.setOnClickListener {
                    listener.onDistrictItemClicked(data)
                }
            }
        }
    }
}