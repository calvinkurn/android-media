package com.tokopedia.vouchercreation.voucherlist.model.remote

/**
 * Created By @ilhamsuaib on 11/05/20
 */

data class PagingModel(
        val perPage: Int = 0,
        val page: Int = 0,
        val hasPrev: Boolean = false,
        val hasNext: Boolean = false
)