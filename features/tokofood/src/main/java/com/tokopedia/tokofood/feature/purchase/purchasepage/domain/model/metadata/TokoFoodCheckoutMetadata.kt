package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.metadata

import android.annotation.SuppressLint
import com.google.gson.Gson
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
import java.net.URLEncoder

data class TokoFoodCheckoutMetadata(
    @SerializedName("shop")
    val shop: CheckoutTokoFoodShop = CheckoutTokoFoodShop(),
    @SerializedName("user_address")
    val userAddress: TokoFoodCheckoutUserAddress = TokoFoodCheckoutUserAddress(),
    @SerializedName("available_section")
    val availableSection: TokoFoodCheckoutAvailabilitySection = TokoFoodCheckoutAvailabilitySection(),
    @SerializedName("unavailable_sections")
    val unavailableSections: List<TokoFoodCheckoutAvailabilitySection> = listOf(),
    @SerializedName("shipping")
    val shipping: TokoFoodCheckoutShipping = TokoFoodCheckoutShipping(),
    @SerializedName("shopping_summary")
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
                unavailableSections = tokoFood.data.unavailableSections.map {
                    TokoFoodCheckoutAvailabilitySection.convertToMetadata(
                        it
                    )
                },
                shipping = TokoFoodCheckoutShipping.convertToMetadata(tokoFood.data.shipping),
                shoppingSummary = TokoFoodCheckoutShoppingSummary.convertToMetadata(tokoFood.data.shoppingSummary)
            )
        }
    }

}

data class TokoFoodCheckoutUserAddress(
    @SerializedName("address_id")
    val addressId: Long = 0L,
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
    val title: String = "",
    @SerializedName("products")
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
    val cartId: Long = 0L,
    @SerializedName("product_id")
    val productId: String = "",
    @SerializedName("name")
    val productName: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: Long = 0L,
    @SerializedName("price_fmt")
    val priceFmt: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("original_price")
    val originalPrice: Long = 0L,
    @SerializedName("original_price_fmt")
    val originalPriceFmt: String = "",
    @SerializedName("discount_percentage")
    val discountPercentage: String = "",
    @SerializedName("notes")
    val notes: String = "",
    @SerializedName("quantity")
    val quantity: Int = 0,
    @SerializedName("variants")
    val variants: List<TokoFoodCheckoutProductVariant> = listOf()
)


data class TokoFoodCheckoutProductVariant(
    @SerializedName("variant_id")
    val variantId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("rules")
    val rules: CheckoutTokoFoodProductVariantRules = CheckoutTokoFoodProductVariantRules(),
    @SerializedName("options")
    val options: List<TokoFoodCheckoutProductVariantOption> = listOf()
)

data class TokoFoodCheckoutProductVariantOption(
    @SerializedName("is_selected")
    val isSelected: Boolean = false,
    @SerializedName("option_id")
    val optionId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: Long = 0L,
    @SerializedName("price_fmt")
    val priceFmt: String = "",
    @SerializedName("status")
    val status: Int = 0
)

data class TokoFoodCheckoutShipping(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("logo_url")
    val logoUrl: String = "",
    @SerializedName("eta")
    val eta: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: Long = 0L,
    @SerializedName("price_fmt")
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
    val total: TokoFoodCheckoutShoppingTotal = TokoFoodCheckoutShoppingTotal(),
    @SerializedName("cost_breakdown")
    val costBreakdown: TokoFoodCheckoutShoppingCostBreakdown = TokoFoodCheckoutShoppingCostBreakdown(),
    @SerializedName("discount_breakdown")
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
    val cost: Long = 0L,
    @SerializedName("savings")
    val savings: Long = 0L
)

data class TokoFoodCheckoutShoppingCostBreakdown(
    @SuppressLint("Invalid Data Type")
    @SerializedName("total_cart_price")
    val totalCartPrice: TokoFoodCheckoutShoppingCostBreakdownItem = TokoFoodCheckoutShoppingCostBreakdownItem(),
    @SerializedName("outlet_fee")
    val takeAwayFee: TokoFoodCheckoutShoppingCostBreakdownItem = TokoFoodCheckoutShoppingCostBreakdownItem(),
    @SerializedName("platform_fee")
    val convenienceFee: TokoFoodCheckoutShoppingCostBreakdownItem = TokoFoodCheckoutShoppingCostBreakdownItem(),
    @SerializedName("delivery_fee")
    val deliveryFee: TokoFoodCheckoutShoppingCostBreakdownItem = TokoFoodCheckoutShoppingCostBreakdownItem(),
    @SerializedName("reimbursement_fee")
    val parkingFee: TokoFoodCheckoutShoppingCostBreakdownItem = TokoFoodCheckoutShoppingCostBreakdownItem()
)

data class TokoFoodCheckoutShoppingCostBreakdownItem(
    @SerializedName("original_amount")
    val originalAmount: Long = 0L,
    @SerializedName("amount")
    val amount: Long = 0L,
    @SerializedName("surge")
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
    @SuppressLint("Invalid Data Type")
    @SerializedName("is_surge_price")
    val isSurgePrice: Boolean = false,
    @SerializedName("factor")
    val factor: Int = 0
)

data class TokoFoodCheckoutShoppingDiscountBreakdown(
    @SerializedName("discount_id")
    val discountId: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("amount")
    val amount: Long = 0L,
    @SerializedName("scope")
    val scope: String = "",
    @SerializedName("type")
    val type: String = ""
)

private fun String.removeFmtCurrencyDot(): String {
    return this.replace(".", "")
}
