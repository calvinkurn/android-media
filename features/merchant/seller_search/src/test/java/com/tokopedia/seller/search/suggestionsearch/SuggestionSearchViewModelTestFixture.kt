package com.tokopedia.seller.search.suggestionsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.seller.search.common.domain.GetSellerSearchUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.seller.search.feature.suggestion.domain.usecase.InsertSuccessSearchUseCase
import com.tokopedia.seller.search.feature.suggestion.view.viewmodel.SuggestionSearchViewModel
import com.tokopedia.usecase.coroutines.Fail
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule

abstract class SuggestionSearchViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getSellerSearchUseCase: GetSellerSearchUseCase

    @RelaxedMockK
    lateinit var insertSuccessSearchUseCase: InsertSuccessSearchUseCase

    protected lateinit var viewModel: SuggestionSearchViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = SuggestionSearchViewModel(CoroutineTestDispatchersProvider, getSellerSearchUseCase, insertSuccessSearchUseCase)
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