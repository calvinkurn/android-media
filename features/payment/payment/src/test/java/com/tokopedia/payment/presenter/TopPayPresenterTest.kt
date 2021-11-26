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
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
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
        presenter.attachView(view)
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
    fun `When process payment without view Then should not do anything`() {
        // Given
        every { view.paymentPassData } returns PaymentPassData()

        // When
        presenter.processUriPayment()

        // Then
        verify(inverse = true) {
            view.renderWebViewPostUrl(any(), any(), any())
            view.showToastMessageWithForceCloseView(any())
        }
    }

    @Test
    fun `When register fingerprint success Then should show success`() {
        // Given
        every { saveFingerPrintUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<Boolean>).onNext(true)
            (secondArg() as Subscriber<Boolean>).onCompleted()
        }

        // When
        presenter.attachView(view)
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
            (secondArg() as Subscriber<Boolean>).onCompleted()
        }

        // When
        presenter.attachView(view)
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
            (secondArg() as Subscriber<Boolean>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.registerFingerPrint("", "", "", "", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onErrorRegisterFingerPrint()
        }
    }

    @Test
    fun `When register fingerprint success without view Then should not show success`() {
        // Given
        every { saveFingerPrintUseCase.execute(any(), any()) } answers {
            presenter.detachView()
            (secondArg() as Subscriber<Boolean>).onNext(true)
            (secondArg() as Subscriber<Boolean>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.registerFingerPrint("", "", "", "", "")

        // Then
        verify(inverse = true) {
            view.hideProgressDialog()
            view.onSuccessRegisterFingerPrint()
        }
    }

    @Test
    fun `When register fingerprint failed without view Then should not show failed`() {
        // Given
        every { saveFingerPrintUseCase.execute(any(), any()) } answers {
            presenter.detachView()
            (secondArg() as Subscriber<Boolean>).onNext(false)
            (secondArg() as Subscriber<Boolean>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.registerFingerPrint("", "", "", "", "")

        // Then
        verify(inverse = true) {
            view.hideProgressDialog()
            view.onErrorRegisterFingerPrint()
        }
    }

    @Test
    fun `When register fingerprint error without view Then should not show failed`() {
        // Given
        every { saveFingerPrintUseCase.execute(any(), any()) } answers {
            presenter.detachView()
            (secondArg() as Subscriber<Boolean>).onError(Throwable())
            (secondArg() as Subscriber<Boolean>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.registerFingerPrint("", "", "", "", "")

        // Then
        verify(inverse = true) {
            view.hideProgressDialog()
            view.onErrorRegisterFingerPrint()
        }
    }

    @Test
    fun `When register fingerprint without view Then should not execute`() {
        // Given
        every { saveFingerPrintUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<Boolean>).onError(Throwable())
            (secondArg() as Subscriber<Boolean>).onCompleted()
        }

        // When
        presenter.registerFingerPrint("", "", "", "", "")

        // Then
        verify(inverse = true) {
            view.showProgressDialog()
            view.hideProgressDialog()
            saveFingerPrintUseCase.execute(any(), any())
        }
    }

    @Test
    fun `When payment fingerprint success Then should show success`() {
        // Given
        every { paymentFingerprintUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onNext(ResponsePaymentFingerprint(isSuccess = true))
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onCompleted()
        }

        // When
        presenter.attachView(view)
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
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.paymentFingerPrint("", "", "", "", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onErrorPaymentFingerPrint()
        }
    }

    @Test
    fun `When payment fingerprint failed with null data Then should show failed`() {
        // Given
        every { paymentFingerprintUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onNext(null)
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onCompleted()
        }

        // When
        presenter.attachView(view)
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
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.paymentFingerPrint("", "", "", "", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onErrorPaymentFingerPrint()
        }
    }

    @Test
    fun `When payment fingerprint success without view Then should not show success`() {
        // Given
        every { paymentFingerprintUseCase.execute(any(), any()) } answers {
            presenter.detachView()
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onNext(ResponsePaymentFingerprint(isSuccess = true))
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.paymentFingerPrint("", "", "", "", "")

        // Then
        verify(inverse = true) {
            view.hideProgressDialog()
            view.onSuccessPaymentFingerprint(any(), any())
        }
    }

    @Test
    fun `When payment fingerprint failed without view Then should not show failed`() {
        // Given
        every { paymentFingerprintUseCase.execute(any(), any()) } answers {
            presenter.detachView()
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onNext(ResponsePaymentFingerprint(isSuccess = false))
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.paymentFingerPrint("", "", "", "", "")

        // Then
        verify(inverse = true) {
            view.hideProgressDialog()
            view.onErrorPaymentFingerPrint()
        }
    }

    @Test
    fun `When payment fingerprint error without view Then should not show failed`() {
        // Given
        every { paymentFingerprintUseCase.execute(any(), any()) } answers {
            presenter.detachView()
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onError(Throwable())
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.paymentFingerPrint("", "", "", "", "")

        // Then
        verify(inverse = true) {
            view.hideProgressDialog()
            view.onErrorPaymentFingerPrint()
        }
    }

    @Test
    fun `When payment fingerprint without view Then should not execute`() {
        // Given
        every { paymentFingerprintUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onError(Throwable())
            (secondArg() as Subscriber<ResponsePaymentFingerprint?>).onCompleted()
        }

        // When
        presenter.paymentFingerPrint("", "", "", "", "")

        // Then
        verify(inverse = true) {
            view.showProgressDialog()
            view.hideProgressDialog()
            paymentFingerprintUseCase.execute(any(), any())
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
            (secondArg() as Subscriber<HashMap<String, String>?>).onCompleted()
        }

        // When
        presenter.attachView(view)
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
            (secondArg() as Subscriber<HashMap<String, String>?>).onCompleted()
        }

        // When
        presenter.attachView(view)
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
            (secondArg() as Subscriber<HashMap<String, String>?>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.getPostDataOtp("", "")

        // Then
        verify(ordering = Ordering.SEQUENCE) {
            view.showProgressDialog()
            view.hideProgressDialog()
            view.onErrorGetPostDataOtp(any())
        }
    }

    @Test
    fun `When get otp success without view Then should not show success`() {
        // Given
        val result = hashMapOf(
                "query" to "value",
                "asdf" to "qwerty"
        )
        every { getPostDataOtpUseCase.execute(any(), any()) } answers {
            presenter.detachView()
            (secondArg() as Subscriber<HashMap<String, String>?>).onNext(result)
            (secondArg() as Subscriber<HashMap<String, String>?>).onCompleted()
        }

        // When
        presenter.attachView(view)
        val urlOtp = "urlOtp"
        presenter.getPostDataOtp("", urlOtp)

        // Then
        verify(inverse = true) {
            view.hideProgressDialog()
            view.onSuccessGetPostDataOTP("asdf=qwerty&query=value", urlOtp)
        }
    }

    @Test
    fun `When get otp failed without view Then should not show failed`() {
        // Given
        every { getPostDataOtpUseCase.execute(any(), any()) } answers {
            presenter.detachView()
            (secondArg() as Subscriber<HashMap<String, String>?>).onNext(null)
            (secondArg() as Subscriber<HashMap<String, String>?>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.getPostDataOtp("", "")

        // Then
        verify(inverse = true) {
            view.hideProgressDialog()
            view.onErrorGetPostDataOtp(any())
        }
    }

    @Test
    fun `When get otp error without view Then should not show failed`() {
        // Given
        every { getPostDataOtpUseCase.execute(any(), any()) } answers {
            presenter.detachView()
            (secondArg() as Subscriber<HashMap<String, String>?>).onError(Throwable())
            (secondArg() as Subscriber<HashMap<String, String>?>).onCompleted()
        }

        // When
        presenter.attachView(view)
        presenter.getPostDataOtp("", "")

        // Then
        verify(inverse = true) {
            view.hideProgressDialog()
            view.onErrorGetPostDataOtp(any())
        }
    }

    @Test
    fun `When get otp without view Then should not execute`() {
        // Given
        every { getPostDataOtpUseCase.execute(any(), any()) } answers {
            (secondArg() as Subscriber<HashMap<String, String>?>).onError(Throwable())
            (secondArg() as Subscriber<HashMap<String, String>?>).onCompleted()
        }

        // When
        presenter.getPostDataOtp("", "")

        // Then
        verify(inverse = true) {
            view.showProgressDialog()
            view.hideProgressDialog()
            getPostDataOtpUseCase.execute(any(), any())
        }
    }

    @Test
    fun `When get userId Then should return userId from userSession`() {
        // Given
        val userId = "1234"
        every { userSession.userId } returns userId

        // When
        presenter.attachView(view)
        val result = presenter.userId

        // Then
        assertEquals(userId, result)
    }

    @Test
    fun `When add timeout subscription Then should hold subscription`() {
        // Given
        val t = PublishSubject.create<Long>()

        // When
        presenter.attachView(view)
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
        presenter.attachView(view)
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