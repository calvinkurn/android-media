package com.tokopedia.expresscheckout.view.variant.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.VariantChangeListener
import com.tokopedia.expresscheckout.view.variant.viewholder.OptionVariantViewHolder
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_NOT_AVAILABLE
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_NOT_SELECTED
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel.Companion.STATE_SELECTED

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class VariantOptionAdapter(var dataList: ArrayList<OptionVariantViewModel>, val listener: CheckoutVariantActionListener) :
        RecyclerView.Adapter<OptionVariantViewHolder>(), VariantChangeListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionVariantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(OptionVariantViewHolder.LAYOUT, parent, false)
        return OptionVariantViewHolder(view, this)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return OptionVariantViewHolder.LAYOUT
    }

    override fun onBindViewHolder(holder: OptionVariantViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun onSelectedVariantChanged(selectedVariant: OptionVariantViewModel) {
        for (item: OptionVariantViewModel in dataList) {
            if (item.equals(selectedVariant)) {
                item.currentState = STATE_SELECTED
            } else if (item.currentState != STATE_NOT_AVAILABLE) {
                item.currentState = STATE_NOT_SELECTED
            }
        }
        notifyDataSetChanged()
        listener.onChangeVariant(selectedVariant)
    }

}