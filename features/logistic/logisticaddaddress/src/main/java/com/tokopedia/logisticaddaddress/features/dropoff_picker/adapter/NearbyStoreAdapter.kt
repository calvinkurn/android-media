package com.tokopedia.logisticaddaddress.features.dropoff_picker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.domain.model.dropoff.DropoffNearbyModel
import com.tokopedia.logisticdata.data.entity.address.LocationDataModel
import kotlinx.android.synthetic.main.item_nearby_location.view.*

class NearbyStoreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mData: MutableList<DropoffVisitable> = mutableListOf()
    var mListener: ActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when(viewType) {
            R.layout.item_nearby_location -> NearbiesViewHolder(view)
            R.layout.item_empty_nearby_location -> EmptyViewHolder(view)
            else -> LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is NearbiesViewHolder -> {
                holder.bind(mData[position] as DropoffNearbyModel)
                holder.view.setOnClickListener {
                    mListener?.onItemClicked(it)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when(mData[position]) {
        is DropoffNearbyModel -> R.layout.item_nearby_location
        is EmptyType -> R.layout.item_empty_nearby_location
        is LoadingType -> com.tokopedia.design.R.layout.item_shimmering_list
        else -> throw RuntimeException("View type does not match")
    }

    fun setData(item: List<DropoffNearbyModel>) {
        mData.clear()
        if (item.isEmpty()) {
            mData.add(EmptyType())
        } else {
            mData.addAll(item)
        }
        notifyDataSetChanged()
    }

    fun setStateLoading() {
        mData.clear()
        mData.add(LoadingType())
        notifyDataSetChanged()
    }

    fun setActionListener(listener: ActionListener) {
        mListener = listener
    }

    class NearbiesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(datum: DropoffNearbyModel) {
            view.tv_location_title.text = datum.addrName
            view.tv_location_desc.text = datum.districtName
            view.tv_distance.text = datum.storeDistance
            view.tag = datum
        }
    }

    class EmptyViewHolder(val view: View): RecyclerView.ViewHolder(view)
    class LoadingViewHolder(val view: View): RecyclerView.ViewHolder(view)

    interface ActionListener {
        fun onItemClicked(view: View)
    }

    interface DropoffVisitable
    data class EmptyType(var id: Int = 0): DropoffVisitable
    data class LoadingType(var id: Int = 0): DropoffVisitable
}