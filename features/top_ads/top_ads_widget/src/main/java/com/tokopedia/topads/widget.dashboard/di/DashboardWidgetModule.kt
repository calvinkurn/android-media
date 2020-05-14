package com.tokopedia.topads.widget.dashboard.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.CommonErrorResponseInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import com.tokopedia.topads.widget.dashboard.internal.UrlConstant.BASE_REST_URL
import com.tokopedia.topads.widget.dashboard.internal.UrlConstant.PATH_TOPADS_DASHBOARD_STATISTIC
import com.tokopedia.topads.widget.dashboard.viewmodel.DashboardWidgetViewModel
import dagger.Binds

/**
 * Author errysuprayogi on 25,October,2019
 */
@DashboardWidgetScope
@Module
class DashboardWidgetModule {
    @DashboardWidgetScope
    @Provides
    @IntoMap
    @StringKey(PATH_TOPADS_DASHBOARD_STATISTIC)
    fun provideGroupProductAdsURL(): String = BASE_REST_URL + PATH_TOPADS_DASHBOARD_STATISTIC

    @DashboardWidgetScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @DashboardWidgetScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @DashboardWidgetScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @DashboardWidgetScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

}