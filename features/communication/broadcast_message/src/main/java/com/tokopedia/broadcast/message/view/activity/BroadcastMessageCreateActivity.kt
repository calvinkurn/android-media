package com.tokopedia.broadcast.message.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.broadcast.message.common.di.component.BroadcastMessageComponent
import com.tokopedia.broadcast.message.common.di.component.DaggerBroadcastMessageComponent
import com.tokopedia.broadcast.message.view.fragment.BroadcastMessageCreateFragment

class BroadcastMessageCreateActivity: BaseSimpleActivity(), HasComponent<BroadcastMessageComponent> {

    companion object {
        fun createIntent(context: Context) = Intent(context, BroadcastMessageCreateActivity::class.java)
    }

    override fun getNewFragment() = BroadcastMessageCreateFragment()

    override fun getComponent() = DaggerBroadcastMessageComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()
}