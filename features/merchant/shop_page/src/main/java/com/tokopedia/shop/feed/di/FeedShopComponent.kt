package com.tokopedia.shop.feed.di

import com.tokopedia.kol.common.di.KolComponent
import com.tokopedia.shop.feed.view.fragment.FeedShopFragment
import dagger.Component

/**
 * @author by yfsx on 08/05/19.
 */
@FeedShopScope
@Component(
        modules = arrayOf(FeedShopModule::class),
        dependencies = arrayOf(KolComponent::class)
)
interface FeedShopComponent {
    fun inject(fragment: FeedShopFragment)
}