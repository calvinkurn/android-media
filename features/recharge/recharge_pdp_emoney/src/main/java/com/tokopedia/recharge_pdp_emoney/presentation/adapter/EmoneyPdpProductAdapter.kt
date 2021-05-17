package com.tokopedia.recharge_pdp_emoney.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder.EmoneyPdpProductViewHolder

/**
 * @author by jessica on 09/04/21
 */
class EmoneyPdpProductAdapter : RecyclerView.Adapter<EmoneyPdpProductViewHolder>() {

    var products = listOf<CatalogProduct>()
        set(list) {
            field = list
            notifyDataSetChanged()
        }

    var listener: EmoneyPdpProductViewHolder.ActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmoneyPdpProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(EmoneyPdpProductViewHolder.LAYOUT, parent, false)
        return EmoneyPdpProductViewHolder(view, listener)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: EmoneyPdpProductViewHolder, position: Int) {
        holder.bind(products[position])
    }
}