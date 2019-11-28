package com.tokopedia.instantloan.test.view.presenter


import com.tokopedia.instantloan.domain.interactor.GetFilterDataUseCase
import com.tokopedia.instantloan.view.contractor.OnlineLoanContractor
import com.tokopedia.instantloan.view.presenter.OnlineLoanPresenter
import io.mockk.mockk

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(OnlineLoanPresenter::class)
class OnlineLoanPresenterTest {

    lateinit var onlineLoanPresenter: OnlineLoanPresenter

    lateinit var view: OnlineLoanContractor.View
    lateinit var mGetFilterDataUseCase: GetFilterDataUseCase


    @Before
    @Throws(Exception::class)
    fun setUp() {
        view = PowerMockito.mock(OnlineLoanContractor.View::class.java)
        mGetFilterDataUseCase = mockk()
        onlineLoanPresenter = OnlineLoanPresenter(mGetFilterDataUseCase)
        onlineLoanPresenter.attachView(view)
    }


    @Test
    fun get_filter_data_test() {


    }
}
