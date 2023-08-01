package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.purchase_platform.common.feature.bometadata.BoEligibility
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Before
import org.junit.Test

class GenerateClearBoParamTest {

    private lateinit var cartFirstOrderList: MutableList<CartItemHolderData>
    private lateinit var cartSecondOrderList: MutableList<CartItemHolderData>

    @Before
    fun setup() {
        cartFirstOrderList = PromoRequestMapperTestHelper.getFirstCartOrder()
        cartSecondOrderList = PromoRequestMapperTestHelper.getSecondCartOrder()
    }

    @Test
    fun `WHEN promoData is null should generate correct params`() {
        // GIVEN
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val secondGroupProductUiModelList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "333333-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "3",
                    poDuration = "0"
                ),
                productId = "4",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "444444-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "4",
                    poDuration = "0"
                ),
                productId = "5",
                quantity = 5,
                bundleId = "0"
            )
        )
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                promoCodes = listOf("CODE-A"),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 1,
                    boEligibilities = listOf(
                        BoEligibility(
                            key = "is_bo_reg",
                            value = "true"
                        ),
                        BoEligibility(
                            key = "bo_type",
                            value = "1"
                        ),
                        BoEligibility(
                            key = "campaign_ids",
                            value = "213,198,212"
                        )
                    )
                ),
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                isPo = false,
                boCode = "",
                productUiModelList = productUiModelList
            ),
            CartGroupHolderData(
                promoCodes = listOf("CODE-B"),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 1,
                    boEligibilities = listOf(
                        BoEligibility(
                            key = "is_bo_reg",
                            value = "true"
                        ),
                        BoEligibility(
                            key = "bo_type",
                            value = "1"
                        ),
                        BoEligibility(
                            key = "campaign_ids",
                            value = "213,198,212"
                        )
                    )
                ),
                cartString = "_-0-9466960-169751270-KEY_OWOC",
                isPo = false,
                boCode = "",
                productUiModelList = secondGroupProductUiModelList
            )
        )

        // WHEN
        val clearPromoOrderData = PromoRequestMapper.generateClearBoParam(
            promoData = null,
            availableCartGroupHolderDataList = groupShopList
        )

        // THEN
        assertNull(clearPromoOrderData)
    }

    @Test
    fun `WHEN promoData is lastApplyPromo should generate correct params`() {
        // GIVEN
        val promoData = LastApplyPromo(
            lastApplyPromoData = LastApplyPromoData(
                listVoucherOrders = listOf(
                    VoucherOrders(
                        uniqueId = "111111-KEY",
                        code = "TESTBO",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "222222-KEY",
                        code = "TESTBO",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "333333-KEY",
                        code = "TESTBO2",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        cartStringGroup = "_-0-9466960-169751270-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "444444-KEY",
                        code = "TESTBO2",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        cartStringGroup = "_-0-9466960-169751270-KEY_OWOC"
                    )
                )
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val secondGroupProductUiModelList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "333333-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "3",
                    poDuration = "0"
                ),
                productId = "4",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "444444-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "4",
                    poDuration = "0"
                ),
                productId = "5",
                quantity = 5,
                bundleId = "0"
            )
        )
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                promoCodes = listOf("CODE-A"),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 1,
                    boEligibilities = listOf(
                        BoEligibility(
                            key = "is_bo_reg",
                            value = "true"
                        ),
                        BoEligibility(
                            key = "bo_type",
                            value = "1"
                        ),
                        BoEligibility(
                            key = "campaign_ids",
                            value = "213,198,212"
                        )
                    )
                ),
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                isPo = false,
                boCode = "",
                productUiModelList = productUiModelList
            ),
            CartGroupHolderData(
                promoCodes = listOf("CODE-B"),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 1,
                    boEligibilities = listOf(
                        BoEligibility(
                            key = "is_bo_reg",
                            value = "true"
                        ),
                        BoEligibility(
                            key = "bo_type",
                            value = "1"
                        ),
                        BoEligibility(
                            key = "campaign_ids",
                            value = "213,198,212"
                        )
                    )
                ),
                cartString = "_-0-9466960-169751270-KEY_OWOC",
                isPo = false,
                boCode = "",
                productUiModelList = secondGroupProductUiModelList
            )
        )

        // WHEN
        val clearPromoOrderData = PromoRequestMapper.generateClearBoParam(
            promoData = promoData,
            availableCartGroupHolderDataList = groupShopList
        )

        // THEN
        assertEquals(
            ClearPromoOrderData(
                orders = listOf(
                    ClearPromoOrder(
                        uniqueId = "222222-KEY",
                        boType = 1,
                        shopId = 2,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        codes = mutableListOf()
                    ),
                    ClearPromoOrder(
                        uniqueId = "444444-KEY",
                        boType = 1,
                        shopId = 4,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751270-KEY_OWOC",
                        codes = mutableListOf()
                    ),
                    ClearPromoOrder(
                        uniqueId = "111111-KEY",
                        boType = 1,
                        shopId = 1,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        codes = mutableListOf("TESTBO")
                    ),
                    ClearPromoOrder(
                        uniqueId = "333333-KEY",
                        boType = 1,
                        shopId = 3,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751270-KEY_OWOC",
                        codes = mutableListOf("TESTBO2")
                    )
                )
            ),
            clearPromoOrderData
        )
    }

    @Test
    fun `WHEN promoData is lastApplyPromo with unselected item should generate correct params`() {
        // GIVEN
        val promoData = LastApplyPromo(
            lastApplyPromoData = LastApplyPromoData(
                listVoucherOrders = listOf(
                    VoucherOrders(
                        uniqueId = "111111-KEY",
                        code = "TESTBO",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "222222-KEY",
                        code = "TESTBO",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "444444-KEY",
                        code = "TESTBO2",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        cartStringGroup = "_-0-9466960-169751270-KEY_OWOC"
                    )
                )
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val secondGroupProductUiModelList = mutableListOf(
            CartItemHolderData(
                isSelected = false,
                cartStringOrder = "333333-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "3",
                    poDuration = "0"
                ),
                productId = "4",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "444444-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "4",
                    poDuration = "0"
                ),
                productId = "5",
                quantity = 5,
                bundleId = "0"
            )
        )
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                promoCodes = listOf("CODE-A"),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 1,
                    boEligibilities = listOf(
                        BoEligibility(
                            key = "is_bo_reg",
                            value = "true"
                        ),
                        BoEligibility(
                            key = "bo_type",
                            value = "1"
                        ),
                        BoEligibility(
                            key = "campaign_ids",
                            value = "213,198,212"
                        )
                    )
                ),
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                isPo = false,
                boCode = "",
                productUiModelList = productUiModelList
            ),
            CartGroupHolderData(
                promoCodes = listOf("CODE-B"),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 1,
                    boEligibilities = listOf(
                        BoEligibility(
                            key = "is_bo_reg",
                            value = "true"
                        ),
                        BoEligibility(
                            key = "bo_type",
                            value = "1"
                        ),
                        BoEligibility(
                            key = "campaign_ids",
                            value = "213,198,212"
                        )
                    )
                ),
                cartString = "_-0-9466960-169751270-KEY_OWOC",
                isPo = false,
                boCode = "",
                productUiModelList = secondGroupProductUiModelList
            )
        )

        // WHEN
        val clearPromoOrderData = PromoRequestMapper.generateClearBoParam(
            promoData = promoData,
            availableCartGroupHolderDataList = groupShopList
        )

        // THEN
        assertEquals(
            ClearPromoOrderData(
                orders = listOf(
                    ClearPromoOrder(
                        uniqueId = "222222-KEY",
                        boType = 1,
                        shopId = 2,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        codes = mutableListOf()
                    ),
                    ClearPromoOrder(
                        uniqueId = "444444-KEY",
                        boType = 1,
                        shopId = 4,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751270-KEY_OWOC",
                        codes = mutableListOf("TESTBO2")
                    ),
                    ClearPromoOrder(
                        uniqueId = "111111-KEY",
                        boType = 1,
                        shopId = 1,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        codes = mutableListOf("TESTBO")
                    )
                )
            ),
            clearPromoOrderData
        )
    }

    @Test
    fun `WHEN promoData is promoUiModel should generate correct params`() {
        // GIVEN
        val promoData = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "111111-KEY",
                    code = "TESTBO",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "222222-KEY",
                    code = "TESTBO",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "444444-KEY",
                    code = "TESTBO2",
                    shippingId = 3,
                    spId = 4,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751270-KEY_OWOC"
                )
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val secondGroupProductUiModelList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "333333-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "3",
                    poDuration = "0"
                ),
                productId = "4",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "444444-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "4",
                    poDuration = "0"
                ),
                productId = "5",
                quantity = 5,
                bundleId = "0"
            )
        )
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                promoCodes = listOf(),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 1,
                    boEligibilities = listOf(
                        BoEligibility(
                            key = "is_bo_reg",
                            value = "true"
                        ),
                        BoEligibility(
                            key = "bo_type",
                            value = "1"
                        ),
                        BoEligibility(
                            key = "campaign_ids",
                            value = "213,198,212"
                        )
                    )
                ),
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                isPo = false,
                boCode = "",
                productUiModelList = productUiModelList
            ),
            CartGroupHolderData(
                promoCodes = listOf(),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 1,
                    boEligibilities = listOf(
                        BoEligibility(
                            key = "is_bo_reg",
                            value = "true"
                        ),
                        BoEligibility(
                            key = "bo_type",
                            value = "1"
                        ),
                        BoEligibility(
                            key = "campaign_ids",
                            value = "213,198,212"
                        )
                    )
                ),
                cartString = "_-0-9466960-169751270-KEY_OWOC",
                isPo = false,
                boCode = "",
                productUiModelList = secondGroupProductUiModelList
            )
        )

        // WHEN
        val clearPromoOrderData = PromoRequestMapper.generateClearBoParam(
            promoData = promoData,
            availableCartGroupHolderDataList = groupShopList
        )

        // THEN
        assertEquals(
            ClearPromoOrderData(
                orders = listOf(
                    ClearPromoOrder(
                        uniqueId = "222222-KEY",
                        boType = 1,
                        shopId = 2,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        codes = mutableListOf()
                    ),
                    ClearPromoOrder(
                        uniqueId = "444444-KEY",
                        boType = 1,
                        shopId = 4,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751270-KEY_OWOC",
                        codes = mutableListOf("TESTBO2")
                    ),
                    ClearPromoOrder(
                        uniqueId = "111111-KEY",
                        boType = 1,
                        shopId = 1,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        codes = mutableListOf("TESTBO")
                    ),
                    ClearPromoOrder(
                        uniqueId = "333333-KEY",
                        boType = 1,
                        shopId = 3,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751270-KEY_OWOC",
                        codes = mutableListOf()
                    )
                )
            ),
            clearPromoOrderData
        )
    }

    @Test
    fun `WHEN promoData is promoUiModel with unselected item should generate correct params`() {
        // GIVEN
        val promoData = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "111111-KEY",
                    code = "TESTBO",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "222222-KEY",
                    code = "TESTBO",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "444444-KEY",
                    code = "TESTBO2",
                    shippingId = 3,
                    spId = 4,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751270-KEY_OWOC"
                )
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val secondGroupProductUiModelList = mutableListOf(
            CartItemHolderData(
                isSelected = false,
                cartStringOrder = "333333-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "3",
                    poDuration = "0"
                ),
                productId = "4",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "444444-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "4",
                    poDuration = "0"
                ),
                productId = "5",
                quantity = 5,
                bundleId = "0"
            )
        )
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                promoCodes = listOf(),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 1,
                    boEligibilities = listOf(
                        BoEligibility(
                            key = "is_bo_reg",
                            value = "true"
                        ),
                        BoEligibility(
                            key = "bo_type",
                            value = "1"
                        ),
                        BoEligibility(
                            key = "campaign_ids",
                            value = "213,198,212"
                        )
                    )
                ),
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                isPo = false,
                boCode = "",
                productUiModelList = productUiModelList
            ),
            CartGroupHolderData(
                promoCodes = listOf(),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 1,
                    boEligibilities = listOf(
                        BoEligibility(
                            key = "is_bo_reg",
                            value = "true"
                        ),
                        BoEligibility(
                            key = "bo_type",
                            value = "1"
                        ),
                        BoEligibility(
                            key = "campaign_ids",
                            value = "213,198,212"
                        )
                    )
                ),
                cartString = "_-0-9466960-169751270-KEY_OWOC",
                isPo = false,
                boCode = "",
                productUiModelList = secondGroupProductUiModelList
            )
        )

        // WHEN
        val clearPromoOrderData = PromoRequestMapper.generateClearBoParam(
            promoData = promoData,
            availableCartGroupHolderDataList = groupShopList
        )

        // THEN
        assertEquals(
            ClearPromoOrderData(
                orders = listOf(
                    ClearPromoOrder(
                        uniqueId = "222222-KEY",
                        boType = 1,
                        shopId = 2,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        codes = mutableListOf()
                    ),
                    ClearPromoOrder(
                        uniqueId = "444444-KEY",
                        boType = 1,
                        shopId = 4,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751270-KEY_OWOC",
                        codes = mutableListOf("TESTBO2")
                    ),
                    ClearPromoOrder(
                        uniqueId = "111111-KEY",
                        boType = 1,
                        shopId = 1,
                        warehouseId = 0,
                        isPo = false,
                        poDuration = "0",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        codes = mutableListOf("TESTBO")
                    )
                )
            ),
            clearPromoOrderData
        )
    }
}
