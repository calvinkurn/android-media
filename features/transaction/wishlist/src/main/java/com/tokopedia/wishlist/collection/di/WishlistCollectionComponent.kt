package com.tokopedia.wishlist.collection.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.wishlist.collection.view.bottomsheet.BottomSheetAddCollectionWishlist
import com.tokopedia.wishlist.collection.view.bottomsheet.BottomSheetCreateNewCollectionWishlist
import com.tokopedia.wishlist.collection.view.bottomsheet.BottomSheetUpdateWishlistCollectionName
import com.tokopedia.wishlist.collection.view.bottomsheet.BottomSheetWishlistAffiliateOnBoarding
import com.tokopedia.wishlist.collection.view.fragment.WishlistCollectionDetailFragment
import com.tokopedia.wishlist.collection.view.fragment.WishlistCollectionEditFragment
import com.tokopedia.wishlist.collection.view.fragment.WishlistCollectionFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [WishlistCollectionModule::class, WishlistCollectionViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface WishlistCollectionComponent {
    fun inject(fragment: WishlistCollectionFragment)
    fun inject(fragment: WishlistCollectionDetailFragment)
    fun inject(fragment: WishlistCollectionEditFragment)
    fun inject(bottomsheet: BottomSheetAddCollectionWishlist)
    fun inject(bottomsheet: BottomSheetUpdateWishlistCollectionName)
    fun inject(bottomsheet: BottomSheetCreateNewCollectionWishlist)
    fun inject(bottomsheet: BottomSheetWishlistAffiliateOnBoarding)
}
