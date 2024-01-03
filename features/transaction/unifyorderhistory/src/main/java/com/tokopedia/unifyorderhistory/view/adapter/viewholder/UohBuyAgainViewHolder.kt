package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.visible
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
            visible()
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                UohBuyAgainWidget(
                    recom,
                    onChevronClicked = {
                        actionListener?.onChevronBuyAgainWidgetClicked(recom.seeMoreAppLink)
                    },
                    onProductCardClick = { recommItem, index ->
                        actionListener?.onProductCardClicked(recommItem, index)
                    },
                    onButtonBuyAgainClick = { recommItem, index ->
                        actionListener?.onBuyAgainWidgetButtonClicked(recommItem, index)
                    },
                    onSeeAllCardClick = {
                        actionListener?.onSeeAllCardClicked(recom.seeMoreAppLink)
                    },
                    onItemScrolled = { recommItem, index ->
                        actionListener?.onBuyAgainItemScrolled(recommItem, index)
                    },
                    onWidgetImpressed = {
                        actionListener?.onImpressBuyAgainWidget()
                    }
                )
            }
        }
    }
}
