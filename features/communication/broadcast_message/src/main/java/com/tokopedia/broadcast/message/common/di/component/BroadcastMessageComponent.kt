package com.tokopedia.broadcast.message.common.di.component

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.broadcast.message.common.di.module.BroadcastMessageModule
import com.tokopedia.broadcast.message.common.di.scope.BroadcastMessageScope
import com.tokopedia.broadcast.message.view.fragment.BroadcastMessageListFragment
import dagger.Component
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@BroadcastMessageScope
@Component(modules = arrayOf(BroadcastMessageModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface BroadcastMessageComponent {
    fun httpLoggingInterceptor(): HttpLoggingInterceptor
    @ApplicationContext
    fun getContext(): Context
    fun getUserSession(): UserSession
    fun getAbstractionRouter(): AbstractionRouter
    fun retrofitBuilder(): Retrofit.Builder

    fun inject(fragment: BroadcastMessageListFragment)
}