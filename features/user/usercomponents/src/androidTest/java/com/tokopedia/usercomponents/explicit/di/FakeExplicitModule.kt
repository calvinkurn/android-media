package com.tokopedia.usercomponents.explicit.di

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usercomponents.explicit.domain.GetQuestionUseCase
import com.tokopedia.usercomponents.explicit.domain.SaveAnswerUseCase
import com.tokopedia.usercomponents.explicit.domain.UpdateStateUseCase
import com.tokopedia.usercomponents.explicit.stub.data.ExplicitRepositoryStub
import dagger.Module
import dagger.Provides

@Module
class FakeExplicitModule {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideExplicitRepositoryStub(): GraphqlRepository {
        return ExplicitRepositoryStub()
    }

    @Provides
    @ApplicationScope
    fun provideGetQuestionUseCase(
        @ApplicationContext repositoryStub: ExplicitRepositoryStub,
        dispatcher: CoroutineDispatchers
    ): GetQuestionUseCase {
        return GetQuestionUseCase(repositoryStub, dispatcher)
    }

    @Provides
    @ApplicationScope
    fun provideSaveAnswerUseCase(
        @ApplicationContext repositoryStub: ExplicitRepositoryStub,
        dispatcher: CoroutineDispatchers
    ): SaveAnswerUseCase {
        return SaveAnswerUseCase(repositoryStub, dispatcher)
    }

    @Provides
    @ApplicationScope
    fun provideUpdateStateUseCase(
        @ApplicationContext repositoryStub: ExplicitRepositoryStub,
        dispatcher: CoroutineDispatchers
    ): UpdateStateUseCase {
        return UpdateStateUseCase(repositoryStub, dispatcher)
    }
}