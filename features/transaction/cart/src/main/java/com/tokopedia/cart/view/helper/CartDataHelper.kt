package com.tokopedia.cart.view.helper

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.view.uimodel.CartBuyAgainHolderData
import com.tokopedia.cart.view.uimodel.CartGroupHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.uimodel.CartItemHolderData.Companion.BUNDLING_ITEM_FOOTER
import com.tokopedia.cart.view.uimodel.CartModel
import com.tokopedia.cart.view.uimodel.CartProductBenefitData
import com.tokopedia.cart.view.uimodel.CartRecentViewHolderData
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.cart.view.uimodel.CartSectionHeaderHolderData
import com.tokopedia.cart.view.uimodel.CartShopBottomHolderData
import com.tokopedia.cart.view.uimodel.CartTopAdsHeadlineData
import com.tokopedia.cart.view.uimodel.CartWishlistHolderData
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.cart.view.uimodel.DisabledItemHeaderHolderData
import com.tokopedia.cartcommon.data.response.bmgm.BmGmProductBenefit
import com.tokopedia.purchase_platform.common.constant.BmGmConstant.CART_DETAIL_TYPE_BMGM

object CartDataHelper {

    fun getAllAvailableCartItemData(cartDataList: ArrayList<Any>): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    if (!data.isError) {
                        val cartItemHolderDataList = data.productUiModelList
                        for (cartItemHolderData in cartItemHolderDataList) {
                            cartItemHolderData.shopBoMetadata = data.boMetadata
                            cartItemHolderData.shopCartShopGroupTickerData =
                                data.cartShopGroupTicker
                            cartItemDataList.add(cartItemHolderData)
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemDataList
    }

    fun getAllAvailableCartItemHolderData(cartDataList: ArrayList<Any>): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    data.productUiModelList.let {
                        cartItemDataList.addAll(it)
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemDataList
    }

    fun getAllAvailableShopGroupDataList(cartDataList: ArrayList<Any>): List<CartGroupHolderData> {
        val availableShopGroupList = mutableListOf<CartGroupHolderData>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    if (!data.isError) {
                        availableShopGroupList.add(data)
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }
        return availableShopGroupList
    }

    fun getAllCartItemData(
        cartDataList: ArrayList<Any>,
        cartModel: CartModel
    ): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    val cartItemHolderDataList = data.productUiModelList
                    for (cartItemHolderData in cartItemHolderDataList) {
                        cartItemHolderData.shopBoMetadata = data.boMetadata
                        cartItemHolderData.shopCartShopGroupTickerData = data.cartShopGroupTicker
                        cartItemDataList.add(cartItemHolderData)
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        if (cartModel.tmpAllUnavailableShop?.isNotEmpty() == true) {
            cartItemDataList.addAll(getCollapsedUnavailableCartItemData(cartModel))
        }

        return cartItemDataList
    }

    fun getAllCartItemProductId(cartDataList: ArrayList<Any>): List<String> {
        val productIdList = ArrayList<String>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    data.productUiModelList.let {
                        for (cartItemHolderData in it) {
                            productIdList.add(cartItemHolderData.productId)
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return productIdList
    }

    fun getAllDisabledCartItemData(
        cartDataList: ArrayList<Any>,
        cartModel: CartModel
    ): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        if (cartModel.tmpAllUnavailableShop?.isNotEmpty() == true) {
            cartItemDataList.addAll(getCollapsedUnavailableCartItemData(cartModel))
        } else {
            loop@ for (data in cartDataList) {
                when (data) {
                    is CartGroupHolderData -> {
                        if (data.isError) {
                            cartItemDataList.addAll(data.productUiModelList)
                        }
                    }

                    hasReachAllShopItems(data) -> break@loop
                }
            }
        }

        return cartItemDataList
    }

    fun getAllShopGroupDataList(cartDataList: ArrayList<Any>): List<CartGroupHolderData> {
        val shopGroupList = mutableListOf<CartGroupHolderData>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    shopGroupList.add(data)
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }
        return shopGroupList
    }

    fun getAllShopGroupDataListWithIndex(cartDataList: ArrayList<Any>): List<Pair<Int, CartGroupHolderData>> {
        val shopGroupList = mutableListOf<Pair<Int, CartGroupHolderData>>()
        loop@ for ((index, data) in cartDataList.withIndex()) {
            when (data) {
                is CartGroupHolderData -> {
                    shopGroupList.add(Pair(index, data))
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }
        return shopGroupList
    }

    fun getCartGroupHolderDataAndIndexByCartString(
        cartDataList: ArrayList<Any>,
        cartString: String,
        isUnavailableGroup: Boolean
    ): Pair<Int, List<Any>> {
        val cartGroupList = arrayListOf<Any>()
        var startingIndex = RecyclerView.NO_POSITION
        for ((index, data) in cartDataList.withIndex()) {
            if (data is CartGroupHolderData && data.cartString == cartString && data.isError == isUnavailableGroup) {
                startingIndex = index
                cartGroupList.add(data)
            } else if (data is CartItemHolderData && startingIndex >= 0 && data.cartString == cartString) {
                cartGroupList.add(data)
            } else if (data is CartShopBottomHolderData && startingIndex >= 0 && data.shopData.cartString == cartString) {
                cartGroupList.add(data)
                break
            }
        }
        return Pair(startingIndex, cartGroupList)
    }

    fun getCartGroupHolderDataByCartItemHolderData(
        cartDataList: ArrayList<Any>,
        cartItemHolderData: CartItemHolderData
    ): CartGroupHolderData? {
        loop@ for (data in cartDataList) {
            if (data is CartGroupHolderData && data.cartString == cartItemHolderData.cartString && data.isError == cartItemHolderData.isError) {
                return data
            }
        }

        return null
    }

    fun getCartItemIndexByCartId(cartDataList: ArrayList<Any>, cartId: String): Int {
        // indexOfFirst will return -1 when item not found
        return cartDataList.indexOfFirst { any ->
            any is CartItemHolderData && any.cartId == cartId
        }
    }

    fun getCartShopBottomHolderDataFromIndex(
        cartDataList: ArrayList<Any>,
        shopBottomIndex: Int
    ): CartShopBottomHolderData? {
        val bottomItem = cartDataList[shopBottomIndex]
        return if (bottomItem is CartShopBottomHolderData) {
            bottomItem
        } else {
            null
        }
    }

    fun getCartWishlistHolderData(cartDataList: ArrayList<Any>): CartWishlistHolderData {
        cartDataList.forEach {
            if (it is CartWishlistHolderData) {
                return it
            }
        }

        return CartWishlistHolderData()
    }

    private fun getCollapsedUnavailableCartItemData(cartModel: CartModel): List<CartItemHolderData> {
        val cartItemDataList = mutableListOf<CartItemHolderData>()
        cartModel.tmpAllUnavailableShop?.let {
            loop@ for (data in it) {
                when (data) {
                    is CartGroupHolderData -> {
                        cartItemDataList.addAll(data.productUiModelList)
                    }
                }
            }
        }
        return cartItemDataList
    }

    fun getDisabledAccordionHolderData(cartDataList: ArrayList<Any>): DisabledAccordionHolderData? {
        cartDataList.forEach {
            if (it is DisabledAccordionHolderData) {
                return it
            }
        }

        return null
    }

    fun getNearestCartItemHolderDataPosition(
        startingIndex: Int,
        cartDataList: ArrayList<Any>
    ): Int {
        if (startingIndex == RecyclerView.NO_POSITION) return RecyclerView.NO_POSITION
        outer@ for (i in startingIndex until cartDataList.size) {
            when (val data = cartDataList[i]) {
                is CartItemHolderData -> {
                    if (data.isBundlingItem && data.isMultipleBundleProduct && data.bundlingItemPosition != BUNDLING_ITEM_FOOTER) {
                        continue@outer
                    }
                    if (data.isError) {
                        return -1
                    }
                    return i
                }

                hasReachAllShopItems(data) -> return RecyclerView.NO_POSITION
            }
        }
        return RecyclerView.NO_POSITION
    }

    fun getNearestShopBottomHolderDataIndex(
        cartDataList: ArrayList<Any>,
        startingIndex: Int,
        cartGroupHolderData: CartGroupHolderData
    ): Pair<CartShopBottomHolderData?, Int> {
        loop@ for (index in startingIndex until cartDataList.size) {
            when (val data = cartDataList[index]) {
                is CartShopBottomHolderData -> {
                    if (data.shopData.cartString == cartGroupHolderData.cartString) {
                        return Pair(data, index)
                    } else {
                        break@loop
                    }
                }

                is CartGroupHolderData -> {
                    if (index > startingIndex && data.cartString != cartGroupHolderData.cartString) {
                        break@loop
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }
        return Pair(null, RecyclerView.NO_POSITION)
    }

    fun getSelectedAvailableCartItemData(cartDataList: ArrayList<Any>): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    if (!data.isError) {
                        for (cartItemHolderData in data.productUiModelList) {
                            if (cartItemHolderData.isSelected) {
                                cartItemDataList.add(cartItemHolderData)
                            }
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemDataList
    }

    fun getDisabledItemHeaderPosition(cartDataList: ArrayList<Any>): Int {
        for (i in cartDataList.indices) {
            if (cartDataList[i] is DisabledItemHeaderHolderData) {
                return i
            }
        }

        return 0
    }

    fun getRecommendationItem(
        itemCount: Int,
        cartDataList: ArrayList<Any>
    ): List<CartRecommendationItemHolderData> {
        var firstRecommendationItemIndex = 0
        for ((index, item) in cartDataList.withIndex()) {
            if (item is CartRecommendationItemHolderData) {
                firstRecommendationItemIndex = index
                break
            }
        }

        var lastIndex = itemCount
        // Check if last item is not loading view type
        if (cartDataList[itemCount - 1] !is CartRecommendationItemHolderData) {
            lastIndex = itemCount - 1
        }
        val recommendationList = cartDataList.subList(firstRecommendationItemIndex, lastIndex)

        return recommendationList as List<CartRecommendationItemHolderData>
    }

    fun getSelectedCartGroupHolderData(cartDataList: ArrayList<Any>): List<CartGroupHolderData> {
        val cartGroupHolderDataList = ArrayList<CartGroupHolderData>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    if (data.isPartialSelected || data.isAllSelected) {
                        cartGroupHolderDataList.add(data)
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartGroupHolderDataList
    }

    fun getSelectedCartItemData(cartDataList: ArrayList<Any>): List<CartItemHolderData> {
        val cartItemDataList = ArrayList<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    if ((data.isPartialSelected || data.isAllSelected)) {
                        for (cartItemHolderData in data.productUiModelList) {
                            if (cartItemHolderData.isSelected && !cartItemHolderData.isError) {
                                cartItemHolderData.shopBoMetadata = data.boMetadata
                                cartItemHolderData.shopCartShopGroupTickerData =
                                    data.cartShopGroupTicker
                                cartItemDataList.add(cartItemHolderData)
                            }
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemDataList
    }

    fun checkSelectedCartItemDataWithOfferBmGm(cartDataList: ArrayList<Any>): ArrayList<String> {
        val listCartStringOrderWithBmGmOfferId = ArrayList<String>()
        loop@ for (data in cartDataList) {
            when (data) {
                is CartGroupHolderData -> {
                    if ((data.isPartialSelected || data.isAllSelected)) {
                        for (cartItemHolderData in data.productUiModelList) {
                            if (cartItemHolderData.isSelected &&
                                !cartItemHolderData.isError &&
                                cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.cartDetailType == CART_DETAIL_TYPE_BMGM &&
                                !listCartStringOrderWithBmGmOfferId.contains("${cartItemHolderData.cartStringOrder}||${cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId}")
                            ) {
                                listCartStringOrderWithBmGmOfferId.add("${cartItemHolderData.cartStringOrder}||${cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId}")
                            }
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return listCartStringOrderWithBmGmOfferId
    }

    fun getCartItemByBundleGroupId(
        cartDataList: ArrayList<Any>,
        bundleId: String,
        bundleGroupId: String
    ): List<CartItemHolderData> {
        val cartItemHolderDataList = mutableListOf<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            if (cartItemHolderDataList.isNotEmpty()) {
                break@loop
            }
            when (data) {
                is CartGroupHolderData -> {
                    data.productUiModelList.forEach { cartItemHolderData ->
                        if (cartItemHolderData.isBundlingItem && cartItemHolderData.bundleId == bundleId && cartItemHolderData.bundleGroupId == bundleGroupId) {
                            cartItemHolderDataList.add(cartItemHolderData)
                        }
                    }
                }

                hasReachAllShopItems(data) -> break@loop
            }
        }

        return cartItemHolderDataList
    }

    fun hasReachAllShopItems(data: Any): Boolean {
        return data is CartRecentViewHolderData ||
            data is CartWishlistHolderData ||
            data is CartTopAdsHeadlineData ||
            data is CartRecommendationItemHolderData
    }

    fun hasAvailableItemLeft(cartDataList: ArrayList<Any>): Boolean {
        cartDataList.forEach {
            when (it) {
                is CartGroupHolderData -> {
                    if (it.productUiModelList.isNotEmpty()) {
                        return true
                    }
                }

                is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                    return false
                }
            }
        }

        return false
    }

    fun hasSelectedCartItem(cartDataList: ArrayList<Any>): Boolean {
        cartDataList.forEach {
            when (it) {
                is CartGroupHolderData -> {
                    it.productUiModelList.forEach { cartItem ->
                        if (cartItem.isSelected) return true
                    }
                }

                is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                    return false
                }
            }
        }

        return false
    }

    fun isAllAvailableItemChecked(cartDataList: ArrayList<Any>): Boolean {
        cartDataList.forEach {
            when (it) {
                is CartGroupHolderData -> {
                    it.productUiModelList.forEach { itemHolderData ->
                        if (!itemHolderData.isSelected) {
                            return false
                        }
                    }
                }

                is DisabledItemHeaderHolderData, is CartSectionHeaderHolderData -> {
                    return true
                }
            }
        }

        return true
    }

    fun getListProductByOfferIdAndCartStringOrder(
        cartDataList: ArrayList<Any>,
        offerId: Long,
        cartStringOrder: String
    ): ArrayList<CartItemHolderData> {
        val listProductByOfferId = arrayListOf<CartItemHolderData>()
        loop@ for (data in cartDataList) {
            if (data is CartItemHolderData &&
                data.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId == offerId &&
                data.cartStringOrder == cartStringOrder
            ) {
                listProductByOfferId.add(data)
            }
        }

        return listProductByOfferId
    }

    fun getCartItemHolderDataListAndIndexByCartStringOrderAndOfferId(
        cartDataList: ArrayList<Any>,
        cartStringOrder: String,
        offerId: Long
    ): Pair<Int, ArrayList<CartItemHolderData>> {
        var indexReturned = RecyclerView.NO_POSITION
        val itemReturned = arrayListOf<CartItemHolderData>()
        for ((index, data) in cartDataList.withIndex()) {
            if (data is CartItemHolderData &&
                data.cartStringOrder == cartStringOrder &&
                data.cartBmGmTickerData.bmGmCartInfoData.cartDetailType == CART_DETAIL_TYPE_BMGM &&
                data.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId == offerId
            ) {
                if (indexReturned == RecyclerView.NO_POSITION) indexReturned = index
                itemReturned.add(data)
            }
        }
        return Pair(indexReturned, itemReturned)
    }

    fun getAllCartStringOrderWithBmGmOfferId(cartDataList: ArrayList<Any>): ArrayList<String> {
        val listCartStringOrderWithBmGmOfferId = arrayListOf<String>()
        for (cartItem in cartDataList) {
            if (cartItem is CartItemHolderData &&
                cartItem.cartBmGmTickerData.bmGmCartInfoData.cartDetailType == CART_DETAIL_TYPE_BMGM &&
                !listCartStringOrderWithBmGmOfferId.contains("${cartItem.cartStringOrder}||${cartItem.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId}")
            ) {
                listCartStringOrderWithBmGmOfferId.add("${cartItem.cartStringOrder}||${cartItem.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId}")
            }
        }
        return listCartStringOrderWithBmGmOfferId
    }

    fun getDefaultAndMultipleBundlingProductPositionInPair(
        cartDataList: ArrayList<Any>,
        startPosition: Int,
        endPosition: Int
    ): Pair<Int, Int> {
        var defaultProductPosition: Int = RecyclerView.NO_POSITION
        var bundlingProductPosition: Int = RecyclerView.NO_POSITION
        loop@ for (i in startPosition..endPosition) {
            val currentData = cartDataList[i]

            if (hasReachAllShopItems(currentData)) {
                return Pair(
                    defaultProductPosition,
                    bundlingProductPosition
                )
            }

            if (currentData !is CartItemHolderData || currentData.isError) continue@loop

            if (defaultProductPosition == RecyclerView.NO_POSITION && (!currentData.isBundlingItem || (!currentData.isMultipleBundleProduct))) {
                defaultProductPosition = i
            }

            if (bundlingProductPosition == RecyclerView.NO_POSITION && currentData.isBundlingItem && currentData.isMultipleBundleProduct) {
                bundlingProductPosition = i
            }

            if (defaultProductPosition != RecyclerView.NO_POSITION && bundlingProductPosition != RecyclerView.NO_POSITION) {
                break@loop
            }
        }
        return Pair(defaultProductPosition, bundlingProductPosition)
    }

    fun getBenefitProducts(
        productsBenefit: List<BmGmProductBenefit>?
    ): List<CartProductBenefitData> {
        return productsBenefit?.map {
            CartProductBenefitData(
                id = it.productId,
                name = it.productName,
                imageUrl = it.productImage,
                qty = it.quantity,
                finalPrice = it.finalPrice,
                originalPrice = it.originalPrice
            )
        } ?: emptyList()
    }

    fun getBuyAgainViewHolderIndex(cartDataList: ArrayList<Any>): Int {
        return cartDataList.indexOfFirst { it is CartBuyAgainHolderData }
    }
}
