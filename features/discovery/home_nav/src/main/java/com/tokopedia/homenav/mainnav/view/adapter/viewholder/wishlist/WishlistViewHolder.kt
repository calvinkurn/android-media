package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderWishlistListBinding
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.WishlistTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.NavOrderSpacingDecoration
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.EmptyStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.OtherWishlistModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Frenzel on 18/04/22
 */

@MePage(MePage.Widget.WISHLIST)
class WishlistViewHolder(itemView: View,
                         val mainNavListener: MainNavListener
): AbstractViewHolder<WishlistDataModel>(itemView) {
    private var binding: HolderWishlistListBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_wishlist_list
        private const val EDGE_MARGIN = 16f
        private const val SPACING_BETWEEN = 8f
        private const val MAX_CARD_HEIGHT = 80f
    }

    override fun bind(element: WishlistDataModel) {
        val context = itemView.context
        val adapter = WishlistAdapter(WishlistTypeFactoryImpl(mainNavListener))

        val edgeMargin = EDGE_MARGIN.toDpInt()
        val spacingBetween = SPACING_BETWEEN.toDpInt()

        binding?.wishlistRv?.adapter = adapter
        binding?.wishlistRv?.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        if (binding?.wishlistRv?.itemDecorationCount == 0) {
            binding?.wishlistRv?.addItemDecoration(
                NavOrderSpacingDecoration(spacingBetween, edgeMargin)
            )
        }
        val visitableList = mutableListOf<Visitable<*>>()
        if (element.collections.isEmpty() || element.isEmptyState) {
            visitableList.add(EmptyStateWishlistDataModel())
        } else {
            visitableList.addAll(element.collections.mapIndexed { index, it -> WishlistModel(it, index) })
            if(element.showViewAll) {
                visitableList.add(OtherWishlistModel())
                binding?.wishlistRv?.setHeightBasedOnCardMaxHeight()
            }
        }
        adapter.setVisitables(visitableList)
    }

    private fun RecyclerView.setHeightBasedOnCardMaxHeight() {
        val productCardHeight = MAX_CARD_HEIGHT.toDpInt()

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }
}
