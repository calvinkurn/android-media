package com.tokopedia.topads.auto.base

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.di.DaggerAutoAdsComponent

/**
 * Author errysuprayogi on 20,May,2019
 */
abstract class AutoAdsBaseActivity : BaseSimpleActivity(), HasComponent<AutoAdsComponent> {

    override fun getComponent(): AutoAdsComponent = DaggerAutoAdsComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
}
