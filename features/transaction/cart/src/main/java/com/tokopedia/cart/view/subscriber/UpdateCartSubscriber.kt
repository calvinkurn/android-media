package com.tokopedia.cart.view.subscriber

import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.updatecart.UpdateCartData
import com.tokopedia.cart.view.CartListPresenter
import com.tokopedia.cart.view.ICartListPresenter
import com.tokopedia.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

class UpdateCartSubscriber(private val view: ICartListView?,
                           private val presenter: ICartListPresenter?,
                           private val fireAndForget: Boolean) : Subscriber<UpdateCartData>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        if (!fireAndForget) {
            view?.let {
                e.printStackTrace()
                it.hideProgressLoading()
                it.renderErrorToShipmentForm(e)
            }
        }
    }

    override fun onNext(data: UpdateCartData) {
        if (!fireAndForget) {
            view?.let {
                it.hideProgressLoading()
                if (!data.isSuccess) {
                    if (data.outOfServiceData.id != 0) {
                        it.renderErrorToShipmentForm(data.outOfServiceData)
                    } else {
                        it.renderErrorToShipmentForm(data.message, if (data.toasterActionData.showCta) data.toasterActionData.text else "")
                    }
                } else {
                    val checklistCondition = getChecklistCondition()
                    val cartItemDataList = it.getAllSelectedCartDataList()
                    cartItemDataList?.let { data ->
                        it.renderToShipmentFormSuccess(
                                presenter?.generateCheckoutDataAnalytics(data, EnhancedECommerceActionField.STEP_1)
                                        ?: hashMapOf(),
                                data, isCheckoutProductEligibleForCashOnDelivery(data), checklistCondition)
                    }
                }
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
                        cartShopHolderData.shopGroupAvailableData?.cartItemDataList?.let { cartItemHolderDataList ->
                            for (cartItemHolderData in cartItemHolderDataList) {
                                if (!cartItemHolderData.isSelected) {
                                    selectedItem++
                                }
                            }
                            if (!selectPartialShopAndItem && selectedItem != cartItemHolderDataList.size) {
                                selectPartialShopAndItem = true
                            }
                        }
                    }
                }
                if (selectPartialShopAndItem) {
                    checklistCondition = CartListPresenter.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM
                } else if (allSelectedItemShopCount < it.size) {
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
            val itemPriceAmount = cartItemData.originData?.pricePlan?.times(cartItemData.updatedData?.quantity
                    ?: 0) ?: 0.toDouble()
            totalAmount += itemPriceAmount
            if (cartItemData.originData?.isCod == false) return false
        }
        return totalAmount <= maximalTotalAmountEligible
    }

}