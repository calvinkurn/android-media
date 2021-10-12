package com.tokopedia.smartbills.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RechargeCatalogProductInputMultiTabData(
        @SerializedName("rechargeCatalogProductInputMultiTab")
        @Expose
        val multitabData: RechargeCatalogProductInputMultiTab = RechargeCatalogProductInputMultiTab()
): Parcelable

@Parcelize
data class RechargeCatalogProductInputMultiTab(
        @SerializedName("productInputs")
        @Expose
        val productInputs: List<RechargeCatalogProductInput> = emptyList()
): Parcelable

@Parcelize
data class RechargeCatalogProductInput(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("needEnquiry")
        @Expose
        val needEnquiry: Boolean = false,
        @SerializedName("isShowingProduct")
        @Expose
        val isShowingProduct: Boolean = false,
        @SerializedName("enquiryFields")
        @Expose
        val enquiryFields: List<RechargeCatalogEnquiryFields> = emptyList(),
        @SerializedName("product")
        @Expose
        val product: RechargeCatalogProduct = RechargeCatalogProduct()
): Parcelable

@Parcelize
data class RechargeCatalogEnquiryFields(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("param_name")
        @Expose
        val paramName: String = "",
        @SerializedName("name")
        @Expose
        val name: String = ""
): Parcelable

@Parcelize
data class RechargeCatalogProduct(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("placeholder")
        @Expose
        val placeholder: String = "",
        @SerializedName("validation")
        @Expose
        val validation: List<RechargeCatalogProductValidation> = emptyList(),
        @SerializedName("dataCollections")
        @Expose
        val dataCollections: List<RechargeCatalogDataCollection> = emptyList()
): Parcelable

@Parcelize
data class RechargeCatalogProductValidation(
        @SerializedName("rule")
        @Expose
        val name: String = ""
): Parcelable

@Parcelize
data class RechargeCatalogDataCollection(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("products")
        @Expose
        val products: List<RechargeProduct> = listOf()
): Parcelable

@Parcelize
data class RechargeProduct(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: RechargeAttributesProduct = RechargeAttributesProduct()
): Parcelable

@Parcelize
data class RechargeAttributesProduct(
        @SerializedName("product_labels")
        @Expose
        var productLabels: List<String> = listOf(),
        @SerializedName("desc")
        @Expose
        val desc: String = "",
        @SerializedName("detail")
        @Expose
        val detail: String = "",
        @SerializedName("detail_url")
        @Expose
        val detailUrl: String = "",
        @SerializedName("detail_url_text")
        @Expose
        val detailUrlText: String = "",
        @SerializedName("info")
        @Expose
        val info: String = "",
        @SerializedName("price")
        @Expose
        val price: String = "",
        @SerializedName("price_plain")
        @Expose
        val pricePlain: Int = 0,
        @SerializedName("status")
        @Expose
        var status: Int = 0,
        @SerializedName("detail_compact")
        @Expose
        val detailCompact: String = "",
        @SerializedName("promo")
        @Expose
        val productPromo: RechargeProductPromo? = RechargeProductPromo(),
        @SerializedName("category_id")
        @Expose
        val categoryId: String = "",
        @SerializedName("operator_id")
        @Expose
        val operatorId: String = ""
): Parcelable

@Parcelize
data class RechargeProductPromo(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("bonus_text")
        @Expose
        val bonusText: String = "",
        @SerializedName("new_price")
        @Expose
        val newPrice: String = "",
        @SerializedName("new_price_plain")
        @Expose
        val newPricePlain: Int = 0,
        @SerializedName("value_text")
        @Expose
        val valueText: String = ""
): Parcelable