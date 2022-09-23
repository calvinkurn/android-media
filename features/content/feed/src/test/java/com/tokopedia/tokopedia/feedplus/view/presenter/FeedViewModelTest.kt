package com.tokopedia.tokopedia.feedplus.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.DeletePostViewModel
import com.tokopedia.kolcommon.data.SubmitReportContentResponse
import com.tokopedia.kolcommon.domain.interactor.SubmitReportContentUseCase
import com.tokopedia.tokopedia.feedplus.helper.assertEqualTo
import com.tokopedia.tokopedia.feedplus.helper.assertTrue
import com.tokopedia.tokopedia.feedplus.helper.assertType
import com.tokopedia.tokopedia.feedplus.robot.create
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 23/09/22
 */
@ExperimentalCoroutinesApi
class FeedViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val mockReport: SubmitReportContentUseCase = mockk(relaxed = true)

    @Test
    fun `send report - success`() {
        val expected = SubmitReportContentResponse(
            content = SubmitReportContentResponse.FeedReportSubmit(
                data = SubmitReportContentResponse.FeedReportData(success = 1)
            )
        )

        coEvery { mockReport.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, sendReportUseCase = mockReport)
            .use {
                it.vm.sendReport(1, 1, "", "")
                it.vm.reportResponse.getOrAwaitValue().assertType<Success<DeletePostViewModel>> { dt ->
                    dt.data.errorMessage.assertEqualTo(expected.content.errorMessage)
                    dt.data.isSuccess.assertTrue()
                }
            }
    }

    @Test
    fun `send report - failed`() {
        val expected = SubmitReportContentResponse(
            content = SubmitReportContentResponse.FeedReportSubmit(
                data = SubmitReportContentResponse.FeedReportData(success = 200),
                errorMessage = "OOps"
            )
        )

        coEvery { mockReport.executeOnBackground() } returns expected

        create(dispatcher = testDispatcher, sendReportUseCase = mockReport)
            .use {
                it.vm.sendReport(1, 1, "", "")
                it.vm.reportResponse.getOrAwaitValue().assertType<Fail> { dt ->
                    dt.throwable.message.assertEqualTo(expected.content.errorMessage)
                }
            }
    }
}
