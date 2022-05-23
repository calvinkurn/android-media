package com.tokopedia.createpost.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics
import com.tokopedia.createpost.common.di.CreatePostScope
import com.tokopedia.createpost.producttag.di.ContentCreationProductTagBindModule
import com.tokopedia.createpost.producttag.di.ContentCreationProductTagModule
import com.tokopedia.createpost.view.activity.CreatePostActivityNew
import com.tokopedia.createpost.view.activity.ProductTagActivity
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragment
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragmentNew
import com.tokopedia.createpost.view.plist.*
import dagger.Component

/**
 * @author by milhamj on 9/26/18.
 */
@CreatePostScope
@Component(
    modules = [
        CreatePostModule::class,
        ViewModelModule::class,
        ContentCreationProductTagBindModule::class,
        ContentCreationProductTagModule::class,
    ],
)
interface CreatePostComponent {
    fun inject(fragment: BaseCreatePostFragment)

    fun provideAffiliateAnalytics(): AffiliateAnalytics

    fun provideCreatePostAnalytics(): CreatePostAnalytics

    fun inject(baseCreatePostFragmentNew: BaseCreatePostFragmentNew)
    fun inject(createPostActivityNew: CreatePostActivityNew)
    fun inject(shopProductListFragment: ShopProductListFragment)
    fun inject(shopProductSearchPageListParentFragment: ShopProductSearchPageListParentFragment)
    fun inject(barangSearchResultFragment: BarangSearchResultFragment)
    fun inject(tokoSearchResultFragment: TokoSearchResultFragment)
    fun inject(productTagActivity: ProductTagActivity)
}
