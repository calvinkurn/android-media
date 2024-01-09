package com.tokopedia.deals.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.common.ui.activity.DealsBaseActivity
import com.tokopedia.deals.ui.brand.DealsBrandFragment
import com.tokopedia.deals.ui.brand_detail.DealsBrandDetailFragment
import com.tokopedia.deals.ui.category.DealsCategoryFragment
import com.tokopedia.deals.ui.home.ui.fragment.DealsHomeFragment
import com.tokopedia.deals.utils.DealsLocationUtils
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.logging.HttpLoggingInterceptor

@ActivityScope
@Component(
    modules = [DealsModule::class, DealsViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface DealsComponent {

    @ApplicationContext
    fun context(): Context

    fun userSessionInterface(): UserSessionInterface

    fun coroutineDispatcher(): CoroutineDispatcher

    fun dispatcher(): Dispatchers

    fun graphQlRepository(): GraphqlRepository

    fun dealsLocationUtils(): DealsLocationUtils

    fun irisSession(): IrisSession

    fun dispatcherProvider(): CoroutineDispatchers

    fun inject(dealsBaseActivity: DealsBaseActivity)

    fun httpLoggingInterceptor(): HttpLoggingInterceptor

    fun inject(dealsHomeFragment: DealsHomeFragment)
    fun inject(dealsHomeFragment: DealsBrandFragment)

    fun inject(dealsCategoryFragment: DealsCategoryFragment)
    fun inject(dealsBrandFragment: DealsBrandDetailFragment)
}
