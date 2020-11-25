package com.tokopedia.dropoff.ui.dropoff_picker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.dropoff.R
import com.tokopedia.dropoff.ui.dropoff_picker.model.*
import com.tokopedia.dropoff.util.getDescription
import com.tokopedia.logisticCommon.util.toKilometers
import kotlinx.android.synthetic.main.item_empty_nearby_location.view.*
import kotlinx.android.synthetic.main.item_nearby_location.view.*

internal class NearbyStoreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mData: MutableList<DropoffVisitable> = mutableListOf()
    private var mListener: ActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_nearby_location -> NearbyStoreViewHolder(view)
            R.layout.item_empty_nearby_location -> EmptyViewHolder(view)
            R.layout.item_dropoff_list_header -> HeaderViewHolder(view)
            else -> ShimmeringViewHolder(view)
        }
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NearbyStoreViewHolder -> {
                holder.bind(mData[position] as DropoffNearbyModel)
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
        is LoadingType -> com.tokopedia.design.R.layout.item_shimmering_list
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
        fun onItemClicked(view: View, position: Int)
        fun requestAutoComplete()
    }

    /**
     *  Set as inner class and private to hide access from outside of the class while having access
     *  to member of the adapter, e.g. listener
     * */
    private inner class NearbyStoreViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(datum: DropoffNearbyModel) {
            view.tv_location_title.text = datum.addrName
            view.tv_location_desc.text = datum.getDescription()
            view.tv_distance.text = datum.storeDistance.toKilometers()
            view.setOnClickListener { mListener?.onItemClicked(it, adapterPosition) }
            view.tag = datum
        }

    }

    private inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private inner class ShimmeringViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        const val NEAREST_ITEM = 1
    }
}