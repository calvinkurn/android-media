package com.tokopedia.cart.view.adapter.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.ItemCartAddOnSummaryBinding
import com.tokopedia.cart.domain.model.cartlist.SummaryTransactionUiModel
import com.tokopedia.cart.view.viewholder.CartAddOnSummaryViewHolder

class CartAddOnSummaryAdapter(private val addOnSummaryList: List<SummaryTransactionUiModel.SummaryAddOns>) :
    RecyclerView.Adapter<CartAddOnSummaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAddOnSummaryViewHolder =
        CartAddOnSummaryViewHolder(
            ItemCartAddOnSummaryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CartAddOnSummaryViewHolder, position: Int) {
        holder.bind(addOnSummaryList[position])
    }

    override fun getItemCount(): Int = addOnSummaryList.size
}
