package com.tokopedia.recharge_pdp_emoney.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder.EmoneyPdpProductViewHolder

/**
 * @author by jessica on 09/04/21
 */
class EmoneyPdpProductAdapter : RecyclerView.Adapter<EmoneyPdpProductViewHolder>(),
        EmoneyPdpProductViewHolder.ActionListener {

    private var products = listOf<CatalogProduct>()

    var listener: EmoneyPdpProductViewHolder.ActionListener? = null

    var selectedProductIndex: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmoneyPdpProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(EmoneyPdpProductViewHolder.LAYOUT, parent, false)
        return EmoneyPdpProductViewHolder(view, this)
    }

    fun updateProducts(productList: List<CatalogProduct>) {
        products = productList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: EmoneyPdpProductViewHolder, position: Int) {
        holder.bind(products[position], position == selectedProductIndex, position)
    }

    override fun onClickProduct(product: CatalogProduct, position: Int) {
        if (selectedProductIndex == null) {
            selectedProductIndex = position
            notifyItemChanged(position)
        } else {
            selectedProductIndex?.let { selectedPos ->
                notifyItemChanged(selectedPos)
            }
            selectedProductIndex = position
            notifyItemChanged(position)
        }
        listener?.onClickProduct(product, position)
    }

    override fun onClickSeeDetailProduct(product: CatalogProduct) {
        listener?.onClickSeeDetailProduct(product)
    }
}