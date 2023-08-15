package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData.Companion.ERROR_CODE_TO_OPEN_ADDRESS_LIST
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData.Companion.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class CheckoutViewModelLoadSafTest : BaseCheckoutViewModelTest() {

    @Test
    fun load_SAF_empty_data() {
        // given
        val response = CartShipmentAddressFormData()
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(CheckoutPageState.EmptyData, viewModel.pageState.value)
    }

    @Test
    fun load_SAF_cache_expired() {
        // given
        val response = CartShipmentAddressFormData(
            isError = true,
            isOpenPrerequisiteSite = true,
            errorMessage = "error Message"
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(
            CheckoutPageState.CacheExpired(response.errorMessage),
            viewModel.pageState.value
        )
    }

    @Test
    fun load_SAF_no_address() {
        // given
        val response = CartShipmentAddressFormData(
            isError = false,
            errorCode = ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        every {
            eligibleForAddressUseCase.get().eligibleForAddressFeature(any(), any(), any())
        } answers {
            (firstArg() as (KeroAddrIsEligibleForAddressFeatureData) -> Unit).invoke(
                KeroAddrIsEligibleForAddressFeatureData(
                    eligibleForRevampAna = EligibleForAddressFeature(
                        true
                    )
                )
            )
        }

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(CheckoutPageState.NoAddress(response, true), viewModel.pageState.value)
    }

    @Test
    fun load_SAF_no_match_address() {
        // given
        val response = CartShipmentAddressFormData(
            isError = false,
            errorCode = ERROR_CODE_TO_OPEN_ADDRESS_LIST,
            groupAddress = listOf(
                GroupAddress(
                    userAddress = UserAddress(
                        state = 123
                    )
                )
            )
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(CheckoutPageState.NoMatchedAddress(123), viewModel.pageState.value)
    }

    @Test
    fun load_SAF_no_match_address_with_null_group_address() {
        // given
        val response = CartShipmentAddressFormData(
            isError = false,
            errorCode = ERROR_CODE_TO_OPEN_ADDRESS_LIST,
            groupAddress = emptyList()
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(CheckoutPageState.NoMatchedAddress(0), viewModel.pageState.value)
    }

    @Test
    fun load_SAF_failed() {
        // given
        val exception = IOException()
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } throws exception

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(CheckoutPageState.Error(exception), viewModel.pageState.value)
    }

    @Test
    fun load_SAF_error() {
        // given
        val response = CartShipmentAddressFormData(
            isError = true,
            isOpenPrerequisiteSite = false,
            errorMessage = "error message"
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(
            CheckoutPageState.Error::class,
            viewModel.pageState.value::class
        )
        assertEquals(
            response.errorMessage,
            (viewModel.pageState.value as CheckoutPageState.Error).throwable.message
        )
    }

    @Test
    fun load_SAF_success() {
        // given
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
            )
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(
            CheckoutPageState.Success(response),
            viewModel.pageState.value
        )
    }

    @Test
    fun load_SAF_with_ticker() {
        // given
        val tickerData = TickerData(
            id = "id",
            message = "message",
            page = "checkout",
            title = "title"
        )
        val response = CartShipmentAddressFormData(
            isError = false,
            tickerData = tickerData,
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
            )
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData(tickerData.id, tickerData.title, tickerData.message)),
            viewModel.listData.value[1]
        )
        verify {
            mTrackerShipment.eventViewInformationAndWarningTickerInCheckout(tickerData.id)
        }
    }
}
