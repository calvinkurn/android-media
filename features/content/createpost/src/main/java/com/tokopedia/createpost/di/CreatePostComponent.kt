package com.tokopedia.createpost.di


import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics
import com.tokopedia.createpost.analyics.CreatePostAnalytics
import com.tokopedia.createpost.view.activity.CreatePostActivityNew
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragment
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragmentNew
import com.tokopedia.createpost.view.plist.ShopProductListFragment
import com.tokopedia.createpost.view.service.SubmitPostService
import com.tokopedia.createpost.view.service.SubmitPostServiceNew
import dagger.Component

/**
 * @author by milhamj on 9/26/18.
 */
@CreatePostScope
@Component(modules = [CreatePostModule::class, ViewModelModule::class])
interface CreatePostComponent {
    fun inject(fragment: BaseCreatePostFragment)

    fun inject(service: SubmitPostService)

    fun inject(service: SubmitPostServiceNew)

    fun provideAffiliateAnalytics(): AffiliateAnalytics

    fun provideCreatePostAnalytics(): CreatePostAnalytics

    fun inject(baseCreatePostFragmentNew: BaseCreatePostFragmentNew)
    fun inject(createPostActivityNew: CreatePostActivityNew)
    fun inject(shopProductListFragment: ShopProductListFragment)
}
