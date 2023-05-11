package com.tokopedia.imagepicker_insta.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.content.common.ui.analytic.FeedAccountTypeAnalytic
import com.tokopedia.content.common.ui.analytic.FeedAccountTypeAnalyticImpl
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.imagepicker_insta.analytic.FeedVideoDepreciationAnalytic
import com.tokopedia.imagepicker_insta.usecase.FeedVideoDepreciationUseCase
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on April 21, 2022
 */
@Module
class ImagePickerModule {
    @Provides
    fun provideGraphqlRepositoryCase(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideFeedAccountTypeAnalytic(userSession: UserSessionInterface): FeedAccountTypeAnalytic {
        return FeedAccountTypeAnalyticImpl(userSession)
    }

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideFeedVideoDepreciationUseCase(@ApplicationContext context: Context): FeedVideoDepreciationUseCase {
        return FeedVideoDepreciationUseCase(context)
    }

    @Provides
    fun provideFeedVideoDeprecationAnalytic(userSession: UserSessionInterface): FeedVideoDepreciationAnalytic {
        return FeedVideoDepreciationAnalytic(
            userSession,
            TrackApp.getInstance().gtm.irisSessionId
        )
    }
}
