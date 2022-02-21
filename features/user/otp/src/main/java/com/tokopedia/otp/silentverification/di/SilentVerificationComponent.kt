package com.tokopedia.otp.silentverification.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.otp.silentverification.view.fragment.SilentVerificationFragment
import dagger.Component

/**
 * Created by Yoris on 18/10/21.
 */

@ActivityScope
@Component(modules = [
    SilentVerificationModule::class,
    SilentVerificationViewModelModule::class], dependencies = [BaseAppComponent::class])
interface SilentVerificationComponent {
    fun inject(fragment: SilentVerificationFragment)
}