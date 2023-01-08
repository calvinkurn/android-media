package com.tokopedia.topchat.chatroom.view.custom.product_bundling

import android.graphics.Rect
import com.tokopedia.unifycomponents.toPx

class BroadcastBundleSpaceItemDecoration(
    private val source: ProductBundlingCardAttachmentContainer.BundlingSource
): BundleSpaceItemDecoration() {

    override fun setupLeftItem(outRect: Rect) {
        outRect.set(getMargin().toPx(), 0, getSpace().toPx(), 0)
    }

    override fun setupRightItem(outRect: Rect) {
        outRect.set(getSpace().toPx(), 0, getMargin().toPx(), 0)
    }

    override fun setupItem(outRect: Rect) {
        outRect.set(getSpace().toPx(), 0, getSpace().toPx(), 0)
    }

    private fun getSpace(): Int {
        return when (source) {
            ProductBundlingCardAttachmentContainer
                .BundlingSource.BROADCAST_ATTACHMENT_SINGLE -> SPACE_SINGLE_BUNDLING
            ProductBundlingCardAttachmentContainer.BundlingSource
                .BROADCAST_ATTACHMENT_MULTIPLE -> SPACE_BC
            else -> 0
        }
    }

    private fun getMargin(): Int {
        return when (source) {
            ProductBundlingCardAttachmentContainer
                .BundlingSource.BROADCAST_ATTACHMENT_SINGLE -> SPACE_SINGLE_MARGIN
            ProductBundlingCardAttachmentContainer.BundlingSource
                .BROADCAST_ATTACHMENT_MULTIPLE -> SPACE_BC / 2
            else -> 0
        }
    }

    companion object {
        private const val SPACE_BC = 6
        private const val SPACE_SINGLE_BUNDLING = 18
        private const val SPACE_SINGLE_MARGIN = 24
    }
}