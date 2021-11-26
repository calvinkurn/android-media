package com.tokopedia.createpost.di


import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics
import com.tokopedia.createpost.common.di.CreatePostScope
import com.tokopedia.createpost.view.activity.CreatePostActivityNew
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragment
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragmentNew
import com.tokopedia.createpost.view.plist.ShopProductListFragment
import dagger.Component

/**
 * @author by milhamj on 9/26/18.
 */
@CreatePostScope
@Component(modules = [CreatePostModule::class, ViewModelModule::class])
interface CreatePostComponent {
    fun inject(fragment: BaseCreatePostFragment)

    fun provideAffiliateAnalytics(): AffiliateAnalytics

    fun provideCreatePostAnalytics(): CreatePostAnalytics

    fun inject(baseCreatePostFragmentNew: BaseCreatePostFragmentNew)
    fun inject(createPostActivityNew: CreatePostActivityNew)
    fun inject(shopProductListFragment: ShopProductListFragment)
}
