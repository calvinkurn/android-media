package com.tokopedia.otp.silentverification.di

import android.content.Context
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Yoris on 28/10/21.
 */

@Module
class SilentVerificationModule(private val context: Context) {

    @Provides
    @SilentVerificationContext
    fun provideContext(): Context {
        return context
    }

    @Provides
    @ActivityScope
    fun provideUserSession(
        @SilentVerificationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

//    @Provides
//    @ActivityScope
//    fun provideGraphqlRepository(): GraphqlRepository {
//        return GraphqlInteractor.getInstance().graphqlRepository
//    }

//    @Provides
//    @ActivityScope
//    fun provideOtpTracker(): TrackingOtpUtil {
//        return TrackingOtpUtil(user)
//    }
}