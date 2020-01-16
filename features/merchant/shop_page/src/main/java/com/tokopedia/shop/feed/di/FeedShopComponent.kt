package com.tokopedia.shop.feed.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.feedcomponent.di.FeedComponentModule
import com.tokopedia.kolcommon.di.KolCommonModule
import com.tokopedia.shop.feed.view.fragment.FeedShopFragment
import dagger.Component

/**
 * @author by yfsx on 08/05/19.
 */
@FeedShopScope
@Component(
        modules = [FeedShopModule::class, FeedComponentModule::class, KolCommonModule::class],
        dependencies = [BaseAppComponent::class]
)
interface FeedShopComponent {

    fun inject(fragment: FeedShopFragment)
}