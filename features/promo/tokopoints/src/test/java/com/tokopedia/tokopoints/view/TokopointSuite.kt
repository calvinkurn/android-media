package com.tokopedia.tokopoints.view

import com.tokopedia.tokopoints.view.catalogdetail.CouponCatalogRepositoryTest
import com.tokopedia.tokopoints.view.catalogdetail.CouponCatalogViewModelTest
import com.tokopedia.tokopoints.view.cataloglisting.CatalogListItemViewModelTest
import com.tokopedia.tokopoints.view.cataloglisting.CatalogListingRepositoryTest
import com.tokopedia.tokopoints.view.cataloglisting.CatalogListingViewModelTest
import com.tokopedia.tokopoints.view.coupondetail.CouponDetailRepositoryTest
import com.tokopedia.tokopoints.view.coupondetail.CouponDetailViewModelTest
import com.tokopedia.tokopoints.view.couponlisting.CouponLisitingStackedViewModelTest
import com.tokopedia.tokopoints.view.couponlisting.StackedCouponActivtyViewModelTest
import com.tokopedia.tokopoints.view.couponlisting.StackedCouponRepositoryTest
import com.tokopedia.tokopoints.view.sendgift.SendGiftRespositoryTest
import com.tokopedia.tokopoints.view.sendgift.SendGiftViewModelTest
import com.tokopedia.tokopoints.view.tokopointhome.TokoPointsHomeViewModelTest
import com.tokopedia.tokopoints.view.tokopointhome.TokopointsHomeRepositoryTest
import com.tokopedia.tokopoints.view.validatePin.ValidateMerchantPinViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(CouponDetailRepositoryTest::class,
        CouponDetailViewModelTest::class,
        CouponLisitingStackedViewModelTest::class,
        StackedCouponRepositoryTest::class,
        StackedCouponActivtyViewModelTest::class,
        CouponCatalogRepositoryTest::class,
        CouponCatalogViewModelTest::class,
        CatalogListingRepositoryTest::class,
        CatalogListingViewModelTest::class,
        CatalogListItemViewModelTest::class,
        SendGiftRespositoryTest::class,
        SendGiftViewModelTest::class,
        ValidateMerchantPinViewModelTest::class,
        TokopointsHomeRepositoryTest::class,
        TokoPointsHomeViewModelTest::class
)
class TokopointSuite {
}