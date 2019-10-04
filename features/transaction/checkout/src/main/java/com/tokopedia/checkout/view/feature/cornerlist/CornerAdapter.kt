package com.tokopedia.checkout.view.feature.cornerlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.tokopedia.checkout.R
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel

/**
 * Created by fajarnuha on 09/02/19.
 */
class CornerAdapter(
        private val mData: MutableList<RecipientAddressModel>,
        private val mListener: OnItemClickListener) : RecyclerView.Adapter<CornerAdapter.CornerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CornerViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_corner_branch, parent, false)
        return CornerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CornerViewHolder, position: Int) {
        holder.bind(mData[position], mListener)
    }

    override fun getItemCount(): Int = mData.size

    fun setAddress(list: List<RecipientAddressModel>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    fun appendAddress(list: List<RecipientAddressModel>) {
        mData.addAll(list)
        notifyDataSetChanged()
    }

    class CornerViewHolder(var mView: View) : RecyclerView.ViewHolder(mView) {

        var mCornerName: TextView = mView.findViewById(R.id.text_view_branch_name)
        var mCornerDesc: TextView = mView.findViewById(R.id.text_view_branch_desc)

        fun bind(model: RecipientAddressModel, listener: OnItemClickListener) {
            val description = "${model.addressName}, ${model.destinationDistrictName}, ${model.cityName}"
            mCornerName.text = model.partnerName
            mCornerDesc.text = description
            mView.setOnClickListener { listener.onItemClick(model) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(corner: RecipientAddressModel)
    }
}
