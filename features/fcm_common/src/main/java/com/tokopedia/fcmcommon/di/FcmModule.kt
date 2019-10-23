package com.tokopedia.fcmcommon.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.fcmcommon.CoroutineContextProviders
import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.fcmcommon.FirebaseMessagingManagerImpl
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

@Module
class FcmModule(@ApplicationContext private val context: Context) {

    @Provides
    @Singleton
    fun provideFcmManager(
            sharedPreferences: SharedPreferences,
            repository: GraphqlRepository,
            coroutineContextProviders: CoroutineContextProviders,
            queries: Map<String, String>
    ): FirebaseMessagingManager {
        return FirebaseMessagingManagerImpl(
                context,
                sharedPreferences,
                repository,
                coroutineContextProviders,
                queries
        )
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @Singleton
    fun provideCoroutineContextProviders(): CoroutineContextProviders = CoroutineContextProviders(
            Dispatchers.Main,
            Dispatchers.IO
    )

}
