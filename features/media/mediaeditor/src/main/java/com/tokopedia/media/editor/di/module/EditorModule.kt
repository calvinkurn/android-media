package com.tokopedia.media.editor.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.editor.data.EditorNetworkServices
import com.tokopedia.media.editor.data.repository.BitmapConverterRepository
import com.tokopedia.media.editor.data.repository.BitmapConverterRepositoryImpl
import com.tokopedia.media.editor.data.repository.RemoveBgRepository
import com.tokopedia.media.editor.data.repository.RemoveBgRepositoryImpl
import com.tokopedia.media.editor.data.repository.ColorFilterRepository
import com.tokopedia.media.editor.data.repository.ColorFilterRepositoryImpl
import com.tokopedia.media.editor.data.repository.ContrastFilterRepository
import com.tokopedia.media.editor.data.repository.ContrastFilterRepositoryImpl
import com.tokopedia.media.editor.di.EditorQualifier
import com.tokopedia.media.editor.domain.SetRemoveBackgroundUseCase
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
    fun provideColorFilterManager(): ColorFilterRepository {
        return ColorFilterRepositoryImpl()
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
    ): RemoveBgRepository {
        return RemoveBgRepositoryImpl(bitmapConverterRepository, services)
    }

    @Provides
    @ActivityScope
    fun provideRemoveBackgroundUseCase(
        @ApplicationScope dispatchers: CoroutineDispatchers,
        removeBgRepository: RemoveBgRepository
    ) = SetRemoveBackgroundUseCase(dispatchers, removeBgRepository)

    @Provides
    @ActivityScope
    fun provideContrastFilterRepository(): ContrastFilterRepository {
        return ContrastFilterRepositoryImpl()
    }
}