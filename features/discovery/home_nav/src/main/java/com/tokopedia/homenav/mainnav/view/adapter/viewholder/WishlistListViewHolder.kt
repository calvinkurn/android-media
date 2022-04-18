package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderWishlistListBinding
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.WishlistListItemDataModel
import com.tokopedia.utils.view.binding.viewBinding

class WishlistListViewHolder(itemView: View,
                             val mainNavListener: MainNavListener
): AbstractViewHolder<WishlistListItemDataModel>(itemView) {
    private var binding: HolderWishlistListBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_wishlist_list
    }

    override fun bind(element: WishlistListItemDataModel) {
    }
}