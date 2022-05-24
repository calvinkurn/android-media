package com.tokopedia.createpost.producttag.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.tokopedia.createpost.producttag.analytic.ContentProductTagAnalytic
import com.tokopedia.createpost.producttag.analytic.product.ProductTagAnalytic
import com.tokopedia.createpost.producttag.analytic.product.ProductTagAnalyticImpl
import com.tokopedia.createpost.producttag.analytic.srp.SRPProductTagAnalytic
import com.tokopedia.createpost.producttag.analytic.srp.SRPProductTagAnalyticImpl
import com.tokopedia.createpost.producttag.data.ProductTagRepositoryImpl
import com.tokopedia.createpost.producttag.di.key.FragmentKey
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.view.bottomsheet.ProductTagSourceBottomSheet
import com.tokopedia.createpost.producttag.view.fragment.*
import com.tokopedia.createpost.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.createpost.producttag.view.fragment.factory.ProductTagFragmentFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
@Module
abstract class ContentCreationProductTagBindModule {

    /** Fragment */
    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: ProductTagFragmentFactory): FragmentFactory

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

    /** Repository */
    @Binds
    abstract fun bindProductTagRepository(productTagRepositoryImpl: ProductTagRepositoryImpl): ProductTagRepository

    /** Analytic */
    @Binds
    abstract fun bindSRPProductTagAnalytic(srpProductTagAnalytic: SRPProductTagAnalyticImpl): SRPProductTagAnalytic

    @Binds
    abstract fun bindProductTagAnalytic(productTagAnalytic: ProductTagAnalyticImpl): ProductTagAnalytic
}