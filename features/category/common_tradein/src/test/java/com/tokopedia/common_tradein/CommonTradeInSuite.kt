package com.tokopedia.common_tradein

import com.tokopedia.common_tradein.usecase.CheckTradeInUseCaseTest
import com.tokopedia.common_tradein.viewmodel.TradeInTextViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
        CheckTradeInUseCaseTest::class,
        TradeInTextViewModelTest::class
)
class CommonTradeInSuite {
}