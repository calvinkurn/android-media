package com.tokopedia.wishlistcollection.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetAddCollectionWishlist
import dagger.Component

@ActivityScope
@Component(modules = [BottomSheetWishlistCollectionModule::class, BottomSheetWishlistCollectionViewModelModule::class], dependencies = [BaseAppComponent::class])
interface BottomSheetWishlistCollectionComponent {
    fun inject(bottomsheet: BottomSheetAddCollectionWishlist)
}