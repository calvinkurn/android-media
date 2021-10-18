package com.tokopedia.affiliate

import com.tokopedia.affiliate.viewmodel.AffiliateHomeViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionBSViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        AffiliateHomeViewModelTest::class,
        AffiliatePromotionBSViewModelTest::class,
        AffiliatePromoViewModel::class
)
class AffiliateTestSuite
{

}