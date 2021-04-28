package com.tokopedia.analyticsdebugger.cassava.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
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
)
interface CassavaComponent {
    fun inject(mainValidatorActivity: MainValidatorActivity)
    fun inject(mainValidatorActivity: MainValidatorFragment)
    fun inject(validatorListFragment: ValidatorListFragment)
}