package com.tokopedia.manageaddress.ui.addresschoice

import com.tokopedia.logisticdata.domain.usecase.GetAddressCornerUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress
import io.mockk.MockKAnnotations
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class AddressListPresenterTest  {

    private val useCase: GetAddressCornerUseCase = mockk(relaxed = true)
    private val analytics: CheckoutAnalyticsChangeAddress = mockk(relaxed = true)
    private val view: AddressListContract.View = mockk(relaxed = true)

    private val presenter by lazy {
        AddressListPresenter(useCase, analytics)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        presenter.attachView(view)
    }

    @Test
    fun `Detach View`() {
        presenter.detachView()
    }
}