package com.tokopedia.digital.digital_recommendation.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.digital_recommendation.databinding.ItemDigitalRecommendationBinding
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationItemModel
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

    fun bind(element: DigitalRecommendationItemModel) {
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

    private fun renderCategoryName(element: DigitalRecommendationItemModel) {
        if (element.categoryName.isEmpty()) {
            binding.tgCategoryDigitalRecommendation.hide()
        } else {
            binding.tgCategoryDigitalRecommendation.show()
            binding.tgCategoryDigitalRecommendation.text = element.categoryName.trim().uppercase()
            binding.tgCategoryDigitalRecommendation.weightType = Typography.REGULAR
        }
    }

    private fun renderProductName(element: DigitalRecommendationItemModel) {
        if (element.productName.isEmpty()) {
            binding.tgProductDigitalRecommendation.hide()
        } else {
            binding.tgProductDigitalRecommendation.show()
            binding.tgProductDigitalRecommendation.text = MethodChecker.fromHtml(element.productName.trim())
        }
    }

    private fun renderImage(element: DigitalRecommendationItemModel) {
        binding.ivIconDigitalRecommendation.loadImage(element.iconUrl)
    }

    private fun renderBottomLabel(element: DigitalRecommendationItemModel) {
        with(binding) {
            // render price
            if (element.price.isNotEmpty()) {
                tgPriceDigitalRecommendation.text = element.price
                tgPriceDigitalRecommendation.weightType = Typography.REGULAR
                tgPriceDigitalRecommendation.show()
            } else {
                tgPriceDigitalRecommendation.hide()
            }

            // render real price
            if (element.beforePrice.isNotEmpty()) {
                tgRealPriceDigitalRecommendation.text = element.beforePrice
                tgRealPriceDigitalRecommendation.weightType = Typography.REGULAR
                tgRealPriceDigitalRecommendation.show()
            } else {
                tgRealPriceDigitalRecommendation.hide()
            }

            // render discount tag
            if (element.discountTag.isNotEmpty()) {
                tgDiscountPercentageDigitalRecommendation.text = element.discountTag
                tgDiscountPercentageDigitalRecommendation.weightType = Typography.REGULAR
                tgDiscountPercentageDigitalRecommendation.show()
            } else {
                tgDiscountPercentageDigitalRecommendation.hide()
            }
        }
    }

    interface DigitalRecommendationItemActionListener {
        fun onItemBinding(element: DigitalRecommendationItemModel, position: Int)
        fun onItemClicked(element: DigitalRecommendationItemModel, position: Int)
    }
}