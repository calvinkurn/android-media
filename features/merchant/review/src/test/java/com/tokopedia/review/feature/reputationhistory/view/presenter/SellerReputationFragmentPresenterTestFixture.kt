package com.tokopedia.review.feature.reputationhistory.view.presenter

import com.tokopedia.core.gcm.GCMHandler
import com.tokopedia.review.feature.reputationhistory.domain.interactor.ReviewReputationMergeUseCase
import com.tokopedia.review.feature.reputationhistory.domain.interactor.ReviewReputationUseCase
import com.tokopedia.review.feature.reputationhistory.util.DefaultErrorSubscriber
import com.tokopedia.review.feature.reputationhistory.view.SellerReputationView
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.After
import org.junit.Before

abstract class SellerReputationFragmentPresenterTestFixture {

    @RelaxedMockK
    lateinit var reviewReputationUseCase: ReviewReputationUseCase

    @RelaxedMockK
    lateinit var reviewReputationMergeUseCase: ReviewReputationMergeUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var gcmHandler: GCMHandler

    @RelaxedMockK
    lateinit var view: SellerReputationView

    @RelaxedMockK
    lateinit var errorNetworkListener: DefaultErrorSubscriber.ErrorNetworkListener

    protected lateinit var presenter: SellerReputationFragmentPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = SellerReputationFragmentPresenter()
        presenter.apply {
            setReviewReputationUseCase(reviewReputationUseCase)
            setReviewReputationMergeUseCase(reviewReputationMergeUseCase)
            setUserSession(userSession)
            setGcmHandler(gcmHandler)
            setErrorNetworkListener(errorNetworkListener)
            attachView(view)
        }
    }

    @After
    fun cleanup() {
        presenter.detachView()
    }
}