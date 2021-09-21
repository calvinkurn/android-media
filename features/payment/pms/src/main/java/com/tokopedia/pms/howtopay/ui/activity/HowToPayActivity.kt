package com.tokopedia.pms.howtopay.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.pms.howtopay.ui.fragment.HowToPayFragment
import com.tokopedia.pms.paymentlist.di.DaggerPmsComponent
import com.tokopedia.pms.paymentlist.di.PmsComponent

class HowToPayActivity : BaseSimpleActivity(), HasComponent<PmsComponent> {

    override fun getNewFragment(): Fragment? = HowToPayFragment.getInstance(this, intent.extras)

    override fun getComponent(): PmsComponent {
        return DaggerPmsComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
    }
}
