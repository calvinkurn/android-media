package com.tokopedia.review.feature.reviewreply

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.review.feature.reviewreply.update.domain.usecase.ReviewReplyUpdateUseCase
import com.tokopedia.review.feature.reviewreply.insert.domain.usecase.ReviewReplyInsertUseCase
import com.tokopedia.review.feature.reviewreply.domain.GetReviewTemplateListUseCase
import com.tokopedia.review.feature.reviewreply.domain.InsertTemplateReviewReplyUseCase
import com.tokopedia.review.feature.reviewreply.view.viewmodel.SellerReviewReplyViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule

abstract class SellerReviewReplyViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getReviewTemplateListUseCase: GetReviewTemplateListUseCase

    @RelaxedMockK
    lateinit var reviewReplyInsertUseCase: ReviewReplyInsertUseCase

    @RelaxedMockK
    lateinit var reviewReplyUpdateUseCase: ReviewReplyUpdateUseCase

    @RelaxedMockK
    lateinit var insertTemplateReviewReplyUseCase: InsertTemplateReviewReplyUseCase

    protected lateinit var viewModel: SellerReviewReplyViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SellerReviewReplyViewModel(
            CoroutineTestDispatchersProvider,
            getReviewTemplateListUseCase, reviewReplyInsertUseCase,
            reviewReplyUpdateUseCase, insertTemplateReviewReplyUseCase
        )
    }

    protected fun LiveData<*>.verifyValueEquals(expected: Any) {
        val actual = value
        TestCase.assertEquals(expected, actual)
    }

    protected fun LiveData<*>.verifyErrorEquals(expected: Fail) {
        val expectedResult = expected.throwable::class.java
        val actualResult = (value as Fail).throwable::class.java
        TestCase.assertEquals(expectedResult, actualResult)
    }
}