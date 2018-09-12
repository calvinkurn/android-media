package com.tokopedia.gm.subscribe.membership.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment.Companion.DEFAULT_SUBSCRIPTION_TYPE
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment.Companion.EXTRA_SUBSCRIPTION_TYPE
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipProductFragment
import com.tokopedia.gm.subscribe.view.activity.GmProductActivity

class GmMembershipProductActivity : GmProductActivity() {

    private lateinit var fragment: GmMembershipProductFragment

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, GmMembershipProductActivity::class.java)
    }

    override fun isLightToolbarThemes(): Boolean {
        return true
    }

    override fun setActionVar() {

        val currentSelected = intent.getIntExtra(EXTRA_SUBSCRIPTION_TYPE, DEFAULT_SUBSCRIPTION_TYPE)
        fragment = GmMembershipProductFragment.newInstance(currentSelected)

        inflateFragment(
                fragment,
                false,
                SELECT_PRODUCT_TAG)
    }

    override fun onBackPressed() {
        fragment.setReturn()
    }
}