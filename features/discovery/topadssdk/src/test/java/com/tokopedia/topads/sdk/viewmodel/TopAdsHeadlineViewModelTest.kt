package com.tokopedia.topads.sdk.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsHeadlineViewModelTest {
    @get:Rule
    val testRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val topAdsAddressHelper: TopAdsAddressHelper =
        mockk(relaxed = true)

    private val topAdsHeadlineUseCase: GetTopAdsHeadlineUseCase =
        mockk(relaxed = true)

    private val viewModel =
        spyk(TopAdsHeadlineViewModel(topAdsAddressHelper, topAdsHeadlineUseCase))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }
}