package com.tokopedia.instantloan.test.view.presenter

import com.tokopedia.instantloan.domain.interactor.GetLendingDataUseCase
import com.tokopedia.instantloan.domain.interactor.GetLoanProfileStatusUseCase
import com.tokopedia.instantloan.domain.subscriber.GetLendingDataSubscriber
import com.tokopedia.instantloan.domain.subscriber.GetLoanProfileSubscriber
import com.tokopedia.instantloan.view.contractor.InstantLoanLendingDataContractor
import com.tokopedia.instantloan.view.presenter.InstantLoanLendingDataPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class InstantLoanLendingDataPresenterTest {

    lateinit var lendingDataPresenter: InstantLoanLendingDataPresenter
    var mGetLendingDataUseCase: GetLendingDataUseCase = mockk()
    var mGetLoanProfileStatusUseCase: GetLoanProfileStatusUseCase = mockk()

    var view: InstantLoanLendingDataContractor.View = mockk()


    @Before
    @Throws(Exception::class)
    fun setUp() {
        lendingDataPresenter = InstantLoanLendingDataPresenter(mGetLendingDataUseCase, mGetLoanProfileStatusUseCase)
    }

    @Test
    fun test_attach_view_with_user_logged_in(){
        every {
            mGetLendingDataUseCase.execute(any())
        } returns mockk()

        every {
            mGetLoanProfileStatusUseCase.execute(any())
        } returns mockk()

        every {
            view.IsUserLoggedIn()
        } returns true


        lendingDataPresenter.attachView(view)
        verify (exactly = 1) {
            mGetLendingDataUseCase.execute(any())
        }

        verify (exactly = 1) {
            mGetLoanProfileStatusUseCase.execute(any())
        }
    }

    @Test
    fun test_attach_view_without_user_logged_in(){
        every {
            mGetLendingDataUseCase.execute(any())
        } returns mockk()

        every {
            view.IsUserLoggedIn()
        } returns false

        lendingDataPresenter.attachView(view)
        verify (exactly = 1) {
            mGetLendingDataUseCase.execute(any())
        }

        verify (exactly = 0) {
            mGetLoanProfileStatusUseCase.execute(any())
        }
    }
}
