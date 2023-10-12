package com.tokopedia.chooseaccount.view.ocl

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.chooseaccount.R
import com.tokopedia.chooseaccount.di.ChooseAccountComponent
import com.tokopedia.chooseaccount.di.DaggerChooseAccountComponent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.header.HeaderUnify
import com.tokopedia.sessioncommon.tracker.OclTracker

class OclChooseAccountActivity : BaseSimpleActivity(), HasComponent<ChooseAccountComponent> {

    override fun getNewFragment(): Fragment {
        return OclChooseAccountFragment.createInstance()
    }

    override fun getComponent(): ChooseAccountComponent {
        val appComponent = (application as BaseMainApplication)
            .baseAppComponent
        return DaggerChooseAccountComponent.builder()
            .baseAppComponent(appComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (toolbar as HeaderUnify).apply {
            headerTitle = getString(R.string.login)
            actionText = getString(R.string.register)
            actionTextView?.setOnClickListener {
                OclTracker.sendClickOnButtonDaftarEvent()
                goToRegisterInitial()
            }
        }
    }

    fun goToRegisterInitial() {
        var intent = RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.INIT_REGISTER)
        if (GlobalConfig.isSellerApp()) {
            intent = RouteManager.getIntent(this, ApplinkConst.CREATE_SHOP)
        }
        intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
        startActivity(intent)
        finish()
    }
}
