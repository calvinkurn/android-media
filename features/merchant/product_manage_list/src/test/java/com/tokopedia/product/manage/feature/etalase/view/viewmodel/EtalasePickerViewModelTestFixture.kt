package com.tokopedia.product.manage.feature.etalase.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.product.manage.coroutine.TestCoroutineDispatchers
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule

open class EtalasePickerViewModelTestFixture {

    protected lateinit var viewModel: EtalasePickerViewModel
    protected lateinit var getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        getShopEtalaseByShopUseCase = mockk(relaxed = true)
        viewModel = EtalasePickerViewModel(getShopEtalaseByShopUseCase, TestCoroutineDispatchers)
    }

    protected fun LiveData<*>.verifySuccessEquals(expected: Success<*>) {
        val expectedResult = expected.data
        val actualResult = (value as Success<*>).data
        TestCase.assertEquals(expectedResult, actualResult)
    }

    protected fun LiveData<*>.verifyErrorEquals(expected: Fail) {
        val expectedResult = expected.throwable::class.java
        val actualResult = (value as Fail).throwable::class.java
        TestCase.assertEquals(expectedResult, actualResult)
    }
}