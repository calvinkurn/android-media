package com.tokopedia.home_account.consentwithdrawal.fakes.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.common.di.FakeAppModule
import dagger.Component

@ApplicationScope
@Component(
    modules = [
    FakeAppModule::class,
    FakeConsentWithdrawalModule::class
]
)
interface FakeConsentWithdrawalComponent : BaseAppComponent {

    @ApplicationContext
    fun repo(): GraphqlRepository
}
