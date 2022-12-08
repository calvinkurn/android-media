package com.tokopedia.smartbills.presentation.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smartbills.data.RechargeProduct
import com.tokopedia.smartbills.databinding.ViewSmartBillsItemNominalBinding

class SmartBillsNominalAdapter(val listener: SmartBillNominalListener) : RecyclerView.Adapter<SmartBillsNominalAdapter.SmartBillsNominalViewHolder>() {

    var listRechargeProduct = emptyList<RechargeProduct>()

    inner class SmartBillsNominalViewHolder(
        private val binding: ViewSmartBillsItemNominalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(rechargeProduct: RechargeProduct) {
            with(binding) {
                root.setOnClickListener {
                    listener.onClickProduct(rechargeProduct)
                }
                tvSbmNominalPrice.text = rechargeProduct.attributes.desc
                tvSbmNominalDesc.text = rechargeProduct.attributes.detail
                tvSbmNominalPriceTotal.text = rechargeProduct.attributes.price

                if (!rechargeProduct.attributes.productPromo?.newPrice.isNullOrEmpty()) {
                    tvSbmNominalPricePromo.apply {
                        show()
                        text = rechargeProduct.attributes.price
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    tvSbmNominalPriceTotal.text = rechargeProduct.attributes.productPromo?.newPrice
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listRechargeProduct.size
    }

    override fun onBindViewHolder(holder: SmartBillsNominalViewHolder, position: Int) {
        holder.bind(listRechargeProduct[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmartBillsNominalViewHolder {
        val binding = ViewSmartBillsItemNominalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SmartBillsNominalViewHolder(binding)
    }

    interface SmartBillNominalListener {
        fun onClickProduct(rechargeProduct: RechargeProduct)
    }
}
