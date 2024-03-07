package com.tokopedia.shareexperience.stub.di.base

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [ShareExFakeAppModule::class])
interface ShareExFakeBaseAppComponent : BaseAppComponent
