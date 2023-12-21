package com.tokopedia.shareexperience.data.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.shareexperience.data.util.ShareExResourceProvider
import com.tokopedia.shareexperience.data.util.ShareExResourceProviderImpl
import dagger.Module
import dagger.Provides

@Module
object ShareExModule {

    @Provides
    @ActivityScope
    fun provideResourceProvider(
        @ApplicationContext context: Context
    ): ShareExResourceProvider {
        return ShareExResourceProviderImpl(context)
    }
}
