package com.tokopedia.talk.feature.report

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.talk.feature.reply.domain.usecase.TalkReportCommentUseCase
import com.tokopedia.talk.feature.reply.domain.usecase.TalkReportTalkUseCase
import com.tokopedia.talk.feature.reporttalk.view.viewmodel.ReportTalkViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReportTalkViewModelTestFixture {

    @RelaxedMockK
    lateinit var talkReportTalkUseCase: TalkReportTalkUseCase

    @RelaxedMockK
    lateinit var talkReportCommentUseCase: TalkReportCommentUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReportTalkViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReportTalkViewModel(
                talkReportTalkUseCase,
                talkReportCommentUseCase,
                CoroutineTestDispatchersProvider)
    }

}