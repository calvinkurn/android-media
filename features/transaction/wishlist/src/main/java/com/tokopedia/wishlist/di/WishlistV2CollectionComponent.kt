package com.tokopedia.wishlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.wishlist.view.fragment.WishlistV2CollectionFragment
import dagger.Component

@WishlistV2CollectionScope
@Component(modules = [WishlistV2CollectionModule::class, WishlistV2CollectionViewModelModule::class], dependencies = [BaseAppComponent::class])
interface WishlistV2CollectionComponent {
    fun inject(fragment: WishlistV2CollectionFragment)
}