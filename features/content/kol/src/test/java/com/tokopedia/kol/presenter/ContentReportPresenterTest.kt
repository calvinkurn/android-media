package com.tokopedia.kol.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kol.feature.report.domain.usecase.SendReportUseCase
import com.tokopedia.kol.feature.report.view.listener.ContentReportContract
import com.tokopedia.kol.feature.report.view.presenter.ContentReportPresenter
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

/**
 * Created by meyta.taliti on 24/01/23.
 */
class ContentReportPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var sendReportUseCase: SendReportUseCase

    lateinit var presenter: ContentReportContract.Presenter
    lateinit var view: ContentReportContract.View

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = ContentReportPresenter(
            sendReportUseCase
        )
        view = mockk(relaxed = true)
        presenter.attachView(view)
    }

    @Test
    fun `send report should be triggered`() {
        every {
            sendReportUseCase.execute(any(), any())
        } answers {}

        presenter.sendReport(
            anyInt(),
            anyString(),
            anyString(),
            anyString()
        )

        verify {
            sendReportUseCase.execute(any(), any())
        }
    }

    @Test
    fun `detach view should be triggered`() {
        presenter.detachView()

        verify {
            sendReportUseCase.unsubscribe()
        }
    }
}
