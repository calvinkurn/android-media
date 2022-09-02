package com.tokopedia.usercomponents.explicit.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.UserSessionDataStore
import com.tokopedia.usercomponents.explicit.domain.GetQuestionUseCase
import com.tokopedia.usercomponents.explicit.domain.SaveAnswerUseCase
import com.tokopedia.usercomponents.explicit.domain.UpdateStateUseCase
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewContract
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewModel
import dagger.Module
import dagger.Provides

@Module
object ExplicitViewModule {

    @Provides
    fun provideExplicitView(
        getQuestionUseCase: GetQuestionUseCase,
        saveAnswerUseCase: SaveAnswerUseCase,
        updateStateUseCase: UpdateStateUseCase,
        userSession: UserSessionInterface,
        @ApplicationScope dispatchers: CoroutineDispatchers,
    ): ExplicitViewContract {
        return ExplicitViewModel(
            getQuestionUseCase,
            saveAnswerUseCase, 
            updateStateUseCase,
            userSession,
            dispatchers
        )
    }

}