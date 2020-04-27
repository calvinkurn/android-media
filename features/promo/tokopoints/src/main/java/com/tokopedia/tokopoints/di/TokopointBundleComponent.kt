package com.tokopedia.tokopoints.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopoints.view.catalogdetail.CouponCatalogFragment
import com.tokopedia.tokopoints.view.cataloglisting.CatalogListItemFragment
import com.tokopedia.tokopoints.view.cataloglisting.CatalogListingFragment
import com.tokopedia.tokopoints.view.coupondetail.CouponDetailFragment
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedActivity
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedFragment
import com.tokopedia.tokopoints.view.fragment.SendGiftFragment
import com.tokopedia.tokopoints.view.pointhistory.PointHistoryFragment
import com.tokopedia.tokopoints.view.tokopointhome.TokoPointsHomeFragmentNew
import dagger.Component


@TokoPointScope
@Component(dependencies = [BaseAppComponent::class], modules = [BundleModule::class, ViewModelModule::class, TokopointsQueryModule::class])
interface TokopointBundleComponent {
    fun inject(fragment: CouponDetailFragment)

    fun inject(fragment: PointHistoryFragment)

    fun inject(activity: CouponListingStackedActivity)

    fun inject(fragment: CouponListingStackedFragment)

    fun inject(fragment: CouponCatalogFragment)

    fun inject(fragment: SendGiftFragment)

    fun inject(fragment: CatalogListingFragment)

    fun inject(fragment: CatalogListItemFragment)

    fun inject(fragment: TokoPointsHomeFragmentNew)
}