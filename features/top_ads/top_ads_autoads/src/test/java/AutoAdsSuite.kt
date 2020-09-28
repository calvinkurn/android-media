package com.tokopedia.topads.auto.view

import com.tokopedia.topads.auto.view.viewmodel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite


@ExperimentalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
        AutoAdsWidgetViewModelTest::class,
        DailyBudgetViewModelTest::class,
        TopAdsInfoViewModelTest::class
)
class AutoAdsSuite