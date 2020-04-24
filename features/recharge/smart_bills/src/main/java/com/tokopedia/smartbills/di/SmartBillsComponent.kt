package com.tokopedia.smartbills.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component

@SmartBillsScope
@Component(modules = [SmartBillsModule::class, SmartBillsViewModelModule::class], dependencies = [CommonTopupBillsComponent::class])
interface SmartBillsComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun graphqlRepository(): GraphqlRepository

    fun inject(smartBillsFragment: SmartBillsFragment)

}