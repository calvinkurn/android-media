package com.tokopedia.chat_common.view.adapter.viewholder.listener

import com.tokopedia.chat_common.data.BannedProductAttachmentUiModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel

/**
 * @author by nisie on 28/11/18.
 */
interface ProductAttachmentListener {
    fun onProductClicked(element: ProductAttachmentViewModel)
    fun onClickBuyFromProductAttachment(element: ProductAttachmentViewModel)
    fun onClickATCFromProductAttachment(element: ProductAttachmentViewModel)
    fun trackSeenProduct(element: ProductAttachmentViewModel)
    fun onClickBannedProduct(uiModel: BannedProductAttachmentUiModel)
    fun trackSeenBannedProduct(uiModel: BannedProductAttachmentUiModel)
    fun onClickAddToWishList(product: ProductAttachmentViewModel, success: () -> Unit)
    fun onClickRemoveFromWishList(productId: String, success: () -> Unit)
    fun trackClickProductThumbnail(product: ProductAttachmentViewModel)
}