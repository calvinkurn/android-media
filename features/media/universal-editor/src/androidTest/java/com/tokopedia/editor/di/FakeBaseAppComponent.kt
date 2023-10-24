package com.tokopedia.editor.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.editor.di.module.TestAppModule
import dagger.Component

@ApplicationScope
@Component(modules = [TestAppModule::class])
interface FakeBaseAppComponent : BaseAppComponent
