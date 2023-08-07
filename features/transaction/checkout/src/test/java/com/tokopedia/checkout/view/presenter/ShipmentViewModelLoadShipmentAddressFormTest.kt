package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.NewUpsellData
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.domain.model.cartshipmentform.UpsellData
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.checkout.view.uimodel.ShipmentCostModel
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnWordingData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.PopUpData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShipmentViewModelLoadShipmentAddressFormTest : BaseShipmentViewModelTest() {

    private var shipmentMapper = ShipmentMapper()

    @Test
    fun firstLoadCheckoutPage_ShouldHideInitialLoadingAndRenderPage() {
        // Given
        val data = DataProvider.provideShipmentAddressFormResponse()
        val cartShipmentAddressFormData =
            shipmentMapper.convertToShipmentAddressFormData(data.shipmentAddressFormResponse.data)
        viewModel.shipmentButtonPayment.value = ShipmentButtonPaymentModel()

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns cartShipmentAddressFormData
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )

        // Then
        verifyOrder {
            view.hideInitialLoading()
            view.renderCheckoutPage(any(), any())
            view.stopTrace()
        }
    }

    @Test
    fun firstLoadCheckoutPageWithNoAddress_ShouldShipmentAddressFormEmpty() {
        // Given
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns CartShipmentAddressFormData(groupAddress = emptyList())
        every { shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(any()) } just Runs

        // When
        viewModel.processInitialLoadCheckoutPage(false, false, false)

        // Then
        verifyOrder {
            view.onShipmentAddressFormEmpty()
        }
    }

    @Test
    fun firstLoadCheckoutPageError_ShouldHideInitialLoadingAndShowToastError() {
        // Given
        val errorMessage = "error"

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                isError = true,
                errorMessage = errorMessage
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )

        // Then
        verifyOrder {
            view.hideInitialLoading()
            view.showToastError(errorMessage)
        }
    }

    @Test
    fun firstLoadCheckoutPageErrorPrerequisite_ShouldHideInitialLoadingAndShowCacheExpired() {
        // Given
        val errorMessage = "error"
        val data = CartShipmentAddressFormData(
            isError = true,
            errorMessage = errorMessage,
            isOpenPrerequisiteSite = true
        )

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )

        // Then
        verifyOrder {
            view.hideInitialLoading()
            view.onCacheExpired(errorMessage)
        }
    }

    @Test
    fun firstLoadCheckoutPageFailedException_ShouldHideInitialLoadingAndShowToastError() {
        // Given
        val error = RuntimeException("error")
        coEvery { getShipmentAddressFormV4UseCase(any()) } throws error

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )

        // Then
        verifyOrder {
            view.hideInitialLoading()
            view.showToastError(any())
            view.stopTrace()
        }
    }

    @Test
    fun firstLoadCheckoutPageFailedCartException_ShouldHideInitialLoadingAndShowToastError() {
        // Given
        val error = CartResponseErrorException("error")
        coEvery { getShipmentAddressFormV4UseCase(any()) } throws error

        // When
        viewModel.processInitialLoadCheckoutPage(
            false,
            false,
            false
        )

        // Then
        verifyOrder {
            view.hideInitialLoading()
            view.showToastError(error.message)
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page with no error and address list is empty THEN should trigger finish activity`() {
        // Given
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                errorCode = 0,
                groupAddress = emptyList()
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.onShipmentAddressFormEmpty()
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page get state address id match THEN should render checkout page`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = UserAddress.STATE_CHOSEN_ADDRESS_MATCH)
        }
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress)
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPage(any(), any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page get state address id not match THEN should render checkout page and show toaster`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = UserAddress.STATE_ADDRESS_ID_NOT_MATCH)
        }
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                popUpMessage = "message"
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.updateLocalCacheAddressData(any())
            view.renderCheckoutPage(any(), any())
            view.showToastNormal(any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page get state address id not match but no toaster message THEN should render checkout page and don't show toaster`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = UserAddress.STATE_ADDRESS_ID_NOT_MATCH)
        }
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                popUpMessage = ""
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.updateLocalCacheAddressData(any())
            view.renderCheckoutPage(any(), any())
            view.stopTrace()
        }

        verify(inverse = true) {
            view.showToastNormal(any())
        }
    }

    @Test
    fun `WHEN load checkout page get error code open address list THEN should navigate to address list page`() {
        // Given
        val data = CartShipmentAddressFormData().apply {
            errorCode = CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADDRESS_LIST
            groupAddress = listOf(
                GroupAddress().apply {
                    userAddress = UserAddress(state = UserAddress.STATE_DISTRICT_ID_NOT_MATCH)
                }
            )
        }
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPageNoMatchedAddress(any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page get error code open address list and group address is empty THEN should navigate to address list page with default address state`() {
        // Given
        val defaultAddressState = 0
        val data = CartShipmentAddressFormData().apply {
            errorCode = CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADDRESS_LIST
            groupAddress = listOf()
        }
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPageNoMatchedAddress(defaultAddressState)
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page get state no address THEN should navigate to add new address page`() {
        // Given
        val data = CartShipmentAddressFormData().apply {
            errorCode = CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS
            groupAddress = listOf(
                GroupAddress().apply {
                    userAddress = UserAddress(state = UserAddress.STATE_NO_ADDRESS)
                }
            )
        }
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), any())
        } answers {
            firstArg<(KeroAddrIsEligibleForAddressFeatureData) -> Unit>().invoke(
                KeroAddrIsEligibleForAddressFeatureData()
            )
        }

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPageNoAddress(any(), any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN should navigate to add new address page failed THEN should show toaster`() {
        // Given
        val data = CartShipmentAddressFormData().apply {
            errorCode = CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS
            groupAddress = listOf(
                GroupAddress().apply {
                    userAddress = UserAddress(state = UserAddress.STATE_NO_ADDRESS)
                }
            )
        }

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable())
        }

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.showToastError(any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN should navigate to add new address page failed with error message THEN should show toaster`() {
        // Given
        val data = CartShipmentAddressFormData().apply {
            errorCode = CartShipmentAddressFormData.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS
            groupAddress = listOf(
                GroupAddress().apply {
                    userAddress = UserAddress(state = UserAddress.STATE_NO_ADDRESS)
                }
            )
        }

        val errorMessage = "error"

        coEvery { getShipmentAddressFormV4UseCase(any()) } returns data
        coEvery {
            eligibleForAddressUseCase.eligibleForAddressFeature(any(), any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(Throwable(errorMessage))
        }

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.showToastError(errorMessage)
            view.stopTrace()
        }
    }

    @Test
    fun `GIVEN load checkout page with last apply WHEN get last apply THEN should return last apply data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val lastApplyUiModel = LastApplyUiModel(
            codes = listOf("promoCode")
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                lastApplyData = lastApplyUiModel
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        assertEquals(lastApplyUiModel, viewModel.lastApplyData.value)
    }

    @Test
    fun `GIVEN load checkout page with cod data WHEN get cod data THEN should return cod data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val codData = CodModel(
            isCod = true,
            counterCod = 1
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                cod = codData
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        assertEquals(codData, viewModel.codData)
    }

    @Test
    fun `GIVEN load checkout page with ineligible promo dialog data WHEN get ineligible promo dialog data THEN should return ineligible promo dialog data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val ineligiblePromoDialog = true
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                isIneligiblePromoDialogEnabled = ineligiblePromoDialog
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        assertEquals(ineligiblePromoDialog, viewModel.isIneligiblePromoDialogEnabled)
    }

    @Test
    fun `GIVEN load checkout page with show onboarding data WHEN get show onboarding data THEN should return show onboarding data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val showOnboarding = true
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                isShowOnboarding = showOnboarding
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        assertEquals(showOnboarding, viewModel.isShowOnboarding)
    }

    @Test
    fun `GIVEN load checkout page with pop up data WHEN load checkout page THEN should show pop up`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val popUp = PopUpData(
            title = "title",
            description = "desc"
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                popup = popUp
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verify {
            view.showPopUp(popUp)
        }
    }

    @Test
    fun `GIVEN load checkout page with pop up data without title WHEN load checkout page THEN should not show pop up`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val popUp = PopUpData(
            title = "",
            description = "desc"
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                popup = popUp
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verify(inverse = true) {
            view.showPopUp(popUp)
        }
    }

    @Test
    fun `GIVEN load checkout page with pop up data without description WHEN load checkout page THEN should not show pop up`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val popUp = PopUpData(
            title = "title",
            description = ""
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                popup = popUp
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verify(inverse = true) {
            view.showPopUp(popUp)
        }
    }

    @Test
    fun `GIVEN load checkout page with cross sell data WHEN load checkout page THEN should set cross sell data with the right index`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val crossSell = listOf(CrossSellModel())
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                crossSell = crossSell
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        assertEquals(
            listOf(ShipmentCrossSellModel(index = 0)),
            viewModel.listShipmentCrossSellModel
        )
    }

    @Test
    fun `GIVEN load checkout page with empty cross sell data WHEN load checkout page THEN should set cross sell data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val crossSell = arrayListOf<CrossSellModel>()
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                crossSell = crossSell
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        assertTrue(viewModel.listShipmentCrossSellModel.size == 0)
    }

    @Test
    fun `GIVEN load checkout page with ppp data WHEN load checkout page THEN should set ppp page true and should send ppp impression`() {
        // Given
        val purchaseProtectionPlanData = PurchaseProtectionPlanData(
            isProtectionAvailable = true,
            protectionTitle = "title",
            protectionPricePerProduct = 1000
        )
        val productCatId = 1
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
            groupShop = listOf(
                GroupShop(
                    groupShopData = listOf(
                        GroupShopV2(
                            products = listOf(
                                Product(
                                    purchaseProtectionPlanData = purchaseProtectionPlanData,
                                    productCatId = productCatId
                                )
                            )
                        )
                    )
                )
            )
        }
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                addOnWording = AddOnWordingData()
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verify {
            analyticsPurchaseProtection.eventImpressionOfProduct(
                any(),
                listOf(
                    "${purchaseProtectionPlanData.protectionTitle} " +
                        "- ${purchaseProtectionPlanData.protectionPricePerProduct} " +
                        "- $productCatId"
                )
            )
        }
    }

    @Test
    fun `GIVEN not null shipment button data WHEN get shipment button data THEN should return shipment button data`() {
        // Given
        viewModel.shipmentButtonPayment.value =
            ShipmentButtonPaymentModel(
                totalPrice = "Rp1.000"
//                quantity = 1
            )

        // Then
//        assertEquals(1, presenter.shipmentButtonPayment.value.quantity)
        assertEquals("Rp1.000", viewModel.shipmentButtonPayment.value.totalPrice)
    }

    @Test
    fun `GIVEN not null shipment cost data WHEN get shipment cost data THEN should return shipment cost data`() {
        // Given
        val shipmentCostModel = ShipmentCostModel(
            totalPrice = 1000.0
        )
        viewModel.shipmentCostModel.value = shipmentCostModel

        // Then
        assertEquals(shipmentCostModel, viewModel.shipmentCostModel.value)
    }

    @Test
    fun `GIVEN coupon state not changed WHEN get coupon state data THEN should return coupon state not changed`() {
        assertEquals(false, viewModel.couponStateChanged)
    }

    @Test
    fun `GIVEN coupon state changed WHEN get coupon state data THEN should return coupon state changed`() {
        // Given
        viewModel.couponStateChanged = true

        // Then
        assertEquals(true, viewModel.couponStateChanged)
    }

    @Test
    fun `GIVEN null campaign timer WHEN get campaign timer data THEN should return null`() {
        // Then
        assertEquals(null, viewModel.getCampaignTimer())
    }

    @Test
    fun `GIVEN campaign timer not show timer WHEN get campaign timer data THEN should return null`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val campaignTimerUi = CampaignTimerUi(showTimer = false)
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                campaignTimerUi = campaignTimerUi
            )
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        assertEquals(null, viewModel.getCampaignTimer())
    }

    @Test
    fun `GIVEN campaign timer show timer WHEN get campaign timer data THEN should return campaign timer data`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val campaignTimerUi = CampaignTimerUi(showTimer = true)
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                campaignTimerUi = campaignTimerUi
            )
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        assertEquals(campaignTimerUi, viewModel.getCampaignTimer())
    }

    @Test
    fun `WHEN load checkout page get default value address state THEN should render checkout page`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress)
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPage(any(), any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN reload checkout page success THEN should render checkout page`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress)
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.resetPromoBenefit()
            view.clearTotalBenefitPromoStacking()
            view.hideLoading()
            view.renderCheckoutPage(any(), any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN reload checkout page success with error ticker THEN should initialize shipment ticker error model with error ticker and other data is disabled`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val errorTicker = "error ticker message"
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                errorTicker = errorTicker,
                groupAddress = listOf(groupAddress),
                donation = Donation(),
                egoldAttributes = EgoldAttributeModel()
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        assert(viewModel.shipmentTickerErrorModel.errorMessage == errorTicker)
        assert(viewModel.shipmentTickerErrorModel.isError)
        assert(!viewModel.shipmentDonationModel!!.isEnabled)
        assert(!viewModel.egoldAttributeModel.value!!.isEnabled)
    }

    @Test
    fun `WHEN reload checkout page failed THEN should render checkout page`() {
        // Given
        val error = CartResponseErrorException("error")
        coEvery { getShipmentAddressFormV4UseCase(any()) } throws error

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verifyOrder {
            view.setHasRunningApiCall(false)
            view.hideLoading()
            view.showToastError(error.message)
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN load checkout page with upsell THEN should have corresponding upsell model`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = UpsellData(
            isShow = true,
            title = "title",
            description = "desc",
            appLink = "applink",
            image = "image"
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                upsell = upsell
            )

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        assertEquals(
            ShipmentUpsellModel(
                isShow = true,
                title = "title",
                description = "desc",
                appLink = "applink",
                image = "image"
            ),
            viewModel.shipmentUpsellModel
        )
    }

    @Test
    fun `WHEN load checkout page with new upsell THEN should have corresponding new upsell model`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = true,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress),
                newUpsell = upsell
            )
        viewModel.isPlusSelected = true

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        coVerify {
            getShipmentAddressFormV4UseCase(
                match { it.isPlusSelected }
            )
        }
        assertEquals(
            ShipmentNewUpsellModel(
                isShow = true,
                description = "desc",
                appLink = "applink",
                image = "image",
                isSelected = true,
                price = 100,
                priceWording = "Rp100",
                duration = "duration",
                summaryInfo = "wording",
                buttonText = "button"
            ),
            viewModel.shipmentNewUpsellModel
        )
    }

    @Test
    fun `GIVEN detached view WHEN load checkout page THEN should do nothing`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        coEvery { getShipmentAddressFormV4UseCase(any()) } returns
            CartShipmentAddressFormData(
                groupAddress = listOf(groupAddress)
            )
        viewModel.detachView()

        // When
        viewModel.processInitialLoadCheckoutPage(
            true,
            false,
            false
        )

        // Then
        verify(inverse = true) {
            view.showLoading()
            view.hideLoading()
        }
    }

    @Test
    fun `WHEN presenter detached THEN all usecases is unsubscribed`() {
        // When
        viewModel.detachView()

        // Then
        verify {
            eligibleForAddressUseCase.cancelJobs()
            epharmacyUseCase.cancelJobs()
        }
    }
}
