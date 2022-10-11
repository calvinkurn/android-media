package com.tokopedia.home_account.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_account.getOrAwaitValue
import com.tokopedia.home_account.privacy_account.data.GetConsentDataModel
import com.tokopedia.home_account.privacy_account.data.LinkStatus
import com.tokopedia.home_account.privacy_account.data.LinkStatusResponse
import com.tokopedia.home_account.privacy_account.data.SetConsentDataModel
import com.tokopedia.home_account.privacy_account.domain.GetConsentSocialNetworkUseCase
import com.tokopedia.home_account.privacy_account.domain.GetLinkStatusUseCase
import com.tokopedia.home_account.privacy_account.domain.GetUserProfile
import com.tokopedia.home_account.privacy_account.domain.SetConsentSocialNetworkUseCase
import com.tokopedia.home_account.privacy_account.viewmodel.PrivacyAccountViewModel
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import junit.framework.Assert.assertTrue
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
    private val getLinkStatusUseCase = mockk<GetLinkStatusUseCase>(relaxed = true)
    private val getUserProfile = mockk<GetUserProfile>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private var linkStatusResponse = mockk<Observer<Result<LinkStatusResponse>>>(relaxed = true)
    private val throwable = mockk<Throwable>(relaxed = true)

    private val mockLinkStatusResponse = mockk<LinkStatusResponse>(relaxed = true)
    private val mockGetUserProfile = mockk<ProfilePojo>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = PrivacyAccountViewModel(getLinkStatusUseCase, getUserProfile, getConsentSocialNetworkUseCase, setConsentSocialNetworkUseCase, userSession, CoroutineTestDispatchersProvider)
        viewModel.linkStatus.observeForever(linkStatusResponse)
    }

    @Test
    fun `on Success Get Link Status, without get profile` () {
        coEvery { getLinkStatusUseCase.invoke(any()) } returns mockLinkStatusResponse
        viewModel.getLinkStatus(false)

        verify {
            linkStatusResponse.onChanged(Success(mockLinkStatusResponse))
        }
    }

    @Test
    fun `on Success Get Link Status, without get profile - default param` () {
        coEvery { getLinkStatusUseCase.invoke(any()) } returns mockLinkStatusResponse
        viewModel.getLinkStatus()

        verify {
            linkStatusResponse.onChanged(Success(mockLinkStatusResponse))
        }
    }

    @Test
    fun `on Success Get Link Status, with get profile` () {
        val mockPhoneNo = "08123123123"
        every { mockGetUserProfile.profileInfo.phone } returns mockPhoneNo
        coEvery { getLinkStatusUseCase(any()) } returns mockLinkStatusResponse
        coEvery { getUserProfile(Unit) } returns mockGetUserProfile

        viewModel.getLinkStatus(true)

        coVerify {
            userSession.phoneNumber = mockPhoneNo
            getUserProfile(Unit)
            mockLinkStatusResponse.response.linkStatus.forEach { it.phoneNo = mockPhoneNo }
            linkStatusResponse.onChanged(Success(mockLinkStatusResponse))
        }
    }

    @Test
    fun `on Success Get Link Status, with get profile, empty phone number` () {
        val mockPhoneNo = ""
        every { mockGetUserProfile.profileInfo.phone } returns mockPhoneNo
        coEvery { getLinkStatusUseCase(any()) } returns mockLinkStatusResponse
        coEvery { getUserProfile(Unit) } returns mockGetUserProfile

        viewModel.getLinkStatus(true)

        coVerify {
            getUserProfile(Unit)
            linkStatusResponse.onChanged(Success(mockLinkStatusResponse))
        }
    }

    @Test
    fun `on Success Get Link Status, with failed get profile` () {
        val mockPhoneNo = ""
        every { mockGetUserProfile.profileInfo.phone } returns mockPhoneNo
        coEvery { getLinkStatusUseCase(any()) } returns mockLinkStatusResponse
        coEvery { getUserProfile(Unit) } throws throwable

        viewModel.getLinkStatus(true)

        verify {
            linkStatusResponse.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Failed Get Link Status` () {
        coEvery { getLinkStatusUseCase.invoke(any()) } throws throwable
        viewModel.getLinkStatus(false)

        verify {
            linkStatusResponse.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Failed Get Link Status, with get profile` () {
        coEvery { getLinkStatusUseCase.invoke(any()) } throws throwable
        viewModel.getLinkStatus(true)

        verify {
            linkStatusResponse.onChanged(Fail(throwable))
        }
    }

    @Test
    fun `on Success Get Link Status, no match phone number` () {
        val mockPhoneNo = "08123123123"
        every { mockGetUserProfile.profileInfo.phone } returns mockPhoneNo
        coEvery { getLinkStatusUseCase(any()) } returns mockLinkStatusResponse
        coEvery { getUserProfile(Unit) } returns mockGetUserProfile

        coEvery { mockLinkStatusResponse.response.linkStatus } returns arrayListOf()

        viewModel.getLinkStatus(true)

        assertTrue((viewModel.linkStatus.value as Success).data.response.linkStatus.isEmpty())
    }

    @Test
    fun `on Success Get Link Status, multiple phone` () {
        val mockPhoneNo = "08123123123"
        every { mockGetUserProfile.profileInfo.phone } returns mockPhoneNo
        coEvery { getLinkStatusUseCase(any()) } returns mockLinkStatusResponse
        coEvery { getUserProfile(Unit) } returns mockGetUserProfile

        coEvery { mockLinkStatusResponse.response.linkStatus } returns arrayListOf(
            LinkStatus(phoneNo = "123"),
            LinkStatus(phoneNo = "132")
        )

        viewModel.getLinkStatus(true)

        assertTrue((viewModel.linkStatus.value as Success).data.response.linkStatus[0].phoneNo == mockPhoneNo)
    }

    @Test
    fun `on get consent then success`() {
        val data = GetConsentDataModel()

        coEvery { getConsentSocialNetworkUseCase(Unit) } returns data
        viewModel.getConsentSocialNetwork()

        val result = viewModel.getConsentSocialNetwork.getOrAwaitValue()
        assertTrue(Success(data.socialNetworkGetConsent.data.optIn) == result)
    }

    @Test
    fun `on get consent then error`() {
        coEvery { getConsentSocialNetworkUseCase(Unit) } throws throwable
        viewModel.getConsentSocialNetwork()

        val result = viewModel.getConsentSocialNetwork.getOrAwaitValue()
        assertTrue(Fail(throwable) == result)
    }

    @Test
    fun `on set consent then success`() {
        val value = true
        val data = SetConsentDataModel()

        coEvery { setConsentSocialNetworkUseCase(value) } returns data
        viewModel.setConsentSocialNetwork(value)

        val result = viewModel.setConsentSocialNetwork.getOrAwaitValue()
        assertTrue(Success(data.socialNetworkSetConsent.data) == result)
    }

    @Test
    fun `on set consent then error`() {
        val value = true

        coEvery { setConsentSocialNetworkUseCase(value) } throws throwable
        viewModel.setConsentSocialNetwork(value)

        val result = viewModel.setConsentSocialNetwork.getOrAwaitValue()
        assertTrue(Fail(throwable) == result)
    }

}