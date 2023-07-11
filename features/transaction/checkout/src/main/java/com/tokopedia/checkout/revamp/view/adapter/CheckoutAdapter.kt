package com.tokopedia.checkout.revamp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.ItemCheckoutAddressBinding
import com.tokopedia.checkout.databinding.ItemShipmentProductBinding
import com.tokopedia.checkout.databinding.ItemUpsellNewImprovementBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutAddressViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutProductViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutUpsellViewHolder
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel

class CheckoutAdapter(private val listener: CheckoutAdapterListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<CheckoutItem> = listOf(
        CheckoutAddressModel(RecipientAddressModel())
    )

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is CheckoutAddressModel -> CheckoutAddressViewHolder.VIEW_TYPE
            is CheckoutProductModel -> CheckoutProductViewHolder.VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CheckoutAddressViewHolder.VIEW_TYPE -> {
                CheckoutAddressViewHolder(ItemCheckoutAddressBinding.inflate(inflater))
            }
            CheckoutProductViewHolder.VIEW_TYPE -> {
                CheckoutProductViewHolder(ItemShipmentProductBinding.inflate(inflater), listener)
            }
            else -> {
                CheckoutUpsellViewHolder(ItemUpsellNewImprovementBinding.inflate(inflater), listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is CheckoutAddressModel -> {
                (holder as CheckoutAddressViewHolder).bind(item)
            }

            is CheckoutProductModel -> {
                (holder as CheckoutProductViewHolder).bind(item)
            }
        }
    }
}
