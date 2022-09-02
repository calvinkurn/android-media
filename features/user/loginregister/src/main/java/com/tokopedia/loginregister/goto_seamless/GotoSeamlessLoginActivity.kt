package com.tokopedia.loginregister.goto_seamless

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.loginregister.goto_seamless.di.DaggerGotoSeamlessComponent
import com.tokopedia.loginregister.goto_seamless.di.GotoSeamlessComponent

class GotoSeamlessLoginActivity: BaseSimpleActivity(), HasComponent<GotoSeamlessComponent> {

    override fun getNewFragment(): Fragment {
        return GotoSeamlessLoginFragment.createInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0F
    }

    override fun getComponent(): GotoSeamlessComponent {
        return DaggerGotoSeamlessComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }
}