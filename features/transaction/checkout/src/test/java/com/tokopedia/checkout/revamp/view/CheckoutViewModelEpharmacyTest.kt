package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.EpharmacyData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyMiniConsultationResult
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.EthicalDrugDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.GetPrescriptionIdsResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CheckoutViewModelEpharmacyTest : BaseCheckoutViewModelTest() {

    companion object {
        const val CHECKOUT_ID = "100"
    }

    @Test
    fun `WHEN fetch prescription then should hit fetch prescription use case with checkout id`() {
        // Given
        coEvery { prescriptionIdsUseCase.setParams(any()).executeOnBackground() } returns
            GetPrescriptionIdsResponse(
                detailData = GetPrescriptionIdsResponse.EPharmacyCheckoutData(
                    prescriptionData = GetPrescriptionIdsResponse.EPharmacyCheckoutData.EPharmacyPrescriptionDetailData(
                        prescriptions = listOf(
                            GetPrescriptionIdsResponse.EPharmacyCheckoutData.Prescription("123"),
                            GetPrescriptionIdsResponse.EPharmacyCheckoutData.Prescription("321")
                        ),
                        checkoutId = CHECKOUT_ID
                    )
                )
            )

        val response = CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product()
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            epharmacyData = EpharmacyData(
                true,
                "",
                "",
                CHECKOUT_ID
            )
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // When
        viewModel.loadSAF(false, false, false)

        // Then
        coVerify { prescriptionIdsUseCase.setParams(any()).executeOnBackground() }
    }

    @Test
    fun `GIVEN empty checkout id WHEN fetch prescription ids THEN should not hit fetch prescription use case`() {
        // Given
        val checkoutId = ""
        val response = CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product()
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            epharmacyData = EpharmacyData(
                true,
                "",
                "",
                checkoutId
            )
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // When
        viewModel.loadSAF(false, false, false)

        // Then
        coVerify(inverse = true) { prescriptionIdsUseCase.setParams(any()).executeOnBackground() }
    }

    @Test
    fun `GIVEN not need to upload prescription WHEN fetch prescription THEN should not hit fetch prescription use case`() {
        // Given
        val checkoutId = ""
        val response = CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product()
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            epharmacyData = EpharmacyData(
                false,
                "",
                "",
                checkoutId
            )
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // When
        viewModel.loadSAF(false, false, false)

        // Then
        coVerify(inverse = true) { prescriptionIdsUseCase.setParams(any()).executeOnBackground() }
    }

    @Test
    fun `GIVEN consultation flow prescription WHEN fetch prescription THEN should not hit fetch prescription use case`() {
        // Given
        val checkoutId = ""
        val response = CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product()
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            epharmacyData = EpharmacyData(
                true,
                "",
                "",
                checkoutId,
                consultationFlow = true
            )
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // When
        viewModel.loadSAF(false, false, false)

        // Then
        coVerify(inverse = true) { prescriptionIdsUseCase.setParams(any()).executeOnBackground() }
    }

    @Test
    fun `WHEN set prescription ids THEN should set upload prescription image count`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        val prescriptions = arrayListOf("123", "456")

        // When
        viewModel.setPrescriptionIds(prescriptions)

        // Then
        assertEquals(
            prescriptions.size,
            viewModel.listData.value.epharmacy()?.epharmacy?.uploadedImageCount
        )
    }

    @Test
    fun `GIVEN failed prepare epharmacy data WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (secondArg() as (Throwable) -> Unit).invoke(Throwable())
        }

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(showImageUpload = true, consultationFlow = true),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
        assertNotEquals(
            CheckoutPageState.EpharmacyCoachMark::class.java,
            viewModel.pageState.value::class.java
        )
    }

    @Test
    fun `GIVEN null epharmacy data WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(null)
            )
        }

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(showImageUpload = true, consultationFlow = true),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
        assertNotEquals(
            CheckoutPageState.EpharmacyCoachMark::class.java,
            viewModel.pageState.value::class.java
        )
    }

    @Test
    fun `GIVEN null epharmacy group data WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        null
                    )
                )
            )
        }

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(showImageUpload = true, consultationFlow = true),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
        assertNotEquals(
            CheckoutPageState.EpharmacyCoachMark::class.java,
            viewModel.pageState.value::class.java
        )
    }

    @Test
    fun `GIVEN null epharmacy groups WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    )
                )
            )
        }

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(showImageUpload = true, consultationFlow = true),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
        assertNotEquals(
            CheckoutPageState.EpharmacyCoachMark::class.java,
            viewModel.pageState.value::class.java
        )
    }

    @Test
    fun `GIVEN null shipment cart data WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
                            null,
                            null,
                            listOf(),
                            null,
                            null
                        )
                    )
                )
            )
        }

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
    }

    @Test
    fun `GIVEN null epharmacy group WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
                            null,
                            null,
                            listOf(
                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(
                                    null,
                                    null,
                                    "1",
                                    null,
                                    null,
                                    null,
                                    null,
                                    null
                                )
                            ),
                            null,
                            null
                        )
                    )
                )
            )
        }

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
    }

    @Test
    fun `GIVEN null epharmacy products WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
                            null,
                            null,
                            listOf(
                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(
                                    null,
                                    null,
                                    "1",
                                    null,
                                    null,
                                    null,
                                    listOf(null),
                                    null
                                )
                            ),
                            null,
                            null
                        )
                    )
                )
            )
        }

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                epharmacyGroupIds = arrayListOf("1"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
    }

    @Test
    fun `GIVEN null epharmacy product id WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
                            null,
                            null,
                            listOf(
                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(
                                    null,
                                    null,
                                    "1",
                                    null,
                                    null,
                                    null,
                                    listOf(
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                                            null,
                                            arrayListOf(
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null,
                                                    null
                                                )
                                            ),
                                            "6554231",
                                            null,
                                            null,
                                            null,
                                            null
                                        )
                                    ),
                                    null
                                )
                            ),
                            null,
                            null
                        )
                    )
                )
            )
        }

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                epharmacyGroupIds = arrayListOf("1"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
    }

    @Test
    fun `GIVEN null epharmacy product WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
                            null,
                            null,
                            listOf(
                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(
                                    null,
                                    null,
                                    "1",
                                    null,
                                    null,
                                    null,
                                    listOf(
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                                            null,
                                            null,
                                            "6554231",
                                            null,
                                            null,
                                            null,
                                            null
                                        )
                                    ),
                                    null
                                )
                            ),
                            null,
                            null
                        )
                    )
                )
            )
        }

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                epharmacyGroupIds = arrayListOf("1"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
    }

    @Test
    fun `GIVEN invalid epharmacy shop id WHEN fetch epharmacy data THEN should do nothing`() {
        // Given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
                            null,
                            null,
                            listOf(
                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(
                                    null,
                                    null,
                                    "1",
                                    null,
                                    null,
                                    null,
                                    listOf(
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                                            null,
                                            null,
                                            "asdf",
                                            null,
                                            null,
                                            null,
                                            null
                                        )
                                    ),
                                    null
                                )
                            ),
                            null,
                            null
                        )
                    )
                )
            )
        }

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                epharmacyGroupIds = arrayListOf("1"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
    }

    @Test
    fun `GIVEN rejected consultation WHEN fetch epharmacy data THEN should set error`() {
        // Given
        val errorWording = "error wording"
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    detailData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        groupsData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
                            epharmacyGroups = listOf(
                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "123",
                                    shopInfo = listOf(
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554231",
                                            products = arrayListOf(
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389388,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        ),
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554232",
                                            products = arrayListOf(
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389387,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        )
                                    ),
                                    numberPrescriptionImages = 0,
                                    prescriptionImages = listOf(),
                                    consultationData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData(
                                        consultationString = "",
                                        tokoConsultationId = "123",
                                        partnerConsultationId = "321",
                                        consultationStatus = 4,
                                        doctorDetails = null,
                                        endTime = null,
                                        medicalRecommendation = null,
                                        prescription = listOf(),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    prescriptionCTA = null
                                )
                            ),
                            attachmentPageTickerText = null,
                            attachmentPageTickerLogoUrl = null,
                            toaster = null,
                            papPrimaryCTA = null
                        )
                    )
                )
            )
        }
        val rejectedWording = "rejectedWording"
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true,
                    rejectedWording = rejectedWording
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = true,
                rejectedWording = rejectedWording,
                epharmacyGroupIds = arrayListOf("123"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )

        assertEquals(
            true,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].isError
        )
        assertEquals(
            "Yaah, ada 0 barang tidak bisa diproses. Kamu tetap bisa lanjut bayar yang lain.",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].errorTitle
        )
        assertEquals(
            true,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].isCustomEpharmacyError
        )

        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].isError
        )
        assertEquals(
            true,
            viewModel.listData.value.filterIsInstance(CheckoutProductModel::class.java)[1].isError
        )
        assertEquals(
            rejectedWording,
            viewModel.listData.value.filterIsInstance(CheckoutProductModel::class.java)[1].errorMessage
        )
        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutProductModel::class.java)[2].isError
        )
    }

    @Test
    fun `GIVEN approved consultation WHEN fetch epharmacy data THEN should set consultation data`() {
        // Given
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    detailData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        groupsData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
                            epharmacyGroups = listOf(
                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "123",
                                    shopInfo = listOf(
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554231",
                                            products = arrayListOf(
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389388,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                ),
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389389,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        )
                                    ),
                                    numberPrescriptionImages = 0,
                                    prescriptionImages = listOf(),
                                    consultationData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData(
                                        consultationString = "qwerty",
                                        tokoConsultationId = "123",
                                        partnerConsultationId = "321",
                                        consultationStatus = 2,
                                        doctorDetails = null,
                                        endTime = null,
                                        medicalRecommendation = null,
                                        prescription = listOf(),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    prescriptionCTA = null
                                )
                            ),
                            attachmentPageTickerText = null,
                            attachmentPageTickerLogoUrl = null,
                            toaster = null,
                            papPrimaryCTA = null
                        )
                    )
                )
            )
        }
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "123",
                productId = 2150389389,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("234", shopId = 6554232, hasEthicalProducts = true),
            CheckoutProductModel(
                "345",
                productId = 2150389385
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389384
            ),
            CheckoutOrderModel("345", shopId = 6554233),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = false,
                uploadedImageCount = 1,
                epharmacyGroupIds = arrayListOf("123"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].isError
        )
        assertEquals(
            "qwerty",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].consultationDataString
        )
        assertEquals(
            "123",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].tokoConsultationId
        )
        assertEquals(
            "321",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].partnerConsultationId
        )

        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].isError
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].consultationDataString
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].tokoConsultationId
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].partnerConsultationId
        )

        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].isError
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].consultationDataString
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].tokoConsultationId
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].partnerConsultationId
        )
    }

    @Test
    fun `GIVEN mixed epharmacy data WHEN fetch epharmacy data THEN should set epharmacy data correctly`() {
        // Given
        every { epharmacyUseCase.getEPharmacyPrepareProductsGroup(any(), any()) } answers {
            (firstArg() as (EPharmacyPrepareProductsGroupResponse) -> Unit).invoke(
                EPharmacyPrepareProductsGroupResponse(
                    detailData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData(
                        groupsData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData(
                            epharmacyGroups = listOf(
                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "123",
                                    shopInfo = listOf(
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554231",
                                            products = arrayListOf(
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389389,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                ),
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389388,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        ),
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554234",
                                            products = arrayListOf(
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389382,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        )
                                    ),
                                    numberPrescriptionImages = 0,
                                    prescriptionImages = listOf(),
                                    consultationData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData(
                                        consultationString = "qwerty",
                                        tokoConsultationId = "123",
                                        partnerConsultationId = "321",
                                        consultationStatus = 2,
                                        doctorDetails = null,
                                        endTime = null,
                                        medicalRecommendation = null,
                                        prescription = listOf(),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    prescriptionCTA = null
                                ),
                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "124",
                                    shopInfo = listOf(
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554231",
                                            products = arrayListOf(
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389387,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                ),
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389386,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        )
                                    ),
                                    numberPrescriptionImages = 0,
                                    prescriptionImages = listOf(
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionImage(
                                            expiredAt = null,
                                            prescriptionId = "1",
                                            rejectReason = null,
                                            status = null
                                        ),
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionImage(
                                            expiredAt = null,
                                            prescriptionId = "2",
                                            rejectReason = null,
                                            status = null
                                        )
                                    ),
                                    consultationData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData(
                                        consultationString = "",
                                        tokoConsultationId = "",
                                        partnerConsultationId = "",
                                        consultationStatus = 0,
                                        doctorDetails = null,
                                        endTime = null,
                                        medicalRecommendation = null,
                                        prescription = listOf(),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    prescriptionCTA = null
                                ),
                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup(
                                    epharmacyGroupId = "125",
                                    shopInfo = listOf(
                                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                                            shopId = "6554232",
                                            products = arrayListOf(
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389384,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                ),
                                                EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                                    productId = 2150389385,
                                                    isEthicalDrug = true,
                                                    itemWeight = 0.0,
                                                    name = "",
                                                    productImage = "",
                                                    productTotalWeightFmt = "",
                                                    quantity = "1"
                                                )
                                            ),
                                            partnerLogoUrl = "",
                                            shopLocation = "",
                                            shopLogoUrl = "",
                                            shopName = "",
                                            shopType = ""
                                        )
                                    ),
                                    numberPrescriptionImages = 0,
                                    prescriptionImages = listOf(),
                                    consultationData = EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData(
                                        consultationString = "qwerty",
                                        tokoConsultationId = "125",
                                        partnerConsultationId = "521",
                                        consultationStatus = 4,
                                        doctorDetails = null,
                                        endTime = null,
                                        medicalRecommendation = null,
                                        prescription = listOf(),
                                        startTime = ""
                                    ),
                                    consultationSource = null,
                                    prescriptionSource = null,
                                    prescriptionCTA = null
                                )
                            ),
                            attachmentPageTickerText = null,
                            attachmentPageTickerLogoUrl = null,
                            toaster = null,
                            papPrimaryCTA = null
                        )
                    )
                )
            )
        }
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "123",
                productId = 2150389389,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "123",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "123",
                productId = 2150389386,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "234",
                productId = 2150389385,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389384,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("234", shopId = 6554232, hasEthicalProducts = true),
            CheckoutProductModel(
                "345",
                productId = 2150389383
            ),
            CheckoutOrderModel("345", shopId = 6554233, hasEthicalProducts = false),
            CheckoutProductModel(
                "456",
                productId = 2150389382
            ),
            CheckoutOrderModel("456", shopId = 6554234, hasEthicalProducts = false),
            CheckoutProductModel(
                "567",
                productId = 2150389381,
                isError = true,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("567", shopId = 6554235, hasEthicalProducts = true, isError = true),
            CheckoutProductModel(
                "678",
                productId = 2150389380,
                isError = true
            ),
            CheckoutOrderModel("678", shopId = 6554236, hasEthicalProducts = false, isError = true),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.prepareFullCheckoutPage()

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = true,
                uploadedImageCount = 3,
                epharmacyGroupIds = arrayListOf("123", "124", "125"),
                enablerNames = listOf(""),
                shopIds = listOf(
                    "6554231",
                    "6554231",
                    "6554232",
                    "6554235"
                ),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].isError
        )
        assertEquals(
            "qwerty",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].consultationDataString
        )
        assertEquals(
            "123",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].tokoConsultationId
        )
        assertEquals(
            "321",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].partnerConsultationId
        )
        assertEquals(
            emptyList<String>(),
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].prescriptionIds
        )

        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].isError
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].consultationDataString
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].tokoConsultationId
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].partnerConsultationId
        )
        assertEquals(
            listOf("1", "2"),
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].prescriptionIds
        )

        assertEquals(
            true,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].isError
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].consultationDataString
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].tokoConsultationId
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].partnerConsultationId
        )
        assertEquals(
            emptyList<String>(),
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].prescriptionIds
        )

        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[3].isError
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[3].consultationDataString
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[3].tokoConsultationId
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[3].partnerConsultationId
        )
        assertEquals(
            emptyList<String>(),
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[3].prescriptionIds
        )
    }

    @Test
    fun `GIVEN null shop info WHEN set mini consultation result THEN should set correct data`() {
        // Given
        val results = arrayListOf(
            EPharmacyMiniConsultationResult(
                "123",
                null,
                2,
                "qwerty",
                arrayListOf(),
                "321",
                "123",
                null
            )
        )
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "123",
                productId = 2150389389
            ),
            CheckoutOrderModel(
                "123",
                shopId = 6554231,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389385
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389384
            ),
            CheckoutOrderModel("345", shopId = 6554233),
            CheckoutProductModel(
                "456",
                productId = 2150389383,
                isError = true
            ),
            CheckoutProductModel(
                "456",
                productId = 2150389382,
                isError = true
            ),
            CheckoutOrderModel("456", shopId = 6554234, isError = true),
            CheckoutProductModel(
                "456",
                productId = 2150389381,
                ethicalDrugDataModel = EthicalDrugDataModel(true),
                isError = true
            ),
            CheckoutProductModel(
                "456",
                productId = 2150389380,
                isError = true
            ),
            CheckoutOrderModel("456", shopId = 6554235, isError = true, hasEthicalProducts = true),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.setMiniConsultationResult(results)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = false,
                uploadedImageCount = 0,
                epharmacyGroupIds = arrayListOf(),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232", "6554235"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
    }

    @Test
    fun `GIVEN null shop id WHEN set mini consultation result THEN should set correct data`() {
        // Given
        val results = arrayListOf(
            EPharmacyMiniConsultationResult(
                "123",
                arrayListOf(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                        "",
                        arrayListOf(
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                false,
                                0.0,
                                "",
                                2150389388,
                                "",
                                "",
                                "1"
                            )
                        ),
                        null,
                        "",
                        "",
                        "",
                        ""
                    )
                ),
                2,
                "qwerty",
                arrayListOf(),
                "321",
                "123",
                null
            )
        )
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "123",
                productId = 2150389389
            ),
            CheckoutOrderModel(
                "123",
                shopId = 6554231,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389385
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389384
            ),
            CheckoutOrderModel("345", shopId = 6554233),
            CheckoutProductModel(
                "456",
                productId = 2150389383,
                isError = true
            ),
            CheckoutProductModel(
                "456",
                productId = 2150389382,
                isError = true
            ),
            CheckoutOrderModel("456", shopId = 6554234, isError = true),
            CheckoutProductModel(
                "456",
                productId = 2150389381,
                ethicalDrugDataModel = EthicalDrugDataModel(true),
                isError = true
            ),
            CheckoutProductModel(
                "456",
                productId = 2150389380,
                isError = true
            ),
            CheckoutOrderModel("456", shopId = 6554235, isError = true, hasEthicalProducts = true),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.setMiniConsultationResult(results)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = false,
                uploadedImageCount = 0,
                epharmacyGroupIds = arrayListOf("123"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232", "6554235"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
    }

    @Test
    fun `GIVEN null products WHEN set mini consultation result THEN should set correct data`() {
        // Given
        val results = arrayListOf(
            EPharmacyMiniConsultationResult(
                "123",
                arrayListOf(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                        "",
                        null,
                        "123",
                        "",
                        "",
                        "",
                        ""
                    )
                ),
                2,
                "qwerty",
                arrayListOf(),
                "321",
                "123",
                null
            )
        )
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "123",
                productId = 2150389389
            ),
            CheckoutOrderModel(
                "123",
                shopId = 6554231,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389385
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389384
            ),
            CheckoutOrderModel("345", shopId = 6554233),
            CheckoutProductModel(
                "456",
                productId = 2150389383,
                isError = true
            ),
            CheckoutProductModel(
                "456",
                productId = 2150389382,
                isError = true
            ),
            CheckoutOrderModel("456", shopId = 6554234, isError = true),
            CheckoutProductModel(
                "456",
                productId = 2150389382,
                ethicalDrugDataModel = EthicalDrugDataModel(true),
                isError = true
            ),
            CheckoutProductModel(
                "456",
                productId = 2150389380,
                isError = true
            ),
            CheckoutOrderModel("456", shopId = 6554235, isError = true, hasEthicalProducts = true),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.setMiniConsultationResult(results)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = false,
                uploadedImageCount = 0,
                epharmacyGroupIds = arrayListOf("123"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232", "6554235"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
    }

    @Test
    fun `GIVEN invalid shop id WHEN set mini consultation result THEN should set correct data`() {
        // Given
        val results = arrayListOf(
            EPharmacyMiniConsultationResult(
                "123",
                arrayListOf(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                        "",
                        arrayListOf(
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                false,
                                0.0,
                                "",
                                2150389388,
                                "",
                                "",
                                "1"
                            )
                        ),
                        "asdf",
                        "",
                        "",
                        "",
                        ""
                    )
                ),
                2,
                "qwerty",
                arrayListOf(),
                "321",
                "123",
                null
            )
        )
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "123",
                productId = 2150389389
            ),
            CheckoutOrderModel(
                "123",
                shopId = 6554231,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389385
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389384
            ),
            CheckoutOrderModel("345", shopId = 6554233),
            CheckoutProductModel(
                "456",
                productId = 2150389383,
                isError = true
            ),
            CheckoutProductModel(
                "456",
                productId = 2150389382,
                isError = true
            ),
            CheckoutOrderModel("456", shopId = 6554234, isError = true),
            CheckoutProductModel(
                "456",
                productId = 2150389381,
                ethicalDrugDataModel = EthicalDrugDataModel(true),
                isError = true
            ),
            CheckoutProductModel(
                "456",
                productId = 2150389380,
                isError = true
            ),
            CheckoutOrderModel("456", shopId = 6554235, isError = true, hasEthicalProducts = true),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.setMiniConsultationResult(results)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = false,
                uploadedImageCount = 0,
                epharmacyGroupIds = arrayListOf("123"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232", "6554235"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
    }

    @Test
    fun `GIVEN approved consultation WHEN set mini consultation result THEN should set correct data`() {
        // Given
        val results = arrayListOf(
            EPharmacyMiniConsultationResult(
                "123",
                arrayListOf(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                        "",
                        arrayListOf(
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                false,
                                0.0,
                                "",
                                2150389388,
                                "",
                                "",
                                "1"
                            )
                        ),
                        "6554231",
                        "",
                        "",
                        "",
                        ""
                    )
                ),
                2,
                "qwerty",
                arrayListOf(),
                "321",
                "123",
                null
            )
        )
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "123",
                productId = 2150389389
            ),
            CheckoutOrderModel(
                "123",
                shopId = 6554231,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389385
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389384
            ),
            CheckoutOrderModel("345", shopId = 6554233),
            CheckoutProductModel(
                "456",
                productId = 2150389383,
                isError = true
            ),
            CheckoutProductModel(
                "456",
                productId = 2150389382,
                isError = true
            ),
            CheckoutOrderModel("456", shopId = 6554234, isError = true),
            CheckoutProductModel(
                "456",
                productId = 2150389381,
                ethicalDrugDataModel = EthicalDrugDataModel(true),
                isError = true
            ),
            CheckoutProductModel(
                "456",
                productId = 2150389380,
                isError = true
            ),
            CheckoutOrderModel("456", shopId = 6554235, isError = true, hasEthicalProducts = true),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.setMiniConsultationResult(results)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = false,
                uploadedImageCount = 1,
                epharmacyGroupIds = arrayListOf("123"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232", "6554235"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].isError
        )
        assertEquals(
            "qwerty",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].consultationDataString
        )
        assertEquals(
            "123",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].tokoConsultationId
        )
        assertEquals(
            "321",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].partnerConsultationId
        )

        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].isError
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].consultationDataString
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].tokoConsultationId
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].partnerConsultationId
        )

        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].isError
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].consultationDataString
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].tokoConsultationId
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].partnerConsultationId
        )
    }

    @Test
    fun `GIVEN rejected consultation in the mixed order WHEN set mini consultation result THEN should set error`() {
        // Given
        val result = arrayListOf(
            EPharmacyMiniConsultationResult(
                epharmacyGroupId = "123",
                shopInfo = listOf(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                        shopId = "6554231",
                        products = arrayListOf(
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                productId = 2150389388,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    ),
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                        shopId = "6554232",
                        products = arrayListOf(
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                productId = 2150389387,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    )
                ),
                prescriptionImages = listOf(),
                consultationStatus = 4,
                consultationString = "",
                tokoConsultationId = "123",
                partnerConsultationId = "321",
                prescription = listOf()
            )
        )
        val rejectedWording = "rejectedWording"
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "234",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "234",
                productId = 2150389386
            ),
            CheckoutOrderModel(
                "234",
                shopId = 6554232,
                hasEthicalProducts = true,
                hasNonEthicalProducts = true
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389385
            ),
            CheckoutOrderModel(
                "345",
                shopId = 6554233,
                hasEthicalProducts = false,
                hasNonEthicalProducts = true
            ),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true,
                    rejectedWording = rejectedWording
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.setMiniConsultationResult(result)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                rejectedWording = rejectedWording,
                hasInvalidPrescription = true,
                epharmacyGroupIds = arrayListOf("123"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554232"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )

        assertEquals(
            true,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].isError
        )
        assertEquals(
            "Yaah, ada 0 barang tidak bisa diproses. Kamu tetap bisa lanjut bayar yang lain.",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].errorTitle
        )
        assertEquals(
            true,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].isCustomEpharmacyError
        )

        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].isError
        )
        assertEquals(
            true,
            viewModel.listData.value.filterIsInstance(CheckoutProductModel::class.java)[1].isError
        )
        assertEquals(
            rejectedWording,
            viewModel.listData.value.filterIsInstance(CheckoutProductModel::class.java)[1].errorMessage
        )
        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutProductModel::class.java)[2].isError
        )

        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].isError
        )
    }

    @Test
    fun `GIVEN mixed epharmacy data WHEN set mini consultation result THEN should set epharmacy data correctly`() {
        // Given
        val result = arrayListOf(
            EPharmacyMiniConsultationResult(
                epharmacyGroupId = "123",
                shopInfo = listOf(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                        shopId = "6554231",
                        products = arrayListOf(
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                productId = 2150389389,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            ),
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                productId = 2150389388,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    ),
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                        shopId = "6554234",
                        products = arrayListOf(
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                productId = 2150389382,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    )
                ),
                prescriptionImages = listOf(),
                consultationString = "qwerty",
                tokoConsultationId = "123",
                partnerConsultationId = "321",
                consultationStatus = 2,
                prescription = listOf()
            ),
            EPharmacyMiniConsultationResult(
                epharmacyGroupId = "124",
                shopInfo = listOf(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                        shopId = "6554231",
                        products = arrayListOf(
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                productId = 2150389387,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            ),
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                productId = 2150389386,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    )
                ),
                prescriptionImages = listOf(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionImage(
                        expiredAt = null,
                        prescriptionId = "1",
                        rejectReason = null,
                        status = null
                    ),
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionImage(
                        expiredAt = null,
                        prescriptionId = "2",
                        rejectReason = null,
                        status = null
                    )
                ),
                consultationString = "",
                tokoConsultationId = "",
                partnerConsultationId = "",
                consultationStatus = 0,
                prescription = listOf()
            ),
            EPharmacyMiniConsultationResult(
                epharmacyGroupId = "125",
                shopInfo = listOf(
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo(
                        shopId = "6554232",
                        products = arrayListOf(
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                productId = 2150389384,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            ),
                            EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product(
                                productId = 2150389385,
                                isEthicalDrug = true,
                                itemWeight = 0.0,
                                name = "",
                                productImage = "",
                                productTotalWeightFmt = "",
                                quantity = "1"
                            )
                        ),
                        partnerLogoUrl = "",
                        shopLocation = "",
                        shopLogoUrl = "",
                        shopName = "",
                        shopType = ""
                    )
                ),
                prescriptionImages = listOf(),
                consultationString = "qwerty",
                tokoConsultationId = "125",
                partnerConsultationId = "521",
                consultationStatus = 4,
                prescription = listOf()
            )
        )
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel(
                "123",
                productId = 2150389388,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "123",
                productId = 2150389389,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("123", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "123",
                productId = 2150389387,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "123",
                productId = 2150389386,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("234", shopId = 6554231, hasEthicalProducts = true),
            CheckoutProductModel(
                "345",
                productId = 2150389385,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutProductModel(
                "345",
                productId = 2150389384,
                ethicalDrugDataModel = EthicalDrugDataModel(true)
            ),
            CheckoutOrderModel("345", shopId = 6554232, hasEthicalProducts = true),
            CheckoutProductModel(
                "456",
                productId = 2150389383
            ),
            CheckoutOrderModel("456", shopId = 6554233),
            CheckoutProductModel(
                "456",
                productId = 2150389382
            ),
            CheckoutOrderModel("456", shopId = 6554234),
            CheckoutEpharmacyModel(
                epharmacy = UploadPrescriptionUiModel(
                    showImageUpload = true,
                    consultationFlow = true
                )
            ),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.setMiniConsultationResult(result)

        // Then
        assertEquals(
            UploadPrescriptionUiModel(
                hasInvalidPrescription = true,
                uploadedImageCount = 3,
                epharmacyGroupIds = arrayListOf("123", "124", "125"),
                enablerNames = listOf(""),
                shopIds = listOf("6554231", "6554231", "6554232"),
                showImageUpload = true,
                consultationFlow = true
            ),
            viewModel.listData.value.epharmacy()?.epharmacy
        )
        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].isError
        )
        assertEquals(
            "qwerty",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].consultationDataString
        )
        assertEquals(
            "123",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].tokoConsultationId
        )
        assertEquals(
            "321",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].partnerConsultationId
        )
        assertEquals(
            emptyList<String>(),
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[0].prescriptionIds
        )

        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].isError
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].consultationDataString
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].tokoConsultationId
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].partnerConsultationId
        )
        assertEquals(
            listOf("1", "2"),
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[1].prescriptionIds
        )

        assertEquals(
            true,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].isError
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].consultationDataString
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].tokoConsultationId
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].partnerConsultationId
        )
        assertEquals(
            emptyList<String>(),
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[2].prescriptionIds
        )

        assertEquals(
            false,
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[3].isError
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[3].consultationDataString
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[3].tokoConsultationId
        )
        assertEquals(
            "",
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[3].partnerConsultationId
        )
        assertEquals(
            emptyList<String>(),
            viewModel.listData.value.filterIsInstance(CheckoutOrderModel::class.java)[3].prescriptionIds
        )
    }
}
