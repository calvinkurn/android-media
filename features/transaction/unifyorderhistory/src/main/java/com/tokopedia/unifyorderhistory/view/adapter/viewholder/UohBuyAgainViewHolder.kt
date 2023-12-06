package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohBuyAgainBinding
import com.tokopedia.unifyorderhistory.view.adapter.UohItemAdapter
import com.tokopedia.unifyorderhistory.view.widget.buy_again.UohBuyAgainWidget

class UohBuyAgainViewHolder(
    private val binding: UohBuyAgainBinding,
    private val actionListener: UohItemAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UohTypeData) {
        if (item.dataObject is RecommendationWidget) {
            setupBuyAgainWidget(item.dataObject)
        }
    }

    private fun setupBuyAgainWidget(recom: RecommendationWidget) {
        binding.layoutBuyAgain.apply {
            setContent {
                UohBuyAgainWidget(
                    recom,
                    onChevronClicked = {
                        actionListener?.onChevronBuyAgainWidgetClicked(recom.seeMoreAppLink)
                    },
                    onProductCardClick = { pdpApplink ->
                        actionListener?.onProductCardClicked(pdpApplink)
                    },
                    onButtonBuyAgainClick = { recommItem ->
                        actionListener?.onBuyAgainWidgetButtonClicked(recommItem)
                    }
                )
            }
        }
    }

    companion object {
        private const val UOH_BUY_AGAIN_BACKGROUND = "https://images.tokopedia.net/img/android/uoh_buy_again_bg.png"
    }
}
