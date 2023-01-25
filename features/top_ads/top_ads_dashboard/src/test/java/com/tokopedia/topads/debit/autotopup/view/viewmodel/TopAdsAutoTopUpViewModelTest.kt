package com.tokopedia.topads.debit.autotopup.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.common.domain.usecase.GetWhiteListedUserUseCase
import com.tokopedia.topads.dashboard.data.model.TkpdProducts
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveSelectionUseCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsTopUpCreditUseCase
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpData
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.debit.autotopup.data.model.ResponseSaving
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
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
    private val  userSession: UserSessionInterface = mockk(relaxed = true)
    private val whiteListedUserUseCase: GetWhiteListedUserUseCase = mockk(relaxed = true)
    private val topAdsTopUpCreditUseCase: TopAdsTopUpCreditUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = TopAdsAutoTopUpViewModel(
            useCase, autoTopUpUSeCase, topAdsTopUpCreditUseCase,
            saveSelectionUseCase, userSession, whiteListedUserUseCase, rule.dispatchers
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
        every { autoTopUpUSeCase.execute(captureLambda(),any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(mockObject)
        }

        viewModel.getAutoTopUpStatusFull()
        Assert.assertTrue(viewModel.getAutoTopUpStatus.value is Fail)
    }

    @Test
    fun `getAutoTopUpStatus success - response not null and error is empty test, livedata should contain data`() {
        val mockObject = spyk(AutoTopUpData.Response(AutoTopUpData(AutoTopUpStatus())))

        every { autoTopUpUSeCase.execute(captureLambda(),any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(mockObject)
        }

        viewModel.getAutoTopUpStatusFull()
        Assert.assertEquals((viewModel.getAutoTopUpStatus.value as Success).data, mockObject.response?.data)
    }

    @Test
    fun `getAutoTopUpStatus response not null and error not empty test, livedata should be fail`() {

        val actual = spyk(AutoTopUpData.Response(AutoTopUpData(errors = listOf(Error()))))

        every { autoTopUpUSeCase.execute(captureLambda(),any()) } answers {
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
        Assert.assertEquals((viewModel.getAutoTopUpStatus.value as Fail).throwable , actual)
    }

    @Test
    fun `saveSelection on success - response not null and error is empty test, livedata should have isSuccess as true`(){
        val mockObject = spyk(AutoTopUpData.Response(AutoTopUpData(AutoTopUpStatus())))

        every { saveSelectionUseCase.execute(captureLambda(), any()) } answers {
            firstArg<(AutoTopUpData.Response) -> Unit>().invoke(mockObject)
        }

        viewModel.saveSelection(true, mockk())
        val data = viewModel.statusSaveSelection.value as ResponseSaving
        Assert.assertTrue(data.isSuccess && data.throwable == null)
    }

    @Test
    fun `saveSelection on exception occurred test, livedata should have isSuccess as true`(){
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
}
