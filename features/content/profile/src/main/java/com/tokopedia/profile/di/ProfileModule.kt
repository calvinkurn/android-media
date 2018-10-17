package com.tokopedia.profile.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.CommonNetwork
import com.tokopedia.network.NetworkRouter
import com.tokopedia.profile.data.network.TOPADS_BASE_URL
import com.tokopedia.profile.data.network.TopAdsApi
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.presenter.ProfilePresenter
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * @author by milhamj on 9/21/18.
 */
@Module
class ProfileModule {

    @ProfileScope
    @Provides
    fun provideTopAdsApi(retrofit: Retrofit): TopAdsApi {
        return retrofit.create(TopAdsApi::class.java)
    }

    @ProfileScope
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context, userSession: UserSession): Retrofit {
        if ((context is NetworkRouter).not()) {
            throw IllegalStateException("Application must implement "
                    .plus(NetworkRouter::class.java.simpleName)
            )
        }

        return CommonNetwork.createRetrofit(
                context,
                TOPADS_BASE_URL,
                context as NetworkRouter,
                userSession
        )
    }

    @ProfileScope
    @Provides
    fun providePresenter(profilePresenter: ProfilePresenter): ProfileContract.Presenter {
        return profilePresenter
    }

    @ProfileScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }
}