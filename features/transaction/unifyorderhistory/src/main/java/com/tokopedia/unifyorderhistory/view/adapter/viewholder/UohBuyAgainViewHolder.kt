package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohBuyAgainBinding
import com.tokopedia.unifyorderhistory.view.widget.buy_again.UohBuyAgainCard

class UohBuyAgainViewHolder(
    private val binding: UohBuyAgainBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UohTypeData) {
        if (item.dataObject is RecommendationWidget)
            setupBuyAgainWidget(item.dataObject)
        }

    private fun setupBuyAgainWidget(recom: RecommendationWidget) {
        binding.layoutBuyAgain.apply {
            setContent {
                recom.recommendationItemList.forEach {
                    UohBuyAgainCard(
                            productName = it.name,
                            productPrice = it.price,
                            slashedPrice = it.slashedPrice,
                            discountPercentage = it.discountPercentage,
                            imageUrl = it.imageUrl,
                            onProductCardClick = {}) {

                    }
                }
            }
        }
    }
}
