package com.tokopedia.atc_variant.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.atc_variant.view.AddToCartVariantActionListener
import com.tokopedia.atc_variant.view.VariantChangeListener
import com.tokopedia.atc_variant.view.viewholder.OptionVariantViewHolder
import com.tokopedia.atc_variant.view.viewmodel.OptionVariantViewModel
import com.tokopedia.atc_variant.view.viewmodel.OptionVariantViewModel.Companion.STATE_NOT_AVAILABLE
import com.tokopedia.atc_variant.view.viewmodel.OptionVariantViewModel.Companion.STATE_NOT_SELECTED
import com.tokopedia.atc_variant.view.viewmodel.OptionVariantViewModel.Companion.STATE_SELECTED

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class VariantOptionAdapter(var dataList: ArrayList<OptionVariantViewModel>, val listenerNormal: AddToCartVariantActionListener) :
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
        listenerNormal.onChangeVariant(selectedVariant)
    }

}