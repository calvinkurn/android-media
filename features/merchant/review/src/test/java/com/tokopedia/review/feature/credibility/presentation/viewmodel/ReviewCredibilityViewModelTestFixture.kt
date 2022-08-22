package com.tokopedia.review.feature.credibility.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.review.feature.credibility.domain.GetReviewerCredibilityUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.cancelChildren
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ReviewCredibilityViewModelTestFixture {

    @RelaxedMockK
    lateinit var getReviewerCredibilityUseCase: GetReviewerCredibilityUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @get:Rule
    val rule = CoroutineTestRule()

    protected lateinit var viewModel: ReviewCredibilityViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ReviewCredibilityViewModel(
            getReviewerCredibilityUseCase,
            userSessionInterface
        )
    }

    @After
    fun cleanup() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }
}