package com.tokopedia.tokofood.common.domain.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO

data class CartListTokofoodResponse(
    @SerializedName("cart_general_cart_list")
    val cartGeneralCartList: CartGeneralCartList 
) {

    fun isSuccess(): Boolean = cartGeneralCartList.data.success == Int.ONE

}

data class CartGeneralCartList(
    @SerializedName("data")
    val data: CartGeneralCartListData = CartGeneralCartListData()
)

data class CartGeneralCartListData(
    @SerializedName("success")
    val success: Int = Int.ZERO,
    @SerializedName("message")
    val message: String = String.EMPTY,
    @SerializedName("data")
    val data: CartListData = CartListData()
)

data class CartListData(
    @SerializedName("shopping_summary")
    val shoppingSummary: CartListShoppingSummary = CartListShoppingSummary(),
    @SerializedName("business_data")
    val businessData: List<CartListBusinessData> = listOf()
)

data class CartListShoppingSummary(
    @SerializedName("business_breakdown")
    val businessBreakdowns: List<CartListBusinessBreakdown> = listOf(),
)

data class CartListBusinessBreakdown(
    @SerializedName("business_id")
    val businessId: String = String.EMPTY,
    @SerializedName("custom_response")
    val customResponse: CartListBusinessBreakdownCustomResponse = CartListBusinessBreakdownCustomResponse(),
    @SerializedName("total_bill")
    val totalBill: Double = 0.0,
    @SerializedName("total_bill_fmt")
    val totalBillFmt: String = String.EMPTY,
    @SerializedName("product")
    val product: CartListBusinessBreakdownProduct = CartListBusinessBreakdownProduct(),
    @SerializedName("add_ons")
    val addOns: CartListBusinessBreakdownAddOns = CartListBusinessBreakdownAddOns()
)

data class CartListBusinessBreakdownCustomResponse(
    @SerializedName("hide_summary")
    val hideSummary: Boolean = false
)

data class CartListBusinessBreakdownProduct(
    @SerializedName("title")
    val title: String = String.EMPTY,
    @SerializedName("total_price")
    val totalPrice: Double = 0.0,
    @SerializedName("total_price_fmt")
    val totalPriceFmt: String = String.EMPTY,
    @SerializedName("total_quantity")
    val totalQuantity: Int = Int.ZERO,
    @SerializedName("total_cart")
    val totalCart: Int = Int.ZERO
)

data class CartListBusinessBreakdownAddOns(
    @SerializedName("title")
    val title: String = String.EMPTY,
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("price_fmt")
    val priceFmt: String = String.EMPTY,
    @SerializedName("custom_response")
    val customResponse: CartListAddOnsCustomResponse = CartListAddOnsCustomResponse()
)

data class CartListAddOnsCustomResponse(
    @SerializedName("info")
    val info: CartListAddOnsCustomResponseInfo = CartListAddOnsCustomResponseInfo()
)

data class CartListAddOnsCustomResponseInfo(
    @SerializedName("image_url")
    val imageUrl: String = String.EMPTY,
    @SerializedName("bottomsheet")
    val bottomsheet: CartListAddOnsCustomResponseBottomsheet = CartListAddOnsCustomResponseBottomsheet()
)

data class CartListAddOnsCustomResponseBottomsheet(
    @SerializedName("title")
    val title: String = String.EMPTY,
    @SerializedName("description")
    val description: String = String.EMPTY
)

data class CartListBusinessData(
    @SerializedName("business_id")
    val businessId: String = String.EMPTY,
    @SerializedName("success")
    val success: Int = Int.ONE,
    @SerializedName("message")
    val message: String = String.EMPTY,
    @SerializedName("ticker")
    val ticker: CartListBusinessDataTicker = CartListBusinessDataTicker(),
    @SerializedName("additional_grouping")
    val additionalGrouping: CartListBusinessDataAdditionalGrouping = CartListBusinessDataAdditionalGrouping(),
    @SerializedName("custom_response")
    val customResponse: CartListBusinessDataCustomResponse = CartListBusinessDataCustomResponse(),
    @SerializedName("cart_groups")
    val cartGroups: List<CartListBusinessDataCartGroup> = listOf()
)

data class CartListBusinessDataTicker(
    @SerializedName("top")
    val top: CartListBusinessDataTickerInfo = CartListBusinessDataTickerInfo(),
    @SerializedName("bottom")
    val bottom: CartListBusinessDataTickerInfo = CartListBusinessDataTickerInfo(),
    @SerializedName("error_tickers")
    val errorTickers: CartListBusinessDataErrorTicker = CartListBusinessDataErrorTicker()
)

data class CartListBusinessDataTickerInfo(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("page")
    val page: String = ""
)

data class CartListBusinessDataErrorTicker(
    @SerializedName("top")
    val top: CartListBusinessDataTickerInfo = CartListBusinessDataTickerInfo(),
    @SerializedName("bottom")
    val bottom: CartListBusinessDataTickerInfo = CartListBusinessDataTickerInfo()
)

data class CartListBusinessDataAdditionalGrouping(
    @SerializedName("detail")
    val details: List<CartListBusinessDataAdditionalGroupingDetail> = listOf()
)

data class CartListBusinessDataAdditionalGroupingDetail(
    @SerializedName("additional_group_id")
    val additionalGroupId: String = String.EMPTY,
    @SerializedName("cart_ids")
    val cartIds: List<String> = listOf()
)

data class CartListBusinessDataCustomResponse(
    @SerializedName("user_address")
    val userAddress: CartListBusinessDataUserAddress = CartListBusinessDataUserAddress(),
    @SerializedName("promo")
    val promo: CartListBusinessDataPromo = CartListBusinessDataPromo(),
    @SerializedName("popup_message")
    val popupMessage: String = String.EMPTY,
    @SerializedName("popup_error_message")
    val popupErrorMessage: String = String.EMPTY,
    @SerializedName("popup_message_type")
    val popupMessageType: String = String.EMPTY,
    @SerializedName("error_unblocking")
    val errorUnblocking: String = String.EMPTY,
    @SerializedName("bottomsheet")
    val bottomSheet: CartListBusinessDataBottomSheet = CartListBusinessDataBottomSheet(),
    @SerializedName("shop")
    val shop: CartListBusinessDataShop = CartListBusinessDataShop(),
    @SerializedName("shipping")
    val shipping: CartListBusinessDataShipping = CartListBusinessDataShipping(),
    @SerializedName("checkout_additional_data")
    val checkoutAdditionalData: CartListBusinessDataCheckoutAdditionalData = CartListBusinessDataCheckoutAdditionalData(),
    @SerializedName("shopping_summary")
    val shoppingSummary: CartListBusinessDataShoppingSummary = CartListBusinessDataShoppingSummary()
)

data class CartListBusinessDataUserAddress(
    @SerializedName("address_id")
    val addressId: Long = 0L,
    @SerializedName("address_name")
    val addressName: String = String.EMPTY,
    @SerializedName("address")
    val address: String = String.EMPTY,
    @SerializedName("phone")
    val phone: String = String.EMPTY,
    @SerializedName("receiver_name")
    val receiverName: String = String.EMPTY,
    @SerializedName("status")
    val status: Int = Int.ZERO
)

data class CartListBusinessDataPromo(
    @SerializedName("is_promo_applied")
    val isPromoApplied: Boolean = false,
    @SerializedName("hide_promo")
    val hidePromo: Boolean = false,
    @SerializedName("title")
    val title: String = String.EMPTY,
    @SerializedName("subtitle")
    val subtitle: String = String.EMPTY
)

data class CartListBusinessDataBottomSheet(
    @SerializedName("is_show_bottomsheet")
    val isShowBottomSheet: Boolean = false,
    @SerializedName("image_url")
    val imageUrl: String = String.EMPTY,
    @SerializedName("title")
    val title: String = String.EMPTY,
    @SerializedName("description")
    val description: String = String.EMPTY,
    @SerializedName("terms_and_condition")
    val termsAndCondition: String = String.EMPTY,
    @SerializedName("buttons")
    val buttons: List<CartListBusinessDataBottomSheetButton> = listOf()
)

data class CartListBusinessDataBottomSheetButton(
    @SerializedName("text")
    val text: String = String.EMPTY,
    @SerializedName("color")
    val color: String = String.EMPTY,
    @SerializedName("action")
    val action: Int = Int.ZERO,
    @SerializedName("link")
    val link: String = String.EMPTY
)

data class CartListBusinessDataShop(
    @SerializedName("shop_id")
    val shopId: String = String.EMPTY,
    @SerializedName("name")
    val name: String = String.EMPTY,
    @SerializedName("distance")
    val distance: String = String.EMPTY
)

data class CartListBusinessDataShipping(
    @SerializedName("name")
    val name: String = String.EMPTY,
    @SerializedName("logo_url")
    val logoUrl: String = String.EMPTY,
    @SerializedName("eta")
    val eta: String = String.EMPTY,
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("price_fmt")
    val priceFmt: String = String.EMPTY
)

data class CartListBusinessDataCheckoutAdditionalData(
    @SerializedName("data_type")
    val dataType: String = String.EMPTY,
    @SerializedName("flow_type")
    val flowType: String = String.EMPTY,
    @SerializedName("checkout_business_id")
    val checkoutBusinessId: Int = Int.ZERO
)

data class CartListBusinessDataCartGroup(
    @SerializedName("cart_group_id")
    val cartGroupId: String = String.EMPTY,
    @SerializedName("success")
    val success: Int = Int.ZERO,
    @SerializedName("carts")
    val carts: List<CartListCartGroupCart> = listOf()
)

data class CartListBusinessDataShoppingSummary(
    @SerializedName("cost_breakdown")
    val costBreakdown: CartListBusinessDataShoppingCostBreakdown = CartListBusinessDataShoppingCostBreakdown(),
    @SerializedName("discount_breakdown")
    val discountBreakdown: List<CartListBusinessDataShoppingDiscountBreakdown> = listOf(),
    @SerializedName("total_items")
    val totalItems: Int = Int.ZERO,
    @SerializedName("total")
    val total: CartListBusinessDataShoppingTotal = CartListBusinessDataShoppingTotal()
)

data class CartListBusinessDataShoppingTotal(
    @SerializedName("cost")
    val cost: Double = 0.0,
    @SerializedName("savings")
    val savings: Double = 0.0
)

data class CartListBusinessDataShoppingCostBreakdown(
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_cart_price")
    val totalCartPrice: CartListBusinessDataShoppingCostBreakdownItem = CartListBusinessDataShoppingCostBreakdownItem(),
    @SerializedName("outlet_fee")
    val outletFee: CartListBusinessDataShoppingCostBreakdownItem = CartListBusinessDataShoppingCostBreakdownItem(),
    @SerializedName("platform_fee")
    val platformFee: CartListBusinessDataShoppingCostBreakdownItem = CartListBusinessDataShoppingCostBreakdownItem(),
    @SerializedName("delivery_fee")
    val deliveryFee: CartListBusinessDataShoppingCostBreakdownItem = CartListBusinessDataShoppingCostBreakdownItem(),
    @SerializedName("reimbursement_fee")
    val reimbursementFee: CartListBusinessDataShoppingCostBreakdownItem = CartListBusinessDataShoppingCostBreakdownItem()
)

data class CartListBusinessDataShoppingCostBreakdownItem(
    @SerializedName("original_amount")
    val originalAmount: Double = 0.0,
    @SerializedName("amount")
    val amount: Double = 0.0,
    @SerializedName("surge")
    val surge: CartListBusinessDataShoppingSurge = CartListBusinessDataShoppingSurge()
)

data class CartListBusinessDataShoppingSurge(
    @SuppressLint("Invalid Data Type")
    @SerializedName("is_surge_price")
    val isSurgePrice: Boolean = false,
    @SerializedName("factor")
    val factor: Double = 0.0
)

data class CartListBusinessDataShoppingDiscountBreakdown(
    @SerializedName("discount_id")
    val discountId: String = String.EMPTY,
    @SerializedName("title")
    val title: String = String.EMPTY,
    @SerializedName("amount")
    val amount: Double = 0.0,
    @SerializedName("scope")
    val scope: String = String.EMPTY,
    @SerializedName("type")
    val type: String = String.EMPTY
)

data class CartListCartGroupCart(
    @SerializedName("cart_id")
    val cartId: String = String.EMPTY,
    @SerializedName("success")
    val success: Int = Int.ZERO,
    @SerializedName("product_id")
    val productId: String = String.EMPTY,
    @SerializedName("shop_id")
    val shopId: String = String.EMPTY,
    @SerializedName("quantity")
    val quantity: Int = Int.ZERO,
    @SerializedName("metadata")
    val metadata: CartListCartMetadata = CartListCartMetadata(),
    @SerializedName("custom_response")
    val customResponse: CartListCartGroupCartCustomResponse = CartListCartGroupCartCustomResponse(),
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("price_fmt")
    val priceFmt: String = String.EMPTY
)

data class CartListCartMetadata(
    @SerializedName("notes")
    val notes: String = String.EMPTY,
    @SerializedName("variants")
    val variants: List<CartListCartMetadataVariant> = listOf()
)

data class CartListCartMetadataVariant(
    @SerializedName("variant_id")
    val variantId: String = String.EMPTY,
    @SerializedName("option_id")
    val optionId: String = String.EMPTY
)

data class CartListCartGroupCartCustomResponse(
    @SerializedName("category_id")
    val categoryId: String = String.EMPTY,
    @SerializedName("notes")
    val notes: String = String.EMPTY,
    @SerializedName("variants")
    val variants: List<CartListCartGroupCartVariant> = listOf(),
    @SerializedName("name")
    val name: String = String.EMPTY,
    @SerializedName("description")
    val description: String = String.EMPTY,
    @SerializedName("image_url")
    val imageUrl: String = String.EMPTY,
    @SerializedName("original_price")
    val originalPrice: Double = 0.0,
    @SerializedName("original_price_fmt")
    val originalPriceFmt: String = String.EMPTY,
    @SerializedName("discount_percentage")
    val discountPercentage: String = String.EMPTY
)

data class CartListCartGroupCartVariant(
    @SerializedName("variant_id")
    val variantId: String = String.EMPTY,
    @SerializedName("name")
    val name: String = String.EMPTY,
    @SerializedName("rules")
    val rules: CartListCartGroupCartRules = CartListCartGroupCartRules(),
    @SerializedName("option")
    val options: List<CartListCartGroupCartOption> = listOf()
)

data class CartListCartGroupCartRules(
    @SerializedName("selection_rule")
    val selectionRule: CartListCartGroupCartSelectionRule = CartListCartGroupCartSelectionRule()
)

data class CartListCartGroupCartSelectionRule(
    @SerializedName("type")
    val type: Int = Int.ZERO,
    @SerializedName("max_quantity")
    val maxQty: Int = Int.ZERO,
    @SerializedName("min_quantity")
    val minQty: Int = Int.ZERO,
    @SerializedName("required")
    val isRequired: Boolean = false
)

data class CartListCartGroupCartOption(
    @SerializedName("option_id")
    val optionId: String = String.EMPTY,
    @SerializedName("is_selected")
    val isSelected: Boolean = false,
    @SerializedName("name")
    val name: String = String.EMPTY,
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("price_fmt")
    val priceFmt: String = String.EMPTY
)

