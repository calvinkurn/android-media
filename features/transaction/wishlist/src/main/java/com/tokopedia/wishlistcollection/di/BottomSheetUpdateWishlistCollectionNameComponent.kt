package com.tokopedia.wishlistcollection.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.wishlistcollection.view.bottomsheet.BottomSheetUpdateWishlistCollectionName
import dagger.Component

@ActivityScope
@Component(
    modules = [BottomSheetUpdateWishlistCollectionNameModule::class, BottomSheetUpdateWishlistCollectionNameViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface BottomSheetUpdateWishlistCollectionNameComponent {
    fun inject(bottomsheet: BottomSheetUpdateWishlistCollectionName)
}