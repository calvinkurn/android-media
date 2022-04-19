package com.tokopedia.interestpick.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.interestpick.view.fragment.InterestPickFragment
import dagger.Component

/**
 * @author by milhamj on 07/09/18.
 */

@InterestPickScope
@Component(
        modules = [InterestPickModule::class, InterestPickViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface InterestPickComponent {
    fun inject(fragment: InterestPickFragment)
}