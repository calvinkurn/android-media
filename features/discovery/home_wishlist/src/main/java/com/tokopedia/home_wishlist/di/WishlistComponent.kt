package com.tokopedia.home_wishlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment
import dagger.Component

/**
 * A Component class for Recommendation
 */
@WishlistScope
@Component(modules = [
    WishlistModule::class,
    ViewModelModule::class
], dependencies = [BaseAppComponent::class])
interface WishlistComponent{
    fun inject(fragment: WishlistFragment)
}