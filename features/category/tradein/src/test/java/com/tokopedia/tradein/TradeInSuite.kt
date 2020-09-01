package com.tokopedia.tradein

import com.tokopedia.tradein.usecase.*
import com.tokopedia.tradein.viewmodel.FinalPriceViewModelTest
import com.tokopedia.tradein.viewmodel.MoneyInCheckoutViewModelTest
import com.tokopedia.tradein.viewmodel.MoneyInHomeViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
        FinalPriceViewModelTest::class,
        MoneyInCheckoutViewModelTest::class,
        MoneyInHomeViewModelTest::class,
        CheckMoneyInUseCaseTest::class,
        DiagnosticDataUseCaseTest::class,
        GetAddressUseCaseTest::class,
        MoneyInCheckoutUseCaseTest::class,
        MoneyInCourierRatesUseCaseTest::class,
        MoneyInPickupScheduleUseCaseTest::class,
        ProcessMessageUseCaseTest::class
)
class TradeInSuite {
}