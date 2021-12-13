package com.tokopedia.product_ar.view

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.model.ProductAr
import com.tokopedia.product_ar.view.adapter.VariantArAdapter
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class PartialBottomArView private constructor(val view: View) {

    companion object {
        fun build(view: View) = PartialBottomArView(view)
    }

    private val atcButton = view.findViewById<UnifyButton>(R.id.btn_atc_ar)
    private val txtStock = view.findViewById<Typography>(R.id.txt_stock_ar)
    private val txtMainPrice = view.findViewById<Typography>(R.id.txt_main_price_ar)
    private val txtSlashPrice = view.findViewById<Typography>(R.id.txt_slash_price_ar)
    private val lblDiscounted = view.findViewById<Label>(R.id.lbl_discounted_ar)
    private val rvVariant = view.findViewById<RecyclerView>(R.id.rv_ar)

    private val adapter = VariantArAdapter()

    fun renderRecyclerView(data: ProductAr) {
        renderVariantRv(data)
    }

    fun renderBottomInfo(data: ProductAr) {
        atcButton.text = data.button.text
        txtStock.text = data.stock
        renderCampaign(data)
        renderVariantRv(ProductAr())
    }

    private fun renderVariantRv(data: ProductAr) {
        rvVariant.adapter = adapter
        adapter.setInitialData(listOf(
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2021/9/25/3ed61569-fd5e-4570-835c-63a36d6363cd.png",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2021/9/25/3ed61569-fd5e-4570-835c-63a36d6363cd.png",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2021/9/25/3ed61569-fd5e-4570-835c-63a36d6363cd.png",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2021/9/25/3ed61569-fd5e-4570-835c-63a36d6363cd.png",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2021/9/25/3ed61569-fd5e-4570-835c-63a36d6363cd.png",
                "https://images.tokopedia.net/img/cache/900/VqbcmM/2021/9/25/3ed61569-fd5e-4570-835c-63a36d6363cd.png"
        ))
    }

    private fun renderCampaign(data: ProductAr) = with(view) {
        if (data.campaignInfo.isActive) {
            txtMainPrice.text = data.campaignInfo.discountedPrice.getCurrencyFormatted()
            txtSlashPrice.text = data.campaignInfo.originalPrice.getCurrencyFormatted()
            lblDiscounted.text = context.getString(
                    com.tokopedia.product.detail.common.R.string.template_campaign_off,
                    data.campaignInfo.percentageAmount.toString())

            txtSlashPrice.paintFlags = txtSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            txtSlashPrice.show()
            lblDiscounted.show()
        } else {
            txtMainPrice.text = data.price.getCurrencyFormatted()
            txtSlashPrice.hide()
            lblDiscounted.hide()
        }
    }
}