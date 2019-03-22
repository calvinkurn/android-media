package com.tokopedia.checkout.view.feature.promostacking.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.checkout.view.feature.promostacking.viewholder.ClashingInnerViewHolder
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 22/03/19.
 */

class ClashingInnerAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = ArrayList<ClashingVoucherOrderUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ClashingInnerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return ClashingInnerViewHolder.LAYOUT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ClashingInnerViewHolder) {
            holder.bind(data[position])
        }
    }

}