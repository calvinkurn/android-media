package com.tokopedia.digital_checkout.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common_digital.atc.data.response.FintechProduct
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.data.model.CartDigitalInfoData
import com.tokopedia.digital_checkout.presentation.adapter.vh.DigitalMyBillsViewHolder
import com.tokopedia.digital_checkout.presentation.adapter.vh.MyBillsActionListener

/**
 * @author by jessica on 10/03/21
 */

class DigitalMyBillsAdapter(private val crossSellingType: Int,
                            private val listener: MyBillsActionListener) : RecyclerView.Adapter<DigitalMyBillsViewHolder>() {
    private var subscriptions = listOf<CartDigitalInfoData.CrossSellingConfig>()
    private var fintechProducts = listOf<FintechProduct>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigitalMyBillsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false)
        return DigitalMyBillsViewHolder(view, listener)
    }

    override fun getItemCount(): Int = subscriptions.size + fintechProducts.size

    override fun getItemViewType(position: Int): Int {
        return if (position < subscriptions.size) SUBSCRIPTION_VIEWTYPE else FINTECH_VIEWTYPE
    }

    fun setItems(subscriptions: List<CartDigitalInfoData.CrossSellingConfig>, fintechProducts: List<FintechProduct>) {
        this.subscriptions = subscriptions
        this.fintechProducts = fintechProducts
        notifyDataSetChanged()
    }

    fun setActiveFintechProducts(activeFintechProducts: HashMap<String, FintechProduct> = hashMapOf()) {
        for (fintechProduct in fintechProducts) {
            if (activeFintechProducts.containsKey(fintechProduct.tierId)) {
                fintechProduct.optIn = true
            }
        }
        notifyDataSetChanged()
    }

    fun setActiveSubscriptions() {
        subscriptions.firstOrNull()?.let {
            it.isChecked = true
            notifyItemChanged(0)
        }
    }

    override fun onBindViewHolder(holder: DigitalMyBillsViewHolder, position: Int) {
        if (holder.itemViewType == SUBSCRIPTION_VIEWTYPE) {
            holder.bindSubscription(subscriptions[position], crossSellingType)
        } else {
            holder.bindFintechProduct(fintechProducts[position - subscriptions.size], position - subscriptions.size)
        }
    }

    companion object {
        const val SUBSCRIPTION_VIEWTYPE = 88
        const val FINTECH_VIEWTYPE = 99

        val LAYOUT = R.layout.item_digital_checkout_my_bills_section
    }
}