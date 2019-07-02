package com.tokopedia.checkout.view.feature.cornerlist

import com.tokopedia.checkout.domain.datamodel.newaddresscorner.AddressListModel
import com.tokopedia.checkout.domain.usecase.GetCornerList
import com.tokopedia.checkout.helper.AddressDummyDataProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable

@RunWith(MockitoJUnitRunner::class)
class CornerListPresenterTest {

    @Mock
    lateinit var usecase: GetCornerList
    @Mock
    lateinit var view: CornerContract.View
    @InjectMocks
    lateinit var presenter: CornerListPresenter

    @Before
    fun setup() {
        presenter.attachView(view)
    }

    @Test
    fun searchQuery() {
        val items: AddressListModel = AddressDummyDataProvider.getCornerList()

        `when`(usecase.execute("")).thenReturn(Observable.just(items))

        presenter.getList("")

        inOrder(view).apply {
            this.verify(view).setLoadingState(true)
            this.verify(view).showData(items.listAddress)
            this.verify(view).setLoadingState(false)
        }
    }
}