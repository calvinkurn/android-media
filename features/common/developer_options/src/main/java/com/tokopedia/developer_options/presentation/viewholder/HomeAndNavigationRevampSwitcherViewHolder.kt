package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.HomeAndNavigationRevampSwitcherUiModel
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.remoteconfig.RollenceKey.NAVIGATION_EXP_OS_BOTTOM_NAV_EXPERIMENT
import com.tokopedia.remoteconfig.RollenceKey.NAVIGATION_VARIANT_OS_BOTTOM_NAV_EXPERIMENT
import com.tokopedia.unifycomponents.UnifyButton

class HomeAndNavigationRevampSwitcherViewHolder(
    itemView: View
): AbstractViewHolder<HomeAndNavigationRevampSwitcherUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_home_and_navigation_revamp_switcher
    }

    override fun bind(element: HomeAndNavigationRevampSwitcherUiModel) {
        val btnNewNavigation = itemView.findViewById<UnifyButton>(R.id.new_navigation_btn)
        val btnExpOsBottomNavigation = itemView.findViewById<UnifyButton>(R.id.os_exp_btn)
        val btnOldBalanceWidget = itemView.findViewById<UnifyButton>(R.id.old_balance_widget_btn)
        val btnNewBalanceWidget = itemView.findViewById<UnifyButton>(R.id.new_balance_widget_btn)
        val btnOldInbox = itemView.findViewById<UnifyButton>(R.id.old_inbox_btn)
        val btnNewInbox = itemView.findViewById<UnifyButton>(R.id.new_inbox_btn)

        itemView.context.apply {
            btnNewNavigation.setOnClickListener {
                RemoteConfigInstance.getInstance().abTestPlatform.deleteKeyLocally(NAVIGATION_EXP_OS_BOTTOM_NAV_EXPERIMENT)
                Toast.makeText(this, "Navigation: Revamped", Toast.LENGTH_SHORT).show()
            }

            btnExpOsBottomNavigation.setOnClickListener {
                RemoteConfigInstance.getInstance().abTestPlatform.setString(NAVIGATION_EXP_OS_BOTTOM_NAV_EXPERIMENT, NAVIGATION_VARIANT_OS_BOTTOM_NAV_EXPERIMENT)
                Toast.makeText(this, "Navigation: OS Removed", Toast.LENGTH_SHORT).show()
            }

            btnOldBalanceWidget.setOnClickListener {
                RemoteConfigInstance.getInstance().abTestPlatform.setString(RollenceKey.BALANCE_EXP, RollenceKey.BALANCE_VARIANT_OLD)
                Toast.makeText(this, "balance widget: Old", Toast.LENGTH_SHORT).show()
            }

            btnNewBalanceWidget.setOnClickListener {
                RemoteConfigInstance.getInstance().abTestPlatform.setString(RollenceKey.BALANCE_EXP, RollenceKey.BALANCE_VARIANT_NEW)
                Toast.makeText(this, "balance widget: Revamped", Toast.LENGTH_SHORT).show()
            }
        }
    }
}