package com.tokopedia.shop.score.uitest.features.performance.base

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.shop.score.uitest.stub.performance.presentation.activity.ShopPerformanceActivityStub
import org.junit.Before
import org.junit.Rule

abstract class ShopScoreTest {

    @get:Rule
    var activityRule = IntentsTestRule(ShopPerformanceActivityStub::class.java, false, false)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var applicationContext: Application

    @Before
    fun setup() {
        applicationContext = ApplicationProvider.getApplicationContext()

    }
}