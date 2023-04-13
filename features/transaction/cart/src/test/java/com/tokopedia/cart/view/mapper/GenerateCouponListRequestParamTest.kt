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
import junit.framework.TestCase
import org.junit.Test

class GenerateCouponListRequestParamTest {

    @Test
    fun `WHEN promoData is null should generate correct params`() {
        // GIVEN
//        val promoData = LastApplyPromo(
//            lastApplyPromoData = LastApplyPromoData(
//                listVoucherOrders = listOf(
//                    VoucherOrders(
//                        uniqueId = "111111-KEY",
//                        code = "",
//                        shippingId = 1,
//                        spId = 2,
//                        type = "",
//                        boCampaignId = "10",
//                        shippingSubsidy = 10000,
//                        benefitClass = "",
//                        shippingPrice = 15000.0,
//                        etaText = "",
//                        shippingMetadata = "",
//                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
//                    ),
//                    VoucherOrders(
//                        uniqueId = "222222-KEY",
//                        code = "",
//                        shippingId = 3,
//                        spId = 4,
//                        type = "",
//                        boCampaignId = "10",
//                        shippingSubsidy = 10000,
//                        benefitClass = "",
//                        shippingPrice = 15000.0,
//                        etaText = "",
//                        shippingMetadata = "",
//                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC"
//                    )
//                )
//            )
//        )
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
                        product_details = PromoRequestMapperTestUtil
                            .mapToCouponListProductDetailsItem(cartSecondOrderList),
                        codes = mutableListOf(),
                        isChecked = true,
                        shippingId = 0,
                        spId = 0,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        shippingMetadata = "",
                        boType = 1
                    ),
                    Order(
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        product_details = PromoRequestMapperTestUtil
                            .mapToCouponListProductDetailsItem(cartFirstOrderList),
                        codes = mutableListOf(),
                        isChecked = true,
                        shippingId = 0,
                        spId = 0,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        shippingMetadata = "",
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
                        product_details = PromoRequestMapperTestUtil
                            .mapToCouponListProductDetailsItem(cartSecondOrderList),
                        codes = mutableListOf(),
                        isChecked = true,
                        shippingId = 0,
                        spId = 0,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        shippingMetadata = "",
                        boType = 1
                    ),
                    Order(
                        shopId = 1,
                        uniqueId = "111111-KEY",
                        product_details = PromoRequestMapperTestUtil
                            .mapToCouponListProductDetailsItem(cartFirstOrderList),
                        codes = mutableListOf(),
                        isChecked = true,
                        shippingId = 0,
                        spId = 0,
                        isInsurancePrice = 0,
                        freeShippingMetadata = "",
                        validationMetadata = "",
                        cartStringGroup = "_-0-9466960-169751269-KEY_OWOC",
                        shippingMetadata = "",
                        boType = 1
                    )
                )
            ),
            promoRequest
        )
    }
}
