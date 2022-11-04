package com.tokopedia.privacycenter

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.privacycenter.di.DaggerPrivacyCenterComponent
import com.tokopedia.privacycenter.di.PrivacyCenterComponent

class PrivacyCenterActivity : BaseSimpleActivity(), HasComponent<PrivacyCenterComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()

        //make full transparent statusBar
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun getNewFragment(): Fragment =
        PrivacyCenterFragment.newInstance()

    override fun getComponent(): PrivacyCenterComponent {
        return DaggerPrivacyCenterComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }
}
