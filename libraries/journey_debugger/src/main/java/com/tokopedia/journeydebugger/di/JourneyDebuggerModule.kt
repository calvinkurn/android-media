package com.tokopedia.journeydebugger.di

import NAMED_JOURNEY
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.journeydebugger.domain.*
import com.tokopedia.journeydebugger.ui.presenter.*
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class JourneyDebuggerModule {
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Named(NAMED_JOURNEY)
    fun provideJourneyPresenter(getJourneyLogUseCase: GetJourneyLogUseCase,
                                deleteJourneyLogUseCase: DeleteJourneyLogUseCase): JourneyDebugger.Presenter {
        return JourneyDebuggerPresenter(getJourneyLogUseCase, deleteJourneyLogUseCase)
    }
}
