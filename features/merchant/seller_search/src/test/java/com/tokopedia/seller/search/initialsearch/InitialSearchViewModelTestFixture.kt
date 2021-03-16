package com.tokopedia.seller.search.initialsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.seller.search.common.domain.GetSellerSearchUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.seller.search.feature.initialsearch.domain.usecase.DeleteSuggestionHistoryUseCase
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchViewModel
import com.tokopedia.seller.search.feature.suggestion.domain.usecase.InsertSuccessSearchUseCase
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule

abstract class InitialSearchViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getSellerSearchUseCase: GetSellerSearchUseCase

    @RelaxedMockK
    lateinit var deleteSuggestionHistoryUseCase: DeleteSuggestionHistoryUseCase

    @RelaxedMockK
    lateinit var insertSellerSearchUseCase: InsertSuccessSearchUseCase

    protected lateinit var viewModel: InitialSearchViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = InitialSearchViewModel(CoroutineTestDispatchersProvider, getSellerSearchUseCase, deleteSuggestionHistoryUseCase, insertSellerSearchUseCase)
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