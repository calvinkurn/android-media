package com.tokopedia.product.addedit.draft.presentation.listener

interface ProductDraftListListener {
    fun onDraftClickListener(draftId: Long)
    fun onDraftDeleteListener(draftId: Long, position: Int, productName: String)
}