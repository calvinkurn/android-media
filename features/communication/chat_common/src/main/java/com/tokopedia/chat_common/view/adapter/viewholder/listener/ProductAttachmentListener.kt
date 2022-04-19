package com.tokopedia.chat_common.view.adapter.viewholder.listener

import com.tokopedia.chat_common.data.BannedProductAttachmentUiModel
import com.tokopedia.chat_common.data.ProductAttachmentUiModel

/**
 * @author by nisie on 28/11/18.
 */
interface ProductAttachmentListener {
    fun onProductClicked(element: ProductAttachmentUiModel)
    fun onClickBuyFromProductAttachment(element: ProductAttachmentUiModel)
    fun onClickATCFromProductAttachment(element: ProductAttachmentUiModel)
    fun trackSeenProduct(element: ProductAttachmentUiModel)
    fun onClickBannedProduct(uiModel: BannedProductAttachmentUiModel)
    fun trackSeenBannedProduct(uiModel: BannedProductAttachmentUiModel)
    fun onClickAddToWishList(product: ProductAttachmentUiModel, success: () -> Unit)
    fun onClickRemoveFromWishList(productId: String, success: () -> Unit)
    fun trackClickProductThumbnail(product: ProductAttachmentUiModel)
}