package com.tokopedia.checkout.revamp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellItemBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellItem
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutCrossSellItemViewHolder

class CrossSellAdapter: RecyclerView.Adapter<CheckoutCrossSellItemViewHolder>() {

    var list: List<CheckoutCrossSellItem> = emptyList()

    var parent: ViewGroup? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutCrossSellItemViewHolder {
        this.parent = parent
        return CheckoutCrossSellItemViewHolder(
            ItemCheckoutCrossSellItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CheckoutCrossSellItemViewHolder, position: Int) {
        holder.bind(list[position], this.parent)
    }
}
