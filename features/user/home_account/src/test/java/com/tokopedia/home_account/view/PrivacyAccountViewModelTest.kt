package com.tokopedia.home_account.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_account.consentWithdrawal.data.ConsentGroupListDataModel
import com.tokopedia.home_account.consentWithdrawal.data.GetConsentGroupListDataModel
import com.tokopedia.home_account.consentWithdrawal.domain.GetConsentGroupListUseCase
import com.tokopedia.home_account.getOrAwaitValue
import com.tokopedia.home_account.privacy_account.data.GetConsentDataModel
import com.tokopedia.home_account.privacy_account.data.SetConsentDataModel
import com.tokopedia.home_account.privacy_account.domain.GetConsentSocialNetworkUseCase
import com.tokopedia.home_account.privacy_account.domain.SetConsentSocialNetworkUseCase
import com.tokopedia.home_account.privacy_account.viewmodel.PrivacyAccountViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris on 08/09/21.
 */

class PrivacyAccountViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PrivacyAccountViewModel

    private val getConsentSocialNetworkUseCase = mockk<GetConsentSocialNetworkUseCase>(relaxed = true)
    private val setConsentSocialNetworkUseCase = mockk<SetConsentSocialNetworkUseCase>(relaxed = true)
    private val getConsentGroupListUseCase = mockk<GetConsentGroupListUseCase>(relaxed = true)

    private var getConsentGroupListObserver = mockk<Observer<Result<ConsentGroupListDataModel>>>(relaxed = true)
    private val throwable = mockk<Throwable>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = PrivacyAccountViewModel(
            getConsentSocialNetworkUseCase,
            setConsentSocialNetworkUseCase,
            getConsentGroupListUseCase,
            CoroutineTestDispatchersProvider
        )

        viewModel.getConsentGroupList.observeForever(getConsentGroupListObserver)
    }

    @After
    fun tearDown() {
        viewModel.getConsentGroupList.removeObserver(getConsentGroupListObserver)
    }

    @Test
    fun `on get consent social network then success`() {
        val data = GetConsentDataModel()

        coEvery { getConsentSocialNetworkUseCase(Unit) } returns data
        viewModel.getConsentSocialNetwork()

        val result = viewModel.getConsentSocialNetwork.getOrAwaitValue()
        assertTrue(Success(data.socialNetworkGetConsent.data.optIn) == result)
    }

    @Test
    fun `on get consent social network then error`() {
        coEvery { getConsentSocialNetworkUseCase(Unit) } throws throwable
        viewModel.getConsentSocialNetwork()

        val result = viewModel.getConsentSocialNetwork.getOrAwaitValue()
        assertTrue(Fail(throwable) == result)
    }

    @Test
    fun `on set consent social network then success`() {
        val value = true
        val data = SetConsentDataModel()

        coEvery { setConsentSocialNetworkUseCase(value) } returns data
        viewModel.setConsentSocialNetwork(value)

        val result = viewModel.setConsentSocialNetwork.getOrAwaitValue()
        assertTrue(Success(data.socialNetworkSetConsent.data) == result)
    }

    @Test
    fun `on set consent social network then error`() {
        val value = true

        coEvery { setConsentSocialNetworkUseCase(value) } throws throwable
        viewModel.setConsentSocialNetwork(value)

        val result = viewModel.setConsentSocialNetwork.getOrAwaitValue()
        assertTrue(Fail(throwable) == result)
    }

    @Test
    fun `get consent group list - success flow`() {
        val successResponse = ConsentGroupListDataModel(
            success = true
        )

        val mockResponse = GetConsentGroupListDataModel(successResponse)

        coEvery {
            getConsentGroupListUseCase(Unit)
        } returns mockResponse

        viewModel.getConsentGroupList()

        assert(viewModel.getConsentGroupList.value is Success)
        assert((viewModel.getConsentGroupList.value as Success).data.success)
    }

    @Test
    fun `get consent group list - failed flow - from BE`() {
        val failedResponse = ConsentGroupListDataModel(
            success = false,
            errorMessages = listOf(
                "Opss!, Something wrong!"
            )
        )

        val mockResponse = GetConsentGroupListDataModel(failedResponse)

        coEvery {
            getConsentGroupListUseCase(Unit)
        } returns mockResponse

        viewModel.getConsentGroupList()

        assert(viewModel.getConsentGroupList.value is Fail)
        assert((viewModel.getConsentGroupList.value as Fail).throwable.message.toString() == mockResponse.consentGroupList.errorMessages.toString())
    }

    @Test
    fun `get consent group list - failed flow - from Exception`() {
        coEvery {
            getConsentGroupListUseCase(Unit)
        } throws throwable

        viewModel.getConsentGroupList()

        assert(viewModel.getConsentGroupList.value is Fail)
        assert((viewModel.getConsentGroupList.value as Fail).throwable == throwable)
    }
}
