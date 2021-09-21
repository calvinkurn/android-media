package com.tokopedia.digital.digital_recommendation.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital.digital_recommendation.databinding.ItemDigitalRecommendationBinding
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import java.util.*

/**
 * @author by furqan on 17/09/2021
 */
class DigitalRecommendationViewHolder(private val binding: ItemDigitalRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(element: DigitalRecommendationModel) {
        renderCategoryName(element)
        renderProductName(element)
        renderImage(element)
        renderClientNumber(element)

        if (element.applink.isNotEmpty()) {
            binding.root.setOnClickListener {
                RouteManager.route(binding.root.context, element.applink)
            }
        }
    }

    private fun renderCategoryName(element: DigitalRecommendationModel) {
        if (element.categoryName.isEmpty()) {
            binding.tgCategoryDigitalRecommendation.hide()
        } else {
            binding.tgCategoryDigitalRecommendation.show()
            binding.tgCategoryDigitalRecommendation.text = element.categoryName.trim()
                    .replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                        else it.toString()
                    }
        }
    }

    private fun renderProductName(element: DigitalRecommendationModel) {
        if (element.productName.isEmpty()) {
            binding.tgProductDigitalRecommendation.hide()
        } else {
            binding.tgProductDigitalRecommendation.show()
            binding.tgProductDigitalRecommendation.text = element.productName.trim()
        }
    }

    private fun renderImage(element: DigitalRecommendationModel) {
        binding.ivIconDigitalRecommendation.loadImage(element.iconUrl)
    }

    private fun renderClientNumber(element: DigitalRecommendationModel) {
        if (element.clientNumber.isEmpty()) {
            binding.tgNumberDigitalRecommendation.hide()
            binding.tgProductDigitalRecommendation.maxLines = 3
        } else {
            binding.tgNumberDigitalRecommendation.show()
            binding.tgNumberDigitalRecommendation.text = element.clientNumber.trim()
            binding.tgProductDigitalRecommendation.maxLines = 2
        }
    }
}