package com.tokopedia.common.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.universal_sharing.di.UniversalShareComponent
import com.tokopedia.universal_sharing.di.UniversalShareUseCaseModule
import dagger.Component

@ActivityScope
@Component(modules = [UniversalShareStubModule::class, UniversalShareUseCaseModule::class], dependencies = [AppStubComponent::class])
interface UniversalShareStubComponent : UniversalShareComponent
