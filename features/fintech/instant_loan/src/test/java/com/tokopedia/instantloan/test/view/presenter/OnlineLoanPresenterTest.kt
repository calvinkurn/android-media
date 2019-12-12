package com.tokopedia.instantloan.test.view.presenter

import com.tokopedia.instantloan.domain.interactor.GetFilterDataUseCase
import com.tokopedia.instantloan.domain.subscriber.GetFilterDataSubscriber
import com.tokopedia.instantloan.helper.InstantLoanDummyProvider
import com.tokopedia.instantloan.view.contractor.OnlineLoanContractor
import com.tokopedia.instantloan.view.presenter.OnlineLoanPresenter
import io.mockk.*

import org.junit.Before
import org.junit.Test
import org.powermock.core.classloader.annotations.PrepareForTest
import rx.Observable

@PrepareForTest(OnlineLoanPresenter::class)
class OnlineLoanPresenterTest {

    lateinit var onlineLoanPresenter: OnlineLoanPresenter

    var view: OnlineLoanContractor.View = mockk(relaxed = true)
    var mGetFilterDataUseCase: GetFilterDataUseCase = mockk()
    var subscriber: GetFilterDataSubscriber = mockk()


    @Before
    @Throws(Exception::class)
    fun setUp() {
        onlineLoanPresenter = OnlineLoanPresenter(mGetFilterDataUseCase)

    }

    @Test
    fun test_attach_view(){

        every { mGetFilterDataUseCase.execute(subscriber) } returns Unit

        onlineLoanPresenter.attachView(view)

        verify {
            mGetFilterDataUseCase.execute(subscriber)
        }
    }

    @Test
    fun get_filter_data_test() {
        val expected = InstantLoanDummyProvider.getLoanDataSuccessResponse()
        every { mGetFilterDataUseCase.execute(any()) } answers { Observable.just(expected) }
        onlineLoanPresenter.getFilterData()



    }
}
