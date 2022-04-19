package com.tokopedia.settingbank.domain.model

data class UploadDocumentPojo(
        val acc_id: Long,
        val acc_name: String?,
        val acc_number: String?,
        val bank_id: Long,
        val doc_type: Int,
        val document_name: String = "",
        val document_mime: String = "",
        val document_ext: String = "",
        val document_base64: String = ""
)