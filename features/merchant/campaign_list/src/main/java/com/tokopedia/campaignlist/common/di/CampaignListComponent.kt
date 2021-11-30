package com.tokopedia.campaignlist.common.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaignlist.page.presentation.activity.CampaignListActivity
import com.tokopedia.campaignlist.page.presentation.fragment.CampaignListFragment
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@CampaignListScope
@Component(modules = [CampaignListModule::class, CampaignListViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CampaignListComponent {

    @ApplicationContext
    fun getContext(): Context

    fun getRetrofitBuilder(): Retrofit.Builder

    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor

    fun getCoroutineDispatcher(): CoroutineDispatcher

    fun gson(): Gson

    fun coroutineDispatchers(): CoroutineDispatchers

    @ApplicationContext
    fun graphqlRepository(): GraphqlRepository

    fun inject(campaignListActivity: CampaignListActivity)

    fun inject(campaignListFragment: CampaignListFragment)
}