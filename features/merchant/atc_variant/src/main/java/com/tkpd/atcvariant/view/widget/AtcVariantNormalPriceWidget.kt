package com.tkpd.atcvariant.view.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.data.uidata.ProductHeaderData
import com.tkpd.atcvariant.databinding.AtcVariantNormalPriceBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.common.R as productdetailcommonR

class AtcVariantNormalPriceWidget : ConstraintLayout {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    private var _binding: AtcVariantNormalPriceBinding? = null
    private fun init(context: Context) {
        _binding = AtcVariantNormalPriceBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.atc_variant_normal_price, this)
        )
    }

    fun renderView(
        headerData: ProductHeaderData,
        cashbackPercentage:Int
    ) {
        renderCashback(cashbackPercentage)

        if (headerData.isCampaignActive) {
            renderCampaignActive(headerData)
        } else if (headerData.hideGimmick) {
            renderNoCampaign(headerData.productSlashPrice)
        } else {
            renderNoCampaign(headerData.productMainPrice)
        }
    }

    private fun renderCashback(cashBackPercentage: Int) = _binding?.run {
        lblHeaderCashbackPercentage.showIfWithBlock(cashBackPercentage > 0) {
            text = context.getString(
                productdetailcommonR.string.template_cashback,
                cashBackPercentage.toString()
            )
        }
    }

    private fun renderNoCampaign(price: String) = _binding?.run {
        lblHeaderDiscountedPercentage.hide()
        txtHeaderSlashPrice.hide()
        txtHeaderMainPrice.text = price
    }

    private fun renderCampaignActive(headerData: ProductHeaderData) = _binding?.run {
        txtHeaderMainPrice.text = headerData.productMainPrice

        txtHeaderSlashPrice.showIfWithBlock(headerData.productSlashPrice.isNotBlank()) {
            text = headerData.productSlashPrice
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        lblHeaderDiscountedPercentage.showIfWithBlock(headerData.shouldShowDiscPercentage) {
            text = headerData.productDiscountedPercentage
        }
    }
}