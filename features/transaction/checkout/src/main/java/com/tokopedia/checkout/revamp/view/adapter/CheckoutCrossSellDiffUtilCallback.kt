package com.tokopedia.checkout.revamp.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel

class CheckoutCrossSellDiffUtilCallback(
    private val newList: List<CheckoutCrossSellItem>,
    private val oldList: List<CheckoutCrossSellItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldList.size) return false
        if (newItemPosition >= newList.size) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is CheckoutEgoldModel && newItem is CheckoutEgoldModel) {
            oldItem == newItem
        } else if (oldItem is CheckoutCrossSellModel && newItem is CheckoutCrossSellModel) {
            oldItem == newItem
        } else if (oldItem is CheckoutDonationModel && newItem is CheckoutDonationModel) {
            oldItem == newItem
        } else {
            oldItem::class == newItem::class
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldList.size) return false
        if (newItemPosition >= newList.size) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }
}
