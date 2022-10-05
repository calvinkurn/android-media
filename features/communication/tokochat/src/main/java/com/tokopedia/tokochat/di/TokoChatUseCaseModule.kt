package com.tokopedia.tokochat.di

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokochat.data.repository.TokoChatRepository
import com.tokopedia.tokochat.domain.usecase.CreateChannelUseCase
import com.tokopedia.tokochat.domain.usecase.GetAllChannelsUseCase
import com.tokopedia.tokochat.domain.usecase.GetChatHistoryUseCase
import com.tokopedia.tokochat.domain.usecase.MarkAsReadUseCase
import com.tokopedia.tokochat.domain.usecase.RegistrationActiveChannelUseCase
import com.tokopedia.tokochat.domain.usecase.SendMessageUseCase
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
    ): CreateChannelUseCase {
        return CreateChannelUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideGetChatHistoryUseCase(
        repository: TokoChatRepository
    ): GetChatHistoryUseCase {
        return GetChatHistoryUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideGetAllChannelsUseCase(
        repository: TokoChatRepository
    ): GetAllChannelsUseCase {
        return GetAllChannelsUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideMarkAsReadUseCase(
        repository: TokoChatRepository
    ): MarkAsReadUseCase {
        return MarkAsReadUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideRegistrationActiveChannelUseCase(
        repository: TokoChatRepository
    ): RegistrationActiveChannelUseCase {
        return RegistrationActiveChannelUseCase(repository)
    }

    @TokoChatScope
    @Provides
    fun provideSendMessageUseCase(
        repository: TokoChatRepository
    ): SendMessageUseCase {
        return SendMessageUseCase(repository)
    }


    @TokoChatScope
    @Provides
    internal fun provideTopchatCacheManager(@TokoChatContext context: Context): TokoChatCacheManager {
        val topchatCachePref = context.getSharedPreferences("tokoChatCache", Context.MODE_PRIVATE)
        return TokoChatCacheManagerImpl(topchatCachePref)
    }
}
