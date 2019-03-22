package com.tokopedia.checkout.view.feature.promostacking.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.checkout.view.feature.promostacking.viewholder.ClashingViewHolder
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOptionUiModel
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 22/03/19.
 */

class ClashingAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = ArrayList<ClashingVoucherOptionUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ClashingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return ClashingViewHolder.LAYOUT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ClashingViewHolder) {
            holder.bind(data[position])
        }
    }

}