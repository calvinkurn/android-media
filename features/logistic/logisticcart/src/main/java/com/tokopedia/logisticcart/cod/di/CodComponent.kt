package com.tokopedia.logisticcart.cod.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.logisticcart.cod.view.CodActivity
import com.tokopedia.logisticcart.cod.view.CodFragment
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