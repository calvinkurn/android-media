package com.tokopedia.kyc_centralized.gotoKyc.domain

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.gojek.OneKycSdk
import com.gojek.kyc.sdk.core.network.model.UnifiedKycResponse
import com.gojek.kyc.sdk.core.utils.KycSdkPartner
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CheckEligibilityUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var useCase: CheckEligibilityUseCase
    private val oneKycSdk = mockk<OneKycSdk>(relaxed = true)
    private val context = mockk<Context>(relaxed = true)
    private val dispatcher = CoroutineTestDispatchersProvider

    @Before
    fun setup() {
        useCase = CheckEligibilityUseCase(oneKycSdk, context, dispatcher)
    }

    @Test
    fun `get eligibility then return progressive`() = runBlocking {
        val maskedName = "H*****"
        val response = UnifiedKycResponse.EligibilityStatusApiModel(
            success = true,
            data = UnifiedKycResponse.EligibilityStatusData(
                flow = "PROGRESSIVE_ELIGIBLE",
                reasonCode = "",
                userIdentityData = UnifiedKycResponse.EligibilityStatusIdentityData(
                    formFlow = "",
                    hashedNIK = "",
                    maskedName = maskedName
                )
            )
        )

        coEvery { oneKycSdk.getOneKycNetworkRepository().getEligibilityStatus(partnerName = KycSdkPartner.TOKOPEDIA_CORE.name) } returns response

        val result = useCase.invoke()
        assertTrue(result is CheckEligibilityResult.Progressive)
        assertEquals(maskedName, result.encryptedName)
    }

    @Test
    fun `get eligibility then return non progressive`() = runBlocking {
        val response = UnifiedKycResponse.EligibilityStatusApiModel(
            success = true,
            data = UnifiedKycResponse.EligibilityStatusData(
                flow = "NON_PROGRESSIVE_ELIGIBLE",
                reasonCode = "",
                userIdentityData = UnifiedKycResponse.EligibilityStatusIdentityData(
                    formFlow = "",
                    hashedNIK = "",
                    maskedName = ""
                )
            )
        )

        coEvery { oneKycSdk.getOneKycNetworkRepository().getEligibilityStatus(partnerName = KycSdkPartner.TOKOPEDIA_CORE.name) } returns response

        val result = useCase.invoke()
        assertTrue(result is CheckEligibilityResult.NonProgressive)
    }

    @Test
    fun `get eligibility then return awaiting approval gopay`() = runBlocking {
        val response = UnifiedKycResponse.EligibilityStatusApiModel(
            success = true,
            data = UnifiedKycResponse.EligibilityStatusData(
                flow = "ONEKYC_BLOCKED",
                reasonCode = "KYC_AWAITING_APPROVAL",
                userIdentityData = UnifiedKycResponse.EligibilityStatusIdentityData(
                    formFlow = "",
                    hashedNIK = "",
                    maskedName = ""
                )
            )
        )

        coEvery { oneKycSdk.getOneKycNetworkRepository().getEligibilityStatus(partnerName = KycSdkPartner.TOKOPEDIA_CORE.name) } returns response

        val result = useCase.invoke()
        assertTrue(result is CheckEligibilityResult.AwaitingApprovalGopay)
    }

    @Test
    fun `get eligibility then return failed in blocked`() = runBlocking {
        val flow = "ONEKYC_BLOCKED"
        val reasonCode = "KYC_AWAITING"
        val messageError = "flow: $flow - reasonCode: $reasonCode"
        val response = UnifiedKycResponse.EligibilityStatusApiModel(
            success = true,
            data = UnifiedKycResponse.EligibilityStatusData(
                flow = flow,
                reasonCode = reasonCode,
                userIdentityData = UnifiedKycResponse.EligibilityStatusIdentityData(
                    formFlow = "",
                    hashedNIK = "",
                    maskedName = ""
                )
            )
        )

        coEvery { oneKycSdk.getOneKycNetworkRepository().getEligibilityStatus(partnerName = KycSdkPartner.TOKOPEDIA_CORE.name) } returns response

        val result = useCase.invoke()
        assertTrue(result is CheckEligibilityResult.Failed)
        assertEquals(messageError, result.throwable.message)
    }

    @Test
    fun `get eligibility then return failed in flow`() = runBlocking {
        val flow = "ONEKYC_BLOCKED_123"
        val response = UnifiedKycResponse.EligibilityStatusApiModel(
            success = true,
            data = UnifiedKycResponse.EligibilityStatusData(
                flow = flow,
                reasonCode = "",
                userIdentityData = UnifiedKycResponse.EligibilityStatusIdentityData(
                    formFlow = "",
                    hashedNIK = "",
                    maskedName = ""
                )
            )
        )

        coEvery { oneKycSdk.getOneKycNetworkRepository().getEligibilityStatus(partnerName = KycSdkPartner.TOKOPEDIA_CORE.name) } returns response

        val result = useCase.invoke()
        assertTrue(result is CheckEligibilityResult.Failed)
        assertEquals(flow, result.throwable.message)
    }

    @Test
    fun `get eligibility then return failed in response`() = runBlocking {
        val message = "Something wrong!"
        val response = UnifiedKycResponse.EligibilityStatusApiModel(
            success = false,
            errors = arrayOf( UnifiedKycResponse.Error(
                code = "",
                messageTitle = "",
                message = message,
                cause = "",
                details = null
            )),
            data = UnifiedKycResponse.EligibilityStatusData(
                flow = "",
                reasonCode = "",
                userIdentityData = UnifiedKycResponse.EligibilityStatusIdentityData(
                    formFlow = "",
                    hashedNIK = "",
                    maskedName = ""
                )
            )
        )

        coEvery { oneKycSdk.getOneKycNetworkRepository().getEligibilityStatus(partnerName = KycSdkPartner.TOKOPEDIA_CORE.name) } returns response

        val result = useCase.invoke()
        assertTrue(result is CheckEligibilityResult.Failed)
        assertEquals("$message ", result.throwable.message)
    }

}
