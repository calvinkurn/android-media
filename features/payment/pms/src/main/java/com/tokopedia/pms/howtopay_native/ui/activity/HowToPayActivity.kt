package com.tokopedia.pms.howtopay_native.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.pms.howtopay_native.di.DaggerHowToPayComponent
import com.tokopedia.pms.howtopay_native.di.HowToPayComponent
import com.tokopedia.pms.howtopay_native.di.HowToPayModule
import com.tokopedia.pms.howtopay_native.ui.fragment.HowToPayFragment

class HowToPayActivity : BaseSimpleActivity(), HasComponent<HowToPayComponent> {

    override fun getNewFragment(): Fragment? = HowToPayFragment.getInstance(this, intent.extras)

    override fun getComponent(): HowToPayComponent {
        return DaggerHowToPayComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .howToPayModule(HowToPayModule(this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
