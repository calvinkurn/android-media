package com.tokopedia.recharge_pdp_emoney.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.item_emoney_product.view.*

/**
 * @author by jessica on 09/04/21
 */
class EmoneyPdpProductViewHolder(val view: View,
                                 private val listener: ActionListener?) : RecyclerView.ViewHolder(view) {

    fun bind(item: CatalogProduct, isSelectedItem: Boolean, position: Int) {
        with(itemView) {
            emoneyProductTitle.text = item.attributes.desc
            emoneyProductSubtitle.text = MethodChecker.fromHtml(item.attributes.detail)

            if (item.attributes.price.isNotEmpty()) {
                emoneyProductPrice.text = item.attributes.price
            } else {
                emoneyProductPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(item.attributes.pricePlain.toIntOrZero())
            }
            setOnClickListener { listener?.onClickProduct(item, position) }
            emoneyProductSeeDetailText.setOnClickListener {
                listener?.onClickSeeDetailProduct(item)
            }
            cardEmoneyProduct.cardType = if (isSelectedItem) CardUnify.TYPE_BORDER_ACTIVE else CardUnify.TYPE_SHADOW
        }
    }

    companion object {
        val LAYOUT = R.layout.item_emoney_product
    }

    interface ActionListener {
        fun onClickProduct(product: CatalogProduct, position: Int)
        fun onClickSeeDetailProduct(product: CatalogProduct)
    }
}