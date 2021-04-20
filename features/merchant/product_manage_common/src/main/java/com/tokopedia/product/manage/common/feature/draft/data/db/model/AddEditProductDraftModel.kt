package com.tokopedia.product.manage.common.feature.draft.data.db.model

class AddEditProductDraftModel(
        val productId: Long,
        val primaryImageUrl: String,
        val productName: String,
        val completionPercent: Int,
        val isEdit: Boolean
) {
    companion object {
        const val TYPE = 1
    }
}