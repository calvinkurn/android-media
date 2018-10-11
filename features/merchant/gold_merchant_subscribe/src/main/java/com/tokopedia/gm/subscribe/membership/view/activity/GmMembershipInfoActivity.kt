package com.tokopedia.gm.subscribe.membership.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipInfoFragment

class GmMembershipInfoActivity : BaseSimpleActivity(){

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, GmMembershipInfoActivity::class.java)
    }

    override fun getNewFragment() = GmMembershipInfoFragment.newInstance()
}