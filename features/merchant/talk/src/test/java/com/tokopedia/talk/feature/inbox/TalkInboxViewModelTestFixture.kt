package com.tokopedia.talk.feature.inbox

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.talk.feature.inbox.analytics.TalkInboxTracking
import com.tokopedia.talk.feature.inbox.domain.usecase.TalkInboxListUseCase
import com.tokopedia.talk.feature.inbox.presentation.viewmodel.TalkInboxViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class TalkInboxViewModelTestFixture {

    @RelaxedMockK
    lateinit var talkInboxListUseCase: TalkInboxListUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var talkInboxTracking: TalkInboxTracking

    @RelaxedMockK
    lateinit var firebaseRemoteConfig: RemoteConfig

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected val viewModel: TalkInboxViewModel by lazy {
        TalkInboxViewModel(
            CoroutineTestDispatchersProvider,
            talkInboxListUseCase,
            userSession,
            talkInboxTracking,
            firebaseRemoteConfig
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }
}
