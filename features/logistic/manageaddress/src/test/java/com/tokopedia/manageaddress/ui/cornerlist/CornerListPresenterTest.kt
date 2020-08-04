package com.tokopedia.manageaddress.ui.cornerlist

import com.tokopedia.logisticdata.domain.model.AddressListModel
import com.tokopedia.manageaddress.AddressDummyDataProvider
import com.tokopedia.manageaddress.domain.GetCornerList
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Test
import rx.Observable

class CornerListPresenterTest {

    private val useCase: GetCornerList = mockk(relaxed = true)
    private val view: CornerContract.View = mockk(relaxed = true)

    private val presenter by lazy {
        CornerListPresenter(useCase)
    }

    @Before
    fun setup() {
        presenter.attachView(view)
    }

    @Test
    fun `Search Query Success`() {
        val items = AddressDummyDataProvider.getAddressList()

        every { useCase.execute(any()) } returns Observable.just(items)

        presenter.getList("")

        verifyOrder {
            view.setLoadingState(true)
            view.showData(items.listAddress)
            view.setLoadingState(false)
        }
    }

    @Test
    fun `Search Query Return Empty`() {
        val items = AddressListModel()

        every { useCase.execute(any()) } returns Observable.just(items)

        presenter.getList("")

        verifyOrder {
            view.setLoadingState(true)
            view.showEmptyView()
            view.setLoadingState(false)
        }
    }

    @Test
    fun `Search Query Error`() {
        val response = Throwable()

        every { useCase.execute(any()) } returns Observable.error(response)

        presenter.getList("")

        verifyOrder {
            view.setLoadingState(true)
            view.setLoadingState(false)
            view.showError(response)
        }
    }

    @Test
    fun `LoadMore Address Success`() {
        val items = AddressDummyDataProvider.getAddressList()

        every { useCase.loadMore(any(), any()) } returns Observable.just(items)

        presenter.loadMore(2)

        verifyOrder {
            view.setLoadingState(true)
            view.appendData(items.listAddress)
            view.setLoadingState(false)
        }
    }

    @Test
    fun `Page has No LoadMore`() {
        val items = AddressListModel()

        every { useCase.loadMore(any(), any()) } returns Observable.just(items)

        presenter.loadMore(2)

        verifyOrder {
            view.setLoadingState(true)
            view.notifyHasNotNextPage()
            view.setLoadingState(false)
        }
    }

    @Test
    fun `LoadMore Address Error`() {
        val response = Throwable()

        every { useCase.loadMore(any(), any()) } returns Observable.error(response)

        presenter.loadMore(2)

        verifyOrder {
            view.setLoadingState(true)
            view.setLoadingState(false)
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