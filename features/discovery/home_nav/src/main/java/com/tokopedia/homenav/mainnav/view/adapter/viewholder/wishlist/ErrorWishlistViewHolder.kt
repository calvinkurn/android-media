package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderErrorWishlistBinding
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ErrorStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.utils.view.binding.viewBinding

class ErrorWishlistViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<ErrorStateWishlistDataModel>(itemView) {
    private var binding: HolderErrorWishlistBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_error_wishlist
    }

    override fun bind(errorStateWishlistDataModel: ErrorStateWishlistDataModel) {
    }
}