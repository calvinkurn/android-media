package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticdata.data.entity.address.LocationDataModel
import kotlinx.android.synthetic.main.item_nearby_location.view.*

class NearbyStoreAdapter : RecyclerView.Adapter<NearbyStoreAdapter.NearbiesViewHolder>() {

    val mData: MutableList<LocationDataModel> = mutableListOf()
    var mListener: ActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbiesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nearby_location, parent, false)
        return NearbiesViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: NearbiesViewHolder, position: Int) {
        holder.bind(mData[position])
        holder.view.setOnClickListener {
            mListener?.onItemClicked(it)
        }
    }

    fun setData(item: List<LocationDataModel>) {
        mData.clear()
        mData.addAll(item)
        notifyDataSetChanged()
    }

    fun setActionListener(listener: ActionListener) {
        mListener = listener
    }

    class NearbiesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(datum: LocationDataModel) {
            view.tv_location_title.text = datum.addrName
            view.tv_location_desc.text = datum.districtName
            view.tv_distance.text = datum.storeDistance
            view.tag = datum
        }
    }

    interface ActionListener {
        fun onItemClicked(view: View)
    }
}