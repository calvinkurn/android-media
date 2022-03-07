package com.tokopedia.affiliate

import com.tokopedia.affiliate.viewmodel.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        AffiliateHomeViewModelTest::class,
        AffiliateIncomeViewModelTest::class,
        AffiliateLoginViewModelTest::class,
        AffiliatePromotionBSViewModelTest::class,
        AffiliatePromoViewModelTest::class,
        AffiliatePromotionHistoryViewModelTest::class,
        AffiliateRecommendedProductViewModelTest::class,
        AffiliateTransactionDetailViewModelTest::class,
        AffiliateTermsAndConditionViewModelTest::class,
        AffiliateViewModelTest::class,
        AffiliatePortfolioViewModelTest::class,
        AffiliateIncomeViewModelTest::class

)
class AffiliateTestSuite
{

}