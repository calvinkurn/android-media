package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreTabletConstant
import com.tokopedia.shop.score.databinding.ItemPotentialPmToPmProBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMPotentialPMProListener
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMProBenefitAdapter
import com.tokopedia.shop.score.performance.presentation.model.SectionPMPotentialPMProUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPMPotentialPMProViewHolder(
    view: View,
    private val itemStatusPMProListener: ItemPMPotentialPMProListener
) : AbstractViewHolder<SectionPMPotentialPMProUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_potential_pm_to_pm_pro
    }

    private val binding: ItemPotentialPmToPmProBinding? by viewBinding()
    private var itemPMProBenefitAdapter: ItemPMProBenefitAdapter? = null

    override fun bind(element: SectionPMPotentialPMProUiModel?) {
        itemPMProBenefitAdapter = ItemPMProBenefitAdapter()
        binding?.run {
            tvPmReputationValue.text = getString(R.string.title_pm_value)
            potentialPowerMerchantWidget.setOnClickListener {
                itemStatusPMProListener.onPMToPMProPage()
            }
            tvSeeAllBenefitPmToPmPro.setOnClickListener {
                itemStatusPMProListener.onGotoBenefitPMPro()
            }
            icChevronRightBenefitPmToPmPro.setOnClickListener {
                itemStatusPMProListener.onGotoBenefitPMPro()
            }
        }
        setPotentialPMProBenefitAdapter(element)
    }

    private fun setPotentialPMProBenefitAdapter(element: SectionPMPotentialPMProUiModel?) {
        binding?.run {
            rvPmToPmProPotentialBenefit.run {
                layoutManager = if (DeviceScreenInfo.isTablet(context)) {
                    GridLayoutManager(context, ShopScoreTabletConstant.MAX_COLUMN_GRID)
                } else {
                    LinearLayoutManager(context)
                }
                adapter = itemPMProBenefitAdapter
            }
        }
        element?.potentialPMProPMBenefitList?.let {
            itemPMProBenefitAdapter?.setPotentialPMProBenefit(
                it
            )
        }
    }
}