package com.tokopedia.cartrevamp.view.processor

import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartModel
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.constant.BmGmConstant.CART_DETAIL_TYPE_BMGM
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil
import javax.inject.Inject

class CartCalculator @Inject constructor() {

    companion object {
        private const val PERCENTAGE = 100.0f
    }

    fun calculatePriceMarketplaceProduct(
        allCartItemDataList: ArrayList<CartItemHolderData>,
        cartModel: CartModel,
        updateCartModel: (cartModel: CartModel) -> Unit
    ): Triple<Int, Pair<Double, Double>, Double> {
        var totalItemQty = 0
        var subtotalBeforeSlashedPrice = 0.0
        var subtotalPrice = 0.0
        var subtotalCashback = 0.0

        val subtotalWholesaleBeforeSlashedPriceMap = HashMap<String, Double>()
        val subtotalWholesalePriceMap = HashMap<String, Double>()
        val subtotalWholesaleCashbackMap = HashMap<String, Double>()
        val cartItemParentIdMap = HashMap<String, CartItemHolderData>()
        val calculatedBundlingGroupId = HashSet<String>()
        val totalDiscountBmGmMap = HashMap<Long, Double>()

        for (cartItemHolderData in allCartItemDataList) {
            var itemQty =
                if (cartItemHolderData.isBundlingItem) {
                    cartItemHolderData.bundleQuantity * cartItemHolderData.quantity
                } else {
                    cartItemHolderData.quantity
                }
            totalItemQty += itemQty
            if (cartItemHolderData.parentId.isNotBlank() && cartItemHolderData.parentId.isNotBlank() && cartItemHolderData.parentId != "0") {
                for (cartItemHolderDataTmp in allCartItemDataList) {
                    if (cartItemHolderData.productId != cartItemHolderDataTmp.productId &&
                        cartItemHolderData.parentId == cartItemHolderDataTmp.parentId &&
                        cartItemHolderData.productPrice == cartItemHolderDataTmp.productPrice
                    ) {
                        val tmpQty =
                            if (cartItemHolderDataTmp.isBundlingItem) {
                                cartItemHolderDataTmp.bundleQuantity * cartItemHolderDataTmp.quantity
                            } else {
                                cartItemHolderDataTmp.quantity
                            }
                        itemQty += tmpQty
                    }
                }
            }

            if (cartItemHolderData.isBundlingItem) {
                if (!calculatedBundlingGroupId.contains(cartItemHolderData.bundleGroupId)) {
                    subtotalPrice += cartItemHolderData.bundleQuantity * cartItemHolderData.bundlePrice
                    subtotalBeforeSlashedPrice += cartItemHolderData.bundleQuantity * cartItemHolderData.bundleOriginalPrice
                    calculatedBundlingGroupId.add(cartItemHolderData.bundleGroupId)
                }
            } else if (cartItemHolderData.wholesalePriceData.isNotEmpty()) {
                // Calculate price and cashback for wholesale marketplace product
                val returnValueWholesaleProduct =
                    calculatePriceWholesaleProduct(cartItemHolderData, itemQty)

                if (!subtotalWholesaleBeforeSlashedPriceMap.containsKey(cartItemHolderData.parentId)) {
                    subtotalWholesaleBeforeSlashedPriceMap[cartItemHolderData.parentId] =
                        returnValueWholesaleProduct.first
                }
                if (!subtotalWholesalePriceMap.containsKey(cartItemHolderData.parentId)) {
                    subtotalWholesalePriceMap[cartItemHolderData.parentId] =
                        returnValueWholesaleProduct.second
                }
                if (!subtotalWholesaleCashbackMap.containsKey(cartItemHolderData.parentId)) {
                    subtotalWholesaleCashbackMap[cartItemHolderData.parentId] =
                        returnValueWholesaleProduct.third
                }
            } else {
                // Calculate price and cashback for normal marketplace product
                val returnValueNormalProduct = calculatePriceNormalProduct(
                    cartItemHolderData,
                    itemQty,
                    cartItemParentIdMap,
                    subtotalBeforeSlashedPrice,
                    subtotalPrice,
                    subtotalCashback
                )
                subtotalBeforeSlashedPrice = returnValueNormalProduct.first
                subtotalPrice = returnValueNormalProduct.second
                subtotalCashback = returnValueNormalProduct.third
            }

            if (cartItemHolderData.addOnsProduct.listData.isNotEmpty()) {
                updateCartModel(cartModel.copy(totalQtyWithAddon = totalItemQty))
                cartItemHolderData.addOnsProduct.listData.forEach {
                    if (it.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY || it.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK) {
                        subtotalPrice += (totalItemQty * it.price)
                    }
                }
            }

            if (cartItemHolderData.bmGmCartInfoData.cartDetailType == CART_DETAIL_TYPE_BMGM) {
                totalDiscountBmGmMap[cartItemHolderData.bmGmCartInfoData.bmGmData.offerId] = cartItemHolderData.bmGmCartInfoData.bmGmData.totalDiscount
            }
        }

        if (subtotalWholesaleBeforeSlashedPriceMap.isNotEmpty()) {
            for ((_, value) in subtotalWholesaleBeforeSlashedPriceMap) {
                subtotalBeforeSlashedPrice += value
            }
        }

        if (subtotalWholesalePriceMap.isNotEmpty()) {
            for ((_, value) in subtotalWholesalePriceMap) {
                subtotalPrice += value
            }
        }

        if (subtotalWholesaleCashbackMap.isNotEmpty()) {
            for ((_, value) in subtotalWholesaleCashbackMap) {
                subtotalCashback += value
            }
        }

        if (totalDiscountBmGmMap.isNotEmpty()) {
            for ((_, value) in totalDiscountBmGmMap) {
                subtotalPrice -= value
            }
        }

        val pricePair = Pair(subtotalBeforeSlashedPrice, subtotalPrice)
        return Triple(totalItemQty, pricePair, subtotalCashback)
    }

    private fun calculatePriceWholesaleProduct(
        cartItemHolderData: CartItemHolderData,
        itemQty: Int
    ): Triple<Double, Double, Double> {
        var subtotalBeforeSlashedPrice = 0.0
        var subTotalWholesalePrice = 0.0
        var subtotalWholesaleCashback = 0.0

        var hasCalculateWholesalePrice = false
        val wholesalePriceDataList = cartItemHolderData.wholesalePriceData

        for (wholesalePriceData in wholesalePriceDataList) {
            if (itemQty >= wholesalePriceData.qtyMin) {
                subTotalWholesalePrice = (itemQty * wholesalePriceData.prdPrc)
                hasCalculateWholesalePrice = true
                val wholesalePriceFormatted = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    wholesalePriceData.prdPrc,
                    false
                ).removeDecimalSuffix()
                cartItemHolderData.wholesalePriceFormatted = wholesalePriceFormatted
                cartItemHolderData.wholesalePrice = wholesalePriceData.prdPrc
                subtotalBeforeSlashedPrice = itemQty * cartItemHolderData.wholesalePrice
                break
            }
        }

        if (!hasCalculateWholesalePrice) {
            subTotalWholesalePrice = (itemQty * cartItemHolderData.productPrice)
            cartItemHolderData.wholesalePriceFormatted = null
            cartItemHolderData.wholesalePrice = 0.0
            subtotalBeforeSlashedPrice =
                if (cartItemHolderData.productOriginalPrice > 0) {
                    (itemQty * cartItemHolderData.productOriginalPrice)
                } else {
                    (itemQty * cartItemHolderData.productPrice)
                }
        }

        if (cartItemHolderData.productCashBack.isNotBlank()) {
            val cashbackPercentageString = cartItemHolderData.productCashBack
                .replace(" ", "")
                .replace("%", "")
            val cashbackPercentage = cashbackPercentageString.toDouble()
            subtotalWholesaleCashback = cashbackPercentage / PERCENTAGE * subTotalWholesalePrice
        }

        return Triple(subtotalBeforeSlashedPrice, subTotalWholesalePrice, subtotalWholesaleCashback)
    }

    private fun calculatePriceNormalProduct(
        cartItemHolderData: CartItemHolderData,
        itemQty: Int,
        cartItemParentIdMap: HashMap<String, CartItemHolderData>,
        subtotalBeforeSlashedPrice: Double,
        subtotalPrice: Double,
        subtotalCashback: Double
    ): Triple<Double, Double, Double> {
        var tmpSubtotalBeforeSlashedPrice = subtotalBeforeSlashedPrice
        var tmpSubTotalPrice = subtotalPrice
        var tmpSubtotalCashback = subtotalCashback

        val parentIdPriceIndex =
            cartItemHolderData.parentId + cartItemHolderData.productPrice.toString()
        if (!cartItemParentIdMap.containsKey(parentIdPriceIndex)) {
            val itemPrice = itemQty * cartItemHolderData.productPrice
            if (cartItemHolderData.productCashBack.isNotBlank()) {
                val cashbackPercentageString = cartItemHolderData.productCashBack
                    .replace(" ", "")
                    .replace("%", "")
                val cashbackPercentage = cashbackPercentageString.toDouble()
                val itemCashback = cashbackPercentage / PERCENTAGE * itemPrice
                tmpSubtotalCashback += itemCashback
            }

            if (cartItemHolderData.productOriginalPrice > 0) {
                tmpSubtotalBeforeSlashedPrice += (itemQty * cartItemHolderData.productOriginalPrice)
            } else {
                tmpSubtotalBeforeSlashedPrice += itemPrice
            }

            tmpSubTotalPrice += itemPrice
            cartItemHolderData.wholesalePriceFormatted = null
            cartItemParentIdMap[parentIdPriceIndex] = cartItemHolderData
        }

        return Triple(tmpSubtotalBeforeSlashedPrice, tmpSubTotalPrice, tmpSubtotalCashback)
    }
}
