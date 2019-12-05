package com.tokopedia.settingbank.banklist.v2.domain

data class AccountConfirmPojo(
        val acc_id: Long,
        val acc_name: String,
        val acc_number: String,
        val bank_id: Long,
        val doc_type: Int,
        val document_name: String? = null,
        val document_mime: String? = null,
        val document_ext: String? = null,
        val document_base64: String? = null
)