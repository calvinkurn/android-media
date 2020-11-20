package com.tokopedia.talk.feature.report.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.talk.feature.reporttalk.domain.usecase.ReportTalkUseCase
import com.tokopedia.talk.feature.reporttalk.view.listener.ReportTalkContract
import com.tokopedia.talk.feature.reporttalk.view.presenter.ReportTalkPresenter
import com.tokopedia.talk.feature.reporttalk.view.uimodel.BaseActionTalkUiModel
import com.tokopedia.talk.feature.reporttalk.view.uimodel.TalkReportOptionUiModel
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.observers.TestSubscriber

class ReportTalkPresenterTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var view: ReportTalkContract.View

    @RelaxedMockK
    private lateinit var reportTalkUseCase: ReportTalkUseCase

    private lateinit var presenter: ReportTalkPresenter

    private val talkId = ""
    private val shopId = ""
    private val productId = ""
    private val commentId = ""
    private val otherReason = ""

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = ReportTalkPresenter(reportTalkUseCase)
        presenter.attachView(view)
    }

    @After
    fun cleanup() {
        presenter.detachView()
    }

    @Test
    fun `when reportCommentTalk success should execute expected usecase and perform expected view  action`() {
        val expectedReturn = mockk<BaseActionTalkUiModel>(relaxed = true)
        val selectedOption = TalkReportOptionUiModel(isChecked = true)
        val testSubscriber: TestSubscriber<BaseActionTalkUiModel> = TestSubscriber()

        every {
            reportTalkUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        presenter.reportCommentTalk(talkId, shopId, productId, commentId, otherReason, selectedOption)

        verifyReportUseCaseExecuted()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `when reportCommentTalk fail should execute expected usecase and perform expected view  action`() {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val selectedOption = TalkReportOptionUiModel(isChecked = true)
        val testSubscriber: TestSubscriber<BaseActionTalkUiModel> = TestSubscriber()

        every {
            reportTalkUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        presenter.reportCommentTalk(talkId, shopId, productId, commentId, otherReason, selectedOption)

        verifyReportUseCaseExecuted()
        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `when reportTalk success should execute expected usecase and perform expected view  action`() {
        val expectedReturn = mockk<BaseActionTalkUiModel>(relaxed = true)
        val selectedOption = TalkReportOptionUiModel(isChecked = true)
        val testSubscriber: TestSubscriber<BaseActionTalkUiModel> = TestSubscriber()

        every {
            reportTalkUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        presenter.reportTalk(talkId, shopId, productId, otherReason, selectedOption)

        verifyReportUseCaseExecuted()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValue(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `when reportTalk fail should execute expected usecase and perform expected view  action`() {
        val expectedReturn = mockk<Throwable>(relaxed = true)
        val selectedOption = TalkReportOptionUiModel(isChecked = true)
        val testSubscriber: TestSubscriber<BaseActionTalkUiModel> = TestSubscriber()

        every {
            reportTalkUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onError(expectedReturn)
        }

        presenter.reportTalk(talkId, shopId, productId, otherReason, selectedOption)

        verifyReportUseCaseExecuted()
        testSubscriber.assertError(expectedReturn)
        testSubscriber.assertCompleted()
    }

    @Test
    fun `when reportCommentTalk but isChecked is false, should not do anything`() {
        val expectedReturn = mockk<BaseActionTalkUiModel>(relaxed = true)
        val selectedOption = TalkReportOptionUiModel(isChecked = false)
        val testSubscriber: TestSubscriber<BaseActionTalkUiModel> = TestSubscriber()

        every {
            reportTalkUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        presenter.reportCommentTalk(talkId, shopId, productId, commentId, otherReason, selectedOption)

        verifyReportUseCaseNotExecuted()
    }

    @Test
    fun `when reportTalk but isChecked is false, should not do anything`() {
        val expectedReturn = mockk<BaseActionTalkUiModel>(relaxed = true)
        val selectedOption = TalkReportOptionUiModel(isChecked = false)
        val testSubscriber: TestSubscriber<BaseActionTalkUiModel> = TestSubscriber()

        every {
            reportTalkUseCase.execute(any(), any())
        } answers {
            testSubscriber.onStart()
            testSubscriber.onCompleted()
            testSubscriber.onNext(expectedReturn)
        }

        presenter.reportTalk(talkId, shopId, productId, otherReason, selectedOption)

        verifyReportUseCaseNotExecuted()
    }

    private fun verifyReportUseCaseExecuted() {
        verify { reportTalkUseCase.execute(any(), any()) }
    }

    private fun verifyReportUseCaseNotExecuted() {
        verify(exactly = 0) { reportTalkUseCase.execute(any(), any()) }
    }
}