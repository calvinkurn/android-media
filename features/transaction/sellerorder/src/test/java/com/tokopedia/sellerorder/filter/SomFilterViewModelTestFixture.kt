package com.tokopedia.sellerorder.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.sellerorder.SomTestDispatcherProvider
import com.tokopedia.sellerorder.filter.domain.usecase.GetSomOrderFilterUseCase
import com.tokopedia.sellerorder.filter.presentation.viewmodel.SomFilterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule

abstract class SomFilterViewModelTestFixture {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val dispatcher = SomTestDispatcherProvider()

    @RelaxedMockK
    lateinit var getSomOrderFilterUseCase: GetSomOrderFilterUseCase

    protected lateinit var somFilterViewModel: SomFilterViewModel

    companion object {
        val mockDate = "14 Okt 2020 - 24 Okt 2020"
        val mockIdFilter = "Siap Dikirim"
        val isResetFilter = false
        val SOM_FILTER_SUCCESS_RESPONSE = "json/som_get_order_filter_success_response.json"
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        somFilterViewModel = SomFilterViewModel(dispatcher, getSomOrderFilterUseCase)
    }

    protected fun LiveData<*>.verifyCoroutineSuccessEquals(expected: Success<*>) {
        val expectedResult = expected.data
        val actualResult = (value as Success<*>).data
        Assert.assertEquals(expectedResult, actualResult)
    }

    protected fun LiveData<*>.verifyErrorEquals(expected: Fail) {
        val expectedResult = expected.throwable::class.java
        val actualResult = (value as Fail).throwable::class.java
        TestCase.assertEquals(expectedResult, actualResult)
    }
}