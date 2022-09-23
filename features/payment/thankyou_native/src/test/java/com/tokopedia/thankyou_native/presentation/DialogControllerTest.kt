package com.tokopedia.thankyou_native.presentation

import com.tokopedia.promotionstarget.data.notification.NotificationEntryType
import com.tokopedia.promotionstarget.domain.presenter.GratificationPresenter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import org.junit.Before
import org.junit.Test

class DialogControllerTest {

    val gratifPoresenter: GratificationPresenter = mockk()

    @Before
    fun setup() {
        every {
            gratifPoresenter.showGratificationInApp(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns mockk()
    }

    @Test
    fun testShowGratificationDialog() {
        val dialogController = DialogController(gratifPoresenter)
        val paymentId = "0"
        val callback = object : GratificationPresenter.AbstractGratifPopupCallback() {

        }
        val screenName = javaClass.name
        dialogController.showGratifDialog(
            mockk(),
            paymentId,
            gratifPopupCallback = callback,
            screenName = screenName
        )
        verifyOrder {
            gratifPoresenter.showGratificationInApp(
                any(),
                paymentID = paymentId.toLong(),
                gratifPopupCallback = callback,
                screenName = screenName,
                notificationEntryType = NotificationEntryType.ORGANIC,
                timeout = GRATIF_TIMEOUT,
                closeCurrentActivity = true
            )
        }
    }

}
