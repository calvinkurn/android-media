package com.tokopedia.tokofood.home

import com.tokopedia.tokofood.data.createChooseAddress
import com.tokopedia.tokofood.data.createKeroAddrIsEligibleForAddressFeature
import com.tokopedia.tokofood.data.createLoadingState
import com.tokopedia.tokofood.data.createNoAddressState
import com.tokopedia.tokofood.data.createNoPinPoinState
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment.Companion.SOURCE
import org.junit.Test

class TokoFoodHomeViewModelTest: TokoFoodHomeViewModelTestFixture() {

    @Test
    fun `when getting loading state should run and give the success result`() {
        viewModel.getLoadingState()

        val expectedResponse = createLoadingState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting no pin poin state should run and give the success result`() {
        viewModel.getNoPinPoinState()

        val expectedResponse = createNoPinPoinState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting no address state should run and give the success result`() {
        viewModel.getNoAddressState()

        val expectedResponse = createNoAddressState()

        verifyGetHomeLayoutResponseSuccess(expectedResponse)
    }

    @Test
    fun `when getting error state should run and give the error result`() {
        val throwable = Throwable("Error Timeout")

        viewModel.getErrorState(throwable)

        verifyGetErrorLayoutShown()
    }

    @Test
    fun `when getting chooseAddress should throw chooseAddress's exception and get failed result`() {
        onGetChooseAddress_thenReturn(Throwable())

        viewModel.getChooseAddress(SOURCE)

        verifyGetChooseAddressFail()
    }

    @Test
    fun `when getting chooseAddress should run and give the success result`() {
        onGetChooseAddress_thenReturn(createChooseAddress())

        viewModel.getChooseAddress(SOURCE)

        verifyGetChooseAddress()

        val expectedResponse = createChooseAddress().response
        verfifyGetChooseAddressSuccess(expectedResponse)
    }

    @Test
    fun `when check is page showing empty state location should return true`() {
        viewModel.getNoAddressState()

        val actualResponse = viewModel.isShownEmptyState()

        verifyHomeIsShowingEmptyState(actualResponse)
    }

    @Test
    fun `when check is page showing empty state location should return false`() {

        val actualResponse = viewModel.isShownEmptyState()

        verifyHomeIsNotShowingEmptyState(actualResponse)
    }

    @Test
    fun `when getting eligibleForAnaRevamp should throw eligibleForAnaRevamp's exception and get failed result`() {
        onGetEligibleForAnaRevamp_thenReturn(Throwable())

        viewModel.checkUserEligibilityForAnaRevamp()

        verifyEligibleForAnaRevampFail()
    }

    @Test
    fun `when getting eligibleForAnaRevamp should run and give the success result`() {
        onGetEligibleForAnaRevamp_thenReturn(createKeroAddrIsEligibleForAddressFeature())

        viewModel.checkUserEligibilityForAnaRevamp()

        val expectedResponse = createKeroAddrIsEligibleForAddressFeature().data.eligibleForRevampAna
        verfifyEligibleForAnaRevampSuccess(expectedResponse)
    }


}