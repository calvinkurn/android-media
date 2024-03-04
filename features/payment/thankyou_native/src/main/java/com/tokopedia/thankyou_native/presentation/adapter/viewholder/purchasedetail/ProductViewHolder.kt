package com.tokopedia.thankyou_native.presentation.adapter.viewholder.purchasedetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.JvmMediaLoader
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.databinding.ThankPurchaseDetailProductBinding
import com.tokopedia.thankyou_native.presentation.adapter.model.purchasedetail.ProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProductViewHolder(val view: View) : AbstractViewHolder<ProductUiModel>(view) {

    private val binding: ThankPurchaseDetailProductBinding? by viewBinding()

    override fun bind(element: ProductUiModel) {
        binding?.pdName?.shouldShowWithAction(element.name.isNotEmpty()) {
            binding?.pdName?.text = element.name
        }

        binding?.pdProductIcon?.let {
            it.shouldShowWithAction(element.iconUrl.isNotEmpty()) {
                JvmMediaLoader.loadImage(it, element.iconUrl)
            }
        }

        binding?.pdSubProductName?.shouldShowWithAction(element.subName.isNotEmpty()) {
            binding?.pdSubProductName?.text = element.subName
        }

        binding?.pdQuantityXPrice?.shouldShowWithAction(element.quantity != 0L && element.priceStr.isNotEmpty()) {
            binding?.pdQuantityXPrice?.text = element.quantity.toString()+" X "+element.priceStr
        }

        binding?.pdTotalPrice?.shouldShowWithAction(element.totalPriceStr.isNotEmpty()) {
            binding?.pdTotalPrice?.text = element.totalPriceStr
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_purchase_detail_product
    }
}
