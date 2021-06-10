package com.tokopedia.product.detail.imagepreview.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.detail.imagepreview.data.ImagePreviewTracking
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [
    ImagePreviewPdpViewModelModule::class
])
class ImagePreviewPdpModule {

    @ImagePreviewPdpScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

    @ImagePreviewPdpScope
    @Provides
    fun provideGraphqlUseCase() = GraphqlUseCase()

    @ImagePreviewPdpScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @ImagePreviewPdpScope
    @Provides
    fun provideImagePreviewTracking() = ImagePreviewTracking()

    @ImagePreviewPdpScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context) : RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

}