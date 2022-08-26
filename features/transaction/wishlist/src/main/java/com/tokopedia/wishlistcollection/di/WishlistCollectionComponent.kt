package com.tokopedia.wishlistcollection.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionDetailFragment
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [WishlistCollectionModule::class, WishlistCollectionViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface WishlistCollectionComponent {
    fun inject(fragment: WishlistCollectionFragment)
}