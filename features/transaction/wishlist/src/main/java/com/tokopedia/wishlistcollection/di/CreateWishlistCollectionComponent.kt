package com.tokopedia.wishlistcollection.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetCreateNewCollectionWishlist
import com.tokopedia.wishlistcollection.view.fragment.CollectionWishlistFragment
import dagger.Component

@CreateWishlistCollectionScope
@Component(modules = [CreateWishlistCollectionModule::class, CreateWishlistCollectionViewModelModule::class], dependencies = [BaseAppComponent::class])
interface CreateWishlistCollectionComponent {
    fun inject(bottomsheet: BottomSheetCreateNewCollectionWishlist)
}