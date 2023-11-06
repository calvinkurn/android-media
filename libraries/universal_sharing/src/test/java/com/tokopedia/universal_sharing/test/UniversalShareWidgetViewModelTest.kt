package com.tokopedia.universal_sharing.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.requests.LinkerShareRequest
import com.tokopedia.universal_sharing.domain.usecase.ImageGeneratorUseCase
import com.tokopedia.universal_sharing.domain.usecase.ImagePolicyUseCase
import com.tokopedia.universal_sharing.model.ImageGeneratorModel
import com.tokopedia.universal_sharing.model.ImagePolicyResponse
import com.tokopedia.universal_sharing.model.PdpParamModel
import com.tokopedia.universal_sharing.view.model.AffiliateEligibility
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.view.model.LinkShareWidgetProperties
import com.tokopedia.universal_sharing.view.sharewidget.LinkerResultWidget
import com.tokopedia.universal_sharing.view.sharewidget.UniversalShareWidgetViewModel
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class UniversalShareWidgetViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var imagePolicyUseCase: ImagePolicyUseCase

    @RelaxedMockK
    lateinit var imageGeneratorUseCase: ImageGeneratorUseCase

    @RelaxedMockK
    lateinit var affiliateEligibilityUseCase: AffiliateEligibilityCheckUseCase

    @RelaxedMockK
    lateinit var linkProperties: Observer<LinkShareWidgetProperties>

    @RelaxedMockK
    lateinit var linkerResult: Observer<LinkerResultWidget>

    @RelaxedMockK
    lateinit var resultAffiliate: Observer<Result<GenerateAffiliateLinkEligibility>>

    lateinit var viewModel: UniversalShareWidgetViewModel

    val dummyLinkProperties = LinkShareWidgetProperties(page = "", message = "", linkerType = "", id = "", deeplink = "", desktopUrl = "")
    val dummyResult = LinkerShareResult().apply {
        url = "https://tokopedia.link/test"
    }
    val dummyUrl = "https://dummyUrl.com"
    val dummyLinkerError = LinkerError()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = UniversalShareWidgetViewModel(
            affiliateEligibilityUseCase = affiliateEligibilityUseCase,
            imagePolicyUseCase = imagePolicyUseCase,
            imageGeneratorUseCase = imageGeneratorUseCase
        )
        viewModel.linkProperties.observeForever(linkProperties)
        viewModel.linkerResult.observeForever(linkerResult)
        viewModel.resultAffiliate.observeForever(resultAffiliate)
    }

    @After
    fun afterTest() {
        Dispatchers.resetMain()
    }

    @Test
    fun setInitialDataTest() {
        // Given
        val dummyLinkProperties = LinkShareWidgetProperties(page = "", message = "", linkerType = "", id = "", deeplink = "", desktopUrl = "")

        // When
        viewModel.setData(dummyLinkProperties)

        // Then
        verify {
            viewModel.setData(any())

            linkProperties.onChanged(dummyLinkProperties)
        }
    }

    @Test
    fun executeLinkRequestIsSuccess() {
        // Given
        mockLinkGeneration(true)

        // When
        viewModel.executeLinkRequest(
            LinkerShareData().apply {
                linkerData = LinkerData()
            }
        )

        // Then
        verify {
            LinkerUtils.createShareRequest(any(), any<Any>(), any())
            linkerResult.onChanged(LinkerResultWidget.Success(dummyResult))
        }
    }

    @Test
    fun executeLinkRequestIsFailed() {
        // Given
        mockLinkGeneration(false)

        // When
        viewModel.executeLinkRequest(
            LinkerShareData().apply {
                linkerData = LinkerData().apply {
                    isThrowOnError = true
                }
            }
        )

        // Then
        verify {
            LinkerUtils.createShareRequest(any(), any<Any>(), any())
            linkerResult.onChanged(LinkerResultWidget.Failed(dummyLinkerError))
        }
    }

    @Test
    fun executeLinkRequestContextual() {
        // Given
        mockLinkGeneration(true)
        mockImageGenerator()
        val dummyLinkerShareData = LinkerShareData().apply {
            linkerData = LinkerData()
        }

        // When
        viewModel.executeLinkRequest(dummyLinkerShareData, "123", PdpParamModel())

        // Then
        coVerify {
            imagePolicyUseCase(any())
            imageGeneratorUseCase.executeOnBackground()
            linkerResult.onChanged(LinkerResultWidget.Success(dummyResult))
            assertEquals(dummyLinkerShareData.linkerData.ogImageUrl, dummyUrl)
        }
    }

    @Test
    fun checkIfAffiliateEligible() {
        // Given
        val dummyAffiliateInput = AffiliateInput()
        val dumyResponse = GenerateAffiliateLinkEligibility(
            affiliateEligibility = AffiliateEligibility(
                isRegistered = true,
                isEligible = true
            )
        )
        coEvery {
            affiliateEligibilityUseCase.executeOnBackground()
        } returns dumyResponse

        // When
        viewModel.checkIsAffiliate(dummyAffiliateInput)

        // Then
        coVerify {
            affiliateEligibilityUseCase.executeOnBackground()
            assertTrue((viewModel.resultAffiliate.value as? Success)?.data?.affiliateEligibility?.isEligible.orFalse())
        }
    }

    private fun mockLinkGeneration(isSuccess: Boolean) {
        viewModel.setData(dummyLinkProperties)
        mockkStatic(LinkerUtils::class)
        mockkStatic(LinkerManager::class)
        every {
            LinkerManager.getInstance()
        } returns mockk(relaxed = true)
        every {
            LinkerUtils.createShareRequest(any(), any<Any>(), any())
        } answers {
            if (isSuccess) {
                thirdArg<ShareCallback>().urlCreated(dummyResult)
            } else {
                thirdArg<ShareCallback>().onError(dummyLinkerError)
            }
            LinkerShareRequest<Any>(firstArg(), secondArg(), thirdArg())
        }
    }

    private fun mockImageGenerator() {
        coEvery {
            imageGeneratorUseCase.executeOnBackground()
        } returns ImageGeneratorModel(imageUrl = dummyUrl)

        coEvery {
            imagePolicyUseCase(any())
        } returns ImagePolicyResponse()
    }
}
