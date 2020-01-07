package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.purchase_platform.common.data.api.CartResponseErrorException
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.UpdateCartData
import com.tokopedia.purchase_platform.features.cart.view.CartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

class UpdateCartSubscriber(private val view: ICartListView?,
                           private val presenter: ICartListPresenter?,
                           private val cartListData: CartListData?,
                           private val cartItemDataList: List<CartItemData>) : Subscriber<UpdateCartData>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.let {
            e.printStackTrace()
            it.hideProgressLoading()
            var errorMessage = e.message
            if (e !is CartResponseErrorException) {
                errorMessage = ErrorHandler.getErrorMessage(it.getActivityObject(), e)
            }
            it.renderErrorToShipmentForm(errorMessage ?: "")
        }
    }

    override fun onNext(data: UpdateCartData) {
        view?.let {
            it.hideProgressLoading()
            if (!data.isSuccess) {
                it.renderErrorToShipmentForm(data.message ?: "")
            } else {
                val checklistCondition = getChecklistCondition()
                it.renderToShipmentFormSuccess(
                        presenter?.generateCheckoutDataAnalytics(cartItemDataList, EnhancedECommerceActionField.STEP_1) ?: hashMapOf(),
                        cartItemDataList,
                        isCheckoutProductEligibleForCashOnDelivery(cartItemDataList),
                        checklistCondition)
            }
        }
    }

    private fun getChecklistCondition(): Int {
        var checklistCondition = CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES
        val cartShopHolderDataList = view?.getAllShopDataList()

        if (cartShopHolderDataList?.size ?: 0 == 1) {
            cartShopHolderDataList?.get(0)?.shopGroupAvailableData?.cartItemDataList?.let {
                for (cartShopHolderData in it) {
                    if (!cartShopHolderData.isSelected) {
                        checklistCondition = CartListPresenter.ITEM_CHECKED_PARTIAL_ITEM
                        break
                    }
                }
            }
        } else if (cartShopHolderDataList?.size ?: 0 > 1) {
            var allSelectedItemShopCount = 0
            var selectPartialShopAndItem = false
            cartShopHolderDataList?.let {
                for (cartShopHolderData in it) {
                    if (cartShopHolderData.isAllSelected) {
                        allSelectedItemShopCount++
                    } else {
                        var selectedItem = 0
                        cartShopHolderData.shopGroupAvailableData.cartItemDataList?.let {
                            for (cartItemHolderData in it) {
                                if (!cartItemHolderData.isSelected) {
                                    selectedItem++
                                }
                            }
                            if (!selectPartialShopAndItem && selectedItem != it.size) {
                                selectPartialShopAndItem = true
                            }
                        }
                    }
                }
                if (selectPartialShopAndItem) {
                    checklistCondition = CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM
                } else if (allSelectedItemShopCount < cartShopHolderDataList.size) {
                    checklistCondition = CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP
                }
            }
        }

        if (checklistCondition == CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES && presenter?.getHasPerformChecklistChange() == true) {
            checklistCondition = CartListPresenter.ITEM_CHECKED_ALL_WITH_CHANGES
        }
        return checklistCondition
    }

    private fun isCheckoutProductEligibleForCashOnDelivery(cartItemDataList: List<CartItemData>): Boolean {
        var totalAmount = 0.0
        val maximalTotalAmountEligible = 1000000.0
        for (cartItemData in cartItemDataList) {
            val itemPriceAmount = cartItemData.originData?.pricePlan?.times(cartItemData.updatedData?.quantity ?: 0) ?: 0.toDouble()
            totalAmount += itemPriceAmount
            if (cartItemData.originData?.isCod == false) return false
        }
        return totalAmount <= maximalTotalAmountEligible
    }

}