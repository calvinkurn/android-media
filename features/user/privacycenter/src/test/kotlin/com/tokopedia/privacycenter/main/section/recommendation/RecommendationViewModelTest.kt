package com.tokopedia.privacycenter.main.section.recommendation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.domain.DevicePermissionUseCase
import com.tokopedia.privacycenter.domain.GetRecommendationFriendState
import com.tokopedia.privacycenter.domain.SocialNetworkGetConsentUseCase
import com.tokopedia.privacycenter.domain.SocialNetworkSetConsentUseCase
import com.tokopedia.privacycenter.ui.main.section.recommendation.RecommendationViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RecommendationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: RecommendationViewModel

    private val devicePermissionUseCase = mockk<DevicePermissionUseCase>(relaxed = true)
    private val getConsentUseCase = mockk<SocialNetworkGetConsentUseCase>(relaxed = true)
    private val setConsentUseCase = mockk<SocialNetworkSetConsentUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = RecommendationViewModel(
            devicePermissionUseCase,
            getConsentUseCase,
            setConsentUseCase,
            dispatcher
        )
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `get shake shake permission then return not allowed`() {
        val expected = false

        coEvery { devicePermissionUseCase.isShakeShakeAllowed() } returns expected

        val result = viewModel.isShakeShakeAllowed.getOrAwaitValue()
        assertFalse(result)
        assertEquals(expected, result)
    }

    @Test
    fun `set shake shake permission then set permission only called once`() {
        val isAllowed = true

        viewModel.setShakeShakePermission(isAllowed)

        coVerify(exactly = 1) {
            devicePermissionUseCase.setShakeShakePermission(isAllowed)
        }
    }

    @Test
    fun `set geolocation permission then return permission value`() {
        val isAllowed = true

        viewModel.setGeolocationChange(isAllowed)

        val result = viewModel.isGeolocationAllowed.getOrAwaitValue()
        assertTrue(result)
        assertEquals(isAllowed, result)
    }

    @Test
    fun `get geolocation permission then return allowed`() {
        val expected = true

        coEvery { devicePermissionUseCase.isLocationAllowed() } returns expected
        viewModel.refreshGeolocationPermission()

        val result = viewModel.isGeolocationAllowed.getOrAwaitValue()
        assertTrue(result)
        assertEquals(expected, result)
    }

    @Test
    fun `get consent then return allowed`() {
        val isAllowed = true
        val expected = GetRecommendationFriendState.Success(isAllowed)

        coEvery { getConsentUseCase(Unit) } returns expected
        viewModel.getConsentSocialNetwork()

        val resultGetConsent = viewModel.getConsentSocialNetwork.getOrAwaitValue()
        val resultIsAllowed = viewModel.isRecommendationFriendAllowed.getOrAwaitValue()
        assertTrue(resultGetConsent is GetRecommendationFriendState.Success)
        assertEquals(expected, resultGetConsent)
        assertEquals(isAllowed, resultIsAllowed)
    }

    @Test
    fun `get consent then failed`() {
        val expected = GetRecommendationFriendState.Failed(Throwable())

        coEvery { getConsentUseCase(Unit) } returns expected
        viewModel.getConsentSocialNetwork()

        val resultGetConsent = viewModel.getConsentSocialNetwork.getOrAwaitValue()
        assertTrue(resultGetConsent is GetRecommendationFriendState.Failed)
        assertEquals(expected, resultGetConsent)
    }

    @Test
    fun `get consent then throwable`() {
        val throwable = Throwable()
        val expected = GetRecommendationFriendState.Failed(throwable)

        coEvery { getConsentUseCase(Unit) } throws throwable
        viewModel.getConsentSocialNetwork()

        val resultGetConsent = viewModel.getConsentSocialNetwork.getOrAwaitValue()
        assertTrue(resultGetConsent is GetRecommendationFriendState.Failed)
        assertEquals(expected.throwable, resultGetConsent.throwable)
    }

    @Test
    fun `set consent then success`() {
        val isAllowed = true
        val expected = PrivacyCenterStateResult.Success(isAllowed)

        coEvery { setConsentUseCase(isAllowed) } returns expected
        viewModel.setConsentSocialNetwork(isAllowed)

        val resultSetConsent = viewModel.setConsentSocialNetwork.getOrAwaitValue()
        val resultIsAllowed = viewModel.isRecommendationFriendAllowed.getOrAwaitValue()
        assertTrue(resultSetConsent is PrivacyCenterStateResult.Success)
        assertEquals(expected, resultSetConsent)
        assertEquals(isAllowed, resultIsAllowed)
    }

    @Test
    fun `set consent then failed`() {
        val isAllowed = true
        val expected = PrivacyCenterStateResult.Fail<Boolean>(Throwable())

        coEvery { setConsentUseCase(isAllowed) } returns expected
        viewModel.setConsentSocialNetwork(isAllowed)

        val resultSetConsent = viewModel.setConsentSocialNetwork.getOrAwaitValue()
        assertTrue(resultSetConsent is PrivacyCenterStateResult.Fail)
        assertEquals(expected, resultSetConsent)
    }

    @Test
    fun `set consent then throwable`() {
        val isAllowed = true
        val throwable = Throwable()
        val expected = PrivacyCenterStateResult.Fail<Boolean>(throwable)

        coEvery { setConsentUseCase(isAllowed) } throws throwable
        viewModel.setConsentSocialNetwork(isAllowed)

        val resultSetConsent = viewModel.setConsentSocialNetwork.getOrAwaitValue()
        assertTrue(resultSetConsent is PrivacyCenterStateResult.Fail)
        assertEquals(expected.error, resultSetConsent.error)
    }
}
