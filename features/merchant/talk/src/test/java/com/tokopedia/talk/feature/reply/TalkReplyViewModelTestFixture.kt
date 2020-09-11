package com.tokopedia.talk.feature.reply

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.talk.coroutines.TestCoroutineDispatchers
import com.tokopedia.talk.feature.reply.domain.usecase.*
import com.tokopedia.talk.feature.reply.presentation.viewmodel.TalkReplyViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule

abstract class TalkReplyViewModelTestFixture {

    @RelaxedMockK
    lateinit var discussionDataByQuestionIDUseCase: DiscussionDataByQuestionIDUseCase

    @RelaxedMockK
    lateinit var talkFollowUnfollowTalkUseCase: TalkFollowUnfollowTalkUseCase

    @RelaxedMockK
    lateinit var talkDeleteTalkUseCase: TalkDeleteTalkUseCase

    @RelaxedMockK
    lateinit var talkDeleteCommentUseCase: TalkDeleteCommentUseCase

    @RelaxedMockK
    lateinit var talkCreateNewCommentUseCase: TalkCreateNewCommentUseCase

    @RelaxedMockK
    lateinit var talkMarkNotFraudUseCase: TalkMarkNotFraudUseCase

    @RelaxedMockK
    lateinit var talkMarkCommentNotFraudUseCase: TalkMarkCommentNotFraudUseCase

    @RelaxedMockK
    lateinit var talkReportTalkUseCase: TalkReportTalkUseCase

    @RelaxedMockK
    lateinit var talkReportCommentUseCase: TalkReportCommentUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TalkReplyViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TalkReplyViewModel(discussionDataByQuestionIDUseCase,
                talkFollowUnfollowTalkUseCase,
                talkDeleteTalkUseCase,
                talkDeleteCommentUseCase,
                talkCreateNewCommentUseCase,
                talkMarkNotFraudUseCase,
                talkMarkCommentNotFraudUseCase,
                talkReportTalkUseCase,
                talkReportCommentUseCase,
                userSession,
                TestCoroutineDispatchers)
    }

}