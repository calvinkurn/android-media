package com.tokopedia.digital_product_detail.data.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory

data class DigitalCatalogProductInputMultiTab(
    @SerializedName("rechargeCatalogProductInputMultiTab")
    @Expose
    val multitabData: RechargeCatalogProductInputMultiTab = RechargeCatalogProductInputMultiTab()
)

data class RechargeCatalogProductInputMultiTab(
    @SerializedName("productInputs")
    @Expose
    val productInputs: List<RechargeCatalogProductInput> = emptyList()
)


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
    val product: RechargeCatalogProduct = RechargeCatalogProduct(),
    @SerializedName("filterTagComponents")
    @Expose
    var filterTagComponents: List<TelcoFilterTagComponent> = mutableListOf()
)


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
)


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
)


data class RechargeCatalogProductValidation(
    @SerializedName("rule")
    @Expose
    val name: String = ""
)


data class RechargeCatalogDataCollection(
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("cluster_type")
    @Expose
    val clusterType: String = "",
    @SerializedName("products")
    @Expose
    val products: List<RechargeProduct> = listOf()
)


data class RechargeProduct(
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("attributes")
    @Expose
    val attributes: RechargeAttributesProduct = RechargeAttributesProduct()
)


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
    val operatorId: String = "",
    @SerializedName("product_descriptions")
    @Expose
    val productDescriptions: List<String> = listOf()
)


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
)

data class TelcoFilterTagComponent(
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("text")
    @Expose
    val text: String = "",
    @SerializedName("param_name")
    @Expose
    val paramName: String = "",
    @SerializedName("data_collections")
    @Expose
    var filterTagDataCollections: List<FilterTagDataCollection> = mutableListOf()
)

data class FilterTagDataCollection(
    @SerializedName("key")
    @Expose
    val key: String = "",
    @SerializedName("value")
    @Expose
    val value: String = "",
    var isSelected: Boolean = false
): Visitable<BaseListCheckableTypeFactory<FilterTagDataCollection>> {

    override fun type(typeFactory: BaseListCheckableTypeFactory<FilterTagDataCollection>): Int {
        return typeFactory.type(this)
    }
}