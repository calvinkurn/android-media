package com.tokopedia.review.feature.credibility.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.review.feature.credibility.domain.GetReviewerCredibilityUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ReviewCredibilityViewModelTestFixture {

    @RelaxedMockK
    lateinit var getReviewerCredibilityUseCase: GetReviewerCredibilityUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ReviewCredibilityViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewCredibilityViewModel(getReviewerCredibilityUseCase, userSessionInterface, CoroutineTestDispatchersProvider)
    }
}