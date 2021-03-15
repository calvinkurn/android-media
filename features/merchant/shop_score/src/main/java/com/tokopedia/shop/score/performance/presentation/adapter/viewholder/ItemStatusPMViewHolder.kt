package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gm.common.utils.getShopScoreDate
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ItemStatusPowerMerchantListener
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMUiModel
import kotlinx.android.synthetic.main.item_status_power_merchant.view.*

class ItemStatusPMViewHolder(view: View,
                             private val itemStatusPowerMerchantListener: ItemStatusPowerMerchantListener) : AbstractViewHolder<ItemStatusPMUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_status_power_merchant
    }

    override fun bind(element: ItemStatusPMUiModel?) {
        with(itemView) {
            iv_pm_badge_current_status?.loadImage(element?.badgePowerMerchant.orEmpty())
            potentialPowerMerchantWidget?.background = element?.bgPowerMerchant?.let { ContextCompat.getDrawable(context, it) }
            tv_pm_reputation_value?.text = getString(R.string.title_pm_value,
                    element?.statusPowerMerchant.orEmpty())
            tv_update_date_potential_pm?.text = getString(R.string.next_update_date_pm_status, getShopScoreDate(context)).orEmpty()
            tv_desc_potential_pm?.text = MethodChecker.fromHtml(element?.descPotentialPM.orEmpty())
            ic_info_potential_pm?.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedNextUpdatePM()
            }
            ic_pm_reputation_right?.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedGoToPMActivation()
            }
        }
    }
}