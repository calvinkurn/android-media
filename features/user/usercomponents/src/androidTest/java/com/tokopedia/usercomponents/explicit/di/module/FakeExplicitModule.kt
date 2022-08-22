package com.tokopedia.usercomponents.explicit.di.module

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usercomponents.explicit.domain.GetQuestionUseCase
import com.tokopedia.usercomponents.explicit.domain.SaveAnswerUseCase
import com.tokopedia.usercomponents.explicit.domain.UpdateStateUseCase
import com.tokopedia.usercomponents.explicit.stub.data.ExplicitRepositoryStub
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewContract
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewModel
import dagger.Module
import dagger.Provides

@Module
object FakeExplicitModule {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideExplicitRepositoryStub(): GraphqlRepository {
        return ExplicitRepositoryStub()
    }

    @Provides
    fun provideGetQuestionUseCase(
        @ApplicationContext repository: GraphqlRepository,
        @ApplicationScope dispatchers: CoroutineDispatchers,
        ): GetQuestionUseCase {
        return GetQuestionUseCase(
            repository,
            dispatchers
        )
    }

    @Provides
    fun provideSaveAnswerUseCase(
        @ApplicationContext repository: GraphqlRepository,
        @ApplicationScope dispatchers: CoroutineDispatchers,
        ): SaveAnswerUseCase {
        return SaveAnswerUseCase(
            repository,
            dispatchers
        )
    }

    @Provides
    fun provideUpdateStateUseCase(
        @ApplicationContext repository: GraphqlRepository,
        @ApplicationScope dispatchers: CoroutineDispatchers,
        ): UpdateStateUseCase {
        return UpdateStateUseCase(
            repository,
            dispatchers
        )
    }

    @Provides
    fun provideExplicitView(
        getQuestionUseCase: GetQuestionUseCase,
        saveAnswerUseCase: SaveAnswerUseCase,
        updateStateUseCase: UpdateStateUseCase,
        @ApplicationScope dispatchers: CoroutineDispatchers,
    ): ExplicitViewContract {
        return ExplicitViewModel(
            getQuestionUseCase,
            saveAnswerUseCase,
            updateStateUseCase,
            dispatchers
        )
    }

}