package com.tokopedia.chooseaccount.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.chooseaccount.data.DeleteOclData
import com.tokopedia.chooseaccount.data.GetOclAccountData
import com.tokopedia.chooseaccount.data.OclAccount
import com.tokopedia.chooseaccount.domain.usecase.DeleteOclAccountUseCase
import com.tokopedia.chooseaccount.domain.usecase.GetOclAccountUseCase
import com.tokopedia.chooseaccount.view.ocl.OclChooseAccountViewModel
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.ocl.OclPreference
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginOclUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OclChooseAccountViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getOclAccountUseCase = mockk<GetOclAccountUseCase>(relaxed = true)
    private val loginOclUseCase = mockk<LoginOclUseCase>(relaxed = true)
    private val getUserInfoAndSaveSessionUseCase = mockk<GetUserInfoAndSaveSessionUseCase>(relaxed = true)
    private val deleteOclAccountUseCase = mockk<DeleteOclAccountUseCase>(relaxed = true)
    private val oclPreference = mockk<OclPreference>(relaxed = true)

    private val dispatcher = CoroutineTestDispatchersProvider

    private lateinit var viewModel: OclChooseAccountViewModel

    @Before
    fun setUp() {
        viewModel = OclChooseAccountViewModel(
            getOclAccountUseCase, loginOclUseCase, getUserInfoAndSaveSessionUseCase, deleteOclAccountUseCase, oclPreference, dispatcher
        )
    }

    @Test
    fun `getOclAccounts success`() {
        val users = arrayListOf(OclAccount(token = "aaaa", fullName = "Test"))
        val mockResponse = GetOclAccountData(token = "abc123", users = users)
        every { oclPreference.getToken() } returns "abc123"
        coEvery { getOclAccountUseCase(any()) } returns mockResponse

        viewModel.getOclAccounts()
        verify {
            oclPreference.storeToken("abc123")
        }
        assert(viewModel.oclAccounts.getOrAwaitValue() == users)
        assert(!viewModel.mainLoader.getOrAwaitValue())
    }

    @Test
    fun `getOclAccounts success - user empty`() {
        val users = arrayListOf<OclAccount>()
        val mockResponse = GetOclAccountData(token = "abc123", users = users)
        every { oclPreference.getToken() } returns "abc123"
        coEvery { getOclAccountUseCase(any()) } returns mockResponse

        viewModel.getOclAccounts()
        verify {
            oclPreference.storeToken("abc123")
        }
        assert(viewModel.navigateToNormalLogin.getOrAwaitValue() == Unit)
    }

    @Test
    fun `getOclAccounts success - token empty`() {
        val users = arrayListOf(OclAccount(token = "aaaa", fullName = "Test"))
        val mockResponse = GetOclAccountData(token = "", users = users)
        every { oclPreference.getToken() } returns "abc123"
        coEvery { getOclAccountUseCase(any()) } returns mockResponse

        viewModel.getOclAccounts()
        verify(exactly = 0) {
            oclPreference.storeToken("abc123")
        }
        assert(viewModel.oclAccounts.getOrAwaitValue() == users)
        assert(!viewModel.mainLoader.getOrAwaitValue())
    }

    @Test
    fun `getOclAccounts failed - exception`() {
        val mockException = Exception("Error")
        every { oclPreference.getToken() } returns "abc123"
        coEvery { getOclAccountUseCase(any()) } throws mockException

        viewModel.getOclAccounts()
        assert(viewModel.toasterError.getOrAwaitValue() == "Error")
        assert(!viewModel.mainLoader.getOrAwaitValue())
    }

    @Test
    fun `loginOcl success`() {
        val mockResponse = LoginToken(accessToken = "abc1234")
        every { oclPreference.getToken() } returns "abc123"
        coEvery { loginOclUseCase(any()) } returns mockResponse
        viewModel.loginOcl("abc123")

        coVerify { getUserInfoAndSaveSessionUseCase(Unit) }
        assert(viewModel.navigateToSuccessPage.getOrAwaitValue() == Unit)
    }

    @Test
    fun `loginOcl success - access token empty`() {
        val mockResponse = LoginToken(accessToken = "")
        every { oclPreference.getToken() } returns "abc123"
        coEvery { loginOclUseCase(any()) } returns mockResponse
        viewModel.loginOcl("abc123")

        coVerify(exactly = 0) { getUserInfoAndSaveSessionUseCase(Unit) }
        assert(viewModel.navigateToSuccessPage.getOrAwaitValue() == Unit)
    }

    @Test
    fun `loginOcl failed - exception`() {
        val mockException = Exception("Error")
        every { oclPreference.getToken() } returns "abc123"
        coEvery { loginOclUseCase(any()) } throws mockException

        viewModel.loginOcl("abc123")
        assert(viewModel.loginFailedToaster.getOrAwaitValue() == "Error")
    }

    @Test
    fun `deleteAccount success`() {
        val mockResp = DeleteOclData(token = "abc123")
        every { oclPreference.getToken() } returns "abc123"
        coEvery { deleteOclAccountUseCase(any()) } returns mockResp

        val deletedAcc = OclAccount(token = "aaaa", fullName = "Test")
        val users = arrayListOf(deletedAcc)
        val mockResponse = GetOclAccountData(token = "abc123", users = users)
        every { oclPreference.getToken() } returns "abc123"
        coEvery { getOclAccountUseCase(any()) } returns mockResponse

        viewModel.getOclAccounts()
        viewModel.deleteAccount(deletedAcc)

        verify { oclPreference.storeToken("abc123") }
        assert(viewModel.navigateToNormalLogin.getOrAwaitValue() == Unit)
    }

    @Test
    fun `deleteAccount success - token empty`() {
        val mockResp = DeleteOclData(token = "")
        every { oclPreference.getToken() } returns "abc123"
        coEvery { deleteOclAccountUseCase(any()) } returns mockResp

        viewModel.deleteAccount(OclAccount())

        verify(exactly = 0) { oclPreference.storeToken("abc123") }
//        assert(viewModel.navigateToNormalLogin.getOrAwaitValue())
    }

    @Test
    fun `deleteAccount exception`() {
        val mockException = Exception("Error")
        every { oclPreference.getToken() } returns "abc123"
        coEvery { deleteOclAccountUseCase(any()) } throws mockException

        viewModel.deleteAccount(OclAccount())

        assert(viewModel.toasterError.getOrAwaitValue() == "Error")
    }
}
