package com.tokopedia.topchat.stub.chatlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.module.AppModule
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(modules = [AppModule::class])
interface TestAppComponent : BaseAppComponent
