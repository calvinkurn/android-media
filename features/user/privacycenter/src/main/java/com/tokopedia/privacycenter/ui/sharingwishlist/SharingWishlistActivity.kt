package com.tokopedia.privacycenter.ui.sharingwishlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.privacycenter.di.ActivityComponentFactory
import com.tokopedia.privacycenter.di.PrivacyCenterComponent

class SharingWishlistActivity : BaseSimpleActivity(), HasComponent<PrivacyCenterComponent> {

    override fun getComponent(): PrivacyCenterComponent {
        return ActivityComponentFactory.instance.createComponent(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment? {
        return intent.data?.getQueryParameter(ApplinkConstInternalUserPlatform.PARAM_TAB)?.let {
            SharingWishlistFragment.createInstance(it)
        }
    }
}
