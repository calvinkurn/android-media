package com.tokopedia.wishlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.wishlist.view.activity.WishlistV2Activity
import com.tokopedia.wishlist.view.fragment.WishlistV2Fragment
import dagger.Component

/**
 * Created by fwidjaja on 16/10/21.
 */

@WishlistV2Scope
@Component(modules = [WishlistV2Module::class, WishlistV2ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface WishlistV2Component {
    fun inject(fragment: WishlistV2Fragment)
}