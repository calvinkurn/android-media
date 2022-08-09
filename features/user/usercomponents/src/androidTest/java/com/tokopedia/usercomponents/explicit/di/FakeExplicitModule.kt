package com.tokopedia.usercomponents.explicit.di

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usercomponents.explicit.domain.GetQuestionUseCase
import com.tokopedia.usercomponents.explicit.domain.SaveAnswerUseCase
import com.tokopedia.usercomponents.explicit.domain.UpdateStateUseCase
import com.tokopedia.usercomponents.explicit.stub.data.ExplicitRepositoryStub
import com.tokopedia.usercomponents.explicit.view.interactor.ExplicitViewContract
import com.tokopedia.usercomponents.explicit.view.interactor.ExplicitViewModel
import dagger.Module
import dagger.Provides

@Module
object FakeExplicitModule {

    @Provides
    @ApplicationContext
    fun provideExplicitRepositoryStub(): GraphqlRepository {
        return ExplicitRepositoryStub()
    }

    @Provides
    @ApplicationScope
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