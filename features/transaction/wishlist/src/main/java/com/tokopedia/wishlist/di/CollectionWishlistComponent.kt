package com.tokopedia.wishlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.wishlist.view.fragment.CollectionWishlistFragment
import dagger.Component

@CollectionWishlistScope
@Component(modules = [CollectionWishlistModule::class, CollectionWishlistViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CollectionWishlistComponent {
    fun inject(fragment: CollectionWishlistFragment)
}