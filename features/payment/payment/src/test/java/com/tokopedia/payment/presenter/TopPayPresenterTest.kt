package com.tokopedia.payment.presenter

import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.payment.fingerprint.data.model.ResponsePaymentFingerprint
import com.tokopedia.payment.fingerprint.domain.GetPostDataOtpUseCase
import com.tokopedia.payment.fingerprint.domain.PaymentFingerprintUseCase
import com.tokopedia.payment.fingerprint.domain.SaveFingerPrintUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.Ordering
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import rx.Subscriber
import rx.subjects.PublishSubject
import java.util.*

class TopPayPresenterTest {

    private lateinit var presenter: TopPayPresenter

    private lateinit var saveFingerPrintUseCase: SaveFingerPrintUseCase
    private lateinit var paymentFingerprintUseCase: PaymentFingerprintUseCase
    private lateinit var getPostDataOtpUseCase: GetPostDataOtpUseCase
    private lateinit var userSession: UserSessionInterface

    private lateinit var view: TopPayContract.View

    @Before
    fun setup() {
        saveFingerPrintUseCase = mockk(relaxed = true)
        paymentFingerprintUseCase = mockk(relaxed = true)
        getPostDataOtpUseCase = mockk(relaxed = true)
        userSession = mockk(relaxed = true)

        view = mockk(relaxed = true)

        presenter = TopPayPresenter(saveFingerPrintUseCase, paymentFingerprintUseCase, getPostDataOtpUseCase, userSession)

        presenter.attachView(view)
    }

    @After
    fun cleanup() {
        presenter.detachView()
    }

    @Test
    fun `When process payment with null data Then should show default error message`() {
        // Given
        every { view.paymentPassData } returns null

        // When
        presenter.processUriPayment()

        // Then
        verify(exactly = 1) {
            view.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
        }
        verify(inverse = true) {
            view.renderWebViewPostUrl(any(), any(), any())
        }
    }

    @Test
    fun `When process payment with null query data Then should show default error message`() {
        // Given
        every { view.paymentPassData } returns PaymentPassData()

        // When
        presenter.processUriPayment()

        // Then
        verify(exactly = 1) {
            view.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
        }
        verify(inverse = true) {
            view.renderWebViewPostUrl(any(), any(), any())
        }
    }

    @Test
    fun `When process payment with get data Then should render get webview`() {
        // Given
        val mockUrl = "url.com"
        every { view.paymentPassData } returns PaymentPassData().apply {
            queryString = ""
            redirectUrl = mockUrl
            method = PaymentPassData.METHOD_GET
        }

        // When
        presenter.processUriPayment()

        // Then
        verify(exactly = 1) {
            view.renderWebViewPostUrl(mockUrl, any(), true)
        }
        verify(inverse = true) {
            view.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
        }
    }

    @Test
    fun `When process payment with post data Then should render post webview`() {
        // Given
        val mockUrl = "url.com"
        every { view.paymentPassData } returns PaymentPassData().apply {
            queryString = ""
            redirectUrl = mockUrl
            method = PaymentPassData.METHOD_POST
        }

        // When
        presenter.processUriPayment()

        // Then
        verify(exactly = 1) {
            view.renderWebViewPostUrl(mockUrl, any(), false)
        }
        verify(inverse = true) {
            view.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
        }
    }

    @Test
    fun `When process payment with unspecified-method data Then should render post webview`() {
        // Given
        val mockUrl = "url.com"
        every { view.paymentPassData } returns PaymentPassData().apply {
            queryString = ""
            redirectUrl = mockUrl
        }

        // When
        presenter.processUriPayment()

        // Then
        verify(exactly = 1) {
            view.renderWebViewPostUrl(mockUrl, any(), false)
        }
        verify(inverse = true) {
            view.showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
        }
    }

    @Test
    fun `When register fingerprint success Then should show success`() {
        // Given
        every { saveFingerPrintUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<Boolean>).onNext(true)
        }

        // When
        presenter.registerFingerPrint("", "", "", "", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onSuccessRegisterFingerPrint()
        }
    }

    @Test
    fun `When register fingerprint failed Then should show failed`() {
        // Given
        every { saveFingerPrintUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<Boolean>).onNext(false)
        }

        // When
        presenter.registerFingerPrint("", "", "", "", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onErrorRegisterFingerPrint()
        }
    }

    @Test
    fun `When register fingerprint error Then should show failed`() {
        // Given
        every { saveFingerPrintUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<Boolean>).onError(Throwable())
        }

        // When
        presenter.registerFingerPrint("", "", "", "", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onErrorRegisterFingerPrint()
        }
    }

    @Test
    fun `When payment fingerprint success Then should show success`() {
        // Given
        every { paymentFingerprintUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onNext(ResponsePaymentFingerprint(isSuccess = true))
        }

        // When
        presenter.paymentFingerPrint("", "", "", "", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onSuccessPaymentFingerprint(any(), any())
        }
    }

    @Test
    fun `When payment fingerprint failed Then should show failed`() {
        // Given
        every { paymentFingerprintUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onNext(ResponsePaymentFingerprint(isSuccess = false))
        }

        // When
        presenter.paymentFingerPrint("", "", "", "", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onErrorPaymentFingerPrint()
        }
    }

    @Test
    fun `When payment fingerprint error Then should show failed`() {
        // Given
        every { paymentFingerprintUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onError(Throwable())
        }

        // When
        presenter.paymentFingerPrint("", "", "", "", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onErrorPaymentFingerPrint()
        }
    }

    @Test
    fun `When get otp success Then should show success`() {
        // Given
        val result = hashMapOf(
                "query" to "value",
                "asdf" to "qwerty"
        )
        every { getPostDataOtpUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<HashMap<String, String>?>).onNext(result)
        }

        // When
        val urlOtp = "urlOtp"
        presenter.getPostDataOtp("", urlOtp)

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onSuccessGetPostDataOTP("asdf=qwerty&query=value", urlOtp)
        }
    }

    @Test
    fun `When get otp failed Then should show failed`() {
        // Given
        every { getPostDataOtpUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<HashMap<String, String>?>).onNext(null)
        }

        // When
        presenter.getPostDataOtp("", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onErrorGetPostDataOtp(any())
        }
    }

    @Test
    fun `When get otp error Then should show failed`() {
        // Given
        every { getPostDataOtpUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<HashMap<String, String>?>).onError(Throwable())
        }

        // When
        presenter.getPostDataOtp("", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onErrorGetPostDataOtp(any())
        }
    }

    @Test
    fun `When get userId Then should return userId from userSession`() {
        // Given
        val userId = "1234"
        every { userSession.userId } returns userId

        // When
        val result = presenter.userId

        // Then
        assertEquals(userId, result)
    }

    @Test
    fun `When add timeout subscription Then should hold subscription`() {
        // Given
        val t = PublishSubject.create<Long>()

        // When
        var result = 0L
        presenter.addTimeoutSubscription(t.subscribe {
            result += it
        })

        t.onNext(10)

        // Then
        assertEquals(10, result)
    }

    @Test
    fun `When clear timeout subscription Then should remove hold subscription`() {
        // Given
        val t = PublishSubject.create<Long>()

        // When
        var result = 0L
        presenter.addTimeoutSubscription(t.subscribe {
            result += it
        })
        presenter.clearTimeoutSubscription()

        t.onNext(10)

        // Then
        assertEquals(0, result)
    }
}