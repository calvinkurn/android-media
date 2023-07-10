package com.tokopedia.checkout.revamp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.ItemCheckoutAddressBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutAddressViewHolder
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel

class CheckoutAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<CheckoutItem> = listOf(
        CheckoutAddressModel(RecipientAddressModel())
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CheckoutAddressViewHolder(ItemCheckoutAddressBinding.inflate(inflater))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is CheckoutAddressModel -> {
                (holder as CheckoutAddressViewHolder).bind(item)
            }
        }
    }
}
