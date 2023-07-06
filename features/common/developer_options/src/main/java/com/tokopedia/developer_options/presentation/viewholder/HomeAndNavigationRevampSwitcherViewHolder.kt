package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.HomeAndNavigationRevampSwitcherUiModel
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.unifycomponents.UnifyButton

class HomeAndNavigationRevampSwitcherViewHolder(
    itemView: View,
    private val listener: HomeAndNavigationRevampListener
): AbstractViewHolder<HomeAndNavigationRevampSwitcherUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_home_and_navigation_revamp_switcher
    }

    override fun bind(element: HomeAndNavigationRevampSwitcherUiModel) {
        val btnOldBalanceWidget = itemView.findViewById<UnifyButton>(R.id.old_balance_widget_btn)
        val btnNewBalanceWidget = itemView.findViewById<UnifyButton>(R.id.new_balance_widget_btn)
        val btnSkipOnBoardingUserSession = itemView.findViewById<UnifyButton>(R.id.skip_onboarding_user_session_btn)

        itemView.context.apply {
            btnOldBalanceWidget.setOnClickListener {
                RemoteConfigInstance.getInstance().abTestPlatform.setString(RollenceKey.BALANCE_EXP, RollenceKey.BALANCE_VARIANT_OLD)
                Toast.makeText(this, "balance widget: Old", Toast.LENGTH_SHORT).show()
            }

            btnNewBalanceWidget.setOnClickListener {
                RemoteConfigInstance.getInstance().abTestPlatform.setString(RollenceKey.BALANCE_EXP, RollenceKey.BALANCE_VARIANT_NEW)
                Toast.makeText(this, "balance widget: Revamped", Toast.LENGTH_SHORT).show()
            }

            btnSkipOnBoardingUserSession.setOnClickListener {
                listener.onClickSkipOnBoardingBtn()
            }
        }
    }

    interface HomeAndNavigationRevampListener {
        fun onClickSkipOnBoardingBtn()
    }
}
