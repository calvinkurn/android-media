package com.tokopedia.mediauploader.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.mediauploader.manager.UploadMediaNotificationManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [MediaUploaderModule::class])
class MediaUploaderTestModule(
    private val context: Context
) {

    @Provides
    @MediaUploaderTestScope
    fun provideUserSession(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @MediaUploaderTestScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @MediaUploaderTestScope
    fun provideUploadMediaNotificationManager(): UploadMediaNotificationManager {
        return UploadMediaNotificationManager(context)
    }

}