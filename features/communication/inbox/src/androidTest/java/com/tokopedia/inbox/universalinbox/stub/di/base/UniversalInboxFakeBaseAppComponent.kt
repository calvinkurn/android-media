package com.tokopedia.inbox.universalinbox.stub.di.base

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [UniversalInboxFakeAppModule::class])
interface UniversalInboxFakeBaseAppComponent : BaseAppComponent
