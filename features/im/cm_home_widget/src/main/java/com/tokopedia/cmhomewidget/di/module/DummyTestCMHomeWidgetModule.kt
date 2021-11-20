package com.tokopedia.cmhomewidget.di.module

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.cmhomewidget.databinding.ActivityDummyTestCmHomeWidgetBinding
import com.tokopedia.cmhomewidget.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// todo delete cm home widget dummy things

@Module
class DummyTestCMHomeWidgetModule(private val activityContext: Context) {

    @Provides
    fun provideGraphqlRepositoryModule(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @CoroutineMainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun provideDummyTestCmHomeWidgetActivityBinding(): ActivityDummyTestCmHomeWidgetBinding {
        return ActivityDummyTestCmHomeWidgetBinding.inflate(
            LayoutInflater.from(activityContext)
        )
    }
}