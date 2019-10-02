package com.tokopedia.createpost.di


import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics
import com.tokopedia.createpost.view.fragment.BaseCreatePostFragment
import com.tokopedia.createpost.view.service.SubmitPostService
import dagger.Component

/**
 * @author by milhamj on 9/26/18.
 */
@CreatePostScope
@Component(modules = [CreatePostModule::class])
interface CreatePostComponent {
    fun inject(fragment: BaseCreatePostFragment)

    fun inject(service: SubmitPostService)

    fun provideAffiliateAnalytics(): AffiliateAnalytics
}
