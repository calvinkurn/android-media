package com.tokopedia.vouchercreation.product.list.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class VoucherValidationPartialResponse(
        @SerializedName("VoucherValidationPartial")
        @Expose val response: VoucherValidationPartial = VoucherValidationPartial()
)

data class VoucherValidationPartial(
        @SerializedName("header")
        @Expose val header: Header = Header(),
        @SerializedName("data")
        @Expose val voucherValidationData: VoucherValidationData = VoucherValidationData()
)

data class VoucherValidationData(
        @SerializedName("validation_product")
        @Expose val validationPartial: List<VoucherValidationPartialProduct> = listOf()
)

data class VoucherValidationPartialProduct(
        @SerializedName("parent_product_id")
        @Expose val parentProductId: String = "",
        @SerializedName("is_variant")
        @Expose val isVariant: Boolean = false,
        @SerializedName("is_eligible")
        @Expose val isEligible: Boolean = true,
        @SerializedName("reason")
        @Expose val reason: String = "",
        @SerializedName("variant")
        @Expose val variants: List<VariantValidationData> = listOf()
)

data class VariantValidationData(
        @SerializedName("product_id")
        @Expose val productId: String = "",
        @SerializedName("product_name")
        @Expose val productName: String = "",
        @SerializedName("price")
        @Expose val price: Int = 0,
        @SerializedName("price_fmt")
        @Expose val priceFormat: String = "",
        @SerializedName("stock")
        @Expose val stock: Int = 0,
        @SerializedName("sku")
        @Expose val sku: String = "",
        @SerializedName("is_eligible")
        @Expose val is_eligible: Boolean = true,
        @SerializedName("reason")
        @Expose val reason: String = ""
)