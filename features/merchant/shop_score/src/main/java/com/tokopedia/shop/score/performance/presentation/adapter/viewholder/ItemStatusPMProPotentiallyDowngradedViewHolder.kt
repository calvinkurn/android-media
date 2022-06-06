package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMProPotentiallyDowngradedAdapter
import com.tokopedia.shop.score.performance.presentation.model.ItemPotentiallyDowngradedUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMProPotentiallyDowngradedUiModel

/**
 * Created by @ilhamsuaib on 06/06/22.
 */

class ItemStatusPMProPotentiallyDowngradedViewHolder(
    itemView: View
) : AbstractViewHolder<ItemStatusPMProPotentiallyDowngradedUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_status_pm_pro_potentially_downgraded
    }

    private val itemAdapter by lazy {
        ItemPMProPotentiallyDowngradedAdapter()
    }

    override fun bind(element: ItemStatusPMProPotentiallyDowngradedUiModel?) {

        setupRecommendation()
    }

    private fun setupRecommendation() {
        setRecommendationItems()
    }

    private fun setRecommendationItems() {
        val items = listOf(
            ItemPotentiallyDowngradedUiModel(
                imgUrl = ShopScoreConstant.PM_PRO_BENEFIT_URL_1,
                titleRes = R.string.ss_item_pm_recommendation_title_1,
                desRes = R.string.ss_item_pm_recommendation_desc_1
            ),
            ItemPotentiallyDowngradedUiModel(
                imgUrl = ShopScoreConstant.PM_PRO_BENEFIT_URL_1,
                titleRes = R.string.ss_item_pm_recommendation_title_2,
                desRes = R.string.ss_item_pm_recommendation_desc_2
            ),
            ItemPotentiallyDowngradedUiModel(
                imgUrl = ShopScoreConstant.PM_PRO_BENEFIT_URL_1,
                titleRes = R.string.ss_item_pm_recommendation_title_3,
                desRes = R.string.ss_item_pm_recommendation_desc_3
            )
        )

        itemAdapter.setItems(items)
    }
}