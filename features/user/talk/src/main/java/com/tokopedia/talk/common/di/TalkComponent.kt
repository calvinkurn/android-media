package com.tokopedia.talk.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.talk.common.data.TalkApi
import com.tokopedia.talk.talkdetails.data.api.DetailTalkApi
import com.tokopedia.user.session.UserSession
import dagger.Component
import retrofit2.Retrofit

/**
 * @author by nisie on 8/28/18.
 */
@TalkScope
@Component(modules = arrayOf(TalkModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface TalkComponent {

    @ApplicationContext
    fun getContext(): Context

    fun getUserSession(): UserSession

    fun getTalkApi(): TalkApi

    fun getTalkDetailsApi(): DetailTalkApi

    fun retrofitBuilder(): Retrofit.Builder

}
