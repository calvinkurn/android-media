package com.tokopedia.tokomember_seller_dashboard.model

data class TmMVFilter(
    val voucher_type: Int?,
    val voucher_status: String,
    val is_public: String,
    val target_buyer: String,
)