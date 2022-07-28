package com.tokopedia.home_account.privacy_account.stub.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component

@ApplicationScope
@Component(modules = [FakePrivacyAccountModule::class])
interface FakePrivacyAccountComponentStub : BaseAppComponent {
    @ApplicationContext
    fun repo(): GraphqlRepository
}