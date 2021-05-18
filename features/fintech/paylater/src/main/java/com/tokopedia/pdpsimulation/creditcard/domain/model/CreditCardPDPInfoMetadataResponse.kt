package com.tokopedia.pdpsimulation.creditcard.domain.model

import com.google.gson.annotations.SerializedName


data class CreditCardPDPInfoMetadataResponse(
        @SerializedName("cc_fetchpdpinfometadata")
        val creditCardPDPInfoMetadataResponse: CreditCardPdpMetaData,
)

data class CreditCardPdpMetaData(
        @SerializedName("cta_redir_url")
        val ctaRedirectionUrl: String?,
        @SerializedName("cta_redir_applink")
        val ctaRedirectionAppLink: String?,
        @SerializedName("cta_redir_label")
        val ctaRedirectionLabel: String?,
        @SerializedName("pdp_info_content")
        val pdpInfoContentList: ArrayList<CreditCardPdpInfoContent>?,
)

data class CreditCardPdpInfoContent(
        @SerializedName("title")
        val title: String?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("content_parse")
        val contentType: String?,
        @SerializedName("content")
        val content: String?,
        @SerializedName("notes")
        val notesList: ArrayList<String>?,
        var bulletList: ArrayList<String>?,
        var tableData: PdpInfoTableItem?,
        var viewType: Int,
)

data class PdpInfoTableItem(
        @SerializedName("table_data")
        val tableList: ArrayList<ArrayList<String>>?,
)