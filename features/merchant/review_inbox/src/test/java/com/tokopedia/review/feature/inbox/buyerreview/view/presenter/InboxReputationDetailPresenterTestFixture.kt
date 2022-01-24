package com.tokopedia.review.feature.inbox.buyerreview.view.presenter

import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.GetInboxReputationDetailUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.inboxdetail.SendSmileyReputationUseCase
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before

abstract class InboxReputationDetailPresenterTestFixture {
    
    @RelaxedMockK
    lateinit var getInboxReputationDetailUseCase: GetInboxReputationDetailUseCase
    
    @RelaxedMockK
    lateinit var sendSmileyReputationUseCase: SendSmileyReputationUseCase
    
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface
    
    @RelaxedMockK
    lateinit var view: InboxReputationDetail.View
    
    protected lateinit var presenter: InboxReputationDetailPresenter
    
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = InboxReputationDetailPresenter(getInboxReputationDetailUseCase, sendSmileyReputationUseCase, userSession)
        presenter.attachView(view)
    }
    
    @After
    fun cleanup() {
        presenter.detachView()
    }
    
}