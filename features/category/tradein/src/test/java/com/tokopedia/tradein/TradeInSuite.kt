package com.tokopedia.tradein

import com.tokopedia.tradein.usecase.*
import com.tokopedia.tradein.viewmodel.*
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
    PromoUseCaseTest::class,
    TNCInfoUseCaseTest::class,
    TradeInDetailUseCaseTest::class,
    TradeInValidateImeiUseCaseTest::class,
    TradeInHomePageFragmentVMTest::class,
    TradeInHomePageVMTest::class,
    TradeInImeiBSViewModelTest::class,
    TradeInInfoViewModelTest::class,
    TradeInPromoDetailPageVMTest::class
)
class TradeInSuite {
}