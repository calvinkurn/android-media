package com.tokopedia.tokopoints.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopoints.view.coupondetail.CouponDetailFragment
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedActivity
import com.tokopedia.tokopoints.view.couponlisting.CouponListingStackedFragment
import com.tokopedia.tokopoints.view.pointhistory.PointHistoryFragment
import dagger.Component


@TokoPointScope
@Component(dependencies = [BaseAppComponent::class], modules = [BundleModule::class, ViewModelModule::class, TokopointsQueryModule::class])
interface TokopointBundleComponent {
    fun inject(fragment: CouponDetailFragment)

    fun inject(fragment: PointHistoryFragment)

    fun inject(activity: CouponListingStackedActivity)

    fun inject(fragment: CouponListingStackedFragment)

}