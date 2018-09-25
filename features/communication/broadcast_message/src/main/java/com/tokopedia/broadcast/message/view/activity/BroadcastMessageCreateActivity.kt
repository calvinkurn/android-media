package com.tokopedia.broadcast.message.view.activity

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.broadcast.message.common.di.BroadcastMessageComponent
import com.tokopedia.broadcast.message.common.di.DaggerBroadcastMessageComponent
import com.tokopedia.broadcast.message.view.fragment.BroadcastMessageCreateFragment

class BroadcastMessageCreateActivity: BaseSimpleActivity(), HasComponent<BroadcastMessageComponent> {

    override fun getNewFragment() = BroadcastMessageCreateFragment()

    override fun getComponent() = DaggerBroadcastMessageComponent.builder().baseAppComponent(
            (application as BaseMainApplication).getBaseAppComponent()).build()
}