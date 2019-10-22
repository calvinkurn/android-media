package com.tokopedia.shop.feed.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.shop.R
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import com.tokopedia.shop.feed.view.presenter.FeedShopPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by yfsx on 08/05/19.
 */
@Module(includes = [(FeedShopNetworkModule::class)])
class FeedShopModule {

    @FeedShopScope
    @Provides
    fun provideFeedShopPresenter(feedShopPresenter: FeedShopPresenter): FeedShopContract.Presenter {
        return feedShopPresenter
    }

    @Provides
    @FeedShopScope
    @Named("atcMutation")
    fun provideAddToCartMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.mutation_add_to_cart)
    }

}