package com.tokopedia.tokochat.stub.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.stub.di.base.FakeBaseAppComponent
import com.tokopedia.usercomponents.userconsent.di.UserConsentComponent
import com.tokopedia.usercomponents.userconsent.di.UserConsentViewModelModule
import dagger.Component

@ActivityScope
@Component(
    modules = [
        TokoChatUserConsentModuleStub::class,
        UserConsentViewModelModule::class
    ],
    dependencies = [FakeBaseAppComponent::class]
)
interface TokoChatUserConsentComponentStub : UserConsentComponent
