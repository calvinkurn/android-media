package com.tokopedia.usercomponents.tokopediaplus

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usercomponents.common.wrapper.UserComponentsStateResult
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusDataModel
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusResponseDataModel
import com.tokopedia.usercomponents.tokopediaplus.domain.TokopediaPlusUseCase
import com.tokopedia.usercomponents.tokopediaplus.ui.TokopediaPlusViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TokopediaPlusViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProviderTest = CoroutineTestDispatchersProvider
    private var viewModel: TokopediaPlusViewModel? = null

    private var tokopediaPlusUseCase = mockk<TokopediaPlusUseCase>(relaxed = true)
    private var observerTokopediaWidget = mockk<Observer<UserComponentsStateResult<TokopediaPlusDataModel>>>(relaxed = true)

    private var sourcePage = "account_page"
    private var mockErrorMessage = "Opps!"
    private val mockThrowable = Throwable(mockErrorMessage)

    private var mockResponse = TokopediaPlusResponseDataModel(
        TokopediaPlusDataModel(
            isShown = true,
            isSubscriber = true,
            title = "Nikmatin Bebas Ongkir tanpa batas!",
            subtitle = "GoTo PLUS diskon 50%, Rp25 rb/bulan",
            applink = "tokopedia://rewards",
            iconImageURL = "icon url",
            url = "url"
        )
    )

    @Before
    fun setUp() {
        viewModel = TokopediaPlusViewModel(tokopediaPlusUseCase, dispatcherProviderTest)
        viewModel?.tokopedisPlus?.observeForever(observerTokopediaWidget)
    }

    @After
    fun tearDown() {
        viewModel?.tokopedisPlus?.removeObserver(observerTokopediaWidget)
    }

    @Test
    fun `getTokopediaPlusWidgetContent - Success`() {
        coEvery {
            tokopediaPlusUseCase(mapOf())
        } returns mockResponse

        viewModel?.loadTokopediPlus(sourcePage)

        coVerify {
            observerTokopediaWidget.onChanged(
                UserComponentsStateResult.Success(mockResponse.tokopediaPlus)
            )
        }

        val result = viewModel?.tokopedisPlus?.value
        assert(result is UserComponentsStateResult.Success)
    }

    @Test
    fun `getTokopediaPlusWidgetContent - Failed`() {
        coEvery {
            tokopediaPlusUseCase(mapOf())
        } throws mockThrowable

        viewModel?.loadTokopediPlus(sourcePage)

        coVerify {
            observerTokopediaWidget.onChanged(
                UserComponentsStateResult.Fail(mockThrowable)
            )
        }

        val result = viewModel?.tokopedisPlus?.value
        assert(result is UserComponentsStateResult.Fail)
    }
}