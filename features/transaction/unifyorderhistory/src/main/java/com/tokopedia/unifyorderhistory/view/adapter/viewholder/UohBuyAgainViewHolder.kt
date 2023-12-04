package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohBuyAgainBinding
import com.tokopedia.unifyorderhistory.view.widget.buy_again.UohBuyAgainList
import com.tokopedia.unifyorderhistory.view.widget.buy_again.UohBuyAgainWidget

class UohBuyAgainViewHolder(
    private val binding: UohBuyAgainBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UohTypeData) {
        if (item.dataObject is RecommendationWidget)
            setupBuyAgainWidget(item.dataObject)
        }

    private fun setupBuyAgainWidget(recom: RecommendationWidget) {
        /*binding.uohBuyAgainTitle.text = recom.title*/
        // binding.uohBuyAgainBackground.loadImage(UOH_BUY_AGAIN_BACKGROUND)
        binding.layoutBuyAgain.apply {
            setContent {
                UohBuyAgainWidget(recom)
                // UohBuyAgainList(recom.recommendationItemList)
                /*recom.recommendationItemList.forEach {
                    UohBuyAgainCard(
                            productName = it.name,
                            productPrice = it.price,
                            slashedPrice = it.slashedPrice,
                            discountPercentage = it.discountPercentage,
                            imageUrl = it.imageUrl,
                            onProductCardClick = {}) {

                    }
                }*/
            }
        }
    }

    companion object {
        private const val UOH_BUY_AGAIN_BACKGROUND = "https://images.tokopedia.net/img/android/uoh_buy_again_bg.png"
    }
}
