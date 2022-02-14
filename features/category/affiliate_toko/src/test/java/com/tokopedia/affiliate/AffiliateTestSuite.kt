package com.tokopedia.affiliate

import com.tokopedia.affiliate.viewmodel.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        AffiliateHomeViewModelTest::class,
        AffiliatePromotionBSViewModelTest::class,
        AffiliatePromoViewModelTest::class,
        AffiliatePromotionHistoryViewModelTest::class
)
class AffiliateTestSuite
{

}