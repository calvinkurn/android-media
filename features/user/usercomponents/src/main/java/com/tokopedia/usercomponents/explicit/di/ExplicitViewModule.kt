package com.tokopedia.usercomponents.explicit.di

import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usercomponents.explicit.domain.GetQuestionUseCase
import com.tokopedia.usercomponents.explicit.domain.SaveAnswerUseCase
import com.tokopedia.usercomponents.explicit.domain.UpdateStateUseCase
import com.tokopedia.usercomponents.explicit.view.ExplicitViewContract
import com.tokopedia.usercomponents.explicit.view.ExplicitViewViewModel
import dagger.Module
import dagger.Provides

@Module
object ExplicitViewModule {

    @Provides
    fun provideExplicitView(
        getQuestionUseCase: GetQuestionUseCase,
        saveAnswerUseCase: SaveAnswerUseCase,
        updateStateUseCase: UpdateStateUseCase,
        @ApplicationScope dispatchers: CoroutineDispatchers,
    ): ExplicitViewContract {
        return ExplicitViewViewModel(
            getQuestionUseCase,
            saveAnswerUseCase, 
            updateStateUseCase,
            dispatchers
        )
    }

}