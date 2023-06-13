package com.tokopedia.play.broadcaster.shorts.di

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.content.common.producttag.data.ProductTagRepositoryImpl
import com.tokopedia.content.common.producttag.di.module.ContentCreationProductTagBindModule
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.view.bottomsheet.ProductTagSourceBottomSheet
import com.tokopedia.content.common.producttag.view.fragment.*
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
@Module
abstract class ContentCreationProductTagBindTestModule {
    @Binds
    @IntoMap
    @FragmentKey(ProductTagParentFragment::class)
    abstract fun bindProductTagParentFragment(fragment: ProductTagParentFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ProductTagSourceBottomSheet::class)
    abstract fun bindProductTagSourceBottomSheet(fragment: ProductTagSourceBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LastTaggedProductFragment::class)
    abstract fun bindLastTaggedProductFragment(fragment: LastTaggedProductFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LastPurchasedProductFragment::class)
    abstract fun bindLastPurchasedProductFragment(fragment: LastPurchasedProductFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(MyShopProductFragment::class)
    abstract fun bindMyShopProductFragment(fragment: MyShopProductFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(GlobalSearchFragment::class)
    abstract fun bindGlobalSearchFragment(fragment: GlobalSearchFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(GlobalSearchProductTabFragment::class)
    abstract fun bindGlobalSearchProductTabFragment(fragment: GlobalSearchProductTabFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(GlobalSearchShopTabFragment::class)
    abstract fun bindGlobalSearchShopTabFragment(fragment: GlobalSearchShopTabFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ShopProductFragment::class)
    abstract fun bindShopProductFragment(fragment: ShopProductFragment): Fragment
}
