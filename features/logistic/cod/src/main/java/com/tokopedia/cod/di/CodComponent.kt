package com.tokopedia.cod.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.cod.CodActivity
import com.tokopedia.cod.CodFragment
import dagger.Component

/**
 * Created by fajarnuha on 29/12/18.
 */
@CodScope
@Component(modules = arrayOf(CodModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface CodComponent {
    fun inject(fragment: CodFragment)
    fun inject(activity: CodActivity)
}