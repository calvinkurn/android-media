package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.PMConstant
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.databinding.ItemStatusPmProPotentiallyDowngradedBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMProPotentiallyDowngradedAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.ItemPotentiallyDowngradedUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMProPotentiallyDowngradedUiModel

/**
 * Created by @ilhamsuaib on 06/06/22.
 */

class ItemStatusPMProPotentiallyDowngradedViewHolder(
    itemView: View,
    private val shopPerformanceListener: ShopPerformanceListener
) : AbstractViewHolder<ItemStatusPMProPotentiallyDowngradedUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_status_pm_pro_potentially_downgraded
    }

    private val itemAdapter by lazy {
        ItemPMProPotentiallyDowngradedAdapter()
    }

    private val binding: ItemStatusPmProPotentiallyDowngradedBinding by lazy {
        ItemStatusPmProPotentiallyDowngradedBinding.bind(itemView)
    }

    override fun bind(element: ItemStatusPMProPotentiallyDowngradedUiModel) {
        setupRecommendation()
        setupOnPmIdle(element.isPmIdle)
        setupView()
    }

    private fun setupView() {
        binding.run {
            potentialPowerMerchantWidget.setOnClickListener {
                shopPerformanceListener.onGotoPMProPage()
            }
            tvSsPmSectionSeeAllBenefits.setOnClickListener {
                seeAllSteps()
            }
            iconSsPmSectionSeeAllBenefits.setOnClickListener {
                seeAllSteps()
            }
        }
    }

    private fun seeAllSteps() {
        shopPerformanceListener.onBtnLearnNowToSellerEduClicked(ShopScoreConstant.POWER_MERCHANT_IDLE)
    }

    private fun setupOnPmIdle(isPmIdle: Boolean) {
        binding.run {
            tvPmReputationValue.text = if (isPmIdle) {
                root.context.getString(R.string.title_pm_value)
            } else {
                root.context.getString(R.string.title_pm_pro_value)
            }
            tvSsPmProSection.text = if (isPmIdle) {
                root.context.getString(R.string.ss_pm_idle_section_section_description)
            } else {
                root.context.getString(R.string.ss_pm_pro_potentially_downgraded_section_description)
            }
            bgContainerPmStatus.isVisible = !isPmIdle
            ssPmSectionActiveStatus.isVisible = isPmIdle

            if (isPmIdle) {
                icPmBadgeCurrentStatus.loadImage(PMConstant.Images.PM_BADGE_INACTIVE)
            } else {
                icPmBadgeCurrentStatus.setImage(newIconId = IconUnify.BADGE_PMPRO_FILLED)
            }
        }
    }

    private fun setupRecommendation() {
        binding.run {
            setRecommendationItems()
            rvSsPmSectionBenefits.layoutManager = object : LinearLayoutManager(root.context) {
                override fun canScrollVertically(): Boolean = false
            }
            rvSsPmSectionBenefits.adapter = itemAdapter
        }
    }

    private fun setRecommendationItems() {
        val items = listOf(
            ItemPotentiallyDowngradedUiModel(
                imgUrl = ShopScoreConstant.PM_RECOMMENDATION_URL_1,
                titleRes = R.string.ss_item_pm_recommendation_title_1,
                desRes = R.string.ss_item_pm_recommendation_desc_1
            ),
            ItemPotentiallyDowngradedUiModel(
                imgUrl = ShopScoreConstant.PM_RECOMMENDATION_URL_2,
                titleRes = R.string.ss_item_pm_recommendation_title_2,
                desRes = R.string.ss_item_pm_recommendation_desc_2
            ),
            ItemPotentiallyDowngradedUiModel(
                imgUrl = ShopScoreConstant.PM_RECOMMENDATION_URL_3,
                titleRes = R.string.ss_item_pm_recommendation_title_3,
                desRes = R.string.ss_item_pm_recommendation_desc_3
            )
        )

        itemAdapter.setItems(items)
    }
}