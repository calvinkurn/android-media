package com.tokopedia.gm.subscribe.membership.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment
import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.membership.di.DaggerGmSubscribeMembershipComponent
import com.tokopedia.gm.subscribe.membership.di.GmSubscribeMembershipComponent
import com.tokopedia.gm.subscribe.membership.di.GmSubscribeMembershipModule

/**
 * Deeplink: GOLD_MERCHANT_MEMBERSHIP
 */
class GmMembershipActivity : BaseSimpleActivity(), HasComponent<GmSubscribeMembershipComponent> {

    override fun getComponent(): GmSubscribeMembershipComponent {
        return DaggerGmSubscribeMembershipComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .gmSubscribeMembershipModule(GmSubscribeMembershipModule()).build()
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, GmMembershipActivity::class.java)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_gm_subscribe_membership
    }

    override fun getNewFragment() = GmMembershipFragment.newInstance()
}