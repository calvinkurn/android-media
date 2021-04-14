package com.tokopedia.gamification.giftbox.pdp.presentation.presenter

import android.content.Context
import com.tokopedia.gamification.giftbox.data.entities.AutoApplyResponse
import com.tokopedia.gamification.giftbox.domain.AutoApplyUseCase
import com.tokopedia.gamification.giftbox.presentation.presenter.CouponListResultPresenter
import com.tokopedia.gamification.giftbox.presentation.views.CustomToast
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CouponListResultPresenterTest {
    lateinit var presenter: CouponListResultPresenter
    val dispatcher = TestCoroutineDispatcher()
    val context: Context = mockk()
    val autoApplyUseCase: AutoApplyUseCase = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        preparePresenter()
    }

    private fun getRealPresenter(): CouponListResultPresenter {
        return CouponListResultPresenter(context, autoApplyUseCase, dispatcher, dispatcher)
    }

    private fun preparePresenter() {
        presenter = getRealPresenter()
    }

    private fun prepareRelaxedPresenter() {
        presenter = spyk(getRealPresenter())
    }

    @Test
    fun testAutoApply() {

        prepareRelaxedPresenter()
        val code = "123"
        val message = "Hello"
        val requestParams: HashMap<String, Any> = mockk()
        every { autoApplyUseCase.getQueryParams(code) } returns requestParams
        val autoApplyResponse: AutoApplyResponse = mockk()
        coEvery { autoApplyUseCase.getResponse(requestParams) } returns autoApplyResponse
        presenter.autoApply(code, message)
        verify { presenter.showAutoApplyMessage(autoApplyResponse, message) }
    }

    @Test
    fun testShowAutoApplyMessage() {
        preparePresenter()

        val autoApplyResponse: AutoApplyResponse = mockk()
        var autoApplyMessage = "Hello"
        val messageList: ArrayList<String> = arrayListOf()
        var code = "200"
        every { autoApplyResponse.tokopointsSetAutoApply?.resultStatus?.code } returns code
        every { autoApplyResponse.tokopointsSetAutoApply?.resultStatus?.message } returns messageList

        mockkStatic(CustomToast::class)
//        every { CustomToast.show(any(),any()) } just runs  - unable to mock it - missing every calls inside every block
        presenter.showAutoApplyMessage(autoApplyResponse, autoApplyMessage)

        autoApplyMessage = ""
        messageList.add("Temp")

        presenter.showAutoApplyMessage(autoApplyResponse, autoApplyMessage)

        code = ""
        presenter.showAutoApplyMessage(autoApplyResponse, autoApplyMessage)
    }

    @After
    fun cleanup() {
        dispatcher.cleanupTestCoroutines()
    }
}