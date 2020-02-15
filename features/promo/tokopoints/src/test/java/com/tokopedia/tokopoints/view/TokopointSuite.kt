package com.tokopedia.tokopoints.view

import com.tokopedia.tokopoints.view.coupondetail.CouponDetailRepositoryTest
import com.tokopedia.tokopoints.view.coupondetail.CouponDetailViewModelTest
import com.tokopedia.tokopoints.view.couponlisting.CouponLisitingStackedViewModelTest
import com.tokopedia.tokopoints.view.couponlisting.StackedCouponActivtyViewModelTest
import com.tokopedia.tokopoints.view.couponlisting.StackedCouponRepositoryTest
import com.tokopedia.tokopoints.view.pointhistory.PointHistoryRepositoryTest
import com.tokopedia.tokopoints.view.pointhistory.PointHistoryViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses( PointHistoryRepositoryTest::class,
        PointHistoryViewModelTest::class,
        CouponDetailRepositoryTest::class,
        CouponDetailViewModelTest::class,
        CouponLisitingStackedViewModelTest::class,
        StackedCouponRepositoryTest::class, StackedCouponActivtyViewModelTest::class)
class TokopointSuite {
}