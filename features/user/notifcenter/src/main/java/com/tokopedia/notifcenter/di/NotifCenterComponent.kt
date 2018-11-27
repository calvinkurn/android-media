package com.tokopedia.notifcenter.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.notifcenter.view.fragment.NotifCenterFragment
import dagger.Component

/**
 * @author by milhamj on 30/08/18.
 */

@NotifCenterScope
@Component(
        modules = arrayOf(NotifCenterModule::class),
        dependencies = arrayOf(BaseAppComponent::class)
)
interface NotifCenterComponent {
    fun inject(fragment : NotifCenterFragment)
}
