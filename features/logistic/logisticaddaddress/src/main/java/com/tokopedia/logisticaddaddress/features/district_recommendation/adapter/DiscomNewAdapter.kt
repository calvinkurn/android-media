package com.tokopedia.logisticaddaddress.features.district_recommendation.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.domain.model.Address
import kotlinx.android.synthetic.main.bottomsheet_district_recommendation_item.view.*

/**
 * Created by fwidjaja on 2019-05-31.
 */
class DiscomNewAdapter(private var listener: ActionListener) : RecyclerView.Adapter<DiscomNewAdapter.ViewHolder>() {

    private var mData = mutableListOf<Address>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_district_recommendation_item, parent, false))
    }

    override fun getItemCount(): Int = mData.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val provinceName = mData[position].provinceName
        val cityName = mData[position].cityName
        val districtName = mData[position].districtName
        val districtSelected = "$provinceName, $cityName, $districtName"
        holder.itemView.tv_district_name.apply {
            text = districtSelected
            setOnClickListener {
                listener.onDistrictItemClicked(mData[position])
            }
        }
    }

    fun setData(listDistrict: List<Address>) {
        mData.clear()
        mData.addAll(listDistrict)
        notifyDataSetChanged()
    }

    fun appendData(listDistrict: List<Address>) {
        mData.addAll(listDistrict)
        notifyDataSetChanged()
    }

    interface ActionListener {
        fun onDistrictItemClicked(districtModel: Address)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}