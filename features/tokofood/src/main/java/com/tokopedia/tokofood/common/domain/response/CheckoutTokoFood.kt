package com.tokopedia.tokofood.common.domain.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.RemoveItemTokoFoodParam
import com.tokopedia.tokofood.common.minicartwidget.view.MiniCartUiModel

data class CheckoutTokoFoodResponse(
    @SerializedName("cart_list_tokofood")
    @Expose
    val cartListTokofood: CheckoutTokoFood = CheckoutTokoFood()
)

data class MiniCartTokoFoodResponse(
    @SerializedName("mini_cart_tokofood")
    @Expose
    val miniCartTokofood: CheckoutTokoFood = CheckoutTokoFood()
)

data class CheckoutTokoFood(
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("status")
    @Expose
    val status: String = "",
    @SerializedName("data")
    @Expose
    val data: CheckoutTokoFoodData = CheckoutTokoFoodData()
) {

    fun isSuccess(): Boolean = status == TokoFoodCartUtil.SUCCESS_STATUS
    fun getMessageIfError(): String {
        return if (status == TokoFoodCartUtil.ERROR_STATUS) {
            message
        } else {
            String.EMPTY
        }
    }

    /**
     * Get whether the components in the checkout page can be interactable
     */
    fun isEnabled(): Boolean = isSuccess() && data.errorTickers.top.message.isEmpty()

}

data class CheckoutTokoFoodData(
    @SerializedName("popup_message")
    @Expose
    val popupMessage: String = "",
    @SerializedName("popup_error_message")
    @Expose
    val popupErrorMessage: String = "",
    @SerializedName("popup_message_type")
    @Expose
    val popupMessageType: String = "",
    @SerializedName("shop")
    @Expose
    val shop: CheckoutTokoFoodShop = CheckoutTokoFoodShop(),
    @SerializedName("tickers")
    @Expose
    val tickers: CheckoutTokoFoodTicker = CheckoutTokoFoodTicker(),
    @SerializedName("error_tickers")
    @Expose
    val errorTickers: CheckoutTokoFoodTicker = CheckoutTokoFoodTicker(),
    @SerializedName("error_unblocking")
    @Expose
    val errorsUnblocking: String = "",
    @SerializedName("user_address")
    @Expose
    val userAddress: CheckoutTokoFoodUserAddress = CheckoutTokoFoodUserAddress(),
    @SerializedName("available_section")
    @Expose
    val availableSection: CheckoutTokoFoodAvailabilitySection = CheckoutTokoFoodAvailabilitySection(),
    @SerializedName("unavailable_section_header")
    @Expose
    val unavailableSectionHeader: String = "",
    @SerializedName("unavailable_sections")
    @Expose
    val unavailableSections: List<CheckoutTokoFoodAvailabilitySection> = listOf(),
    @SerializedName("shipping")
    @Expose
    val shipping: CheckoutTokoFoodShipping = CheckoutTokoFoodShipping(),
    @SerializedName("promo")
    @Expose
    val promo: CheckoutTokoFoodPromo = CheckoutTokoFoodPromo(),
    @SerializedName("checkout_consent_bottomsheet")
    @Expose
    val checkoutConsentBottomSheet: CheckoutTokoFoodConsentBottomSheet = CheckoutTokoFoodConsentBottomSheet(),
    @SerializedName("shopping_summary")
    @Expose
    val shoppingSummary: CheckoutTokoFoodShoppingSummary = CheckoutTokoFoodShoppingSummary(),
    @SerializedName("summary_detail")
    @Expose
    val summaryDetail: CheckoutTokoFoodSummaryDetail = CheckoutTokoFoodSummaryDetail(),
    @SerializedName("checkout_additional_data")
    @Expose
    val checkoutAdditionalData: CheckoutTokoFoodAdditionalData = CheckoutTokoFoodAdditionalData()
) {
    companion object {
        private const val POPUP_TYPE_PROMO = "promo"
    }

    fun isPromoPopupType(): Boolean = popupMessageType == POPUP_TYPE_PROMO


    fun getMiniCartUiModel(): MiniCartUiModel {
        val totalPrice =
            if (summaryDetail.details.isEmpty()) {
                // From mini cart gql
                summaryDetail.totalPrice
            } else {
                // From cart list gql
                shoppingSummary.costBreakdown.totalCartPrice.originalAmount.getCurrencyFormatted()
            }
        return MiniCartUiModel(
            shopName = shop.name,
            totalPriceFmt = totalPrice,
            totalProductQuantity = summaryDetail.totalItems
        )
    }

    fun getRemoveUnavailableCartParam(shopId: String): RemoveCartTokoFoodParam {
        val cartList = unavailableSections.firstOrNull()?.products?.map {
            it.mapToRemoveItemParam(shopId)
        }.orEmpty()
        return RemoveCartTokoFoodParam(cartList)
    }

    fun getRemoveAllCartParam(shopId: String): RemoveCartTokoFoodParam {
        val cartList = getProductListFromCart().map {
            it.mapToRemoveItemParam(shopId)
        }
        return RemoveCartTokoFoodParam(carts = cartList)
    }

    fun getProductListFromCart(): List<CheckoutTokoFoodProduct> {
        return availableSection.products.plus(unavailableSections.firstOrNull()?.products.orEmpty())
    }
}

data class CheckoutTokoFoodShop(
    @SerializedName("shop_id")
    @Expose
    val shopId: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("distance")
    @Expose
    val distance: String = ""
)

data class CheckoutTokoFoodTicker(
    @SerializedName("top")
    @Expose
    val top: CheckoutTokoFoodTickerInfo = CheckoutTokoFoodTickerInfo(),
    @SerializedName("bottom")
    @Expose
    val bottom: CheckoutTokoFoodTickerInfo = CheckoutTokoFoodTickerInfo()
)

data class CheckoutTokoFoodTickerInfo(
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("page")
    @Expose
    val page: String = ""
)

data class CheckoutTokoFoodUserAddress(
    @SerializedName("address_id")
    @Expose
    val addressId: String = "",
    @SerializedName("address_name")
    @Expose
    val addressName: String = "",
    @SerializedName("address")
    @Expose
    val address: String = "",
    @SerializedName("phone")
    @Expose
    val phone: String = "",
    @SerializedName("receiver_name")
    @Expose
    val receiverName: String = "",
    @SerializedName("status")
    @Expose
    val status: Int = 0
) {
    fun isMainAddress(): Boolean = status == TokoFoodCartUtil.IS_MAIN_ADDRESS_STATUS
}

data class CheckoutTokoFoodAvailabilitySection(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("products")
    @Expose
    val products: List<CheckoutTokoFoodProduct> = listOf()
)

data class CheckoutTokoFoodProduct(
    @SerializedName("cart_id")
    @Expose
    val cartId: String = "",
    @SerializedName("product_id")
    @Expose
    val productId: String = "",
    @SerializedName("category_id")
    @Expose
    val categoryId: String = "",
    @SerializedName("name")
    @Expose
    val productName: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",
    @SuppressLint("Invalid Data Type") 
    @SerializedName("price")
    @Expose
    val price: Double = 0.0,
    @SerializedName("price_fmt")
    @Expose
    val priceFmt: String = "",
    @SerializedName("original_price")
    @Expose
    val originalPrice: Double = 0.0,
    @SerializedName("original_price_fmt")
    @Expose
    val originalPriceFmt: String = "",
    @SerializedName("discount_percentage")
    @Expose
    val discountPercentage: String = "",
    @SerializedName("notes")
    @Expose
    val notes: String = "",
    @SerializedName("quantity")
    @Expose
    val quantity: Int = 0,
    @SerializedName("variants")
    @Expose
    val variants: List<CheckoutTokoFoodProductVariant> = listOf()
) {
    fun mapToRemoveItemParam(shopId: String): RemoveItemTokoFoodParam {
        return RemoveItemTokoFoodParam(
            cartId = cartId.toLongOrZero(),
            productId = productId,
            shopId = shopId
        )
    }
}

data class CheckoutTokoFoodProductVariant(
    @SerializedName("variant_id")
    @Expose
    val variantId: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("rules")
    @Expose
    val rules: CheckoutTokoFoodProductVariantRules = CheckoutTokoFoodProductVariantRules(),
    @SerializedName("options")
    @Expose
    val options: List<CheckoutTokoFoodProductVariantOption> = listOf()
)

data class CheckoutTokoFoodProductVariantRules(
    @SerializedName("selection_rule")
    @Expose
    val selectionRules: CheckoutTokoFoodProductVariantSelectionRules = CheckoutTokoFoodProductVariantSelectionRules()
)

data class CheckoutTokoFoodProductVariantSelectionRules(
    @SerializedName("type")
    @Expose
    val type: Int = 0,
    @SerializedName("max_quantity")
    @Expose
    val maxQuantity: Int = 0,
    @SerializedName("min_quantity")
    @Expose
    val minQuantity: Int = 0,
    @SerializedName("required")
    @Expose
    val isRequired: Boolean = false
) {
    companion object {
        // Unspecified.
        const val TYPE_UNSPECIFIED = 0
        // Allows only one option to be selected.
        const val SELECT_ONE = 1
        // Allows multiple options to be selected.
        const val SELECT_MANY = 2
    }
}

data class CheckoutTokoFoodProductVariantOption(
    @SerializedName("is_selected")
    @Expose
    val isSelected: Boolean = false,
    @SerializedName("option_id")
    @Expose
    val optionId: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    @Expose
    val price: Double = 0.0,
    @SerializedName("price_fmt")
    @Expose
    val priceFmt: String = "",
    @SerializedName("status")
    @Expose
    val status: Int = 0
) {
    companion object {
        // Unspecified.
        const val STATUS_UNSPECIFIED = 0
        // Item is available.
        const val ACTIVE = 1
        // Item is disabled.
        const val INACTIVE = 2
        // Item is out of stock.
        const val OUT_OF_STOCK = 3
        // Deleted item.
        const val DELETED = 4
    }

    fun isOutOfStock(): Boolean = status == OUT_OF_STOCK
}

data class CheckoutTokoFoodShipping(
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("logo_url")
    @Expose
    val logoUrl: String = "",
    @SerializedName("eta")
    @Expose
    val eta: String = "",
    @SerializedName("price")
    @Expose
    val price: Double = 0.0,
    @SerializedName("price_fmt")
    @Expose
    val priceFmt: String = ""
)

data class CheckoutTokoFoodPromo(
    @SerializedName("is_promo_applied")
    @Expose
    val isPromoApplied: Boolean = false,
    @SerializedName("hide_promo")
    @Expose
    val hidePromo: Boolean = false,
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("subtitle")
    @Expose
    val subtitle: String = ""
)

data class CheckoutTokoFoodConsentBottomSheet(
    @SerializedName("is_show_bottomsheet")
    @Expose
    val isShowBottomsheet: Boolean = false,
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("terms_and_condition")
    @Expose
    val termsAndCondition: String = ""
)

data class CheckoutTokoFoodShoppingSummary(
    @SerializedName("total")
    @Expose
    val total: CheckoutTokoFoodShoppingTotal = CheckoutTokoFoodShoppingTotal(),
    @SerializedName("cost_breakdown")
    @Expose
    val costBreakdown: CheckoutTokoFoodShoppingCostBreakdown = CheckoutTokoFoodShoppingCostBreakdown(),
    @SerializedName("discount_breakdown")
    @Expose
    val discountBreakdown: List<CheckoutTokoFoodShoppingDiscountBreakdown> = listOf()
)

data class CheckoutTokoFoodShoppingTotal(
    @SerializedName("cost")
    @Expose
    val cost: Double = 0.0,
    @SerializedName("savings")
    @Expose
    val savings: Double = 0.0
)

data class CheckoutTokoFoodShoppingCostBreakdown(
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_cart_price")
    @Expose
    val totalCartPrice: CheckoutTokoFoodShoppingCostBreakdownItem = CheckoutTokoFoodShoppingCostBreakdownItem(),
    @SerializedName("outlet_fee")
    @Expose
    val outletFee: CheckoutTokoFoodShoppingCostBreakdownItem = CheckoutTokoFoodShoppingCostBreakdownItem(),
    @SerializedName("platform_fee")
    @Expose
    val platformFee: CheckoutTokoFoodShoppingCostBreakdownItem = CheckoutTokoFoodShoppingCostBreakdownItem(),
    @SerializedName("delivery_fee")
    @Expose
    val deliveryFee: CheckoutTokoFoodShoppingCostBreakdownItem = CheckoutTokoFoodShoppingCostBreakdownItem(),
    @SerializedName("reimbursement_fee")
    @Expose
    val reimbursementFee: CheckoutTokoFoodShoppingCostBreakdownItem = CheckoutTokoFoodShoppingCostBreakdownItem()
)

data class CheckoutTokoFoodShoppingCostBreakdownItem(
    @SerializedName("original_amount")
    @Expose
    val originalAmount: Double = 0.0,
    @SerializedName("amount")
    @Expose
    val amount: Double = 0.0,
    @SerializedName("surge")
    @Expose
    val surge: CheckoutTokoFoodShoppingSurge = CheckoutTokoFoodShoppingSurge()
)

data class CheckoutTokoFoodShoppingSurge(
    @SerializedName("is_surge_price")
    @Expose
    val isSurgePrice: Boolean = false,
    @SerializedName("factor")
    @Expose
    val factor: Double = 0.0
)

data class CheckoutTokoFoodShoppingDiscountBreakdown(
    @SerializedName("discount_id")
    @Expose
    val discountId: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("amount")
    @Expose
    val amount: Double = 0.0,
    @SerializedName("scope")
    @Expose
    val scope: String = "",
    @SerializedName("type")
    @Expose
    val type: String = ""
)

data class CheckoutTokoFoodAdditionalData(
    @SerializedName("data_type")
    @Expose
    val dataType: String = "",
    @SerializedName("checkout_business_id")
    @Expose
    val checkoutBusinessId: String = ""
)

data class CheckoutTokoFoodSummaryDetail(
    @SerializedName("hide_summary")
    @Expose
    val hideSummary: Boolean = false,
    @SerializedName("total_items")
    @Expose
    val totalItems: Int = 0,
    @SerializedName("total_price")
    @Expose
    val totalPrice: String = "",
    @SerializedName("details")
    @Expose
    val details: List<CheckoutTokoFoodSummaryItemDetail> = listOf()

)

data class CheckoutTokoFoodSummaryItemDetail(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("price_fmt")
    @Expose
    val priceFmt: String = "",
    @SerializedName("info")
    @Expose
    val info: CheckoutTokoFoodSummaryItemDetailInfo? = null
)

data class CheckoutTokoFoodSummaryItemDetailInfo(
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",
    @SerializedName("bottomsheet")
    @Expose
    val bottomSheet: CheckoutTokoFoodSummaryItemDetailBottomSheet = CheckoutTokoFoodSummaryItemDetailBottomSheet()
)

data class CheckoutTokoFoodSummaryItemDetailBottomSheet(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("description")
    @Expose
    val description: String = ""
)