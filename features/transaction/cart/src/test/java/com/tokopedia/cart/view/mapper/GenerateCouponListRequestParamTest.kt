package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.promo.VoucherOrders
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.purchase_platform.common.feature.bometadata.BoEligibility
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class GenerateCouponListRequestParamTest {

    private lateinit var cartFirstOrderList: MutableList<CartItemHolderData>
    private lateinit var cartSecondOrderList: MutableList<CartItemHolderData>

    @Before
    fun setup() {
        cartFirstOrderList = PromoRequestMapperTestHelper.getFirstCartOrder()
        cartSecondOrderList = PromoRequestMapperTestHelper.getSecondCartOrder()
    }

    @Test
    fun `WHEN promoData is null should generate correct params`() {
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
                boCode = "",
                productUiModelList = productUiModelList,
                cartString = "_-0-9466960-169751269-KEY_OWOC"
            )
        )

        // WHEN
        val promoRequest = PromoRequestMapper.generateCouponListRequestParams(
            promoData = null,
            availableCartGroupHolderDataList = groupShopList,
            null
        )

        // THEN
        TestCase.assertEquals(
            PromoRequest(
                codes = arrayListOf(),
                state = "cart",
                isSuggested = 0,
                orders = listOf(
                    Order(
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(cartSecondOrderList),
                        codes = mutableListOf(),
                        isChecked = true,
                        shippingId = 0,
                        spId = 0,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        boType = 1
                    ),
                    Order(
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(cartFirstOrderList),
                        codes = mutableListOf(),
                        isChecked = true,
                        shippingId = 0,
                        spId = 0,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        boType = 1
                    )
                )
            ),
            promoRequest
        )
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
                        shippingPrice = 20000.0,
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
                boCode = "",
                productUiModelList = productUiModelList,
                cartString = "_-0-9466960-169751269-KEY_OWOC"
            )
        )

        // WHEN
        val promoRequest = PromoRequestMapper.generateCouponListRequestParams(
            promoData = promoData,
            availableCartGroupHolderDataList = groupShopList,
            null
        )

        // THEN
        TestCase.assertEquals(
            PromoRequest(
                codes = arrayListOf(),
                state = "cart",
                isSuggested = 0,
                orders = listOf(
                    Order(
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(cartSecondOrderList),
                        codes = mutableListOf(),
                        isChecked = true,
                        shippingId = 1,
                        spId = 2,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        boType = 1
                    ),
                    Order(
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(cartFirstOrderList),
                        codes = mutableListOf("TESTBO"),
                        isChecked = true,
                        shippingId = 1,
                        spId = 2,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        boType = 1
                    )
                )
            ),
            promoRequest
        )
    }

    @Test
    fun `WHEN promoData is lastApplyPromo and group is more than one should generate correct params`() {
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
                        shippingPrice = 20000.0,
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
                        shippingId = 3,
                        spId = 4,
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
        val secondProductUiModelList = mutableListOf(
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
                isPo = false,
                boCode = "",
                productUiModelList = productUiModelList,
                cartString = "_-0-9466960-169751269-KEY_OWOC"
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
                isPo = false,
                boCode = "",
                productUiModelList = secondProductUiModelList,
                cartString = "_-0-9466960-169751270-KEY_OWOC"
            )
        )

        // WHEN
        val promoRequest = PromoRequestMapper.generateCouponListRequestParams(
            promoData = promoData,
            availableCartGroupHolderDataList = groupShopList,
            null
        )

        // THEN
        TestCase.assertEquals(
            PromoRequest(
                codes = arrayListOf(),
                state = "cart",
                isSuggested = 0,
                orders = listOf(
                    Order(
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(cartSecondOrderList),
                        codes = mutableListOf(),
                        isChecked = true,
                        shippingId = 1,
                        spId = 2,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        boType = 1
                    ),
                    Order(
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(cartFirstOrderList),
                        codes = mutableListOf("TESTBO"),
                        isChecked = true,
                        shippingId = 1,
                        spId = 2,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        boType = 1
                    ),
                    Order(
                        shopId = 3,
                        uniqueId = "333333-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(secondProductUiModelList),
                        codes = mutableListOf("TESTBO2"),
                        isChecked = true,
                        shippingId = 3,
                        spId = 4,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751270-KEY_OWOC",
                        boType = 1
                    )
                )
            ),
            promoRequest
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
                )
            )
        )
        val lastValidateUseRequestData = ValidateUsePromoRequest(
            orders = listOf(
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 20000.0,
                    etaText = "",
                    uniqueId = "111111-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 15000.0,
                    etaText = "",
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
                boCode = "",
                productUiModelList = productUiModelList,
                cartString = "_-0-9466960-169751269-KEY_OWOC"
            )
        )

        // WHEN
        val promoRequest = PromoRequestMapper.generateCouponListRequestParams(
            promoData = promoData,
            availableCartGroupHolderDataList = groupShopList,
            lastValidateUseRequestData
        )

        // THEN
        TestCase.assertEquals(
            PromoRequest(
                codes = arrayListOf(),
                state = "cart",
                isSuggested = 0,
                orders = listOf(
                    Order(
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(cartSecondOrderList),
                        codes = mutableListOf(),
                        isChecked = true,
                        shippingId = 1,
                        spId = 2,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        boType = 1
                    ),
                    Order(
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(cartFirstOrderList),
                        codes = mutableListOf("TESTBO"),
                        isChecked = true,
                        shippingId = 1,
                        spId = 2,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        boType = 1
                    )
                )
            ),
            promoRequest
        )
    }

    @Test
    fun `WHEN promoData is promoUiModel and group is more than one should generate correct params`() {
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
                    uniqueId = "333333-KEY",
                    code = "TESTBO2",
                    shippingId = 3,
                    spId = 4,
                    type = "logistic",
                    cartStringGroup = "_-0-9466960-169751270-KEY_OWOC"
                )
            )
        )
        val lastValidateUseRequestData = ValidateUsePromoRequest(
            orders = listOf(
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 20000.0,
                    etaText = "",
                    uniqueId = "111111-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 15000.0,
                    etaText = "",
                    uniqueId = "222222-KEY",
                    cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
                ),
                OrdersItem(
                    boCampaignId = 10,
                    shippingSubsidy = 10000,
                    benefitClass = "",
                    shippingPrice = 18000.0,
                    etaText = "",
                    uniqueId = "333333-KEY",
                    cartStringGroup = "_-0-9466960-169751270-KEY_OWOC"
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
        val secondProductUiModelList = mutableListOf(
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
                isPo = false,
                boCode = "",
                productUiModelList = productUiModelList,
                cartString = "_-0-9466960-169751269-KEY_OWOC"
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
                isPo = false,
                boCode = "",
                productUiModelList = secondProductUiModelList,
                cartString = "_-0-9466960-169751270-KEY_OWOC"
            )
        )

        // WHEN
        val promoRequest = PromoRequestMapper.generateCouponListRequestParams(
            promoData = promoData,
            availableCartGroupHolderDataList = groupShopList,
            lastValidateUseRequestData
        )

        // THEN
        TestCase.assertEquals(
            PromoRequest(
                codes = arrayListOf(),
                state = "cart",
                isSuggested = 0,
                orders = listOf(
                    Order(
                        shopId = 2,
                        uniqueId = "222222-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(cartSecondOrderList),
                        codes = mutableListOf(),
                        isChecked = true,
                        shippingId = 1,
                        spId = 2,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        boType = 1
                    ),
                    Order(
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(cartFirstOrderList),
                        codes = mutableListOf("TESTBO"),
                        isChecked = true,
                        shippingId = 1,
                        spId = 2,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        boType = 1
                    ),
                    Order(
                        shopId = 3,
                        uniqueId = "333333-KEY",
                        product_details = PromoRequestMapperTestHelper
                            .mapToCouponListProductDetailsItem(secondProductUiModelList),
                        codes = mutableListOf("TESTBO2"),
                        isChecked = true,
                        shippingId = 3,
                        spId = 4,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751270-KEY_OWOC",
                        boType = 1
                    )
                )
            ),
            promoRequest
        )
    }
}
