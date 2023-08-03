package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderViewAllRevampBinding
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.OtherWishlistModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

@MePage(MePage.Widget.WISHLIST)
class OtherWishlistViewHolder(itemView: View, val mainNavListener: MainNavListener) : AbstractViewHolder<OtherWishlistModel>(itemView) {
    private var binding: HolderViewAllRevampBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_view_all_revamp
    }

    override fun bind(otherWishlistModel: OtherWishlistModel) {
        val context = itemView.context
        binding?.cardViewAll?.description = itemView.resources.getString(R.string.wishlist_view_all_desc)
        binding?.cardViewAll?.setCta(context.getString(R.string.global_view_all))

        binding?.cardViewAll?.cardView?.animateOnPress = CardUnify2.ANIMATE_OVERLAY

        itemView.setOnClickListener {
            mainNavListener.onViewAllWishlistClicked()
        }
    }
}
