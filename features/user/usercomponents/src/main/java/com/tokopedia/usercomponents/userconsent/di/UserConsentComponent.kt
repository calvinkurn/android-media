package com.tokopedia.usercomponents.userconsent.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.usercomponents.userconsent.ui.UserConsentWidget
import dagger.Component

@ActivityScope
@Component(modules = [
    UserConsentModule::class,
    UserConsentViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface UserConsentComponent {
    fun inject(widget: UserConsentWidget)
}