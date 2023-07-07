package com.tokopedia.checkout.revamp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.ViewItemShipmentRecipientAddressBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutAddressViewHolder

class CheckoutAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<CheckoutItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CheckoutAddressViewHolder(ViewItemShipmentRecipientAddressBinding.inflate(inflater))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }
}
