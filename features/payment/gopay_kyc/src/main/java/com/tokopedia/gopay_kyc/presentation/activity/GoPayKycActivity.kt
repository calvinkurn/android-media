package com.tokopedia.gopay_kyc.presentation.activity

import android.app.Activity
import android.content.Intent
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gopay_kyc.di.GoPayKycComponent

class GoPayKycActivity : BaseSimpleActivity(), HasComponent<GoPayKycComponent> {

    override fun getNewFragment() = null
    override fun getScreenName() = null

    override fun getComponent() = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}