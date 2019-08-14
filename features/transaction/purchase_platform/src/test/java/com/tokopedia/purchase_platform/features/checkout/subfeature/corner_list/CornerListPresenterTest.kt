package com.tokopedia.purchase_platform.features.checkout.subfeature.corner_list

import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.model.AddressListModel
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.usecase.GetCornerList
import com.tokopedia.purchase_platform.features.checkout.helper.AddressDummyDataProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable

@RunWith(MockitoJUnitRunner::class)
class CornerListPresenterTest {

    @Mock lateinit var usecase: GetCornerList
    @Mock lateinit var view: CornerContract.View
    @InjectMocks lateinit var presenter: CornerListPresenter

    @Before
    fun setup() {
        presenter.attachView(view)
    }

    @Test
    fun searchQueryNormalReturn() {
        val items: AddressListModel = AddressDummyDataProvider.getCornerList()

        `when`(usecase.execute(Matchers.anyString())).thenReturn(Observable.just(items))

        presenter.getList("")

        inOrder(view).apply {
            this.verify(view).setLoadingState(true)
            this.verify(view).showData(items.listAddress)
            this.verify(view).setLoadingState(false)
        }
    }

    @Test
    fun searchQueryEmptyReturn() {
        val items = AddressListModel()

        `when`(usecase.execute(Matchers.anyString())).thenReturn(Observable.just(items))

        presenter.getList("")

        inOrder(view).apply {
            this.verify(view).setLoadingState(true)
            this.verify(view).showEmptyView()
            this.verify(view).setLoadingState(false)
        }
    }

    @Test
    fun searchQueryErrorReturn() {
        val err = Throwable()

        `when`(usecase.execute(Matchers.anyString())).thenReturn(Observable.error(err))

        presenter.getList("")

        inOrder(view).apply {
            this.verify(view).setLoadingState(true)

            // When onError case, doOnTerminate method is executed after onNext/onComplete and
            // before onError
            this.verify(view).setLoadingState(false)
            this.verify(view).showError(err)
        }
    }
}