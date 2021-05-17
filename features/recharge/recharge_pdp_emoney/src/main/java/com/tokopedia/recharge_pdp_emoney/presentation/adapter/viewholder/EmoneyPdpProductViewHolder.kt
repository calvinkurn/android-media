package com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.recharge_pdp_emoney.R
import kotlinx.android.synthetic.main.item_emoney_product.view.*

/**
 * @author by jessica on 09/04/21
 */
class EmoneyPdpProductViewHolder(val view: View,
                                 private val listener: ActionListener?) : RecyclerView.ViewHolder(view) {

    fun bind(item: CatalogProduct) {
        with(itemView) {
            emoneyProductTitle.text = item.attributes.desc
            emoneyProductSubtitle.text = MethodChecker.fromHtml(item.attributes.detail)
            emoneyProductPrice.text = item.attributes.price

            setOnClickListener { listener?.onClickProduct(item) }
            emoneyProductSeeDetailText.setOnClickListener {
                listener?.onClickSeeDetailProduct(item)
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_emoney_product
    }

    interface ActionListener {
        fun onClickProduct(product: CatalogProduct)
        fun onClickSeeDetailProduct(product: CatalogProduct)
    }
}