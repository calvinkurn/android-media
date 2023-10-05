package com.tokopedia.thankyou_native.analytics

import android.text.TextUtils
import androidx.compose.ui.unit.TextUnit
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerCommerceData
import com.tokopedia.linker.model.PaymentData
import com.tokopedia.linker.model.UserData
import com.tokopedia.thankyou_native.data.mapper.DigitalThankPage
import com.tokopedia.thankyou_native.data.mapper.ThankPageTypeMapper
import com.tokopedia.thankyou_native.domain.model.PurchaseItem
import com.tokopedia.thankyou_native.domain.model.ShopOrder
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.user.session.UserSessionInterface


const val CATEGORY_LEVEL_ONE_EGOLD = "egold"
const val CATEGORY_LEVEL_ONE_PURCHASE_PROTECTION = "purchase-protection"
const val DIGITAL_PLUS_IDENTIFIER = "PULSA_PLUS"
const val MARKETPLACE_PLUS_IDENTIFIER = "PLUS"

class BranchPurchaseEvent(val userSession: UserSessionInterface,
                          val thanksPageData: ThanksPageData) {

    fun sendBranchPurchaseEvent() {
        thanksPageData.shopOrder.forEach { shopOrder ->
            val linkerCommerceData = LinkerCommerceData()
            linkerCommerceData.userData = getLinkerUserData()
            linkerCommerceData.paymentData = getBranchPaymentData(shopOrder)
            sendBranchEvent(linkerCommerceData)
            sendGoPayBranchEvent(thanksPageData, shopOrder)
        }
        sendSubscribePlusEvent(thanksPageData)
    }

    private fun sendGoPayBranchEvent(
        thanksPageData: ThanksPageData,
        shopOrder: ShopOrder
    ) {
        val goPayEventList = thanksPageData.paymentDetails?.filter {
            it.gatewayCode == GATEWAY_CODE_PEMUDA ||
            it.gatewayCode == GATEWAY_CODE_PEMUDA_PAYLATER ||
            it.gatewayCode == GATEWAT_CODE_PEMUDA_CICIL
        }
        goPayEventList?.forEach { paymentDetail ->
            val linkerCommerceData = LinkerCommerceData()
            linkerCommerceData.userData = getLinkerUserData()
            val branchPaymentData = getBranchPaymentData(shopOrder)
            branchPaymentData.paymentEventName = paymentDetail.gatewayCode
            linkerCommerceData.paymentData = branchPaymentData
            sendBranchEvent(linkerCommerceData)
        }

    }

    private fun getBranchPaymentData(shopOrder: ShopOrder): PaymentData {
        val paymentData = PaymentData()
        paymentData.isFromNative = true
        paymentData.setPaymentId(thanksPageData.paymentID)
        paymentData.setOrderId(shopOrder.orderId)
        paymentData.setShipping(shopOrder.shippingAmount.toString())
        paymentData.setProductType(getProductTypeForBranch())
        paymentData.isNewBuyer = thanksPageData.isNewUser
        paymentData.isMonthlyNewBuyer = thanksPageData.isMonthlyNewUser
        var revenue = 0.0
        shopOrder.purchaseItemList.forEach { purchaseItem ->
            if (isItemPartOfRevenue(purchaseItem)) {
                revenue += purchaseItem.totalPrice
                paymentData.setProduct(getPurchasedItemBranch(purchaseItem))
            }
        }
        paymentData.setRevenue(revenue.toString())
        return paymentData
    }

    private fun isItemPartOfRevenue(purchaseItem: PurchaseItem): Boolean {
        val categoryLevelOne = getCategoryLevel1(purchaseItem.category)
        return !(getProductTypeForBranch() == LinkerConstants.PRODUCTTYPE_DIGITAL &&
                (categoryLevelOne == CATEGORY_LEVEL_ONE_EGOLD ||
                        categoryLevelOne == CATEGORY_LEVEL_ONE_PURCHASE_PROTECTION))
    }

    private fun getPurchasedItemBranch(productItem: PurchaseItem): HashMap<String, String> {
        val product = HashMap<String, String>()
        product[LinkerConstants.ID] = productItem.productId
        product[LinkerConstants.NAME] = productItem.productName
        product[LinkerConstants.PRICE] = productItem.price.toString()
        product[LinkerConstants.PRICE_IDR_TO_DOUBLE] = productItem.price.toString()
        product[LinkerConstants.QTY] = productItem.quantity.toString()
        product[LinkerConstants.CATEGORY] = getCategoryLevel1(productItem.category)
        return product
    }

    private fun getLinkerUserData(): UserData {
        val userData = UserData()
        userData.userId = userSession.userId
        userData.phoneNumber = userSession.phoneNumber
        userData.name = userSession.name
        userData.email = userSession.email
        return userData
    }

    private fun getProductTypeForBranch(): String {
        return when (ThankPageTypeMapper.getThankPageType(thanksPageData)) {
            DigitalThankPage -> LinkerConstants.PRODUCTTYPE_DIGITAL
            else -> LinkerConstants.PRODUCTTYPE_MARKETPLACE
        }
    }

    private fun getCategoryLevel1(category: String?): String {
        return if (category.isNullOrBlank()) {
            ""
        } else {
            category.split("_")[0]
        }
    }

    private fun sendBranchEvent(linkerCommerceData: LinkerCommerceData) {
        LinkerManager.getInstance()
                .sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_COMMERCE_VAL,
                        linkerCommerceData))
    }

    private fun sendBranchEvent(linkerCommerceData: LinkerCommerceData, branchEventType: Int) {
        LinkerManager.getInstance()
            .sendEvent(LinkerUtils.createGenericRequest(branchEventType,
                linkerCommerceData))
    }

    private fun sendSubscribePlusEvent(thanksPageData: ThanksPageData){
        when(getProductTypeForBranch()){
            LinkerConstants.PRODUCTTYPE_DIGITAL -> {
                //check profile_code = PULSA_PLUS
                if(!TextUtils.isEmpty(thanksPageData.profileCode) &&
                    thanksPageData.profileCode == DIGITAL_PLUS_IDENTIFIER){
                    //send subscribe plus event
                    val linkerCommerceData = getLinkerCommerceData(getLinkerUserData(),
                        getPaymentDataForSubscribePlus(thanksPageData, thanksPageData.amount.toString(),
                            LinkerConstants.PRODUCTTYPE_DIGITAL))
                    sendBranchEvent(linkerCommerceData, LinkerConstants.EVENT_SUBSCRIBE_PLUS)
                }
            }
            LinkerConstants.PRODUCTTYPE_MARKETPLACE -> {
                //check payment_items have item_name containing PLUS
                val iterator = thanksPageData.paymentItems?.iterator()
                if (iterator != null) {
                    while (iterator.hasNext()){
                        val paymentItem = iterator.next()
                        if(paymentItem.itemName == MARKETPLACE_PLUS_IDENTIFIER){
                            // send subscribe plus event
                            val linkerCommerceData = getLinkerCommerceData(getLinkerUserData(),
                                getPaymentDataForSubscribePlus(thanksPageData, paymentItem.amount.toString(),
                                    LinkerConstants.PRODUCTTYPE_MARKETPLACE))
                            sendBranchEvent(linkerCommerceData, LinkerConstants.EVENT_SUBSCRIBE_PLUS)
                            break
                        }
                    }
                }
            }
        }
    }

    private fun getPaymentDataForSubscribePlus(thanksPageData: ThanksPageData,
                                               revenue: String, productType: String) : PaymentData{
        val paymentData = PaymentData()
        paymentData.isFromNative = true
        paymentData.setPaymentId(thanksPageData.paymentID)
        paymentData.setProductType(productType)
        paymentData.isNewBuyer = thanksPageData.isNewUser
        paymentData.isMonthlyNewBuyer = thanksPageData.isMonthlyNewUser
        paymentData.revenue = revenue
        return paymentData
    }

    private fun getLinkerCommerceData(userData: UserData, paymentData: PaymentData): LinkerCommerceData{
        val linkerCommerceData = LinkerCommerceData()
        linkerCommerceData.userData = userData
        linkerCommerceData.paymentData = paymentData
        return linkerCommerceData
    }

    companion object {
        const val GATEWAY_CODE_PEMUDA = "PEMUDA"
        const val GATEWAY_CODE_PEMUDA_PAYLATER = "PEMUDAPAYLATER"
        const val GATEWAT_CODE_PEMUDA_CICIL = "PEMUDACICIL"
    }
}
