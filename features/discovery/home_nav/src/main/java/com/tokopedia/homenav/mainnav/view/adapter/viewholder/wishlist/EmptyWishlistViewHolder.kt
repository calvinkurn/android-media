package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderEmptyWishlistBinding
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.OtherWishlistModel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

class EmptyWishlistViewHolder(itemView: View): AbstractViewHolder<OtherWishlistModel>(itemView) {
    private var binding: HolderEmptyWishlistBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_empty_wishlist
    }

    override fun bind(otherWishlistModel: OtherWishlistModel) {
        binding?.cardEmptyWishlist?.cardType = CardUnify2.TYPE_BORDER
    }
}