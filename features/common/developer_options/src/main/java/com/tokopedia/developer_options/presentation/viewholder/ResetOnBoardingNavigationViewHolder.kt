package com.tokopedia.developer_options.presentation.viewholder

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.EXTRA_IS_COACHMARK
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.KEY_FIRST_VIEW_NAVIGATION
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.KEY_P1_DONE_AS_NON_LOGIN
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.PREFERENCE_NAME
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.PREF_KEY_HOME_COACHMARK
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.PREF_KEY_HOME_COACHMARK_BALANCE
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.PREF_KEY_HOME_COACHMARK_INBOX
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity.Companion.PREF_KEY_HOME_COACHMARK_NAV
import com.tokopedia.developer_options.presentation.model.ResetOnBoardingNavigationUiModel
import com.tokopedia.unifycomponents.UnifyButton

class ResetOnBoardingNavigationViewHolder(
    itemView: View
): AbstractViewHolder<ResetOnBoardingNavigationUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_reset_onboarding_navigation
    }

    override fun bind(element: ResetOnBoardingNavigationUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.reset_onboarding_navigation_btn)
        btn.setOnClickListener {
            itemView.context.apply {
                val sharedPrefs = getSharedPreferences(KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE)
                sharedPrefs.edit()
                    .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, true)
                    .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1, true)
                    .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2, true)
                    .putBoolean(KEY_P1_DONE_AS_NON_LOGIN, false)
                    .apply()

                val homePref = getSharedPreferences(PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE)
                homePref.edit()
                    .putBoolean(PREF_KEY_HOME_COACHMARK_NAV, false)
                    .putBoolean(PREF_KEY_HOME_COACHMARK_INBOX, false)
                    .putBoolean(PREF_KEY_HOME_COACHMARK_BALANCE, false)
                    .apply()

                val chooseAddressPref = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
                chooseAddressPref.edit()
                    .putBoolean(EXTRA_IS_COACHMARK, true)
                    .apply()

                Toast.makeText(itemView.context, "Onboarding and home coachmark reset ssuccessfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}