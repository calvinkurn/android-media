package com.tokopedia.instantdebitbca.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.instantdebitbca.InstantDebitBca2Fragment
import dagger.Component

/**
 * Created by nabillasabbaha on 21/03/19.
 */
@InstantDebitBcaScope
@Component(modules = arrayOf(InstantDebitBcaModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface InstantDebitBcaComponent {

    fun inject(instantDebitBca2Fragment: InstantDebitBca2Fragment)
}