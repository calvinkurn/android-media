package com.tokopedia.home_account.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_account.linkaccount.data.LinkStatusResponse
import com.tokopedia.home_account.linkaccount.domain.GetLinkStatusUseCase
import com.tokopedia.home_account.linkaccount.domain.GetUserProfile
import com.tokopedia.home_account.linkaccount.viewmodel.LinkAccountViewModel
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Yoris on 08/09/21.
 */

class LinkAccountViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LinkAccountViewModel

    private val getLinkStatusUseCase = mockk<GetLinkStatusUseCase>(relaxed = true)
    private val getUserProfile = mockk<GetUserProfile>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private var linkStatusResponse = mockk<Observer<Result<LinkStatusResponse>>>(relaxed = true)
    private val throwable = mockk<Throwable>(relaxed = true)

    private val mockLinkStatusResponse = mockk<LinkStatusResponse>(relaxed = true)
    private val mockGetUserProfile = mockk<ProfilePojo>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = LinkAccountViewModel(getLinkStatusUseCase, getUserProfile, userSession, CoroutineTestDispatchersProvider)
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

}