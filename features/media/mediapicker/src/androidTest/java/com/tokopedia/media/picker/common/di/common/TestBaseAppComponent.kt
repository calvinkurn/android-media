package com.tokopedia.media.picker.common.di.common

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [TestAppModule::class])
interface TestBaseAppComponent : BaseAppComponent