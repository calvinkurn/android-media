package com.tokopedia.media.editor.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.editor.analytics.editorhome.EditorHomeAnalytics
import com.tokopedia.media.editor.analytics.editorhome.EditorHomeAnalyticsImpl
import com.tokopedia.media.editor.analytics.editordetail.EditorDetailAnalytics
import com.tokopedia.media.editor.analytics.editordetail.EditorDetailAnalyticsImpl
import com.tokopedia.media.editor.data.EditorNetworkServices
import com.tokopedia.media.editor.data.repository.BitmapConverterRepository
import com.tokopedia.media.editor.data.repository.BitmapConverterRepositoryImpl
import com.tokopedia.media.editor.data.repository.RemoveBackgroundRepository
import com.tokopedia.media.editor.data.repository.RemoveBackgroundRepositoryImpl
import com.tokopedia.media.editor.data.repository.ColorFilterRepository
import com.tokopedia.media.editor.data.repository.ColorFilterRepositoryImpl
import com.tokopedia.media.editor.data.repository.ContrastFilterRepository
import com.tokopedia.media.editor.data.repository.ContrastFilterRepositoryImpl
import com.tokopedia.media.editor.data.repository.RotateFilterRepository
import com.tokopedia.media.editor.data.repository.RotateFilterRepositoryImpl
import com.tokopedia.media.editor.data.repository.SaveImageRepository
import com.tokopedia.media.editor.data.repository.SaveImageRepositoryImpl
import com.tokopedia.media.editor.data.repository.WatermarkFilterRepository
import com.tokopedia.media.editor.data.repository.WatermarkFilterRepositoryImpl
import com.tokopedia.media.editor.di.EditorQualifier
import com.tokopedia.media.editor.domain.SetRemoveBackgroundUseCase
import com.tokopedia.media.editor.utils.ParamCacheManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module(includes = [EditorNetworkModule::class])
object EditorModule {

    @Provides
    @ActivityScope
    fun provideUserSession(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideImageUploaderServices(
        @EditorQualifier retrofit: Retrofit.Builder,
        @EditorQualifier okHttpClient: OkHttpClient.Builder
    ): EditorNetworkServices {
        val services = retrofit.client(okHttpClient.build()).build()
        return services.create(EditorNetworkServices::class.java)
    }

    @Provides
    @ActivityScope
    fun provideBitmapRepository(
        @ApplicationContext context: Context
    ): BitmapConverterRepository {
        return BitmapConverterRepositoryImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideEditorRepository(
        bitmapConverterRepository: BitmapConverterRepository,
        services: EditorNetworkServices
    ): RemoveBackgroundRepository {
        return RemoveBackgroundRepositoryImpl(bitmapConverterRepository, services)
    }

    @Provides
    @ActivityScope
    fun provideRemoveBackgroundUseCase(
        @ApplicationScope dispatchers: CoroutineDispatchers,
        removeBackgroundRepository: RemoveBackgroundRepository
    ) = SetRemoveBackgroundUseCase(dispatchers, removeBackgroundRepository)

    @Provides
    @ActivityScope
    fun provideColorFilterRepository(): ColorFilterRepository {
        return ColorFilterRepositoryImpl()
    }

    @Provides
    @ActivityScope
    fun provideContrastFilterRepository(): ContrastFilterRepository {
        return ContrastFilterRepositoryImpl()
    }

    @Provides
    @ActivityScope
    fun provideWatermarkFilterRepository(
        @ApplicationContext context: Context
    ): WatermarkFilterRepository {
        return WatermarkFilterRepositoryImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideRotateFilterRepository(): RotateFilterRepository {
        return RotateFilterRepositoryImpl()
    }

    @Provides
    @ActivityScope
    fun provideSaveImageRepository(
        @ApplicationContext context: Context
    ): SaveImageRepository {
        return SaveImageRepositoryImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideSaveEditorHomeAnalytics(
        userSession: UserSessionInterface,
        cacheManager: ParamCacheManager
    ): EditorHomeAnalytics {
        return EditorHomeAnalyticsImpl(userSession, cacheManager)
    }

    @Provides
    @ActivityScope
    fun provideSaveEditorDetailAnalytics(
        userSession: UserSessionInterface,
        cacheManager: ParamCacheManager
    ): EditorDetailAnalytics {
        return EditorDetailAnalyticsImpl(userSession, cacheManager)
    }
}
