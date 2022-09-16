package com.tokopedia.affiliate

import android.os.Build
import com.tokopedia.affiliate.viewmodel.AffiliateDatePickerBottomSheetViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliateHomeViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliateIncomeViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliateLoginViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliatePortfolioViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionBSViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionHistoryViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliateRecommendedProductViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliateRecyclerViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliateTermsAndConditionViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliateTransactionDetailViewModelTest
import com.tokopedia.affiliate.viewmodel.AffiliateViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.robolectric.annotation.Config

@RunWith(Suite::class)
@Config(sdk = [Build.VERSION_CODES.P])
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
        AffiliateIncomeViewModelTest::class,
        AffiliateDatePickerBottomSheetViewModelTest::class,
        AffiliateRecyclerViewModelTest::class

)
class AffiliateTestSuite