package com.tokopedia.topads.debit.autotopup.view.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.network.URLGenerator
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.model.WhiteListUserResponse
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.domain.usecase.GetWhiteListedUserUseCase
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.DataCredit
import com.tokopedia.topads.dashboard.data.model.TkpdProducts
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveSelectionUseCase
import com.tokopedia.topads.debit.autotopup.data.model.*
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsAutoTopUpViewModelTest {

    @get:Rule
    val taskrule = InstantTaskExecutorRule()

    @get:Rule
    val rule = CoroutineTestRule()
    private lateinit var viewModel: TopAdsAutoTopUpViewModel
    private var autoTopUpUSeCase: TopAdsAutoTopUpUSeCase = mockk(relaxed = true)
    private var saveSelectionUseCase: TopAdsSaveSelectionUseCase = mockk(relaxed = true)
    private var useCase: GraphqlUseCase<TkpdProducts> = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val whiteListedUserUseCase: GetWhiteListedUserUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = TopAdsAutoTopUpViewModel(
            useCase,
            autoTopUpUSeCase,
            saveSelectionUseCase,
            userSession,
            whiteListedUserUseCase,
            rule.dispatchers
        )
        every { userSession.shopId } returns "123"
    }

    @Test
    fun `auto topup status pass`() {
        viewModel.getAutoTopUpStatusFull()
        verify {
            autoTopUpUSeCase.execute(any(), any())
        }
    }

    @Test
    fun `auto top up status fail`() {
        val exception = Exception("lalalla")
        every { autoTopUpUSeCase.execute(any(), any()) } answers {
            exception
        }
        viewModel.getAutoTopUpStatusFull()
        verify {
            autoTopUpUSeCase.execute(any(), any())
        }
    }

    @Test
    fun `populate credit list success`() {
        viewModel.populateCreditList() {}
        verify {
            useCase.execute(any(), any())
        }
    }

    @Test
    fun `getAutoTopUpStatus response null test, livedata should be fail`() {
        val mockObject = mockk<AutoTopUpData.Response>(relaxed = true)

        every { mockObject.response } returns null
        every { autoTopUpUSeCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(mockObject)
        }

        viewModel.getAutoTopUpStatusFull()
        Assert.assertTrue(viewModel.getAutoTopUpStatus.value is Fail)
    }

    @Test
    fun `getAutoTopUpStatus success - response not null and error is empty test, livedata should contain data`() {
        val mockObject = spyk(AutoTopUpData.Response(AutoTopUpData(AutoTopUpStatus())))

        every { autoTopUpUSeCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(mockObject)
        }

        viewModel.getAutoTopUpStatusFull()
        Assert.assertEquals((viewModel.getAutoTopUpStatus.value as Success).data, mockObject.response?.data)
    }

    @Test
    fun `getAutoTopUpStatus response not null and error not empty test, livedata should be fail`() {
        val actual = spyk(AutoTopUpData.Response(AutoTopUpData(errors = listOf(Error()))))

        every { autoTopUpUSeCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(actual)
        }

        viewModel.getAutoTopUpStatusFull()
        Assert.assertTrue(viewModel.getAutoTopUpStatus.value is Fail)
    }

    @Test
    fun `getAutoTopUpStatus on exception occured test`() {
        val actual = Exception("it")

        every { autoTopUpUSeCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(actual)
        }

        viewModel.getAutoTopUpStatusFull()
        Assert.assertEquals((viewModel.getAutoTopUpStatus.value as Fail).throwable, actual)
    }

    @Test
    fun `saveSelection on success - response not null and error is empty test, livedata should have isSuccess as true`() {
        val mockObject = spyk(AutoTopUpData.Response(AutoTopUpData(AutoTopUpStatus())))

        every { saveSelectionUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(mockObject)
        }

        viewModel.saveSelection(true, mockk())
        val data = viewModel.statusSaveSelection.value as ResponseSaving
        Assert.assertTrue(data.isSuccess && data.throwable == null)
    }

    @Test
    fun `saveSelection on exception occurred test, livedata should have isSuccess as true`() {
        val actual = spyk(Throwable("ii"))

        every { saveSelectionUseCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(actual)
        }

        viewModel.saveSelection(true, mockk())
        val data = viewModel.statusSaveSelection.value as ResponseSaving
        Assert.assertTrue(!data.isSuccess && data.throwable == actual)
    }

    @Test
    fun `populateCreditList on success`() {
        val actual = TkpdProducts()

        every { useCase.execute(captureLambda(), any()) } answers {
            firstArg<(TkpdProducts) -> Unit>().invoke(actual)
        }

        var success = false
        viewModel.populateCreditList() {
            success = it == actual.tkpdProduct.creditResponse
        }

        Assert.assertTrue(success)
    }

    @Test
    fun `populateCreditList on exception occurred`() {
        val actual = spyk(Throwable())

        every { useCase.execute(any(), captureLambda()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(actual)
        }

        viewModel.populateCreditList() {}

        verify(exactly = 1) { actual.printStackTrace() }
    }

    @Test
    fun `test success in getWhiteListedUser when credit performance is IS_TOP_UP_CREDIT_NEW_UI`() {
        val actual = WhiteListUserResponse.TopAdsGetShopWhitelistedFeature(
            listOf(
                WhiteListUserResponse.TopAdsGetShopWhitelistedFeature.Data(featureName = TopAdsDashboardConstant.TopAdsCreditTopUpConstant.IS_TOP_UP_CREDIT_NEW_UI)
            )
        )
        every { whiteListedUserUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            firstArg<(WhiteListUserResponse.TopAdsGetShopWhitelistedFeature) -> Unit>().invoke(
                actual
            )
        }
        viewModel.getWhiteListedUser()

        Assert.assertTrue((viewModel.isUserWhitelisted.value as Success).data)
    }

    @Test
    fun `test Fail in getWhiteListedUser `() {
        val actual = Throwable("my exception")
        every { whiteListedUserUseCase.executeQuerySafeMode(captureLambda(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(
                actual
            )
        }
        viewModel.getWhiteListedUser()

        Assert.assertEquals(
            (viewModel.isUserWhitelisted.value as Fail).throwable.message,
            actual.message
        )
    }

    @Test
    fun `test clicked in getAutoTopUpCreditList when isAutoTopUpActive is true`() {
        val actual = mockk<AutoTopUpStatus>(relaxed = true)

        every { actual.id } returns 5
        every { actual.availableNominals } returns mutableListOf(AutoTopUpItem(id = 5))

        val list = viewModel.getAutoTopUpCreditList(actual, true)

        Assert.assertTrue(list.find { it.clicked }?.clicked == true)
    }

    @Test
    fun `test clicked in getAutoTopUpCreditList when isAutoTopUpActive is false`() {
        val actual = mockk<AutoTopUpStatus>(relaxed = true)

        every { actual.id } returns 5
        every { actual.availableNominals } returns mutableListOf(AutoTopUpItem(id = 5))

        val list = viewModel.getAutoTopUpCreditList(actual, false)

        Assert.assertFalse(list.find { it.clicked }?.clicked == true)
    }

    @Test
    fun `test bonus in getCreditItemDataList when shopTier is RM`() {
        val list = mutableListOf(DataCredit(productPrice = "25.000"))
        mockkObject(Utils)
        every { Utils.convertMoneyToValue(list.first().productPrice) } returns 25000
        every { Utils.convertToCurrencyString((25000 * 0.0 / 100).toLong()) } returns "0.0"
        val listResponse = viewModel.getCreditItemDataList(list, 0.0f)

        Assert.assertEquals("Bonus Rp0.0", listResponse.first().bonus)
    }

    @Test
    fun `test limit in getAutoTopUpMaxCreditLimit when productPrice is not null`() {
        val price = "25.0000"
        val expected = 100000L
        mockkObject(Utils)
        every { Utils.convertMoneyToValue(price) } returns 25000
        val actual = viewModel.getAutoTopUpMaxCreditLimit(4, price)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test limit in getAutoTopUpMaxCreditLimit when productPrice is null`() {
        val price = null
        val expected = 0L
        mockkObject(Utils)
        every { Utils.convertMoneyToValue(any()) } returns 25000
        val actual = viewModel.getAutoTopUpMaxCreditLimit(4, price)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `test getAutoTopUpCreditHistoryData`() {
        val expectedFrequency = 4
        val data = AutoTopUpStatus(
            id = 5,
            frequency = expectedFrequency,
            availableNominals = mutableListOf(
                AutoTopUpItem(
                    id = 5,
                    priceFmt = "25.000",
                    minCreditFmt = "1.250"
                )
            )
        )
        val expectedBonus = "3.0"

        mockkObject(Utils)
        every { Utils.convertMoneyToValue(any()) } returns 25000
        every { Utils.convertToCurrencyString(any()) } returns expectedBonus

        val actual = viewModel.getAutoTopUpCreditHistoryData(data)

        Assert.assertEquals(data.availableNominals.first().priceFmt, actual.first)
        Assert.assertEquals(expectedBonus, actual.second)
        Assert.assertEquals(data.availableNominals.first().minCreditFmt, actual.third.first)
        Assert.assertEquals(expectedFrequency, actual.third.second)
    }

    @Test
    fun `test getAutoTopUpCreditListFromSelected when clicked`() {
        val price = "25.000"
        val actual = viewModel.getAutoTopUpCreditListFromSelected(
            price,
            mutableListOf(TopUpCreditItemData(productPrice = price))
        )

        Assert.assertTrue(actual.first.first().clicked)
        Assert.assertEquals(0, actual.second)
    }

    @Test
    fun `test getAutoTopUpCreditListFromSelected when not clicked`() {
        val price = "25.000"
        val actual = viewModel.getAutoTopUpCreditListFromSelected(
            null,
            mutableListOf(TopUpCreditItemData(price))
        )

        Assert.assertFalse(actual.first.first().clicked)
        Assert.assertEquals(-1, actual.second)
    }

    @Test
    fun `test getUrl`() {
        val expected = "productUrl"
        mockkStatic(URLGenerator::class)
        every { URLGenerator.generateURLSessionLogin(any(), any(), any()) } returns expected
        mockkStatic(Uri::class)
        every { Uri.encode(any()) } returns ""
        val actual = viewModel.getUrl("url")

        Assert.assertEquals(expected, actual)
    }
}
