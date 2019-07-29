package com.tokopedia.profile.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.feedcomponent.di.FeedComponentModule
import com.tokopedia.profile.R
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.presenter.ProfileEmptyPresenter
import com.tokopedia.profile.view.presenter.ProfilePresenter
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by milhamj on 9/21/18.
 */
@Module(includes = [ProfileNetworkModule::class, FeedComponentModule::class])
class ProfileModule {
    @ProfileScope
    @Provides
    fun provideProfilePresenter(profilePresenter: ProfilePresenter): ProfileContract.Presenter {
        return profilePresenter
    }
    @ProfileScope
    @Provides
    fun provideProfileEmptyPresenter(profileEmptyPresenter: ProfileEmptyPresenter)
        : ProfileEmptyContract.Presenter {
        return profileEmptyPresenter
    }

    @Provides
    @ProfileScope
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart)
    }

}