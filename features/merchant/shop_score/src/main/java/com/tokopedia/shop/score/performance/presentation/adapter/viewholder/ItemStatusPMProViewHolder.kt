package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.gm.common.constant.PMProURL
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.performance.presentation.adapter.ItemStatusPowerMerchantProListener
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMProUiModel
import kotlinx.android.synthetic.main.item_status_power_merchant_pro.view.*

class ItemStatusPMProViewHolder(view: View,
                                private val itemStatusPowerMerchantProListener: ItemStatusPowerMerchantProListener) : AbstractViewHolder<ItemStatusPMProUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_status_power_merchant_pro
    }

    override fun bind(element: ItemStatusPMProUiModel?) {
        setupIconClickListener()
        setupItemPowerMerchantPro()
    }

    private fun setupItemPowerMerchantPro() {
        with(itemView) {
            ic_pm_pro_badge_current_status?.loadImage(PMProURL.ICON_URL)
            tv_pm_pro_reputation_value?.text = getString(R.string.title_pm_pro_value)
            tv_desc_content_pm_pro_section?.setTextMakeHyperlink(getString(R.string.desc_content_pm_pro_section)) {
                itemStatusPowerMerchantProListener.onItemClickedPMProPage()
            }
        }
    }

    private fun setupIconClickListener() {
        with(itemView) {
            ic_pm_pro_reputation_right?.setOnClickListener {
                itemStatusPowerMerchantProListener.onItemClickedGoToPMProActivation()
            }
        }
    }
}