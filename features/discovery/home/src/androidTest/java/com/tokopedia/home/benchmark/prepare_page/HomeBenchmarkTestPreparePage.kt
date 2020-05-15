package com.tokopedia.home.benchmark.prepare_page

import android.os.Bundle
import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.annotation.UiThreadTest
import androidx.test.rule.ActivityTestRule
import androidx.lifecycle.Lifecycle.State
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragmentFactoryImpl
import com.tokopedia.home.environment.BlankTestActivity
import org.junit.Rule
import org.junit.Test


class HomeBenchmarkTestPreparePage {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @get:Rule
    var activityRule: ActivityTestRule<BlankTestActivity> = ActivityTestRule(BlankTestActivity::class.java)

    @Test
    fun benchmark_homeFragment_onCreate() {
        // Template
    }
}