package com.tokopedia.wishlistcollection.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionDetailFragment
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionFragment
import dagger.Component

@WishlistCollectionDetailScope
@Component(
    modules = [WishlistCollectionDetailModule::class, WishlistCollectionDetailViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface WishlistCollectionDetailComponent {
    fun inject(fragment: WishlistCollectionDetailFragment)
}