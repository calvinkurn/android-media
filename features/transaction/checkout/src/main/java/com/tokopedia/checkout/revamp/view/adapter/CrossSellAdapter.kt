package com.tokopedia.checkout.revamp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellItemBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellItem
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutCrossSellItemViewHolder

class CrossSellAdapter(private val listener: CheckoutAdapterListener) : RecyclerView.Adapter<CheckoutCrossSellItemViewHolder>() {

    var list: List<CheckoutCrossSellItem> = emptyList()

    var parentWidth: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutCrossSellItemViewHolder {
        return CheckoutCrossSellItemViewHolder(
            ItemCheckoutCrossSellItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CheckoutCrossSellItemViewHolder, position: Int) {
        holder.bind(list[position], parentWidth)
    }
}
