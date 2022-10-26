package com.tokopedia.tokochat.di

import com.gojek.courier.CourierConnection
import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokochat.data.repository.TokoChatRepository
import com.tokopedia.tokochat.domain.usecase.TokoChatChannelUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatGetChatHistoryUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatGetImageUrlUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatMarkAsReadUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatMutationProfileUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatRegistrationChannelUseCase
import com.tokopedia.tokochat.domain.usecase.TokoChatSendMessageUseCase
import com.tokopedia.tokochat_common.util.TokoChatCacheManager
import com.tokopedia.tokochat_common.util.TokoChatCacheManagerImpl
import dagger.Module
import dagger.Provides

@Module
object TokoChatUseCaseModule {

    @TokoChatScope
    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @TokoChatScope
    @Provides
    fun provideCreateChannelUseCase(
        repository: TokoChatRepository
    ): TokoChatChannelUseCase {
        return TokoChatChannelUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideGetChatHistoryUseCase(
        repository: TokoChatRepository
    ): TokoChatGetChatHistoryUseCase {
        return TokoChatGetChatHistoryUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideMarkAsReadUseCase(
        repository: TokoChatRepository
    ): TokoChatMarkAsReadUseCase {
        return TokoChatMarkAsReadUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideRegistrationActiveChannelUseCase(
        repository: TokoChatRepository
    ): TokoChatRegistrationChannelUseCase {
        return TokoChatRegistrationChannelUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideSendMessageUseCase(
        repository: TokoChatRepository
    ): TokoChatSendMessageUseCase {
        return TokoChatSendMessageUseCase(repository)
    }

    @TokoChatScope
    @Provides
    internal fun provideTopchatCacheManager(@TokoChatContext context: Context): TokoChatCacheManager {
        val topchatCachePref = context.getSharedPreferences("tokoChatCache", Context.MODE_PRIVATE)
        return TokoChatCacheManagerImpl(topchatCachePref)
    }

    @TokoChatScope
    @Provides
    fun provideMutationProfileUseCase(
        repository: TokoChatRepository,
        courierConnection: CourierConnection
    ): TokoChatMutationProfileUseCase {
        return TokoChatMutationProfileUseCase(repository, courierConnection)
    }

    @TokoChatScope
    @Provides
    fun provideGetImageUrlUseCase(
        repository: TokoChatRepository,
        dispatchers: CoroutineDispatchers
    ): TokoChatGetImageUrlUseCase {
        return TokoChatGetImageUrlUseCase(repository, dispatchers)
    }
}
