package com.tokopedia.wishlistcommon.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.wishlistcommon.view.bottomsheet.AddToCollectionWishlistBottomSheet
import dagger.Component

@AddToWishlistCollectionScope
@Component(modules = [AddToWishlistCollectionModule::class, AddToWishlistCollectionViewModelModule::class], dependencies = [BaseAppComponent::class])
interface AddToWishlistCollectionComponent {
    fun inject(bottomsheet: AddToCollectionWishlistBottomSheet)
}