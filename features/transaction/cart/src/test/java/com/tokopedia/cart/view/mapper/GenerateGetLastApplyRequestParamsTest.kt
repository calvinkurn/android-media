package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.feature.bometadata.BoEligibility
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

class GenerateGetLastApplyRequestParamsTest {

    @Before
    fun setup() {

    }

    @Test
    fun `WHEN promoData is lastApplyPromo and no BO should generate correct params`() {
        // GIVEN
        val promoData = LastApplyPromo(
            lastApplyPromoData = LastApplyPromoData(
                listVoucherOrders = listOf(
                    VoucherOrders(
                        uniqueId = "111111-KEY",
                        code = "",
                        shippingId = 1,
                        spId = 2,
                        type = "",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "222222-KEY",
                        code = "",
                        shippingId = 3,
                        spId = 4,
                        type = "",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    )
                )
            )
        )
        val cartFirstOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "1",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "2",
                quantity = 5,
                bundleId = "0"
            )
        )
        val cartSecondOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "222222-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "2",
                    poDuration = "0"
                ),
                productId = "3",
                quantity = 5,
                bundleId = "0"
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                promoCodes = listOf(),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 0,
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
                isPo = false,
                boCode = "",
                productUiModelList = productUiModelList
            )
        )

        // WHEN
        val getLastApplyPromoRequest = PromoRequestMapper.generateGetLastApplyRequestParams(
            promoData = promoData,
            selectedCartGroupHolderDataList = groupShopList,
            null
        )

        // THEN
        assertEquals(
            ValidateUsePromoRequest(
                codes = mutableListOf(),
                state = CartConstant.PARAM_CART,
                skipApply = 0,
                cartType = CartConstant.PARAM_DEFAULT,
                orders = listOf(
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartSecondOrderList
                        ),
                        codes = mutableListOf(),
                        uniqueId = "222222-KEY",
                        shippingId = 0,
                        spId = 0,
                        boCampaignId = 0,
                        shippingSubsidy = 0,
                        benefitClass = "",
                        shippingPrice = 0.0,
                        etaText = "",
                        shippingMetadata = "",
                        shopId = 2
                    ),
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartFirstOrderList
                        ),
                        codes = mutableListOf(),
                        uniqueId = "111111-KEY",
                        shippingId = 0,
                        spId = 0,
                        boCampaignId = 0,
                        shippingSubsidy = 0,
                        benefitClass = "",
                        shippingPrice = 0.0,
                        etaText = "",
                        shippingMetadata = "",
                        shopId = 1
                    )
                )
            ), getLastApplyPromoRequest
        )
    }

    @Test
    fun `WHEN promoData is lastApplyPromo and with BO should generate correct params`() {
        // GIVEN
        val promoData = LastApplyPromo(
            lastApplyPromoData = LastApplyPromoData(
                listVoucherOrders = listOf(
                    VoucherOrders(
                        uniqueId = "111111-KEY",
                        code = "TESTCODE",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "222222-KEY",
                        code = "TESTCODE",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    )
                )
            )
        )
        val cartFirstOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "1",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "2",
                quantity = 5,
                bundleId = "0"
            )
        )
        val cartSecondOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "222222-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "2",
                    poDuration = "0"
                ),
                productId = "3",
                quantity = 5,
                bundleId = "0"
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                promoCodes = listOf(),
                warehouseId = 22712,
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
                isPo = false,
                boCode = "TESTCODE",
                productUiModelList = productUiModelList
            )
        )

        // WHEN
        val getLastApplyPromoRequest = PromoRequestMapper.generateGetLastApplyRequestParams(
            promoData = promoData,
            selectedCartGroupHolderDataList = groupShopList,
            null
        )

        // THEN
        assertEquals(
            ValidateUsePromoRequest(
                codes = mutableListOf(),
                state = CartConstant.PARAM_CART,
                skipApply = 0,
                cartType = CartConstant.PARAM_DEFAULT,
                orders = listOf(
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartSecondOrderList
                        ),
                        codes = mutableListOf("TESTCODE"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartFirstOrderList
                        ),
                        codes = mutableListOf(),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    )
                )
            ), getLastApplyPromoRequest
        )
    }

    @Test
    fun `WHEN promoData is lastApplyPromo and with BO but some group order has been removed should generate correct params`() {
        // GIVEN
        val promoData = LastApplyPromo(
            lastApplyPromoData = LastApplyPromoData(
                listVoucherOrders = listOf(
                    VoucherOrders(
                        uniqueId = "000000-KEY",
                        code = "TESTCODE",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "111111-KEY",
                        code = "TESTCODE",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "222222-KEY",
                        code = "TESTCODE",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    )
                )
            )
        )
        val cartFirstOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "1",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "2",
                quantity = 5,
                bundleId = "0"
            )
        )
        val cartSecondOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "222222-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "2",
                    poDuration = "0"
                ),
                productId = "3",
                quantity = 5,
                bundleId = "0"
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                promoCodes = listOf(),
                warehouseId = 22712,
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
                isPo = false,
                boCode = "TESTCODE",
                boUniqueId = "000000-KEY",
                productUiModelList = productUiModelList
            )
        )

        // WHEN
        val getLastApplyPromoRequest = PromoRequestMapper.generateGetLastApplyRequestParams(
            promoData = promoData,
            selectedCartGroupHolderDataList = groupShopList,
            null
        )

        // THEN
        assertEquals(
            ValidateUsePromoRequest(
                codes = mutableListOf(),
                state = CartConstant.PARAM_CART,
                skipApply = 0,
                cartType = CartConstant.PARAM_DEFAULT,
                orders = listOf(
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartSecondOrderList
                        ),
                        codes = mutableListOf("TESTCODE"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartFirstOrderList
                        ),
                        codes = mutableListOf(),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                )
            ), getLastApplyPromoRequest
        )
    }

    @Test
    fun `WHEN promoData is lastApplyPromo and with BO and MVC should generate correct params`() {
        // GIVEN
        val promoData = LastApplyPromo(
            lastApplyPromoData = LastApplyPromoData(
                listVoucherOrders = listOf(
                    VoucherOrders(
                        uniqueId = "111111-KEY",
                        code = "TESTCODE",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "111111-KEY",
                        code = "TESTMVC",
                        shippingId = 1,
                        spId = 2,
                        type = "",
                        boCampaignId = "0",
                        shippingSubsidy = 0,
                        benefitClass = "",
                        shippingPrice = 0.0,
                        etaText = "",
                        shippingMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "222222-KEY",
                        code = "TESTCODE",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "222222-KEY",
                        code = "TESTMVC2",
                        shippingId = 1,
                        spId = 2,
                        type = "",
                        boCampaignId = "0",
                        shippingSubsidy = 0,
                        benefitClass = "",
                        shippingPrice = 0.0,
                        etaText = "",
                        shippingMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                )
            )
        )
        val cartFirstOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "1",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "2",
                quantity = 5,
                bundleId = "0"
            )
        )
        val cartSecondOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "222222-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "2",
                    poDuration = "0"
                ),
                productId = "3",
                quantity = 5,
                bundleId = "0"
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                promoCodes = listOf(),
                warehouseId = 22712,
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
                isPo = false,
                boCode = "TESTCODE",
                productUiModelList = productUiModelList
            )
        )

        // WHEN
        val getLastApplyPromoRequest = PromoRequestMapper.generateGetLastApplyRequestParams(
            promoData = promoData,
            selectedCartGroupHolderDataList = groupShopList,
            null
        )

        // THEN
        assertEquals(
            ValidateUsePromoRequest(
                codes = mutableListOf(),
                state = CartConstant.PARAM_CART,
                skipApply = 0,
                cartType = CartConstant.PARAM_DEFAULT,
                orders = listOf(
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartSecondOrderList
                        ),
                        codes = mutableListOf("TESTCODE", "TESTMVC2"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartFirstOrderList
                        ),
                        codes = mutableListOf("TESTMVC"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    )
                )
            ), getLastApplyPromoRequest
        )
    }

    @Test
    fun `WHEN promoData is lastApplyPromo and with BO and MVC but some group order has been removed should generate correct params`() {
        // GIVEN
        val promoData = LastApplyPromo(
            lastApplyPromoData = LastApplyPromoData(
                listVoucherOrders = listOf(
                    VoucherOrders(
                        uniqueId = "000000-KEY",
                        code = "TESTCODE",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "000000-KEY",
                        code = "TESTMVC",
                        shippingId = 1,
                        spId = 2,
                        type = "",
                        boCampaignId = "0",
                        shippingSubsidy = 0,
                        benefitClass = "",
                        shippingPrice = 0.0,
                        etaText = "",
                        shippingMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "111111-KEY",
                        code = "TESTCODE",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "111111-KEY",
                        code = "TESTMVC2",
                        shippingId = 0,
                        spId = 0,
                        type = "",
                        boCampaignId = "0",
                        shippingSubsidy = 0,
                        benefitClass = "",
                        shippingPrice = 0.0,
                        etaText = "",
                        shippingMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    VoucherOrders(
                        uniqueId = "222222-KEY",
                        code = "TESTCODE",
                        shippingId = 1,
                        spId = 2,
                        type = "logistic",
                        boCampaignId = "10",
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    )
                )
            )
        )
        val cartFirstOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "1",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "2",
                quantity = 5,
                bundleId = "0"
            )
        )
        val cartSecondOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "222222-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "2",
                    poDuration = "0"
                ),
                productId = "3",
                quantity = 5,
                bundleId = "0"
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                promoCodes = listOf(),
                warehouseId = 22712,
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
                isPo = false,
                boCode = "TESTCODE",
                boUniqueId = "000000-KEY",
                productUiModelList = productUiModelList
            )
        )

        // WHEN
        val getLastApplyPromoRequest = PromoRequestMapper.generateGetLastApplyRequestParams(
            promoData = promoData,
            selectedCartGroupHolderDataList = groupShopList,
            null
        )

        // THEN
        assertEquals(
            ValidateUsePromoRequest(
                codes = mutableListOf(),
                state = CartConstant.PARAM_CART,
                skipApply = 0,
                cartType = CartConstant.PARAM_DEFAULT,
                orders = listOf(
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartSecondOrderList
                        ),
                        codes = mutableListOf("TESTCODE"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartFirstOrderList
                        ),
                        codes = mutableListOf("TESTMVC2"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                )
            ), getLastApplyPromoRequest
        )
    }

    @Test
    fun `WHEN promoData is promoUiModel and no BO should generate correct params`() {
        // GIVEN
        val promoData = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "111111-KEY",
                    code = "",
                    shippingId = 1,
                    spId = 2,
                    type = "",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "222222-KEY",
                    code = "",
                    shippingId = 3,
                    spId = 4,
                    type = "",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                )
            )
        )
        val lastValidateUseRequestData = ValidateUsePromoRequest(
            orders = listOf(
                OrdersItem(
                    boCampaignId = 0,
                    shippingSubsidy = 0,
                    benefitClass = "",
                    shippingPrice = 0.0,
                    etaText = "",
                    shippingMetadata = "",
                    uniqueId = "111111-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                OrdersItem(
                    boCampaignId = 0,
                    shippingSubsidy = 0,
                    benefitClass = "",
                    shippingPrice = 0.0,
                    etaText = "",
                    shippingMetadata = "",
                    uniqueId = "222222-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                )
            )
        )
        val cartFirstOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "1",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "2",
                quantity = 5,
                bundleId = "0"
            )
        )
        val cartSecondOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "222222-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "2",
                    poDuration = "0"
                ),
                productId = "3",
                quantity = 5,
                bundleId = "0"
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                promoCodes = listOf(),
                warehouseId = 0,
                boMetadata = BoMetadata(
                    boType = 0,
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
                isPo = false,
                boCode = "",
                productUiModelList = productUiModelList
            )
        )

        // WHEN
        val getLastApplyPromoRequest = PromoRequestMapper.generateGetLastApplyRequestParams(
            promoData = promoData,
            selectedCartGroupHolderDataList = groupShopList,
            lastValidateUseRequestData
        )

        // THEN
        assertEquals(
            ValidateUsePromoRequest(
                codes = mutableListOf(),
                state = CartConstant.PARAM_CART,
                skipApply = 0,
                cartType = CartConstant.PARAM_DEFAULT,
                orders = listOf(
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartSecondOrderList
                        ),
                        codes = mutableListOf(),
                        uniqueId = "222222-KEY",
                        shippingId = 0,
                        spId = 0,
                        boCampaignId = 0,
                        shippingSubsidy = 0,
                        benefitClass = "",
                        shippingPrice = 0.0,
                        etaText = "",
                        shippingMetadata = "",
                        shopId = 2
                    ),
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartFirstOrderList
                        ),
                        codes = mutableListOf(),
                        uniqueId = "111111-KEY",
                        shippingId = 0,
                        spId = 0,
                        boCampaignId = 0,
                        shippingSubsidy = 0,
                        benefitClass = "",
                        shippingPrice = 0.0,
                        etaText = "",
                        shippingMetadata = "",
                        shopId = 1
                    )
                )
            ), getLastApplyPromoRequest
        )
    }

    @Test
    fun `WHEN promoData is promoUiModel and with BO should generate correct params`() {
        // GIVEN
        val promoData = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "111111-KEY",
                    code = "TESTCODE",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "222222-KEY",
                    code = "",
                    shippingId = 3,
                    spId = 4,
                    type = ""
                )
            )
        )
        val lastValidateUseRequestData = ValidateUsePromoRequest(
            orders = listOf(
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 15000.0,
                    etaText = "",
                    shippingMetadata = "",
                    codes = mutableListOf("TESTCODE"),
                    uniqueId = "111111-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                OrdersItem(
                    boCampaignId = 0,
                    shippingSubsidy = 0,
                    benefitClass = "",
                    shippingPrice = 0.0,
                    etaText = "",
                    shippingMetadata = "",
                    uniqueId = "222222-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                )
            )
        )
        val cartFirstOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "1",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "2",
                quantity = 5,
                bundleId = "0"
            )
        )
        val cartSecondOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "222222-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "2",
                    poDuration = "0"
                ),
                productId = "3",
                quantity = 5,
                bundleId = "0"
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                promoCodes = listOf(),
                warehouseId = 22712,
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
                isPo = false,
                boCode = "TESTCODE",
                productUiModelList = productUiModelList
            )
        )

        // WHEN
        val getLastApplyPromoRequest = PromoRequestMapper.generateGetLastApplyRequestParams(
            promoData = promoData,
            selectedCartGroupHolderDataList = groupShopList,
            lastValidateUseRequestData
        )

        // THEN
        assertEquals(
            ValidateUsePromoRequest(
                codes = mutableListOf(),
                state = CartConstant.PARAM_CART,
                skipApply = 0,
                cartType = CartConstant.PARAM_DEFAULT,
                orders = listOf(
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartSecondOrderList
                        ),
                        codes = mutableListOf(),
                        shippingId = 0,
                        spId = 0,
                        boCampaignId = 0,
                        shippingSubsidy = 0,
                        benefitClass = "",
                        shippingPrice = 0.0,
                        etaText = "",
                        shippingMetadata = "",
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartFirstOrderList
                        ),
                        codes = mutableListOf("TESTCODE"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "",
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    )
                )
            ), getLastApplyPromoRequest
        )
    }

    @Test
    fun `WHEN promoData is promoUiModel and with BO but some group order has been removed should generate correct params`() {
        // GIVEN
        val promoData = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "000000-KEY",
                    code = "TESTCODE",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "111111-KEY",
                    code = "TESTCODE",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "222222-KEY",
                    code = "TESTCODE",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                )
            )
        )
        val lastValidateUseRequestData = ValidateUsePromoRequest(
            orders = listOf(
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 15000.0,
                    etaText = "",
                    shippingMetadata = "{\"order_shipping_price\":15000}",
                    codes = mutableListOf("TESTCODE"),
                    uniqueId = "000000-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 15000.0,
                    etaText = "",
                    shippingMetadata = "{\"order_shipping_price\":15000}",
                    uniqueId = "111111-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 15000.0,
                    etaText = "",
                    shippingMetadata = "{\"order_shipping_price\":15000}",
                    uniqueId = "222222-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                )
            )
        )
        val cartFirstOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "1",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "2",
                quantity = 5,
                bundleId = "0"
            )
        )
        val cartSecondOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "222222-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "2",
                    poDuration = "0"
                ),
                productId = "3",
                quantity = 5,
                bundleId = "0"
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                promoCodes = listOf(),
                warehouseId = 22712,
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
                isPo = false,
                boCode = "TESTCODE",
                boUniqueId = "000000-KEY",
                productUiModelList = productUiModelList
            )
        )

        // WHEN
        val getLastApplyPromoRequest = PromoRequestMapper.generateGetLastApplyRequestParams(
            promoData = promoData,
            selectedCartGroupHolderDataList = groupShopList,
            lastValidateUseRequestData
        )

        // THEN
        assertEquals(
            ValidateUsePromoRequest(
                codes = mutableListOf(),
                state = CartConstant.PARAM_CART,
                skipApply = 0,
                cartType = CartConstant.PARAM_DEFAULT,
                orders = listOf(
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartSecondOrderList
                        ),
                        codes = mutableListOf("TESTCODE"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartFirstOrderList
                        ),
                        codes = mutableListOf(),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                )
            ), getLastApplyPromoRequest
        )
    }

    @Test
    fun `WHEN promoData is promoUiModel and with BO and MVC should generate correct params`() {
        val promoData = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "111111-KEY",
                    code = "TESTCODE",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "111111-KEY",
                    code = "TESTMVC",
                    shippingId = 1,
                    spId = 2,
                    type = "",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "222222-KEY",
                    code = "TESTCODE",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "222222-KEY",
                    code = "TESTMVC2",
                    shippingId = 1,
                    spId = 2,
                    type = "",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                )
            )
        )
        val lastValidateUseRequestData = ValidateUsePromoRequest(
            orders = listOf(
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 15000.0,
                    etaText = "",
                    shippingMetadata = "{\"order_shipping_price\":15000}",
                    codes = mutableListOf("TESTCODE", "TESTMVC"),
                    uniqueId = "111111-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 15000.0,
                    etaText = "",
                    shippingMetadata = "{\"order_shipping_price\":15000}",
                    codes = mutableListOf("TESTMVC2"),
                    uniqueId = "222222-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                )
            )
        )
        val cartFirstOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "1",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "2",
                quantity = 5,
                bundleId = "0"
            )
        )
        val cartSecondOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "222222-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "2",
                    poDuration = "0"
                ),
                productId = "3",
                quantity = 5,
                bundleId = "0"
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                promoCodes = listOf(),
                warehouseId = 22712,
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
                isPo = false,
                boCode = "TESTCODE",
                productUiModelList = productUiModelList
            )
        )

        // WHEN
        val getLastApplyPromoRequest = PromoRequestMapper.generateGetLastApplyRequestParams(
            promoData = promoData,
            selectedCartGroupHolderDataList = groupShopList,
            lastValidateUseRequestData
        )

        // THEN
        assertEquals(
            ValidateUsePromoRequest(
                codes = mutableListOf(),
                state = CartConstant.PARAM_CART,
                skipApply = 0,
                cartType = CartConstant.PARAM_DEFAULT,
                orders = listOf(
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartSecondOrderList
                        ),
                        codes = mutableListOf("TESTCODE", "TESTMVC2"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartFirstOrderList
                        ),
                        codes = mutableListOf("TESTMVC"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    )
                )
            ), getLastApplyPromoRequest
        )
    }

    @Test
    fun `WHEN promoData is promoUiModel and with BO and MVC but some group order has been removed should generate correct params`() {
        // GIVEN
        val promoData = PromoUiModel(
            voucherOrderUiModels = listOf(
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "000000-KEY",
                    code = "TESTCODE",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "000000-KEY",
                    code = "TESTMVC",
                    shippingId = 1,
                    spId = 2,
                    type = "",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "111111-KEY",
                    code = "TESTCODE",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "111111-KEY",
                    code = "TESTMVC2",
                    shippingId = 1,
                    spId = 2,
                    type = "",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "222222-KEY",
                    code = "TESTCODE",
                    shippingId = 1,
                    spId = 2,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                PromoCheckoutVoucherOrdersItemUiModel(
                    uniqueId = "222222-KEY",
                    code = "",
                    shippingId = 1,
                    spId = 2,
                    type = "",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                )
            )
        )
        val lastValidateUseRequestData = ValidateUsePromoRequest(
            orders = listOf(
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 15000.0,
                    etaText = "",
                    shippingMetadata = "{\"order_shipping_price\":15000}",
                    uniqueId = "000000-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 15000.0,
                    etaText = "",
                    shippingMetadata = "{\"order_shipping_price\":15000}",
                    uniqueId = "111111-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 15000.0,
                    etaText = "",
                    shippingMetadata = "{\"order_shipping_price\":15000}",
                    uniqueId = "222222-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                )
            )
        )
        val cartFirstOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "1",
                quantity = 5,
                bundleId = "0"
            ),
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "111111-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "1",
                    poDuration = "0"
                ),
                productId = "2",
                quantity = 5,
                bundleId = "0"
            )
        )
        val cartSecondOrderList = mutableListOf(
            CartItemHolderData(
                isSelected = true,
                cartStringOrder = "222222-KEY",
                shopHolderData = CartShopHolderData(
                    shopId = "2",
                    poDuration = "0"
                ),
                productId = "3",
                quantity = 5,
                bundleId = "0"
            )
        )
        val productUiModelList = cartFirstOrderList
            .plus(cartSecondOrderList)
            .toMutableList()
        val groupShopList = mutableListOf(
            CartGroupHolderData(
                cartString = "_-0-9466960-169751269-KEY_OWOC",
                promoCodes = listOf(),
                warehouseId = 22712,
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
                isPo = false,
                boCode = "TESTCODE",
                boUniqueId = "000000-KEY",
                productUiModelList = productUiModelList
            )
        )

        // WHEN
        val getLastApplyPromoRequest = PromoRequestMapper.generateGetLastApplyRequestParams(
            promoData = promoData,
            selectedCartGroupHolderDataList = groupShopList,
            lastValidateUseRequestData
        )

        // THEN
        assertEquals(
            ValidateUsePromoRequest(
                codes = mutableListOf(),
                state = CartConstant.PARAM_CART,
                skipApply = 0,
                cartType = CartConstant.PARAM_DEFAULT,
                orders = listOf(
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartSecondOrderList
                        ),
                        codes = mutableListOf("TESTCODE"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                    OrdersItem(
                        productDetails = PromoRequestMapperTestUtil.mapCartProductModelToPromoProductDetailsItem(
                            cartFirstOrderList
                        ),
                        codes = mutableListOf("TESTMVC2"),
                        shippingId = 1,
                        spId = 2,
                        boCampaignId = 10,
                        shippingSubsidy = 10000,
                        benefitClass = "",
                        shippingPrice = 15000.0,
                        etaText = "",
                        shippingMetadata = "{\"order_shipping_price\":15000}",
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        boType = 1,
                        warehouseId = 22712,
                        isPo = false,
                        poDuration = 0,
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                    ),
                )
            ), getLastApplyPromoRequest
        )
    }
    
//
//    @Test
//    fun `WHEN generateGetLastApplyRequestParams with validateUsePromoData and BO should generate correct params`() {
//
//    }

    @After
    fun tearDown() {

    }
}
