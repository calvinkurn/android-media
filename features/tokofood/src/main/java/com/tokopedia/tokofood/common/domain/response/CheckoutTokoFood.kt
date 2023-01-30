package com.tokopedia.tokofood.common.domain.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokofoodBusinessData
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokofoodCartGroup
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokofoodParamNew
import com.tokopedia.tokofood.common.domain.param.RemoveItemTokoFoodParam
import com.tokopedia.tokofood.common.minicartwidget.view.MiniCartUiModel

data class CheckoutTokoFoodResponse(
    @SerializedName("cart_list_tokofood")
    val cartListTokofood: CheckoutTokoFood = CheckoutTokoFood()
)

data class MiniCartTokoFoodResponse(
    @SerializedName("mini_cart_tokofood")
    val miniCartTokofood: CheckoutTokoFood = CheckoutTokoFood()
)

data class CheckoutTokoFood(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("data")
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

    fun isEmptyProducts(): Boolean {
        return data.availableSection.products.isEmpty() && data.unavailableSections.firstOrNull()?.products.isNullOrEmpty()
    }

}

data class CheckoutTokoFoodData(
    @SerializedName("popup_message")
    val popupMessage: String = "",
    @SerializedName("popup_error_message")
    val popupErrorMessage: String = "",
    @SerializedName("popup_message_type")
    val popupMessageType: String = "",
    @SerializedName("shop")
    val shop: CheckoutTokoFoodShop = CheckoutTokoFoodShop(),
    @SerializedName("tickers")
    val tickers: CheckoutTokoFoodTicker = CheckoutTokoFoodTicker(),
    @SerializedName("error_tickers")
    val errorTickers: CheckoutTokoFoodTicker = CheckoutTokoFoodTicker(),
    @SerializedName("error_unblocking")
    val errorsUnblocking: String = "",
    @SerializedName("user_address")
    val userAddress: CheckoutTokoFoodUserAddress = CheckoutTokoFoodUserAddress(),
    @SerializedName("available_section")
    val availableSection: CheckoutTokoFoodAvailabilitySection = CheckoutTokoFoodAvailabilitySection(),
    @SerializedName("unavailable_section_header")
    val unavailableSectionHeader: String = "",
    @SerializedName("unavailable_sections")
    val unavailableSections: List<CheckoutTokoFoodAvailabilitySection> = listOf(),
    @SerializedName("shipping")
    val shipping: CheckoutTokoFoodShipping = CheckoutTokoFoodShipping(),
    @SerializedName("promo")
    val promo: CheckoutTokoFoodPromo = CheckoutTokoFoodPromo(),
    @SerializedName("checkout_consent_bottomsheet")
    val checkoutConsentBottomSheet: CheckoutTokoFoodConsentBottomSheet = CheckoutTokoFoodConsentBottomSheet(),
    @SerializedName("shopping_summary")
    val shoppingSummary: CheckoutTokoFoodShoppingSummary = CheckoutTokoFoodShoppingSummary(),
    @SerializedName("summary_detail")
    val summaryDetail: CheckoutTokoFoodSummaryDetail = CheckoutTokoFoodSummaryDetail(),
    @SerializedName("checkout_additional_data")
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
                shoppingSummary.costBreakdown.totalCartPrice.amount.getCurrencyFormatted()
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

    fun getRemoveAllCartParamNew(): RemoveCartTokofoodParamNew {
        // TODO: Add businessId
        return RemoveCartTokofoodParamNew(
            businessData = listOf(
                RemoveCartTokofoodBusinessData(
                    businessId = "",
                    cartGroups = listOf(
                        RemoveCartTokofoodCartGroup(
                            cartIds = getProductListFromCart().map { it.cartId }
                        )
                    )
                )
            )
        )
    }

    fun getProductListFromCart(): List<CheckoutTokoFoodProduct> {
        return availableSection.products.plus(unavailableSections.firstOrNull()?.products.orEmpty())
    }

    fun getShouldShowMiniCart(): Boolean {
        return shop.shopId.isNotBlank() && getProductListFromCart().isNotEmpty()
    }
}

data class CheckoutTokoFoodShop(
    @SerializedName("shop_id")
    val shopId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("distance")
    val distance: String = ""
)

data class CheckoutTokoFoodTicker(
    @SerializedName("top")
    val top: CheckoutTokoFoodTickerInfo = CheckoutTokoFoodTickerInfo(),
    @SerializedName("bottom")
    val bottom: CheckoutTokoFoodTickerInfo = CheckoutTokoFoodTickerInfo()
)

data class CheckoutTokoFoodTickerInfo(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("page")
    val page: String = ""
)

data class CheckoutTokoFoodUserAddress(
    @SerializedName("address_id")
    val addressId: String = "",
    @SerializedName("address_name")
    val addressName: String = "",
    @SerializedName("address")
    val address: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("receiver_name")
    val receiverName: String = "",
    @SerializedName("status")
    val status: Int = 0
) {
    fun isMainAddress(): Boolean = status == TokoFoodCartUtil.IS_MAIN_ADDRESS_STATUS
}

data class CheckoutTokoFoodAvailabilitySection(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("products")
    val products: List<CheckoutTokoFoodProduct> = listOf()
)

data class CheckoutTokoFoodProduct(
    @SerializedName("cart_id")
    val cartId: String = "",
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("category_id")
    val categoryId: String = "",
    @SerializedName("name")
    val productName: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SuppressLint("Invalid Data Type") 
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("price_fmt")
    val priceFmt: String = "",
    @SerializedName("original_price")
    val originalPrice: Double = 0.0,
    @SerializedName("original_price_fmt")
    val originalPriceFmt: String = "",
    @SerializedName("discount_percentage")
    val discountPercentage: String = "",
    @SerializedName("notes")
    val notes: String = "",
    @SerializedName("quantity")
    val quantity: Int = 0,
    @SerializedName("variants")
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
    val variantId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("rules")
    val rules: CheckoutTokoFoodProductVariantRules = CheckoutTokoFoodProductVariantRules(),
    @SerializedName("options")
    val options: List<CheckoutTokoFoodProductVariantOption> = listOf()
)

data class CheckoutTokoFoodProductVariantRules(
    @SerializedName("selection_rule")
    val selectionRules: CheckoutTokoFoodProductVariantSelectionRules = CheckoutTokoFoodProductVariantSelectionRules()
)

data class CheckoutTokoFoodProductVariantSelectionRules(
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("max_quantity")
    val maxQuantity: Int = 0,
    @SerializedName("min_quantity")
    val minQuantity: Int = 0,
    @SerializedName("required")
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
    val isSelected: Boolean = false,
    @SerializedName("option_id")
    val optionId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("price_fmt")
    val priceFmt: String = "",
    @SerializedName("status")
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
    val name: String = "",
    @SerializedName("logo_url")
    val logoUrl: String = "",
    @SerializedName("eta")
    val eta: String = "",
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("price_fmt")
    val priceFmt: String = ""
)

data class CheckoutTokoFoodPromo(
    @SerializedName("is_promo_applied")
    val isPromoApplied: Boolean = false,
    @SerializedName("hide_promo")
    val hidePromo: Boolean = false,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("subtitle")
    val subtitle: String = ""
)

data class CheckoutTokoFoodConsentBottomSheet(
    @SerializedName("is_show_bottomsheet")
    val isShowBottomsheet: Boolean = false,
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("terms_and_condition")
    val termsAndCondition: String = ""
)

data class CheckoutTokoFoodShoppingSummary(
    @SerializedName("total")
    val total: CheckoutTokoFoodShoppingTotal = CheckoutTokoFoodShoppingTotal(),
    @SerializedName("cost_breakdown")
    val costBreakdown: CheckoutTokoFoodShoppingCostBreakdown = CheckoutTokoFoodShoppingCostBreakdown(),
    @SerializedName("discount_breakdown")
    val discountBreakdown: List<CheckoutTokoFoodShoppingDiscountBreakdown> = listOf()
)

data class CheckoutTokoFoodShoppingTotal(
    @SerializedName("cost")
    val cost: Double = 0.0,
    @SerializedName("savings")
    val savings: Double = 0.0
)

data class CheckoutTokoFoodShoppingCostBreakdown(
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_cart_price")
    val totalCartPrice: CheckoutTokoFoodShoppingCostBreakdownItem = CheckoutTokoFoodShoppingCostBreakdownItem(),
    @SerializedName("outlet_fee")
    val outletFee: CheckoutTokoFoodShoppingCostBreakdownItem = CheckoutTokoFoodShoppingCostBreakdownItem(),
    @SerializedName("platform_fee")
    val platformFee: CheckoutTokoFoodShoppingCostBreakdownItem = CheckoutTokoFoodShoppingCostBreakdownItem(),
    @SerializedName("delivery_fee")
    val deliveryFee: CheckoutTokoFoodShoppingCostBreakdownItem = CheckoutTokoFoodShoppingCostBreakdownItem(),
    @SerializedName("reimbursement_fee")
    val reimbursementFee: CheckoutTokoFoodShoppingCostBreakdownItem = CheckoutTokoFoodShoppingCostBreakdownItem()
)

data class CheckoutTokoFoodShoppingCostBreakdownItem(
    @SerializedName("original_amount")
    val originalAmount: Double = 0.0,
    @SerializedName("amount")
    val amount: Double = 0.0,
    @SerializedName("surge")
    val surge: CheckoutTokoFoodShoppingSurge = CheckoutTokoFoodShoppingSurge()
)

data class CheckoutTokoFoodShoppingSurge(
    @SuppressLint("Invalid Data Type")
    @SerializedName("is_surge_price")
    val isSurgePrice: Boolean = false,
    @SerializedName("factor")
    val factor: Double = 0.0
)

data class CheckoutTokoFoodShoppingDiscountBreakdown(
    @SerializedName("discount_id")
    val discountId: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("amount")
    val amount: Double = 0.0,
    @SerializedName("scope")
    val scope: String = "",
    @SerializedName("type")
    val type: String = ""
)

data class CheckoutTokoFoodAdditionalData(
    @SerializedName("data_type")
    val dataType: String = "",
    @SerializedName("checkout_business_id")
    val checkoutBusinessId: String = ""
)

data class CheckoutTokoFoodSummaryDetail(
    @SerializedName("hide_summary")
    val hideSummary: Boolean = false,
    @SerializedName("total_items")
    val totalItems: Int = 0,
    @SerializedName("total_price")
    val totalPrice: String = "",
    @SerializedName("details")
    val details: List<CheckoutTokoFoodSummaryItemDetail> = listOf()

)

data class CheckoutTokoFoodSummaryItemDetail(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("price_fmt")
    val priceFmt: String = "",
    @SerializedName("info")
    val info: CheckoutTokoFoodSummaryItemDetailInfo? = null
)

data class CheckoutTokoFoodSummaryItemDetailInfo(
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("bottomsheet")
    val bottomSheet: CheckoutTokoFoodSummaryItemDetailBottomSheet = CheckoutTokoFoodSummaryItemDetailBottomSheet()
)

data class CheckoutTokoFoodSummaryItemDetailBottomSheet(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = ""
)
