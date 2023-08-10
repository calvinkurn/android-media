package com.tokopedia.autocompletecomponent

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.datastore.UserSessionDataStore
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal fun createIntent(queryParams: String = ""): Intent {
    return Intent(InstrumentationRegistry.getInstrumentation().targetContext, AutoCompleteActivityStub::class.java).also {
        it.data = Uri.parse(ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?q=" + queryParams)
    }
}

internal fun createFakeBaseAppComponent(context: Context) = object : BaseAppComponent {
    override fun getContext(): Context {
        return context.applicationContext
    }

    override fun retrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    override fun gson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .setPrettyPrinting()
            .serializeNulls()
            .create()
    }

    override fun provideAbstractionRouter(): AbstractionRouter {
        TODO("Not yet implemented")
    }

    override fun headerErrorResponseInterceptor(): HeaderErrorResponseInterceptor {
        TODO("Not yet implemented")
    }

    override fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        TODO("Not yet implemented")
    }

    override fun getCacheManager(): CacheManager {
        TODO("Not yet implemented")
    }

    override fun coroutineDispatchers(): CoroutineDispatchers {
        return CoroutineTestDispatchersProvider
    }

    override fun graphqlRepository(): GraphqlRepository {
        TODO("Not yet implemented")
    }

    override fun graphqlInterface(): GraphqlUseCaseInterface {
        TODO("Not yet implemented")
    }

    override fun userSessionDataStore(): UserSessionDataStore {
        TODO("Not yet implemented")
    }
}

internal fun ViewInteraction.isDisplayed() {
    this.check(matches(ViewMatchers.isDisplayed()))
}
