package com.tokopedia.otp.silentverification.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.otp.silentverification.di.DaggerSilentVerificationComponent
import com.tokopedia.otp.silentverification.di.SilentVerificationComponent
import com.tokopedia.otp.silentverification.di.SilentVerificationModule
import com.tokopedia.otp.silentverification.view.fragment.SilentVerificationFragment

/**
 * Created by Yoris on 18/10/21.
 */

class SilentVerificationActivity: BaseSimpleActivity(), HasComponent<SilentVerificationComponent> {

    override fun getComponent(): SilentVerificationComponent {
        return DaggerSilentVerificationComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .silentVerificationModule(SilentVerificationModule(this))
            .build()
    }

    override fun getNewFragment(): Fragment {
        val bundle = if(intent.extras != null) intent.extras else Bundle()
        return SilentVerificationFragment.createInstance(bundle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0F
    }
}