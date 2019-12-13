package com.tokopedia.instantloan.test.view.presenter

import com.tokopedia.instantloan.domain.interactor.GetFilterDataUseCase
import com.tokopedia.instantloan.domain.subscriber.GetFilterDataSubscriber
import com.tokopedia.instantloan.view.contractor.OnlineLoanContractor
import com.tokopedia.instantloan.view.presenter.OnlineLoanPresenter
import io.mockk.*

import org.junit.Before
import org.junit.Test

class OnlineLoanPresenterTest {

    lateinit var onlineLoanPresenter: OnlineLoanPresenter
    var view: OnlineLoanContractor.View = mockk()
    var mGetFilterDataUseCase: GetFilterDataUseCase = mockk()
    var subscriber: GetFilterDataSubscriber = mockk()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        onlineLoanPresenter = OnlineLoanPresenter(mGetFilterDataUseCase)
        onlineLoanPresenter.subscriber = subscriber
    }

    @Test
    fun test_attach_view(){
        every {
            mGetFilterDataUseCase.execute(subscriber)
        } returns mockk()
        onlineLoanPresenter.attachView(view)
        verify (exactly = 1) {
            mGetFilterDataUseCase.execute(subscriber)
        }
    }
}
