package com.tokopedia.shop.score.uitest.features.performance.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.shop.score.uitest.stub.performance.presentation.activity.ShopPerformanceActivityStub
import org.junit.Before
import org.junit.Rule

abstract class ShopScoreTest {

    @get:Rule
    var activityRule = IntentsTestRule(ShopPerformanceActivityStub::class.java, false, false)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {

    }
}