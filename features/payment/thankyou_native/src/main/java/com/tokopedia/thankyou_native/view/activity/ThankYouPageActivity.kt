package com.tokopedia.thankyou_native.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.thankyou_native.di.DaggerThankYouPageComponent
import com.tokopedia.thankyou_native.di.ThankYouPageComponent
import com.tokopedia.thankyou_native.view.fragment.LoaderFragment


class ThankYouPageActivity : BaseSimpleActivity(), HasComponent<ThankYouPageComponent> {

    override fun getNewFragment(): Fragment? = LoaderFragment()

    override fun getComponent(): ThankYouPageComponent = DaggerThankYouPageComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication)
                    .baseAppComponent).build()
}