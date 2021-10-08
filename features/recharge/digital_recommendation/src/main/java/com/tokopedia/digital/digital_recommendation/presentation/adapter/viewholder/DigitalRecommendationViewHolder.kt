package com.tokopedia.digital.digital_recommendation.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.digital_recommendation.databinding.ItemDigitalRecommendationBinding
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by furqan on 17/09/2021
 */
class DigitalRecommendationViewHolder(private val binding: ItemDigitalRecommendationBinding,
                                      private val actionListener: DigitalRecommendationItemActionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(element: DigitalRecommendationModel) {
        setupProductNameMaxLine(element)
        renderCategoryName(element)
        renderProductName(element)
        renderImage(element)
        renderBottomLabel(element)

        if (element.applink.isNotEmpty()) {
            binding.root.setOnClickListener {
                actionListener.onItemClicked(element, adapterPosition)
                RouteManager.route(binding.root.context, element.applink)
            }
        }

        actionListener.onItemBinding(element, adapterPosition)
    }

    private fun setupProductNameMaxLine(element: DigitalRecommendationModel) {
        if (element.discountTag.isNotEmpty() || element.beforePrice.isNotEmpty() || element.price.isNotEmpty()) {
            binding.tgProductDigitalRecommendation.maxLines = 2
        } else {
            binding.tgProductDigitalRecommendation.maxLines = 3
        }
        binding.tgProductDigitalRecommendation.requestLayout()
    }

    private fun renderCategoryName(element: DigitalRecommendationModel) {
        if (element.categoryName.isEmpty()) {
            binding.tgCategoryDigitalRecommendation.hide()
        } else {
            binding.tgCategoryDigitalRecommendation.show()
            binding.tgCategoryDigitalRecommendation.text = element.categoryName.trim().uppercase()
            binding.tgCategoryDigitalRecommendation.weightType = Typography.REGULAR
        }
    }

    private fun renderProductName(element: DigitalRecommendationModel) {
        if (element.productName.isEmpty()) {
            binding.tgProductDigitalRecommendation.hide()
        } else {
            binding.tgProductDigitalRecommendation.show()
            binding.tgProductDigitalRecommendation.text = MethodChecker.fromHtml(element.productName.trim())
        }
    }

    private fun renderImage(element: DigitalRecommendationModel) {
        binding.ivIconDigitalRecommendation.loadImage(element.iconUrl)
    }

    private fun renderBottomLabel(element: DigitalRecommendationModel) {
        with(binding) {
            // render price
            if (element.price.isNotEmpty()) {
                tgPriceDigitalRecommendation.text = element.price
                tgPriceDigitalRecommendation.show()
            } else {
                tgPriceDigitalRecommendation.hide()
            }

            // render real price
            if (element.beforePrice.isNotEmpty()) {
                tgRealPriceDigitalRecommendation.text = element.beforePrice
                tgRealPriceDigitalRecommendation.show()
            } else {
                tgRealPriceDigitalRecommendation.hide()
            }

            // render discount tag
            if (element.discountTag.isNotEmpty()) {
                tgDiscountPercentageDigitalRecommendation.text = element.discountTag
                tgDiscountPercentageDigitalRecommendation.show()
            } else {
                tgDiscountPercentageDigitalRecommendation.hide()
            }
        }
    }

    interface DigitalRecommendationItemActionListener {
        fun onItemBinding(element: DigitalRecommendationModel, position: Int)
        fun onItemClicked(element: DigitalRecommendationModel, position: Int)
    }
}