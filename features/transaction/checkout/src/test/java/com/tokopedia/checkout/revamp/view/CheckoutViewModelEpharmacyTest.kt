package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.EpharmacyData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.GetPrescriptionIdsResponse
import io.mockk.coEvery
import io.mockk.coVerify
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
}
