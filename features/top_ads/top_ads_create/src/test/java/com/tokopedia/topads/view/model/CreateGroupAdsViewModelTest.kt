@file:Suppress("DEPRECATION")

package com.tokopedia.topads.view.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.domain.usecase.TopAdsGroupValidateNameUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CreateGroupAdsViewModelTest {
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: CreateGroupAdsViewModel
    private lateinit var topAdsGroupValidateNameUseCase: TopAdsGroupValidateNameUseCase

    @Before
    fun setUp() {
        topAdsGroupValidateNameUseCase = mockk(relaxed = true)
        viewModel = spyk(CreateGroupAdsViewModel(rule.dispatchers, topAdsGroupValidateNameUseCase))
    }

    @Test
    fun `test exception in validateGroup`() {
        var actual: Throwable? = null
        val expected = Exception("my excep")

        val onError: (String) -> Unit = {
            actual = expected
        }
        every { topAdsGroupValidateNameUseCase.execute(any(), any()) } throws expected

        viewModel.validateGroup(
            groupName = "",
            onSuccess = {
            },
            onError = onError
        )
        Assert.assertEquals(actual?.message, expected.message)
    }

    @Test
    fun `check invocation of onError validateGroup`() {
        val expected = "error"
        var actual = ""
        val data = ResponseGroupValidateName(
            ResponseGroupValidateName.TopAdsGroupValidateNameV2(
                errors = listOf(Error().apply {
                    detail = expected
                })
            )
        )
        val onError: (String) -> Unit = {
            actual = expected
        }
        every {
            topAdsGroupValidateNameUseCase.execute(captureLambda(), any())
        } answers {
            firstArg<(ResponseGroupValidateName) -> Unit>().invoke(data)
        }
        viewModel.validateGroup(
            groupName = "",
            onSuccess = {},
            onError = onError
        )
        verify {
            topAdsGroupValidateNameUseCase.execute(any(), any())
        }
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `check invocation of onSuccess validateGroup`() {
        val expected = "test"
        var actual = ""
        val data = ResponseGroupValidateName(
            ResponseGroupValidateName.TopAdsGroupValidateNameV2(
                ResponseGroupValidateName.TopAdsGroupValidateNameV2.Data(
                    groupName = expected
                )
            )
        )
        val onSuccess: () -> Unit = {
            actual = expected
        }
        every {
            topAdsGroupValidateNameUseCase.execute(captureLambda(), any())
        } answers {
            firstArg<(ResponseGroupValidateName) -> Unit>().invoke(data)
        }
        viewModel.validateGroup(
            groupName = "",
            onSuccess = onSuccess,
            onError = {}
        )
        verify {
            topAdsGroupValidateNameUseCase.execute(any(), any())
        }
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `validateGroup test exception`() {
        var errorCalled = false
        every { topAdsGroupValidateNameUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockk())
        }
        viewModel.validateGroup("", {}, { errorCalled = true })
        Assert.assertTrue(errorCalled)
    }
}
