package com.tokopedia.usercomponents.explicit.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.explicit.domain.GetQuestionUseCase
import com.tokopedia.usercomponents.explicit.domain.SaveAnswerUseCase
import com.tokopedia.usercomponents.explicit.domain.UpdateStateUseCase
import com.tokopedia.usercomponents.explicit.stub.data.ExplicitRepositoryStub
import com.tokopedia.usercomponents.explicit.stub.data.UserSessionStub
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewContract
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewModel
import dagger.Module
import dagger.Provides

@Module
object FakeExplicitModule {

    @Provides
    @ApplicationScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

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
        userSessionInterface: UserSessionInterface,
        @ApplicationScope dispatchers: CoroutineDispatchers,
    ): ExplicitViewContract {
        return ExplicitViewModel(
            getQuestionUseCase,
            saveAnswerUseCase,
            updateStateUseCase,
            userSessionInterface,
            dispatchers
        )
    }

}