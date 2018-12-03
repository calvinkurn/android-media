package com.tokopedia.expresscheckout.view.variant.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.expresscheckout.view.variant.VariantChangeListener
import com.tokopedia.expresscheckout.view.variant.viewholder.CheckoutVariantOptionVariantViewHolder
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantOptionVariantViewModel

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class VariantOptionAdapter(var dataList: ArrayList<CheckoutVariantOptionVariantViewModel>) :
        RecyclerView.Adapter<CheckoutVariantOptionVariantViewHolder>(), VariantChangeListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutVariantOptionVariantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(CheckoutVariantOptionVariantViewHolder.LAYOUT, parent, false)
        return CheckoutVariantOptionVariantViewHolder(view, this)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CheckoutVariantOptionVariantViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun onSelectedVariantChanged(selectedVariant: CheckoutVariantOptionVariantViewModel) {
        for (item: CheckoutVariantOptionVariantViewModel in dataList) {
            if (item.equals(selectedVariant)) {
                item.currentState = item.STATE_SELECTED
            } else if (item.currentState != item.STATE_NOT_AVAILABLE) {
                item.currentState = item.STATE_NOT_SELECTED
            }
        }
        notifyDataSetChanged()
    }

}