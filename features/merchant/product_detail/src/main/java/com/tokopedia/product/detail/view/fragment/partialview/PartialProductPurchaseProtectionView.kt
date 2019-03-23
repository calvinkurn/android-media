package com.tokopedia.product.detail.view.fragment.partialview

import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import kotlinx.android.synthetic.main.partial_variant_rate_estimation.view.*

class PartialProductPurchaseProtectionView private constructor(private val view: View) {
    companion object {
        fun build(_view: View) = PartialProductPurchaseProtectionView(_view)
    }


    fun renderData(productInfo: ProductPurchaseProtectionInfo) {
        with(view) {
            if (productInfo.ppItemDetailPage?.isProtectionAvailable!!) {

                purchase_protection_divider.visible()
                icon_purchase_protection.visible()
                txt_purchase_protection_title.visible()
                txt_purchase_protection_message.visible()

                txt_purchase_protection_message.text = productInfo.ppItemDetailPage!!.titlePDP
                txt_purchase_protection_message.text = productInfo.ppItemDetailPage!!.subTitlePDP
            } else {
                purchase_protection_divider.gone()
                icon_purchase_protection.gone()
                txt_purchase_protection_title.gone()
                txt_purchase_protection_message.gone()
            }
        }
    }

}