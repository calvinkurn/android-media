package com.tokopedia.gamification.giftbox.pdp.presentation.presenter

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gamification.giftbox.data.entities.AutoApplyResponse
import com.tokopedia.gamification.giftbox.data.entities.ResultStatus
import com.tokopedia.gamification.giftbox.data.entities.TokopointsSetAutoApply
import com.tokopedia.gamification.giftbox.domain.AutoApplyUseCase
import com.tokopedia.gamification.giftbox.domain.AutoApplyUseCase.AutoApplyParams.CODE
import com.tokopedia.gamification.giftbox.presentation.presenter.CouponListResultPresenter
import com.tokopedia.gamification.giftbox.presentation.presenter.CouponListResultPresenter.Companion.HTTP_STATUS_OK
import com.tokopedia.gamification.giftbox.presentation.views.CustomToast
import com.tokopedia.gamification.giftbox.util.TestUtil.mockPrivateField
import com.tokopedia.gamification.giftbox.util.TestUtil.verifyAssertEquals
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class CouponListResultPresenterTest {
    @RelaxedMockK
    lateinit var autoApplyUseCase: AutoApplyUseCase

    @RelaxedMockK
    lateinit var context: Context

    @RelaxedMockK
    lateinit var customToast: CustomToast

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var presenter: CouponListResultPresenter
    private lateinit var dispatcher: TestDispatcher

    private val customToastPrivateField = "customToast"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        dispatcher = UnconfinedTestDispatcher()
        presenter = CouponListResultPresenter(
            context,
            autoApplyUseCase,
            dispatcher,
            dispatcher
        )

        presenter.mockPrivateField(customToastPrivateField, customToast)
    }

    private fun stubQueryParams(
        code: String,
        queryParams: HashMap<String, Any>
    ) {
        every {
            autoApplyUseCase.getQueryParams(code)
        } returns queryParams
    }

    private fun stubAutoApplyResponse(
        autoApplyResponse: AutoApplyResponse,
        queryParams: HashMap<String, Any>
    ) {
        coEvery {
            autoApplyUseCase.getResponse(queryParams)
        } returns autoApplyResponse
    }

    private fun stubAutoApplyResponse(
        error: Throwable,
        queryParams: HashMap<String, Any>
    ) {
        coEvery {
            autoApplyUseCase.getResponse(queryParams)
        } throws error
    }

    private fun verifyAutoApplyResponse(
        queryParams: HashMap<String, Any>
    ) {
        coVerify {
            autoApplyUseCase.getResponse(queryParams)
        }
    }

    private fun verifyCustomToastShown(
        message: String,
        inverse: Boolean = false,
        isError: Boolean = false
    ) {
        verify(inverse = inverse) {
            customToast.show(
                activityContext = context,
                text = message,
                isError = isError
            )
        }
    }

    @Test
    fun `when automatically applying but there is an error then should do nothing`() {
        val code = "123111"
        val autoApplyMessage = "success message"
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            error = Throwable(),
            queryParams = queryParams
        )

        presenter.autoApply(
            code = code,
            autoApplyMessage = autoApplyMessage
        )

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyCustomToastShown(
            message = autoApplyMessage,
            inverse = true
        )
    }

    @Test
    fun `after successfully applying automatically with auto apply message, CustomToast should show that message`() {
        val code = "123111"
        val autoApplyMessage = "success message"
        val responseReason = "no reason"
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = TokopointsSetAutoApply(
                resultStatus = ResultStatus(
                    code = HTTP_STATUS_OK,
                    message = listOf(),
                    reason = responseReason
                )
            )
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        presenter.autoApply(
            code = code,
            autoApplyMessage = autoApplyMessage
        )

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyCustomToastShown(
            message = autoApplyMessage
        )
    }

    @Test
    fun `after successfully applying automatically with empty auto apply message, CustomToast should show the message from result status`() {
        val code = "123111"
        val autoApplyMessage = ""
        val responseReason = "no reason"
        val responseMessage = "network error"
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = TokopointsSetAutoApply(
                resultStatus = ResultStatus(
                    code = HTTP_STATUS_OK,
                    message = listOf(responseMessage),
                    reason = responseReason
                )
            )
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubQueryParams(
            code = code,
            queryParams = queryParams)
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        presenter.autoApply(
            code = code,
            autoApplyMessage = autoApplyMessage
        )

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyCustomToastShown(
            message = responseMessage
        )
    }

    @Test
    fun `after successfully applying automatically with null auto apply message, CustomToast should show the message from result status`() {
        val code = "123111"
        val autoApplyMessage: String? = null
        val responseReason = "no reason"
        val responseMessage = "network error"
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = TokopointsSetAutoApply(
                resultStatus = ResultStatus(
                    code = HTTP_STATUS_OK,
                    message = listOf(responseMessage),
                    reason = responseReason
                )
            )
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        presenter.autoApply(
            code = code,
            autoApplyMessage = autoApplyMessage
        )

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyCustomToastShown(
            message = responseMessage
        )
    }

    @Test
    fun `after successfully applying automatically with null auto apply message and empty result message, CustomToast should not be shown`() {
        val code = "123111"
        val autoApplyMessage: String? = null
        val responseReason = "no reason"
        val responseMessage = ""
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = TokopointsSetAutoApply(
                resultStatus = ResultStatus(
                    code = HTTP_STATUS_OK,
                    message = listOf(responseMessage),
                    reason = responseReason
                )
            )
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        presenter.autoApply(code, autoApplyMessage)

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyCustomToastShown(
            message = responseMessage,
            inverse = true
        )
    }

    @Test
    fun `after successfully applying automatically with null auto apply message and null result message, CustomToast should not be shown`() {
        val code = "123111"
        val autoApplyMessage: String? = null
        val responseReason = "no reason"
        val responseMessage = ""
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = TokopointsSetAutoApply(
                resultStatus = ResultStatus(
                    code = HTTP_STATUS_OK,
                    message = null,
                    reason = responseReason
                )
            )
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        presenter.autoApply(code, autoApplyMessage)

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyCustomToastShown(
            message = responseMessage,
            inverse = true
        )
    }

    @Test
    fun `after successfully applying automatically but getting not success code, CustomToast should show the message from result status`() {
        val code = "123111"
        val autoApplyMessage: String? = null
        val responseReason = "no reason"
        val responseMessage = "network error"
        val responseCode = "300"
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = TokopointsSetAutoApply(
                resultStatus = ResultStatus(
                    code = responseCode,
                    message = listOf(responseMessage),
                    reason = responseReason
                )
            )
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        presenter.autoApply(code, autoApplyMessage)

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyCustomToastShown(
            message = responseMessage,
            isError = true
        )
    }

    @Test
    fun `after successfully applying automatically but getting not success code and empty message, CustomToast should not be shown`() {
        val code = "123111"
        val autoApplyMessage: String? = null
        val responseReason = "no reason"
        val responseMessage = ""
        val responseCode = "300"
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = TokopointsSetAutoApply(
                resultStatus = ResultStatus(
                    code = responseCode,
                    message = listOf(responseMessage),
                    reason = responseReason
                )
            )
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        presenter.autoApply(code, autoApplyMessage)

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyCustomToastShown(
            message = responseMessage,
            inverse = true,
            isError = true
        )
    }

    @Test
    fun `after successfully applying automatically but getting null code and empty message, CustomToast should not be shown`() {
        val code = "123111"
        val autoApplyMessage: String? = null
        val responseReason = "no reason"
        val responseMessage = ""
        val responseCode: String? = null
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = TokopointsSetAutoApply(
                resultStatus = ResultStatus(
                    code = responseCode,
                    message = listOf(responseMessage),
                    reason = responseReason
                )
            )
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        presenter.autoApply(code, autoApplyMessage)

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyCustomToastShown(
            message = responseMessage,
            inverse = true,
            isError = true
        )
    }

    @Test
    fun `after successfully applying automatically but getting null result status, CustomToast should not be shown`() {
        val code = "123111"
        val autoApplyMessage: String? = null
        val responseResult: ResultStatus? = null
        val responseMessage = ""
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = TokopointsSetAutoApply(
                resultStatus = responseResult
            )
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        presenter.autoApply(code, autoApplyMessage)

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyCustomToastShown(
            message = responseMessage,
            inverse = true,
            isError = true
        )
    }

    @Test
    fun `after successfully applying automatically but getting null tokopoints set auto apply, CustomToast should not be shown`() {
        val code = "123111"
        val autoApplyMessage: String? = null
        val responseTokopointsSetAutoApply: TokopointsSetAutoApply? = null
        val responseMessage = ""
        val response = AutoApplyResponse(
            tokopointsSetAutoApply = responseTokopointsSetAutoApply
        )
        val queryParams: HashMap<String, Any> = hashMapOf(
            CODE to code
        )

        stubQueryParams(
            code = code,
            queryParams = queryParams
        )
        stubAutoApplyResponse(
            autoApplyResponse = response,
            queryParams = queryParams
        )

        presenter.autoApply(code, autoApplyMessage)

        verifyAutoApplyResponse(
            queryParams = queryParams
        )
        verifyCustomToastShown(
            message = responseMessage,
            inverse = true,
            isError = true
        )
    }

    @Test
    fun `test some variables`() {
        presenter.context
            .verifyAssertEquals(context)

        presenter.uiDispatcher
            .verifyAssertEquals(dispatcher)

        presenter.workerDispatcher
            .verifyAssertEquals(dispatcher)

        presenter.coroutineContext
            .verifyAssertEquals(dispatcher)
    }
}
