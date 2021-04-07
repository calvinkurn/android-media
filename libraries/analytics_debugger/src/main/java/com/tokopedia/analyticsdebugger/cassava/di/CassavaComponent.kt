package com.tokopedia.analyticsdebugger.cassava.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.analyticsdebugger.cassava.validator.MainValidatorActivity
import com.tokopedia.analyticsdebugger.cassava.validator.list.ValidatorListFragment
import com.tokopedia.analyticsdebugger.cassava.validator.main.MainValidatorFragment
import dagger.Component

/**
 * @author by furqan on 07/04/2021
 */
@CassavaScope
@Component(
        modules = [CassavaModule::class, CassavaViewModelModule::class],
        dependencies = [BaseAppComponent::class]
)
interface CassavaComponent {

    @ApplicationContext
    fun context(): Context

    fun inject(mainValidatorActivity: MainValidatorActivity)
    fun inject(mainValidatorActivity: MainValidatorFragment)
    fun inject(validatorListFragment: ValidatorListFragment)
}