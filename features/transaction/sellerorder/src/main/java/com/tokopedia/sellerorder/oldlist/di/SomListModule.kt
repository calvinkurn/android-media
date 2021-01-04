package com.tokopedia.sellerorder.oldlist.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import dagger.Module
import dagger.Provides

/**
 * Created by fwidjaja on 06/05/20.
 */

@Module
@SomListScope
class SomListModule {
    @SomListScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl = FirebaseRemoteConfigImpl(context)
}