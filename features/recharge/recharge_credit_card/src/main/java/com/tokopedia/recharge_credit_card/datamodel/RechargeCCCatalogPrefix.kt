package com.tokopedia.recharge_credit_card.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeCCCatalogPrefix(
        @SerializedName("rechargeCatalogPrefixSelect")
        @Expose
        val prefixSelect: CatalogPrefixSelect = CatalogPrefixSelect())

class CatalogPrefixSelect(
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("prefixes")
        @Expose
        val prefixes: List<CatalogPrefixs> = listOf()
)

class CatalogPrefixs(
        @SerializedName("key")
        @Expose
        val key: String = "",
        @SerializedName("value")
        @Expose
        val value: String = "",
        @SerializedName("operator")
        @Expose
        val operator: CatalogOperator = CatalogOperator()
)

class CatalogOperator(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("attributes")
        @Expose
        val attribute: CatalogPrefixAttributes = CatalogPrefixAttributes()
)

class CatalogPrefixAttributes(
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("default_product_id")
        @Expose
        val defaultProductId: String = "",
        @SerializedName("name")
        @Expose
        val name: String = ""
)