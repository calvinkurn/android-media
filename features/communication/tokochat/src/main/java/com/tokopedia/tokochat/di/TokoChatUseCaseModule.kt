package com.tokopedia.tokochat.di

import android.content.Context
import com.gojek.courier.CourierConnection
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.data.repository.TokoChatImageRepository
import com.tokopedia.tokochat.domain.usecase.TokoChatChannelUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatGetChatHistoryUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatGetImageUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatMarkAsReadUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatMutationProfileUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatRegistrationChannelUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatSendMessageUseCase
import com.tokopedia.tokochat_common.util.TokoChatCacheManager
import com.tokopedia.tokochat_common.util.TokoChatCacheManagerImpl
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import dagger.Module
import dagger.Provides

@Module
object TokoChatUseCaseModule {

    @ActivityScope
    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @ActivityScope
    @Provides
    fun provideCreateChannelUseCase(
        repository: TokoChatRepository
    ): TokoChatChannelUseCase {
        return TokoChatChannelUseCase(repository)
    }

    @ActivityScope
    @Provides
    fun provideGetChatHistoryUseCase(
        repository: TokoChatRepository
    ): TokoChatGetChatHistoryUseCase {
        return TokoChatGetChatHistoryUseCase(repository)
    }

    @ActivityScope
    @Provides
    fun provideMarkAsReadUseCase(
        repository: TokoChatRepository
    ): TokoChatMarkAsReadUseCase {
        return TokoChatMarkAsReadUseCase(repository)
    }

    @ActivityScope
    @Provides
    fun provideRegistrationActiveChannelUseCase(
        repository: TokoChatRepository
    ): TokoChatRegistrationChannelUseCase {
        return TokoChatRegistrationChannelUseCase(repository)
    }

    @ActivityScope
    @Provides
    fun provideSendMessageUseCase(
        repository: TokoChatRepository
    ): TokoChatSendMessageUseCase {
        return TokoChatSendMessageUseCase(repository)
    }

    @ActivityScope
    @Provides
    internal fun provideTopchatCacheManager(@ApplicationContext context: Context): TokoChatCacheManager {
        val topchatCachePref = context.getSharedPreferences(TokoChatValueUtil.TOKOCHAT_CACHE, Context.MODE_PRIVATE)
        return TokoChatCacheManagerImpl(topchatCachePref)
    }

    @ActivityScope
    @Provides
    fun provideMutationProfileUseCase(
        @TokoChatQualifier courierConnection: CourierConnection,
        repository: TokoChatRepository
    ): TokoChatMutationProfileUseCase {
        return TokoChatMutationProfileUseCase(courierConnection, repository)
    }

    @ActivityScope
    @Provides
    fun provideGetImageUseCase(
        repository: TokoChatImageRepository,
        dispatchers: CoroutineDispatchers
    ): TokoChatGetImageUseCase {
        return TokoChatGetImageUseCase(repository, dispatchers)
    }
}
