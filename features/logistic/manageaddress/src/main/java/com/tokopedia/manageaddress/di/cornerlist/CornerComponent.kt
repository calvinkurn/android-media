package com.tokopedia.manageaddress.di.cornerlist

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.manageaddress.ui.cornerlist.CornerListFragment
import dagger.Component

/**
 * Created by Irfan Khoirul on 2019-08-30.
 */

@CornerScope
@Component(modules = [CornerModule::class], dependencies = [BaseAppComponent::class])
interface CornerComponent {
    fun inject(cornerListFragment: CornerListFragment)
}