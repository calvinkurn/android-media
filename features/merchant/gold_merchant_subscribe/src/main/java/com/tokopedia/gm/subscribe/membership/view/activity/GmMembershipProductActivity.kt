package com.tokopedia.gm.subscribe.membership.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment.Companion.DEFAULT_SUBSCRIPTION_TYPE
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment.Companion.EXTRA_SUBSCRIPTION_TYPE
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipProductFragment
import com.tokopedia.gm.subscribe.view.activity.GmProductActivity

class GmMembershipProductActivity : GmProductActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, GmMembershipProductActivity::class.java)
    }

    override fun isLightToolbarThemes(): Boolean {
        return true
    }

    override fun setActionVar() {
        val currentSelected = intent.getIntExtra(EXTRA_SUBSCRIPTION_TYPE, DEFAULT_SUBSCRIPTION_TYPE)
        inflateFragment(
                GmMembershipProductFragment.newInstance(currentSelected, getString(R.string.gmsubscribe_product_button_subscribe)),
                false,
                SELECT_PRODUCT_TAG)
    }
}