package com.tokopedia.settingbank.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadDocumentRequest (
        @SerializedName("acc_id")
        @Expose
        val accountId : Long,
        @SerializedName("acc_name")
        @Expose
        val accountName : String,
        @SerializedName("acc_number")
        @Expose
        val accountNumber : String,
        @SerializedName("bank_id")
        @Expose
        val bankId : Long,
        @SerializedName("data_document")
        @Expose
        val documentType : DocumentType

)

data class DocumentType(
        @SerializedName("type")
        @Expose
        val type : Int,
        @SerializedName("document_name")
        @Expose
        val documentName : String,
        @SerializedName("document_mime")
        @Expose
        val documentMime : String,

        @SerializedName("document_ext")
        @Expose
        val documentExt : String,

        @SerializedName("document_base64")
        @Expose
        val documentBase64 : String?

)
