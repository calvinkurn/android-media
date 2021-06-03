package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.setTextMakeHyperlink
import com.tokopedia.shop.score.performance.presentation.adapter.ItemStatusPowerMerchantListener
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMUiModel
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.item_status_power_merchant.view.*

class ItemStatusPMViewHolder(view: View,
                             private val itemStatusPowerMerchantListener: ItemStatusPowerMerchantListener) : AbstractViewHolder<ItemStatusPMUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_status_power_merchant
    }

    override fun bind(element: ItemStatusPMUiModel?) {
        setupIconClickListener()
        setupItemPowerMerchant(element)
        setupDarkModeColor()
    }

    private fun setupDarkModeColor() {
        with(itemView) {
            if(context.isDarkMode()) {
                containerPowerMerchant?.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Background))
            }
        }
    }

    private fun setupItemPowerMerchant(element: ItemStatusPMUiModel?) {
        with(itemView) {
            tv_pm_reputation_value?.text = getString(R.string.title_pm_value)
            element?.descPM?.let {
                tv_desc_content_pm_section?.setTextMakeHyperlink(it) {
                    itemStatusPowerMerchantListener.onItemClickedGotoPMPro()
                }
                if (element.isNewSellerProjection) {
                    tv_desc_content_pm_section?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                } else {
                    tv_desc_content_pm_section?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
                }
            }
            tv_title_content_pm_section?.showWithCondition(element?.isNewSellerProjection == false)
        }
    }

    private fun setupIconClickListener() {
        with(itemView) {
            ic_pm_reputation_right?.setOnClickListener {
                itemStatusPowerMerchantListener.onItemClickedGoToPMActivation()
            }
            if (ic_pm_reputation_right?.isVisible == true) {
                itemStatusPowerMerchantListener.onImpressHeaderPowerMerchantSection()
            }
        }
    }
}