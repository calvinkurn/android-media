package com.tokopedia.home_account.stub.di.user

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.home_account.stub.di.user.FakeAppModule
import dagger.Component

@ApplicationScope
@Component(modules = [FakeAppModule::class])
interface FakeBaseAppComponent : BaseAppComponent