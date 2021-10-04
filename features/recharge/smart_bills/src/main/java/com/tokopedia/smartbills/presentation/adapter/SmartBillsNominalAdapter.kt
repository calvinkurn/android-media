package com.tokopedia.smartbills.presentation.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.RechargeProduct
import kotlinx.android.synthetic.main.view_smart_bills_item_nominal.view.*

class SmartBillsNominalAdapter(val listener: SmartBillNominalListener) : RecyclerView.Adapter<SmartBillsNominalAdapter.SmartBillsNominalViewHolder>() {

    var listRechargeProduct = emptyList<RechargeProduct>()

    inner class SmartBillsNominalViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun bind(rechargeProduct: RechargeProduct) {
            with(itemView) {
                setOnClickListener {
                    listener.onClickProduct(rechargeProduct)
                }
                tv_sbm_nominal_price.text = rechargeProduct.attributes.desc
                tv_sbm_nominal_desc.text = rechargeProduct.attributes.detail
                tv_sbm_nominal_price_total.text = rechargeProduct.attributes.price

                if (!rechargeProduct.attributes.productPromo?.newPrice.isNullOrEmpty()){
                    tv_sbm_nominal_price_promo.apply {
                        show()
                        text = rechargeProduct.attributes.price
                        paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }
                    tv_sbm_nominal_price_total.text = rechargeProduct.attributes.productPromo?.newPrice
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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_smart_bills_item_nominal, parent, false)
        return SmartBillsNominalViewHolder(itemView)
    }

    interface SmartBillNominalListener{
        fun onClickProduct(rechargeProduct: RechargeProduct)
    }
}