package com.tokopedia.common.topupbills.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.common.topupbills.view.fragment.BaseTopupBillsFragment
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by resakemal on 12/08/19.
 */
@CommonTopupBillsScope
@Component(modules = [CommonTopupBillsModule::class, CommonTopupBillsViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CommonTopupBillsComponent {

    fun userSessionInterface(): UserSessionInterface

    fun coroutineDispatcher(): CoroutineDispatcher

    fun graphqlRepository(): GraphqlRepository

    fun inject(baseTopupBillsFragment: BaseTopupBillsFragment)

}