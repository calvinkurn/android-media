package com.tokopedia.entertainment.home.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.entertainment.home.fragment.EventHomeFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.entertainment.home.di.EventHomeComponent
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.entertainment.home.di.DaggerEventHomeComponent

/**
 * Author errysuprayogi on 27,January,2020
 */
class HomeEventActivity : BaseSimpleActivity(), HasComponent<EventHomeComponent> {
    override fun getNewFragment(): Fragment = EventHomeFragment.getInstance()

    override fun getComponent(): EventHomeComponent = DaggerEventHomeComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

}