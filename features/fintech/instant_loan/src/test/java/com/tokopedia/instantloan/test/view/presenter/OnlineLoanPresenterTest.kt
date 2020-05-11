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

    @Before
    @Throws(Exception::class)
    fun setUp() {
        onlineLoanPresenter = OnlineLoanPresenter(mGetFilterDataUseCase)
    }



    @Test
    fun test_attach_view(){
        every {
            mGetFilterDataUseCase.execute(any())
        } returns mockk()
        every { mGetFilterDataUseCase.setQuery(any()) } just Runs
        every { view.getFilterDataQuery()  } returns "query"
        onlineLoanPresenter.attachView(view)
        verify (exactly = 1) {
            mGetFilterDataUseCase.execute(any())
        }
    }
}
