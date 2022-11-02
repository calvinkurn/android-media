package com.tokopedia.loginregister.registerinitial.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterEmailFragment
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment
import dagger.Component

/**
 * @author by nisie on 10/25/18.
 */
@ActivityScope
@Component(modules = [
    RegisterInitialModule::class,
    RegisterInitialQueryModule::class,
    RegisterInitialUseCaseModule::class,
    RegisterInitialViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface RegisterInitialComponent {
    fun inject(fragment: RegisterInitialFragment)
    fun inject(fragment: RegisterEmailFragment)
}
