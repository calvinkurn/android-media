package com.tokopedia.product.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.view.viewholder.ItemMerchantVoucherViewHolder

/**
 * Created by Yehezkiel on 22/09/20
 */
class ProductMerchantVoucherAdapter : RecyclerView.Adapter<ItemMerchantVoucherViewHolder>() {

    private var listOfMerchantVoucher: List<MerchantVoucherViewModel> = listOf()
    private var listener: PdpMerchantVoucherInterface? = null

    fun setData(data: List<MerchantVoucherViewModel>) {
        listOfMerchantVoucher = data
        notifyDataSetChanged()
    }

    fun setListener(listener: PdpMerchantVoucherInterface) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMerchantVoucherViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(ItemMerchantVoucherViewHolder.LAYOUT, parent, false)
        return ItemMerchantVoucherViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int = listOfMerchantVoucher.size

    override fun onBindViewHolder(holder: ItemMerchantVoucherViewHolder, position: Int) {
        holder.bind(listOfMerchantVoucher[position])
    }

    interface PdpMerchantVoucherInterface {
        fun onMerchantVoucherClicked(data: MerchantVoucherViewModel)
    }
}