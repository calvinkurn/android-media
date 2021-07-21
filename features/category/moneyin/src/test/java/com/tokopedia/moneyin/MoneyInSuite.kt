package com.tokopedia.moneyin

import com.tokopedia.moneyin.usecase.CheckMoneyInUseCaseTest
import com.tokopedia.moneyin.usecase.MoneyInCheckoutUseCaseTest
import com.tokopedia.moneyin.usecase.MoneyInCourierRatesUseCaseTest
import com.tokopedia.moneyin.usecase.MoneyInPickupScheduleUseCaseTest
import com.tokopedia.moneyin.viewmodel.FinalPriceViewModelTest
import com.tokopedia.moneyin.viewmodel.MoneyInCheckoutViewModelTest
import com.tokopedia.moneyin.viewmodel.MoneyInHomeViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(
        FinalPriceViewModelTest::class,
        MoneyInCheckoutViewModelTest::class,
        MoneyInHomeViewModelTest::class,
        CheckMoneyInUseCaseTest::class,
        MoneyInCheckoutUseCaseTest::class,
        MoneyInCourierRatesUseCaseTest::class,
        MoneyInPickupScheduleUseCaseTest::class
)
class MoneyInSuite {
}