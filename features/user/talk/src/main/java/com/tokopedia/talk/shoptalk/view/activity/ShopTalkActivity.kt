package com.tokopedia.talk.shoptalk.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.talk.common.TalkRouter
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.shoptalk.view.fragment.ShopTalkFragment

/**
 * @author by nisie on 9/17/18.
 */
class ShopTalkActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ShopTalkFragment.newInstance(bundle)
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

}