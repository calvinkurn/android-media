package feedcomponent.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.presentation.viewmodel.FeedProductItemInfoViewModel
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import feedcomponent.model.FeedMVCModelBuilder
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by shruti agarwal on 05/12/22.
 */

class FeedProductItemInfoViewModelFetchVoucherSummaryTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private lateinit var mvcSummaryUseCase: MVCSummaryUseCase
    private lateinit var viewModel: FeedProductItemInfoViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        mvcSummaryUseCase = mockk(relaxed = true)
        viewModel =
            FeedProductItemInfoViewModel(mvcSummaryUseCase, testDispatcher)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    val shopid = "1234"
    private val builder = FeedMVCModelBuilder()

    @Test
    fun `given fetch mvc response, it is success with resultStatus 200`() {
        val expectedResult = builder.getMVCResponseSuccess()

        coEvery { mvcSummaryUseCase.getResponse(any()) } returns expectedResult

        viewModel.fetchMerchantVoucherSummary(shopid)

        val result = viewModel.merchantVoucherSummary.getOrAwaitValue()
        val expected = builder.buildTokopointsCatalogMVCSummary()

        Assertions
            .assertThat(result)
            .isEqualTo(Success(expected))
    }

    @Test
    fun `given fetch mvc response, it is fail with resultStatus not 200`() {
        val expectedResult = builder.getMVCResponseSuccessWithResponseNot200()

        coEvery { mvcSummaryUseCase.getResponse(any()) } returns expectedResult

        viewModel.fetchMerchantVoucherSummary(shopid)

        val result = viewModel.merchantVoucherSummary.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }

    @Test
    fun `given fetch mvc response, it is fail with resultStatus not 200 and data null`() {
        val expectedResult = builder.getMVCResponseSuccessWithResponseNot200(data = null)

        coEvery { mvcSummaryUseCase.getResponse(any()) } returns expectedResult

        viewModel.fetchMerchantVoucherSummary(shopid)

        val result = viewModel.merchantVoucherSummary.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }

    @Test
    fun `given fetch mvc response, it is fail with resultStatus not 200 and message null`() {
        val expectedResult = builder.getMVCResponseSuccessWithResponseNot200(data = builder.buildTokopointsCatalogMVCSummaryWithResponseNotResponseAndMsgNull())

        coEvery { mvcSummaryUseCase.getResponse(any()) } returns expectedResult

        viewModel.fetchMerchantVoucherSummary(shopid)

        val result = viewModel.merchantVoucherSummary.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }

    @Test
    fun `given fetch mvc response, it is fail with resultStatus not 200 and resultStatus null`() {
        val expectedResult = builder.getMVCResponseSuccessWithResponseNot200(data = builder.buildTokopointsCatalogMVCSummaryWithResultStatusNull())

        coEvery { mvcSummaryUseCase.getResponse(any()) } returns expectedResult

        viewModel.fetchMerchantVoucherSummary(shopid)

        val result = viewModel.merchantVoucherSummary.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }

    @Test
    fun `given error when fetching initial data, then return error`() {
        coEvery { mvcSummaryUseCase.getResponse(any()) } throws Throwable()

        viewModel.fetchMerchantVoucherSummary(shopid)

        val result = viewModel.merchantVoucherSummary.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(Fail::class.java)
    }
}
