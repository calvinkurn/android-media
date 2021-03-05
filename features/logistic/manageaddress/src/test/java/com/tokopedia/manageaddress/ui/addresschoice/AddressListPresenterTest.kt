package com.tokopedia.manageaddress.ui.addresschoice

import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.logisticCommon.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.manageaddress.AddressDummyDataProvider
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Test
import rx.Observable

class AddressListPresenterTest  {

    private val useCase: GetAddressCornerUseCase = mockk(relaxed = true)
    private val analytics: CheckoutAnalyticsChangeAddress = mockk(relaxed = true)
    private val view: AddressListContract.View = mockk(relaxed = true)

    private val presenter by lazy {
        AddressListPresenter(useCase, analytics)
    }

    @Before
    fun setup() {
        presenter.attachView(view)
    }

    @Test
    fun `Get Address Success`() {
        val items = AddressDummyDataProvider.getAddressList()

        every { useCase.execute(any(), any(), any(), any()) } returns Observable.just(items)

        presenter.getAddress(-1, -1, true)

        verifyOrder {
            view.showLoading()
            view.setToken(items.token)
            view.showList(items.listAddress.toMutableList())
            view.hideLoading()
            view.stopTrace()
        }
    }

    @Test
    fun `Get Address Return Empty`() {
        val items = AddressListModel()

        every { useCase.execute(any(), any(), any(), any()) } returns Observable.just(items)

        presenter.getAddress(-1, -1, true)

        verifyOrder {
            view.showLoading()
            view.showListEmpty()
            analytics.eventSearchResultNotFound("")
            view.hideLoading()
            view.stopTrace()
        }
    }

    @Test
    fun `Get Address Error`() {
        val response = Throwable()

        every { useCase.execute(any(), any(), any(), any()) } returns Observable.error(response)

        presenter.getAddress(-1, -1, true)

        verifyOrder {
            view.showLoading()
            view.hideLoading()
            view.stopTrace()
            view.showError(response)
        }
    }

    @Test
    fun `Search Address Success`() {
        val items = AddressDummyDataProvider.getAddressList()

        every { useCase.execute(any(), any(), any(), any()) } returns Observable.just(items)

        presenter.searchAddress("", -1, -1, true)

        verifyOrder {
            view.showLoading()
            view.setToken(items.token)
            view.showList(items.listAddress.toMutableList())
            view.hideLoading()
            view.stopTrace()
        }
    }

    @Test
    fun `Search Address Return Empty`() {
        val items = AddressListModel()

        every { useCase.execute(any(), any(), any(), any()) } returns Observable.just(items)

        presenter.searchAddress("", -1, -1, true)

        verifyOrder {
            view.showLoading()
            view.showListEmpty()
            analytics.eventSearchResultNotFound("")
            view.hideLoading()
            view.stopTrace()
        }
    }

    @Test
    fun `Search Address Error`() {
        val response = Throwable()

        every { useCase.execute(any(), any(), any(), any()) } returns Observable.error(response)

        presenter.searchAddress("", -1, -1, true)

        verifyOrder {
            view.showLoading()
            view.hideLoading()
            view.stopTrace()
            view.showError(response)
        }
    }

    @Test
    fun `LoadMore Address Success`() {
        val items = AddressDummyDataProvider.getAddressList()

        every { useCase.loadMore(any(), any(), any(), any(), any()) } returns Observable.just(items)

        presenter.loadMore(-1, -1, true)

        verifyOrder {
            view.showLoading()
            view.updateList(items.listAddress.toMutableList())
            view.hideLoading()
            view.stopTrace()
        }
    }

    @Test
    fun `LoadMore Address Error`() {
        val response = Throwable()

        every { useCase.loadMore(any(), any(), any(), any(), any()) } returns Observable.error(response)

        presenter.loadMore(-1, -1, true)

        verifyOrder {
            view.showLoading()
            view.hideLoading()
            view.stopTrace()
            view.showError(response)
        }
    }

    @Test
    fun `Detach View`() {
        presenter.detachView()

        verify {
           useCase.unsubscribe()
        }
    }
}