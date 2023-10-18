package com.tokopedia.product.estimasiongkir.view.adapter.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemFreeShippingBinding
import com.tokopedia.product.detail.databinding.ViewFreeShippingEtaBinding
import com.tokopedia.product.estimasiongkir.data.model.shipping.FreeShippingDataModel
import com.tokopedia.product.share.ekstensions.layoutInflater

class FreeShippingViewHolder(
    view: View
) : AbstractViewHolder<FreeShippingDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_free_shipping

        private const val COUNT_SINGLE_ETA = 1
    }

    private val binding = ItemFreeShippingBinding.bind(view)

    override fun bind(element: FreeShippingDataModel) = with(binding) {
        val logoUrl = element.logoUrl
        val title = element.title
        val description = element.description
        val estimations = element.estimations
        val isQuotaEmpty = element.isQuotaEmpty

        freeShippingLogo.showIfWithBlock(logoUrl.isNotEmpty()) {
            setImageUrl(logoUrl)
        }

        freeShippingTitle.showIfWithBlock(title.isNotEmpty()) {
            text = title
        }

        freeShippingDescription.showIfWithBlock(description.isNotEmpty()) {
            text = description
        }

        val estimationsSize = estimations.size
        if (estimationsSize == COUNT_SINGLE_ETA) {
            renderSingleEstimation(estimations.first(), isQuotaEmpty)
        } else if (estimationsSize > COUNT_SINGLE_ETA) renderMultiEstimation(estimations, isQuotaEmpty)
    }

    private fun renderSingleEstimation(
        estimation: FreeShippingDataModel.Eta,
        isQuotaEmpty: Boolean
    ) = with(binding) {
        val originalPrice = estimation.originalPrice
        val finalPrice = estimation.finalPrice
        val description = estimation.description

        freeShippingEstimation.showIfWithBlock(description.isNotEmpty()) {
            text = description
        }

        freeShippingFinalPrice.showIfWithBlock(!isQuotaEmpty && finalPrice.isNotEmpty()) {
            text = finalPrice
        }

        freeShippingOriginalPrice.showIfWithBlock(!isQuotaEmpty && originalPrice.isNotEmpty()) {
            text = originalPrice
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun renderMultiEstimation(
        estimations: List<FreeShippingDataModel.Eta>,
        isQuotaEmpty: Boolean
    ) = with(binding.freeShippingEtas) {
        removeAllViews()

        estimations.forEach { estimation ->
            val estimationView = ViewFreeShippingEtaBinding.inflate(itemView.context.layoutInflater)
            estimationView.apply {
                val description = estimation.description
                val originalPrice = estimation.originalPrice
                val finalPrice = estimation.finalPrice

                pdpFreeShippingEtaText.showIfWithBlock(description.isNotEmpty()) {
                    text = getString(R.string.location_dot_builder, description)
                }

                pdpFreeShippingOriginalPrice.showIfWithBlock(!isQuotaEmpty && originalPrice.isNotEmpty()) {
                    text = originalPrice
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }

                pdpFreeShippingPrice.showIfWithBlock(!isQuotaEmpty && finalPrice.isNotEmpty()) {
                    text = finalPrice
                }
            }
            addView(estimationView.root)
        }
    }
}
