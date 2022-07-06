package com.tokopedia.wishlistcollection.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetCreateNewCollectionWishlist
import dagger.Component

@BottomSheetCreateWishlistCollectionScope
@Component(modules = [BottomSheetCreateWishlistCollectionModule::class, BottomSheetCreateWishlistCollectionViewModelModule::class], dependencies = [BaseAppComponent::class])
interface BottomSheetCreateWishlistCollectionComponent {
    fun inject(bottomsheet: BottomSheetCreateNewCollectionWishlist)
}