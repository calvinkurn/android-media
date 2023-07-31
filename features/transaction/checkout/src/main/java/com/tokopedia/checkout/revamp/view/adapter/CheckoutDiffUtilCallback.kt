package com.tokopedia.checkout.revamp.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel

class CheckoutDiffUtilCallback(
    private val newList: List<CheckoutItem>,
    private val oldList: List<CheckoutItem>
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

        return if (oldItem is CheckoutTickerModel && newItem is CheckoutAddressModel) {
            oldItem == newItem
        } else if (oldItem is CheckoutAddressModel && newItem is CheckoutAddressModel) {
            oldItem == newItem
        } else if (oldItem is CheckoutUpsellModel && newItem is CheckoutUpsellModel) {
            oldItem == newItem
        } else if (oldItem is CheckoutProductModel && newItem is CheckoutProductModel) {
            oldItem == newItem
        } else if (oldItem is CheckoutOrderModel && newItem is CheckoutOrderModel) {
            oldItem == newItem
        } else if (oldItem is CheckoutEpharmacyModel && newItem is CheckoutEpharmacyModel) {
            oldItem == newItem
        } else if (oldItem is CheckoutPromoModel && newItem is CheckoutPromoModel) {
            oldItem == newItem
        } else if (oldItem is CheckoutCostModel && newItem is CheckoutCostModel) {
            oldItem == newItem
        } else if (oldItem is CheckoutCrossSellGroupModel && newItem is CheckoutCrossSellGroupModel) {
            oldItem == newItem
        } else if (oldItem is CheckoutButtonPaymentModel && newItem is CheckoutButtonPaymentModel) {
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
