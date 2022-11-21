package com.tokopedia.privacycenter.sharingwishlist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.privacycenter.common.di.DaggerPrivacyCenterComponent
import com.tokopedia.privacycenter.common.di.PrivacyCenterComponent

class SharingWishlistActivity : BaseSimpleActivity(), HasComponent<PrivacyCenterComponent> {

    override fun getComponent(): PrivacyCenterComponent {
        return DaggerPrivacyCenterComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
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
