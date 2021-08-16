package com.tokopedia.review.feature.reputationhistory.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides

/**
 * Created by normansyahputa on 2/13/18.
 */
@Module(includes = [SellerReputationViewModelModule::class])
class SellerReputationModule {

    @SellerReputationScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSession {
        return UserSession(context)
    }

    @Provides
    @SellerReputationScope
    fun provideGcmHandler(@ApplicationContext context: Context?): GCMHandler {
        return GCMHandler(context)
    }
}