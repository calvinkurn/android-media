package com.tokopedia.purchase_platform.common.feature.promo_clashing.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.purchase_platform.common.feature.promo_clashing.viewholder.ClashingViewHolder
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOptionUiModel
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 22/03/19.
 */

class ClashingAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data = ArrayList<ClashingVoucherOptionUiModel>()
    private lateinit var listener: ActionListener

    interface ActionListener {
        fun onVoucherItemSelected(index: Int, isSelected: Boolean)
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

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
            holder.bind(data[position], listener)
        }
    }

}