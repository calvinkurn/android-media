package com.tokopedia.createpost.di

import com.tokopedia.content.common.ui.analytic.FeedAccountTypeAnalytic
import com.tokopedia.content.common.ui.analytic.FeedAccountTypeAnalyticImpl
import com.tokopedia.content.product.picker.ugc.analytic.product.ContentProductTagAnalytic
import com.tokopedia.createpost.analytic.FeedProductTagAnalyticImpl
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.di.CreatePostScope
import com.tokopedia.createpost.common.view.contract.CreatePostContract
import com.tokopedia.createpost.domain.entity.GetContentFormDomain
import com.tokopedia.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.createpost.view.presenter.CreatePostPresenter
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by milhamj on 9/26/18.
 */
@Module(includes = [CreatePostCommonModule::class, ShopCommonModule::class])
class CreatePostModule {

    @Provides
    @CreatePostScope
    fun providePresenter(createPostPresenter: CreatePostPresenter): CreatePostContract.Presenter {
        return createPostPresenter
    }

    @Provides
    @CreatePostScope
    fun provideFeedAccountTypeAnalytic(userSession: UserSessionInterface): FeedAccountTypeAnalytic {
        return FeedAccountTypeAnalyticImpl(userSession)
    }

    @Provides
    @CreatePostScope
    fun provideGetContentFormUseCase(graphqlUseCase: GraphqlUseCase): UseCase<GetContentFormDomain> {
        return GetContentFormUseCase(graphqlUseCase)
    }

    @Provides
    @CreatePostScope
    fun provideFeedProductTagAnalytic(
        userSession: UserSessionInterface,
        trackingQueue: TrackingQueue,
    ): ContentProductTagAnalytic {
        return FeedProductTagAnalyticImpl(userSession, trackingQueue)
    }
}
