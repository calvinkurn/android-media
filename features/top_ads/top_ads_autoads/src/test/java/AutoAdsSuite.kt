package com.tokopedia.topads.auto.view

import com.tokopedia.topads.auto.view.viewmodel.DailyBudgetViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite


@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
        DailyBudgetViewModelTest::class,
)
class AutoAdsSuite