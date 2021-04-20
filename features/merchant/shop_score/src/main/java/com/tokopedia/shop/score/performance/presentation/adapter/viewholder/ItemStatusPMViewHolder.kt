package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.common.utils.getShopScoreDate
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ItemStatusPowerMerchantListener
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMUiModel
import kotlinx.android.synthetic.main.item_status_power_merchant.view.*

class ItemStatusPMViewHolder(view: View,
                             private val itemStatusPowerMerchantListener: ItemStatusPowerMerchantListener) : AbstractViewHolder<ItemStatusPMUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_status_power_merchant
    }

    private val impressHolder = ImpressHolder()

    private val impressHolderPowerMerchantHeader = ImpressHolder()

    override fun bind(element: ItemStatusPMUiModel?) {
        if (element == null) return
        with(itemView) {
            addOnImpressionListener(impressHolder) {
                itemStatusPowerMerchantListener.onViewItemPowerMerchantListener(itemView)
            }
            containerDescPmSection?.showWithCondition(element.isInActivePM)

            potentialPowerMerchantWidget?.addOnImpressionListener(impressHolderPowerMerchantHeader) {
                itemStatusPowerMerchantListener.onImpressHeaderPowerMerchantSection()
            }
        }
        setupIconClickListener()
        setupItemPowerMerchant(element)
        setupContainerBackgroundColor(element)
    }

    private fun setupItemPowerMerchant(element: ItemStatusPMUiModel?) {
        with(itemView) {
            element?.badgePowerMerchant?.let { iv_pm_badge_current_status?.loadImage(it) }
            potentialPowerMerchantWidget?.background = element?.bgPowerMerchant?.let { ContextCompat.getDrawable(context, it) }
            tv_pm_reputation_value?.text = getString(R.string.title_pm_value,
                    element?.statusPowerMerchant)
            tv_update_date_potential_pm?.text = getString(R.string.next_update_date_pm_status, element?.updateDatePotential).orEmpty()
            tv_desc_potential_pm?.text = MethodChecker.fromHtml(element?.descPotentialPM)
        }
    }

    private fun setupIconClickListener() {
        with(itemView) {
            ic_info_potential_pm?.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedNextUpdatePM()
            }
            ic_pm_reputation_right?.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedGoToPMActivation()
            }
        }
    }

    private fun setupContainerBackgroundColor(element: ItemStatusPMUiModel?) {
        with(itemView) {
            if (element?.isCardBgColor == true) {
                containerDescPmSection?.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y100))
                ic_speaker_potential_pm?.setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y500))
            } else {
                containerDescPmSection?.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                ic_speaker_potential_pm?.setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            }
        }
    }
}