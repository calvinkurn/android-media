package com.tokopedia.createpost.view.listener

import com.tokopedia.createpost.view.viewmodel.HeaderViewModel

interface CreateContentPostCOmmonLIstener {
    fun deleteItemFromProductTagList(
        position: Int,
        productId: String,
        isDeletedFromBubble: Boolean,
        mediaType: String,
    )
    fun updateHeader(header: HeaderViewModel)
    fun openProductTagginPageOnPreviewMediaClick(position: Int)
    fun clickProductTagBubbleAnalytics(mediaType: String, productId: String)

}