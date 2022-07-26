package com.tokopedia.feed_shop.di

import com.tokopedia.feed_shop.shop.view.contract.FeedShopContract
import com.tokopedia.feed_shop.shop.view.presenter.FeedShopPresenter
import dagger.Module
import dagger.Provides

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
}