package com.tokopedia.otp.validator.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.otp.validator.view.fragment.ValidatorFragment
import dagger.Component
import dagger.Module

/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 */

@ValidatorScope
@Component(modules = [
    ValidatorModule::class,
    ValidatorQueryModule::class,
    ValidatorUseCaseModule::class,
    ValidatorViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface ValidatorComponent{
    fun inject(fragment: ValidatorFragment)
}