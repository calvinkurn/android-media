package com.tokopedia.home.account.presentation.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.viewmodel.PowerMerchantCardViewModel
import kotlinx.android.synthetic.main.view_power_merchant.view.*

class PowerMerchantViewHolder(val view: View,
                              val listener: AccountItemListener) : AbstractViewHolder<PowerMerchantCardViewModel>(view) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_power_merchant
    }

    override fun bind(element: PowerMerchantCardViewModel?) {
        view.txt_pm_setting.text = element?.titleText
        view.txt_pm_desc.text = element?.descText
        view.image_view_pm.setImageResource(element?.iconRes ?: 0)
        view.pm_card_container.setOnClickListener{
            listener.onPowerMerchantSettingClicked()
        }
    }
}