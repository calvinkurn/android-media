package com.tokopedia.sellerorder.list.presentation.models

data class Error(
        val code: String = "",
        val status: String = "",
        val title: String = "",
        val detail: String = ""
)