package com.tokopedia.cart.bundle.view.subscriber

import com.tokopedia.cart.bundle.domain.model.updatecart.UpdateCartData
import com.tokopedia.cart.bundle.view.CartListPresenter
import com.tokopedia.cart.bundle.view.CartLogger
import com.tokopedia.cart.bundle.view.ICartListPresenter
import com.tokopedia.cart.bundle.view.ICartListView
import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceActionField
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-12-18.
 */

class UpdateCartSubscriber(private val view: ICartListView?,
                           private val presenter: ICartListPresenter?,
                           private val cartItemDataList: List<CartItemHolderData>) : Subscriber<UpdateCartData>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.let {
            it.hideProgressLoading()
            it.renderErrorToShipmentForm(e)
            CartLogger.logOnErrorUpdateCartForCheckout(e, cartItemDataList)
        }
    }

    override fun onNext(data: UpdateCartData) {
        view?.let {
            it.hideProgressLoading()
            if (!data.isSuccess) {
                if (data.outOfServiceData.isOutOfService()) {
                    it.renderErrorToShipmentForm(data.outOfServiceData)
                } else {
                    it.renderErrorToShipmentForm(data.message, if (data.toasterActionData.showCta) data.toasterActionData.text else "")
                }
                CartLogger.logOnErrorUpdateCartForCheckout(MessageErrorException(data.message), cartItemDataList)
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

    private fun getChecklistCondition(): Int {
        var checklistCondition = CartListPresenter.ITEM_CHECKED_ALL_WITHOUT_CHANGES
        val cartShopHolderDataList = view?.getAllShopDataList()

        if (cartShopHolderDataList?.size ?: 0 == 1) {
            cartShopHolderDataList?.get(0)?.productUiModelList?.let {
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
                        cartShopHolderData.productUiModelList?.let { cartItemHolderDataList ->
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

    private fun isCheckoutProductEligibleForCashOnDelivery(cartItemDataList: List<CartItemHolderData>): Boolean {
        var totalAmount = 0.0
        for (cartItemData in cartItemDataList) {
            val itemPriceAmount = cartItemData.productPrice * cartItemData.quantity
            totalAmount += itemPriceAmount
            if (!cartItemData.isCod) return false
        }
        return totalAmount <= MAX_TOTAL_AMOUNT_ELIGIBLE_FOR_COD
    }

    companion object {
        const val MAX_TOTAL_AMOUNT_ELIGIBLE_FOR_COD = 1000000.0
    }
}