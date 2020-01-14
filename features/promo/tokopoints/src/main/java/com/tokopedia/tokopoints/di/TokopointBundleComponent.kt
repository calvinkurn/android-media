package com.tokopedia.tokopoints.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopoints.view.coupondetail.CouponDetailFragment
import com.tokopedia.tokopoints.view.pointhistory.PointHistoryFragment
import dagger.Component


@TokoPointScope
@Component(dependencies = [BaseAppComponent::class], modules = [BundleModule::class, ViewModelModule::class, TokopointsQueryModule::class])
interface TokopointBundleComponent {
    abstract fun inject(fragment: CouponDetailFragment)

    abstract fun inject(fragment: PointHistoryFragment)


}