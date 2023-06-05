package com.tokopedia.common.di

import com.tokopedia.universal_sharing.di.UniversalShareComponent
import dagger.Component

@Component(modules = [UniversalShareUseCaseModuleStub::class], dependencies = [AppModuleStub::class])
interface UniversalShareComponentStub : UniversalShareComponent
