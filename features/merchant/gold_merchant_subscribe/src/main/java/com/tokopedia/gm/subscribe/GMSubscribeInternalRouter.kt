package com.tokopedia.gm.subscribe

import android.content.Context
import com.tokopedia.gm.subscribe.membership.view.activity.GmMembershipActivity
import com.tokopedia.gm.subscribe.view.activity.GmSubscribeHomeActivity

object GMSubscribeInternalRouter {

    @JvmStatic
    fun getGMMembershipIntent(context: Context) = GmMembershipActivity.createIntent(context)

    @JvmStatic
    fun getGMSubscribeHomeIntent(context: Context) = GmSubscribeHomeActivity.getCallingIntent(context)
}