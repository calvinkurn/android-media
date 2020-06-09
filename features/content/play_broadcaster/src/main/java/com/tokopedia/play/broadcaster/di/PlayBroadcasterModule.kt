package com.tokopedia.play.broadcaster.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.play.broadcaster.data.model.PlayCoverUploadEntity
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.pusher.PlayPusherBuilder
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocket.Companion.KEY_GROUPCHAT_PREFERENCES
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionUtil
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * Created by jegul on 20/05/20
 */
@Module(includes = [ImageUploaderModule::class])
class PlayBroadcasterModule(val mContext: Context) {

    @Provides
    @Named(PlayBroadcastDispatcher.MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Named(PlayBroadcastDispatcher.IO)
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named(PlayBroadcastDispatcher.COMPUTATION)
    fun provideComputationDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideLocalCacheHandler(): LocalCacheHandler {
        return LocalCacheHandler(mContext, KEY_GROUPCHAT_PREFERENCES)
    }

    @Provides
    fun providePlayPusher(@ApplicationContext context: Context): PlayPusher {
        return PlayPusherBuilder(context).build()
    }

    @Provides
    fun providePlayPermissionUtil(): PlayPermissionUtil {
        return PlayPermissionUtil(mContext)
    }

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @PlayBroadcasterScope
    fun provideUploadImageUseCase(@ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
                                  @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
                                  @ImageUploaderQualifier gson: Gson,
                                  @ImageUploaderQualifier userSession: UserSessionInterface,
                                  @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils)
            : UploadImageUseCase<PlayCoverUploadEntity> {
        return UploadImageUseCase(uploadImageRepository, generateHostRepository, gson, userSession,
                PlayCoverUploadEntity::class.java, imageUploaderUtils)
    }

}