package com.tokopedia.play.broadcaster.di.setup

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.play.broadcaster.data.model.PlayCoverUploadEntity
import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.repository.PlayBroadcastSetupDataStoreImpl
import com.tokopedia.play.broadcaster.util.coroutine.CommonCoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ImageUploaderModule::class])
class PlayBroadcastSetupModule {

    @PlayBroadcastSetupScope
    @Provides
    fun provideSetupDataStore(): PlayBroadcastSetupDataStore = PlayBroadcastSetupDataStoreImpl()

    @PlayBroadcastSetupScope
    @Provides
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: UserSessionInterface,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<PlayCoverUploadEntity> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession, PlayCoverUploadEntity::class.java, imageUploaderUtils)
    }

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideCoroutineDispatcherProvider(): CoroutineDispatcherProvider = CommonCoroutineDispatcherProvider()
}