package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetFirstTimeInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inbox.GetInboxReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before

abstract class InboxReputationPresenterTestFixture {

    @RelaxedMockK
    lateinit var getFirstTimeInboxReputationUseCase: GetFirstTimeInboxReputationUseCase

    @RelaxedMockK
    lateinit var getInboxReputationUseCase: GetInboxReputationUseCase

    @RelaxedMockK
    lateinit var view: InboxReputation.View

    protected lateinit var inboxReputationPresenter: InboxReputationPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        inboxReputationPresenter = InboxReputationPresenter(getFirstTimeInboxReputationUseCase, getInboxReputationUseCase)
        inboxReputationPresenter.attachView(view)
    }

    @After
    fun cleanup() {
        inboxReputationPresenter.detachView()
    }
}