package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.metadata

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodAvailabilitySection
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariantRules
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShipping
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShop
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingCostBreakdownItem
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingSummary
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodUserAddress
import com.tokopedia.utils.currency.CurrencyFormatUtil
import java.net.URLEncoder

data class TokoFoodCheckoutMetadata(
    @SerializedName("shop")
    @Expose
    val shop: CheckoutTokoFoodShop = CheckoutTokoFoodShop(),
    @SerializedName("user_address")
    @Expose
    val userAddress: TokoFoodCheckoutUserAddress = TokoFoodCheckoutUserAddress(),
    @SerializedName("available_section")
    @Expose
    val availableSection: TokoFoodCheckoutAvailabilitySection = TokoFoodCheckoutAvailabilitySection(),
    @SerializedName("unavailable_section")
    @Expose
    val unavailableSection: TokoFoodCheckoutAvailabilitySection = TokoFoodCheckoutAvailabilitySection(),
    @SerializedName("shipping")
    @Expose
    val shipping: TokoFoodCheckoutShipping = TokoFoodCheckoutShipping(),
    @SerializedName("shopping_summary")
    @Expose
    val shoppingSummary: TokoFoodCheckoutShoppingSummary = TokoFoodCheckoutShoppingSummary()
) {

    fun generateString(): String = Gson().toJson(this)

    companion object {
        @JvmStatic
        fun convertCheckoutDataIntoMetadata(tokoFood: CheckoutTokoFood): TokoFoodCheckoutMetadata {
            return TokoFoodCheckoutMetadata(
                shop = tokoFood.data.shop,
                userAddress = TokoFoodCheckoutUserAddress.convertToMetadata(
                    tokoFood.data.userAddress
                ),
                availableSection = TokoFoodCheckoutAvailabilitySection.convertToMetadata(
                    tokoFood.data.availableSection
                ),
                unavailableSection = TokoFoodCheckoutAvailabilitySection.convertToMetadata(
                    tokoFood.data.unavailableSection
                ),
                shipping = TokoFoodCheckoutShipping.convertToMetadata(tokoFood.data.shipping),
                shoppingSummary = TokoFoodCheckoutShoppingSummary.convertToMetadata(tokoFood.data.shoppingSummary)
            )
        }
    }

}

data class TokoFoodCheckoutUserAddress(
    @SerializedName("address_id")
    @Expose
    val addressId: Long = 0L,
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
    companion object {
        @JvmStatic
        fun convertToMetadata(userAddress: CheckoutTokoFoodUserAddress): TokoFoodCheckoutUserAddress {
            return TokoFoodCheckoutUserAddress(
                addressId = userAddress.addressId.toLongOrZero(),
                addressName = userAddress.addressName,
                address = userAddress.address,
                phone = userAddress.phone,
                receiverName = userAddress.receiverName,
                status = userAddress.status
            )
        }
    }
}

data class TokoFoodCheckoutAvailabilitySection(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("products")
    @Expose
    val products: List<TokoFoodCheckoutProduct> = listOf()
) {

    fun mapToCheckoutGeneralParam(): TokoFoodCheckoutAvailabilitySection {
        return this.copy(
            products = this.products.map { product ->
                product.copy(
                    discountPercentage = product.discountPercentage.getEncodedDiscountPercentage()
                )
            }
        )
    }

    private fun String.getEncodedDiscountPercentage(): String {
        val discountPercentage =
            if (isBlank()) {
                ZERO_PERCENT
            } else {
                this
            }
        return URLEncoder.encode(discountPercentage, ENCODING)
    }

    companion object {

        private const val ZERO_PERCENT = "0%"
        private const val ENCODING = "utf-8"

        @JvmStatic
        fun convertToMetadata(section: CheckoutTokoFoodAvailabilitySection): TokoFoodCheckoutAvailabilitySection {
            return TokoFoodCheckoutAvailabilitySection(
                title = section.title,
                products = section.products.map { product ->
                    TokoFoodCheckoutProduct(
                        cartId = product.cartId.toLongOrZero(),
                        productId = product.productId,
                        productName = product.productName,
                        description = product.description,
                        imageUrl = product.imageUrl,
                        price = product.price.toLong(),
                        priceFmt = product.priceFmt.removeFmtCurrencyDot(),
                        originalPrice = product.originalPrice.toLong(),
                        originalPriceFmt = product.originalPriceFmt.removeFmtCurrencyDot(),
                        discountPercentage = product.discountPercentage,
                        notes = product.notes,
                        quantity = product.quantity,
                        variants = product.variants.map { variant ->
                            TokoFoodCheckoutProductVariant(
                                variantId = variant.variantId,
                                name = variant.name,
                                rules = variant.rules,
                                options = variant.options.map { option ->
                                    TokoFoodCheckoutProductVariantOption(
                                        isSelected = option.isSelected,
                                        optionId = option.optionId,
                                        name = option.name,
                                        price = option.price.toLong(),
                                        priceFmt = option.priceFmt.removeFmtCurrencyDot(),
                                        status = option.status
                                    )
                                }
                            )
                        }
                    )
                }
            ).mapToCheckoutGeneralParam()
        }
    }

}

data class TokoFoodCheckoutProduct(
    @SerializedName("cart_id")
    @Expose
    val cartId: Long = 0L,
    @SerializedName("product_id")
    @Expose
    val productId: String = "",
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
    val price: Long = 0L,
    @SerializedName("price_fmt")
    @Expose
    val priceFmt: String = "",
    @SerializedName("original_price")
    @Expose
    val originalPrice: Long = 0L,
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
    val variants: List<TokoFoodCheckoutProductVariant> = listOf()
)


data class TokoFoodCheckoutProductVariant(
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
    val options: List<TokoFoodCheckoutProductVariantOption> = listOf()
)

data class TokoFoodCheckoutProductVariantOption(
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
    val price: Long = 0L,
    @SerializedName("price_fmt")
    @Expose
    val priceFmt: String = "",
    @SerializedName("status")
    @Expose
    val status: Int = 0
)

data class TokoFoodCheckoutShipping(
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
    val price: Long = 0L,
    @SerializedName("price_fmt")
    @Expose
    val priceFmt: String = ""
) {
    
    companion object {
        @JvmStatic
        fun convertToMetadata(shipping: CheckoutTokoFoodShipping): TokoFoodCheckoutShipping {
            return TokoFoodCheckoutShipping(
                name = shipping.name,
                logoUrl = shipping.logoUrl,
                eta = shipping.eta,
                price = shipping.price.toLong(),
                priceFmt = shipping.priceFmt.removeFmtCurrencyDot()
            )
        }
    }
}

data class TokoFoodCheckoutShoppingSummary(
    @SerializedName("total")
    @Expose
    val total: TokoFoodCheckoutShoppingTotal = TokoFoodCheckoutShoppingTotal(),
    @SerializedName("cost_breakdown")
    @Expose
    val costBreakdown: TokoFoodCheckoutShoppingCostBreakdown = TokoFoodCheckoutShoppingCostBreakdown(),
    @SerializedName("discount_breakdown")
    @Expose
    val discountBreakdown: List<TokoFoodCheckoutShoppingDiscountBreakdown> = listOf()
) {

    companion object {
        @JvmStatic
        fun convertToMetadata(shopping: CheckoutTokoFoodShoppingSummary): TokoFoodCheckoutShoppingSummary {
            return TokoFoodCheckoutShoppingSummary(
                total = TokoFoodCheckoutShoppingTotal(
                    cost = shopping.total.cost.toLong(),
                    savings = shopping.total.savings.toLong()
                ),
                costBreakdown = TokoFoodCheckoutShoppingCostBreakdown(
                    totalCartPrice = TokoFoodCheckoutShoppingCostBreakdownItem.convertToMetadata(shopping.costBreakdown.totalCartPrice),
                    takeAwayFee = TokoFoodCheckoutShoppingCostBreakdownItem.convertToMetadata(shopping.costBreakdown.outletFee),
                    convenienceFee = TokoFoodCheckoutShoppingCostBreakdownItem.convertToMetadata(shopping.costBreakdown.platformFee),
                    deliveryFee = TokoFoodCheckoutShoppingCostBreakdownItem.convertToMetadata(shopping.costBreakdown.deliveryFee),
                    parkingFee = TokoFoodCheckoutShoppingCostBreakdownItem.convertToMetadata(shopping.costBreakdown.reimbursementFee),
                ),
                discountBreakdown = shopping.discountBreakdown.map { discount ->
                    TokoFoodCheckoutShoppingDiscountBreakdown(
                        discountId = discount.discountId,
                        title = discount.title,
                        amount = discount.amount.toLong(),
                        scope = discount.scope,
                        type = discount.type
                    )
                }
            )
        }
    }
}

data class TokoFoodCheckoutShoppingTotal(
    @SerializedName("cost")
    @Expose
    val cost: Long = 0L,
    @SerializedName("savings")
    @Expose
    val savings: Long = 0L
)

data class TokoFoodCheckoutShoppingCostBreakdown(
    @SerializedName("total_cart_price")
    @Expose
    val totalCartPrice: TokoFoodCheckoutShoppingCostBreakdownItem = TokoFoodCheckoutShoppingCostBreakdownItem(),
    @SerializedName("outlet_fee")
    @Expose
    val takeAwayFee: TokoFoodCheckoutShoppingCostBreakdownItem = TokoFoodCheckoutShoppingCostBreakdownItem(),
    @SerializedName("platform_fee")
    @Expose
    val convenienceFee: TokoFoodCheckoutShoppingCostBreakdownItem = TokoFoodCheckoutShoppingCostBreakdownItem(),
    @SerializedName("delivery_fee")
    @Expose
    val deliveryFee: TokoFoodCheckoutShoppingCostBreakdownItem = TokoFoodCheckoutShoppingCostBreakdownItem(),
    @SerializedName("reimbursement_fee")
    @Expose
    val parkingFee: TokoFoodCheckoutShoppingCostBreakdownItem = TokoFoodCheckoutShoppingCostBreakdownItem()
)

data class TokoFoodCheckoutShoppingCostBreakdownItem(
    @SerializedName("original_amount")
    @Expose
    val originalAmount: Long = 0L,
    @SerializedName("amount")
    @Expose
    val amount: Long = 0L,
    @SerializedName("surge")
    @Expose
    val surge: TokoFoodCheckoutShoppingSurge = TokoFoodCheckoutShoppingSurge()
) {

    companion object {
        @JvmStatic
        fun convertToMetadata(breakdown: CheckoutTokoFoodShoppingCostBreakdownItem): TokoFoodCheckoutShoppingCostBreakdownItem {
            return TokoFoodCheckoutShoppingCostBreakdownItem(
                originalAmount = breakdown.originalAmount.toLong(),
                amount = breakdown.amount.toLong(),
                surge = TokoFoodCheckoutShoppingSurge(
                    isSurgePrice = breakdown.surge.isSurgePrice,
                    factor = breakdown.surge.factor.toInt()
                )
            )
        }
    }
}

data class TokoFoodCheckoutShoppingSurge(
    @SerializedName("is_surge_price")
    @Expose
    val isSurgePrice: Boolean = false,
    @SerializedName("factor")
    @Expose
    val factor: Int = 0
)

data class TokoFoodCheckoutShoppingDiscountBreakdown(
    @SerializedName("discount_id")
    @Expose
    val discountId: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("amount")
    @Expose
    val amount: Long = 0L,
    @SerializedName("scope")
    @Expose
    val scope: String = "",
    @SerializedName("type")
    @Expose
    val type: String = ""
)

private fun String.removeFmtCurrencyDot(): String {
    return this.replace(".", "")
}