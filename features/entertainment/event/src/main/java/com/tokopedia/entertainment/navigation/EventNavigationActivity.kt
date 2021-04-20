package com.tokopedia.entertainment.navigation

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.di.DaggerEventHomeComponent
import com.tokopedia.entertainment.home.di.EventHomeComponent

class EventNavigationActivity : BaseSimpleActivity(), HasComponent<EventHomeComponent> {

    override fun getComponent(): EventHomeComponent = DaggerEventHomeComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.activity_navigation_event
    }
}