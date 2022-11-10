package com.tokopedia.deals.pdp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.databinding.ItemDealsDetailLocationBinding
import com.tokopedia.deals.pdp.data.Outlet
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class DealsDetailAllLocationAdapter(private val locationCallBack: LocationCallBack?) : RecyclerView.Adapter<DealsDetailAllLocationAdapter.ViewHolder>() {

    private var outlets: MutableList<Outlet> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDealsDetailLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return outlets.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(outlets[position])
    }

    fun addOutlets(outlets: List<Outlet>) {
        this.outlets.clear()
        this.outlets = outlets.toMutableList()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemDealsDetailLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(outlet: Outlet) {
            with(binding) {
                tgLocationName.text = outlet.name
                tgLocationAddress.text = outlet.district
                if (outlet.coordinates.isNullOrEmpty()) {
                    ivMap.hide()
                } else {
                    ivMap.show()
                    ivMap.setOnClickListener {
                        locationCallBack?.onClickLocation(outlet.coordinates)
                    }
                }
            }
        }
    }

    interface LocationCallBack {
        fun onClickLocation(latLang: String)
    }
}
