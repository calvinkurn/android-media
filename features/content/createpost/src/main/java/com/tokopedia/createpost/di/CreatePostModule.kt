package com.tokopedia.createpost.di

import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.di.CreatePostScope
import com.tokopedia.createpost.common.view.contract.CreatePostContract
import com.tokopedia.createpost.view.presenter.CreatePostPresenter
import com.tokopedia.imagepicker_insta.common.ui.analytic.FeedAccountTypeAnalytic
import com.tokopedia.imagepicker_insta.common.ui.analytic.FeedAccountTypeAnalyticImpl
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by milhamj on 9/26/18.
 */
@Module(includes = [CreatePostCommonModule::class, ShopCommonModule::class])
class CreatePostModule(private val context: Context) {

    @Provides
    @CreatePostScope
    fun providePresenter(createPostPresenter: CreatePostPresenter): CreatePostContract.Presenter {
        return createPostPresenter
    }

    @Provides
    @CreatePostScope
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }

    @Provides
    @CreatePostScope
    fun provideFeedAccountTypeAnalytic(userSession: UserSessionInterface): FeedAccountTypeAnalytic {
        return FeedAccountTypeAnalyticImpl(userSession)
    }
}
