package com.tokopedia.seller.action.common.presentation.presenter

import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.seller.action.common.interfaces.SellerActionContract
import com.tokopedia.seller.action.common.provider.SellerActionProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Rule

open class SellerActionPresenterTestFixture {

    @get:Rule
    val rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var sliceMainOrderListUseCase: SliceMainOrderListUseCase

    @RelaxedMockK
    lateinit var view: SellerActionContract.View

    @RelaxedMockK
    lateinit var sliceUri: Uri

    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var provider: SellerActionProvider

    open lateinit var presenter: SellerActionPresenter

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter = SellerActionPresenter(sliceMainOrderListUseCase, CoroutineTestDispatchersProvider, provider)
    }

    protected fun attachView() {
        presenter.attachView(view)
    }

    protected fun onGetOrderList_thenReturn(orderList: List<Order>) {
        coEvery {
            sliceMainOrderListUseCase.executeOnBackground()
        } returns orderList
    }

    protected fun onGetOrderList_thenReturn(error: Throwable) {
        coEvery {
            sliceMainOrderListUseCase.executeOnBackground()
        } throws error
    }

    protected fun verifySliceMainOrderListUseCaseCalled() {
        coVerify {
            sliceMainOrderListUseCase.executeOnBackground()
        }
    }

    protected fun verifySliceMainOrderListUseCaseNotCalled() {
        coVerify(exactly = 0) {
            sliceMainOrderListUseCase.executeOnBackground()
        }
    }

    protected fun verifyOnSuccessGetOrderListCalled(sliceUri: Uri, orderList: List<Order>) {
        verify(exactly = 1) {
            view.onSuccessGetOrderList(sliceUri, orderList)
        }
    }

    protected fun verifyOnSuccessGetOrderListNotCalled() {
        verify(exactly = 0) {
            view.onSuccessGetOrderList(any(), any())
        }
    }

    protected fun verifyOnErrorGetOrderListCalled(sliceUri: Uri) {
        verify(exactly = 1) {
            view.onErrorGetOrderList(sliceUri)
        }
    }

    protected fun verifyOnErrorGetOrderListNotCalled() {
        verify(exactly = 0) {
            view.onErrorGetOrderList(any())
        }
    }

}