package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.CrossSellOrderSummaryModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckoutViewModelPaymentAddOnsTest : BaseCheckoutViewModelTest() {

    @Test
    fun generate_all_payment_add_ons_analytic() {
        // given
        val eGoldData = CheckoutEgoldModel(
            egoldAttributeModel = EgoldAttributeModel(
                isEligible = true,
                isChecked = true
            )
        )
        val crossSellData = CheckoutCrossSellModel(
            crossSellModel = CrossSellModel(
                orderSummary = CrossSellOrderSummaryModel(title = "Pulsa"),
                id = "50"
            ),
            isChecked = true
        )
        val donationData = CheckoutDonationModel(donation = Donation(isChecked = true))
        viewModel.listData.value = listOf(
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    eGoldData,
                    crossSellData,
                    donationData
                )
            ),
            CheckoutButtonPaymentModel()
        )

        // when
        val data = viewModel.generatePaymentLevelAddOnsAnalyticData()

        // then
        val eGoldPairResult = data.getOrNull(0)
        val crossSellPairResult = data.getOrNull(1)
        val donationPairResult = data.getOrNull(2)

        assertNotNull(eGoldPairResult)
        assertNotNull(crossSellPairResult)
        assertNotNull(donationPairResult)

        assertEquals(eGoldData.getCategoryName(), eGoldPairResult!!.first)
        assertEquals(eGoldData.getCrossSellProductId(), eGoldPairResult.second)

        assertEquals(crossSellData.getCategoryName(), crossSellPairResult!!.first)
        assertEquals(crossSellData.getCrossSellProductId(), crossSellPairResult.second)

        assertEquals(donationData.getCategoryName(), donationPairResult!!.first)
        assertEquals(donationData.getCrossSellProductId(), donationPairResult.second)
    }

    @Test
    fun generate_eGold_donation_payment_add_ons_analytic() {
        // given
        val eGoldData = CheckoutEgoldModel(
            egoldAttributeModel = EgoldAttributeModel(
                isEligible = true,
                isChecked = true
            )
        )
        val donationData = CheckoutDonationModel(donation = Donation(isChecked = true))
        viewModel.listData.value = listOf(
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    eGoldData,
                    donationData
                )
            ),
            CheckoutButtonPaymentModel()
        )

        // when
        val data = viewModel.generatePaymentLevelAddOnsAnalyticData()

        // then
        val eGoldPairResult = data.getOrNull(0)
        val donationPairResult = data.getOrNull(1)

        assertNotNull(eGoldPairResult)
        assertNotNull(donationPairResult)

        assertEquals(eGoldData.getCategoryName(), eGoldPairResult!!.first)
        assertEquals(eGoldData.getCrossSellProductId(), eGoldPairResult.second)

        assertEquals(donationData.getCategoryName(), donationPairResult!!.first)
        assertEquals(donationData.getCrossSellProductId(), donationPairResult.second)
    }

    @Test
    fun generate_eGold_crossSell_payment_add_ons_analytic() {
        val eGoldData = CheckoutEgoldModel(
            egoldAttributeModel = EgoldAttributeModel(
                isEligible = true,
                isChecked = true
            )
        )
        val crossSellData = CheckoutCrossSellModel(
            crossSellModel = CrossSellModel(
                orderSummary = CrossSellOrderSummaryModel(title = "Pulsa"),
                id = "50"
            ),
            isChecked = true
        )
        viewModel.listData.value = listOf(
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    eGoldData,
                    crossSellData
                )
            ),
            CheckoutButtonPaymentModel()
        )

        // when
        val data = viewModel.generatePaymentLevelAddOnsAnalyticData()

        // then
        val eGoldPairResult = data.getOrNull(0)
        val crossSellPairResult = data.getOrNull(1)

        assertNotNull(eGoldPairResult)
        assertNotNull(crossSellPairResult)

        assertEquals(eGoldData.getCategoryName(), eGoldPairResult!!.first)
        assertEquals(eGoldData.getCrossSellProductId(), eGoldPairResult.second)

        assertEquals(crossSellData.getCategoryName(), crossSellPairResult!!.first)
        assertEquals(crossSellData.getCrossSellProductId(), crossSellPairResult.second)
    }

    @Test
    fun generate_crossSell_donation_payment_add_ons_analytic() {
        // given
        val crossSellData = CheckoutCrossSellModel(
            crossSellModel = CrossSellModel(
                orderSummary = CrossSellOrderSummaryModel(title = "Pulsa"),
                id = "50"
            ),
            isChecked = true
        )
        val donationData = CheckoutDonationModel(donation = Donation(isChecked = true))
        viewModel.listData.value = listOf(
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    crossSellData,
                    donationData
                )
            ),
            CheckoutButtonPaymentModel()
        )

        // when
        val data = viewModel.generatePaymentLevelAddOnsAnalyticData()

        // then
        val crossSellPairResult = data.getOrNull(0)
        val donationPairResult = data.getOrNull(1)

        assertNotNull(crossSellPairResult)
        assertNotNull(donationPairResult)

        assertEquals(crossSellData.getCategoryName(), crossSellPairResult!!.first)
        assertEquals(crossSellData.getCrossSellProductId(), crossSellPairResult.second)

        assertEquals(donationData.getCategoryName(), donationPairResult!!.first)
        assertEquals(donationData.getCrossSellProductId(), donationPairResult.second)
    }

    @Test
    fun generate_eGold_payment_add_ons_not_checked() {
        // given
        val eGoldData = CheckoutEgoldModel(
            egoldAttributeModel = EgoldAttributeModel(
                isEligible = true,
                isChecked = false
            )
        )
        viewModel.listData.value = listOf(
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    eGoldData
                )
            )
        )

        // when
        val data = viewModel.generatePaymentLevelAddOnsAnalyticData()

        // then
        assertTrue(data.isEmpty())
    }

    @Test
    fun generate_eGold_payment_add_ons_not_eligible() {
        // given
        val eGoldData = CheckoutEgoldModel(
            egoldAttributeModel = EgoldAttributeModel(
                isEligible = false,
                isChecked = false
            )
        )
        viewModel.listData.value = listOf(
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    eGoldData
                )
            )
        )

        // when
        val data = viewModel.generatePaymentLevelAddOnsAnalyticData()

        // then
        assertTrue(data.isEmpty())
    }

    @Test
    fun generate_donation_payment_add_ons_analytic_not_checked() {
        // given
        val donationData = CheckoutDonationModel(donation = Donation(isChecked = false))
        viewModel.listData.value = listOf(
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(donationData)
            )
        )

        // when
        val data = viewModel.generatePaymentLevelAddOnsAnalyticData()

        // then
        assertTrue(data.isEmpty())
    }

    @Test
    fun generate_crossSell_payment_add_ons_analytic_empty() {
        // given
        viewModel.listData.value = listOf(
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutCrossSellGroupModel(
                crossSellList = emptyList()
            )
        )

        // when
        val data = viewModel.generatePaymentLevelAddOnsAnalyticData()

        // then
        assertTrue(data.isEmpty())
    }
}
