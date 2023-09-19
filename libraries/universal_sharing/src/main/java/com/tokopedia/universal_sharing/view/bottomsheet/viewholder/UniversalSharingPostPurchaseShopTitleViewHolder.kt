package com.tokopedia.universal_sharing.view.bottomsheet.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.model.UniversalSharingPostPurchaseShopTitleUiModel

class UniversalSharingPostPurchaseShopTitleViewHolder(
    itemView: View
) : AbstractViewHolder<UniversalSharingPostPurchaseShopTitleUiModel>(itemView) {

    override fun bind(element: UniversalSharingPostPurchaseShopTitleUiModel?) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_sharing_post_purchase_shop_title_item
    }
}
