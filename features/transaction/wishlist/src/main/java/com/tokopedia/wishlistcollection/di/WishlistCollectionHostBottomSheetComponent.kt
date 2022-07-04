package com.tokopedia.wishlistcollection.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.wishlistcollection.view.fragment.WishlistCollectionHostBottomSheetFragment
import dagger.Component

@ActivityScope
@Component(modules = [WishlistCollectionHostBottomSheetModule::class, WishlistCollectionHostBottomSheetViewModelModule::class], dependencies = [BaseAppComponent::class])
interface WishlistCollectionHostBottomSheetComponent {
    fun inject(fragment: WishlistCollectionHostBottomSheetFragment)
}