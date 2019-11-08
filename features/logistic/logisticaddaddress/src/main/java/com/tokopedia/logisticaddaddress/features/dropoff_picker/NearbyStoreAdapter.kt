package com.tokopedia.logisticaddaddress.features.dropoff_picker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.domain.model.dropoff.DropoffNearbyModel
import com.tokopedia.logisticaddaddress.utils.toKilometers
import kotlinx.android.synthetic.main.item_empty_nearby_location.view.*
import kotlinx.android.synthetic.main.item_nearby_location.view.*

class NearbyStoreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mData: MutableList<DropoffVisitable> = mutableListOf()
    private var mListener: ActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_nearby_location -> NearbiesViewHolder(view)
            R.layout.item_empty_nearby_location -> EmptyViewHolder(view)
            R.layout.item_dropoff_list_header -> HeaderViewHolder(view)
            else -> LoadingViewholder(view)
        }
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NearbiesViewHolder -> {
                holder.bind(mData[position] as DropoffNearbyModel)
                holder.view.setOnClickListener {
                    mListener?.onItemClicked(it)
                }
            }
            is EmptyViewHolder -> {
                holder.itemView.button_search.setOnClickListener { mListener?.requestAutoComplete() }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (mData[position]) {
        is DropoffNearbyModel -> R.layout.item_nearby_location
        is EmptyType -> R.layout.item_empty_nearby_location
        is HeaderType -> R.layout.item_dropoff_list_header
        is LoadingType -> LoadingViewholder.LAYOUT
        else -> throw RuntimeException("View type does not match")
    }

    fun setData(item: List<DropoffNearbyModel>) {
        mData.clear()
        if (item.isEmpty()) {
            mData.add(EmptyType())
        } else {
            mData.add(HeaderType())
            mData.addAll(item)
        }
        notifyDataSetChanged()
    }

    fun setStateLoading() {
        mData.clear()
        mData.add(LoadingType())
        notifyDataSetChanged()
    }

    fun setError() {
        mData.clear()
        mData.add(EmptyType())
        notifyDataSetChanged()
    }

    fun setActionListener(listener: ActionListener) {
        mListener = listener
    }

    interface ActionListener {
        fun onItemClicked(view: View)
        fun requestAutoComplete()
    }

    internal interface DropoffVisitable
    private data class HeaderType(var text: String = "") : DropoffVisitable
    private data class EmptyType(var id: Int = 0) : DropoffVisitable
    private data class LoadingType(var id: Int = 0) : DropoffVisitable

    private class NearbiesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(datum: DropoffNearbyModel) {
            val desc = "${datum.districtName}, ${datum.cityName}, ${datum.provinceName}"
            view.tv_location_title.text = datum.addrName
            view.tv_location_desc.text = desc
            view.tv_distance.text = datum.storeDistance.toKilometers()
            view.tag = datum
        }

    }

    private class EmptyViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    private class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}