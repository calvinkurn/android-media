package com.tokopedia.play.broadcaster.di.setup

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.di.MediaUploaderModule
import com.tokopedia.play.broadcaster.util.cover.ImageTransformer
import com.tokopedia.play.broadcaster.util.cover.PlayCoverImageUtil
import com.tokopedia.play.broadcaster.util.cover.PlayCoverImageUtilImpl
import com.tokopedia.play.broadcaster.util.cover.PlayMinimumCoverImageTransformer
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import dagger.Module
import dagger.Provides

@Module(includes = [MediaUploaderModule::class])
class PlayBroadcastSetupModule {

    @Provides
    fun provideGraphQLRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideUpdateChannelUseCase(graphqlRepository: GraphqlRepository): UpdateChannelUseCase {
        return UpdateChannelUseCase(graphqlRepository)
    }

    @Provides
    @PlayBroadcastSetupScope
    fun provideCoverImageUtil(@ApplicationContext context: Context): PlayCoverImageUtil = PlayCoverImageUtilImpl(context)

    @Provides
    @PlayBroadcastSetupScope
    fun provideCoverImageTransformer(): ImageTransformer = PlayMinimumCoverImageTransformer()

}