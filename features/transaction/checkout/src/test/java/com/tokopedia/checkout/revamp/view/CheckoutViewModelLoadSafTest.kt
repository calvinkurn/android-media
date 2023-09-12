package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData.Companion.ERROR_CODE_TO_OPEN_ADDRESS_LIST
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData.Companion.ERROR_CODE_TO_OPEN_ADD_NEW_ADDRESS
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import io.mockk.coEvery
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

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(CheckoutPageState.NoAddress(response), viewModel.pageState.value)
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
            CheckoutTickerModel(
                ticker = TickerAnnouncementHolderData(
                    tickerData.id,
                    tickerData.title,
                    tickerData.message
                )
            ),
            viewModel.listData.value[1]
        )
        verify {
            mTrackerShipment.eventViewInformationAndWarningTickerInCheckout(tickerData.id)
        }
    }

    @Test
    fun load_SAF_with_cross_sell_auto_checked() {
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
            ),
            crossSell = listOf(
                CrossSellModel(
                    isChecked = true,
                    checkboxDisabled = false
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
            CheckoutCrossSellModel(
                crossSellModel = CrossSellModel(isChecked = true),
                isChecked = true,
                isEnabled = true,
                index = 0
            ),
            viewModel.listData.value.crossSellGroup()!!.crossSellList[0]
        )
        verify {
            mTrackerShipment.eventViewAutoCheckCrossSell(any(), "0", any(), any(), any())
        }
    }

    @Test
    fun load_SAF_with_cross_sell_no_auto_checked() {
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
            ),
            crossSell = listOf(
                CrossSellModel(
                    isChecked = false,
                    checkboxDisabled = false
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
            CheckoutCrossSellModel(
                crossSellModel = CrossSellModel(isChecked = false),
                isChecked = false,
                isEnabled = true,
                index = 0
            ),
            viewModel.listData.value.crossSellGroup()!!.crossSellList[0]
        )
        verify(inverse = true) {
            mTrackerShipment.eventViewAutoCheckCrossSell(any(), "0", any(), any(), any())
        }
    }

    @Test
    fun load_SAF_with_cross_sell_disabled() {
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
            ),
            crossSell = listOf(
                CrossSellModel(
                    isChecked = false,
                    checkboxDisabled = true
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
            0,
            viewModel.listData.value.crossSellGroup()!!.crossSellList.size
        )
        verify(inverse = true) {
            mTrackerShipment.eventViewAutoCheckCrossSell(any(), "0", any(), any(), any())
        }
    }

    @Test
    fun load_SAF_with_egold() {
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
            ),
            egoldAttributes = EgoldAttributeModel(
                isEligible = true,
                isEnabled = true
            )
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(
            CheckoutEgoldModel(EgoldAttributeModel(isEligible = true, isEnabled = true)),
            viewModel.listData.value.crossSellGroup()!!.crossSellList[0]
        )
    }

    @Test
    fun load_SAF_with_donation() {
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
            ),
            donation = Donation(
                title = "donasi",
                nominal = 1
            )
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(
            CheckoutDonationModel(Donation(title = "donasi", nominal = 1)),
            viewModel.listData.value.crossSellGroup()!!.crossSellList[0]
        )
        verify(inverse = true) {
            mTrackerShipment.eventViewAutoCheckDonation(any())
        }
    }

    @Test
    fun load_SAF_with_donation_auto_checked() {
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
            ),
            donation = Donation(
                title = "donasi",
                nominal = 1,
                isChecked = true
            )
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response

        // when
        viewModel.loadSAF(false, false, false)

        // then
        assertEquals(
            CheckoutDonationModel(Donation(title = "donasi", nominal = 1, isChecked = true), isChecked = true),
            viewModel.listData.value.crossSellGroup()!!.crossSellList[0]
        )
        verify {
            mTrackerShipment.eventViewAutoCheckDonation(any())
        }
    }
}
