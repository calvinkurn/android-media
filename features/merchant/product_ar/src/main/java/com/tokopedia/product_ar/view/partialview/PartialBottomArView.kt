package com.tokopedia.product_ar.view.partialview

import android.graphics.Paint
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.product.detail.common.generateTheme
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.model.ModifaceUiModel
import com.tokopedia.product_ar.model.ProductAr
import com.tokopedia.product_ar.view.ProductArListener
import com.tokopedia.product_ar.view.adapter.VariantArAdapter
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class PartialBottomArView private constructor(val view: View, val listener: ProductArListener) {

    companion object {
        fun build(view: View, listener: ProductArListener) = PartialBottomArView(view, listener)
    }

    private val atcButton = view.findViewById<UnifyButton>(R.id.btn_atc_ar)
    private val txtStock = view.findViewById<Typography>(R.id.txt_stock_ar)
    private val txtMainPrice = view.findViewById<Typography>(R.id.txt_main_price_ar)
    private val txtSlashPrice = view.findViewById<Typography>(R.id.txt_slash_price_ar)
    private val lblDiscounted = view.findViewById<Label>(R.id.lbl_discounted_ar)
    val rvVariant = view.findViewById<RecyclerView>(R.id.rv_ar)
    private val buttonContainer = view.findViewById<ConstraintLayout>(R.id.container_button_ar)
    private val txtUnavailableProduct = view.findViewById<Typography>(R.id.txt_unavailable_product)

    val adapter = VariantArAdapter(listener)

    init {
        rvVariant.adapter = adapter
    }

    fun showView() {
        rvVariant?.show()
        buttonContainer?.show()
    }

    fun renderRecyclerView(data: List<ModifaceUiModel>) {
        adapter.updateList(data)
    }

    fun renderBottomInfoText(data: ProductAr) {
        if (data.unavailableCopy.isNotEmpty()) {
            hideNormalFlow()
            txtUnavailableProduct.show()
            txtUnavailableProduct.text = data.unavailableCopy
        } else {
            txtUnavailableProduct.hide()
            txtStock.run {
                text = data.stockCopy
                show()
            }
            renderCampaign(data)
        }
        atcButton.text = data.button.text
        atcButton?.run {
            text = data.button.text
            generateTheme(data.button.color)
            setOnClickListener {
                setLoadingButton()
                listener.onButtonClicked("")
            }
        }
    }

    private fun setLoadingButton() = with(atcButton) {
        isLoading = true
        isClickable = false
    }

    fun stopLoadingButton() = with(atcButton) {
        isLoading = false
        isClickable = true
    }

    private fun hideNormalFlow() {
        txtStock.hide()
        txtMainPrice.hide()
        txtSlashPrice.hide()
        lblDiscounted.hide()
        txtSlashPrice.hide()
    }

    private fun renderCampaign(data: ProductAr) = with(view) {
        if (data.campaignInfo.isActive == true) {
            txtMainPrice.text = data.campaignInfo.discountedPrice?.getCurrencyFormatted()
            txtSlashPrice.text = data.campaignInfo.originalPrice?.getCurrencyFormatted()
            lblDiscounted.text = context.getString(
                    com.tokopedia.product.detail.common.R.string.template_campaign_off_string,
                    data.campaignInfo.discountedPercentage.toIntSafely().toString())

            txtSlashPrice.paintFlags = txtSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            txtMainPrice.show()
            txtSlashPrice.show()
            lblDiscounted.show()
        } else {
            txtMainPrice.text = data.price.getCurrencyFormatted()
            txtMainPrice.show()
            txtSlashPrice.hide()
            lblDiscounted.hide()
        }
    }
}