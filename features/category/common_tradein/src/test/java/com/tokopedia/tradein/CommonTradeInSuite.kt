package com.tokopedia.tradein

import com.tokopedia.tradein.usecase.*
import com.tokopedia.tradein.viewmodel.FinalPriceViewModelTest
import com.tokopedia.tradein.viewmodel.TradeInHomeViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
        DiagnosticDataUseCaseTest::class,
        GetAddressUseCaseTest::class,
        ProcessMessageUseCaseTest::class
)
class CommonTradeInSuite {
}