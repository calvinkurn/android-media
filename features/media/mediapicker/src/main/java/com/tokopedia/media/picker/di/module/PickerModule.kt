package com.tokopedia.media.picker.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.picker.analytics.PickerAnalytics
import com.tokopedia.media.picker.analytics.camera.CameraAnalyticsImpl
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalyticsImpl
import com.tokopedia.picker.common.ParamCacheManager
import com.tokopedia.media.picker.data.repository.AlbumRepository
import com.tokopedia.media.picker.data.repository.AlbumRepositoryImpl
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.media.picker.data.repository.MediaRepositoryImpl
import com.tokopedia.media.picker.di.scope.PickerScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class PickerModule {

    @Provides
    @PickerScope
    fun provideUserSession(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @PickerScope
    fun providePickerAnalytics(
        userSession: UserSessionInterface
    ) = PickerAnalytics(
        CameraAnalyticsImpl(userSession),
        GalleryAnalyticsImpl()
    )

    @Provides
    @PickerScope
    fun provideParamCacheManager(
        @ApplicationContext context: Context
    ): ParamCacheManager {
        return ParamCacheManager(context)
    }

    @Provides
    @PickerScope
    fun provideAlbumRepository(
        @ApplicationContext context: Context
    ): AlbumRepository {
        return AlbumRepositoryImpl(context)
    }

    @Provides
    @PickerScope
    fun provideMediaRepository(
        @ApplicationContext context: Context
    ): MediaRepository {
        return MediaRepositoryImpl(context)
    }

}