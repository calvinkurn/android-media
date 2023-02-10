package com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderFavoriteShopListBinding
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.FavoriteShopTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.NavOrderSpacingDecoration
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.EmptyStateFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.OtherFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Frenzel on 18/04/22
 */
class FavoriteShopViewHolder(itemView: View,
                             val mainNavListener: MainNavListener
): AbstractViewHolder<FavoriteShopListDataModel>(itemView) {
    private var binding: HolderFavoriteShopListBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_favorite_shop_list
        private const val EDGE_MARGIN = 16f
        private const val SPACING_BETWEEN = 8f
        private const val MAX_CARD_HEIGHT = 80f
    }

    override fun bind(element: FavoriteShopListDataModel) {
        val context = itemView.context
        val adapter = FavoriteShopAdapter(FavoriteShopTypeFactoryImpl(mainNavListener))

        val edgeMargin = EDGE_MARGIN.toDpInt()
        val spacingBetween = SPACING_BETWEEN.toDpInt()

        binding?.favoriteShopRv?.adapter = adapter
        binding?.favoriteShopRv?.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        if (binding?.favoriteShopRv?.itemDecorationCount == 0) {
            binding?.favoriteShopRv?.addItemDecoration(
                NavOrderSpacingDecoration(spacingBetween, edgeMargin)
            )
        }
        val visitableList = mutableListOf<Visitable<*>>()
        visitableList.addAll(element.favoriteShops.map { FavoriteShopModel(it) })
        if(visitableList.isEmpty()){
            visitableList.add(EmptyStateFavoriteShopDataModel())
        } else if(element.showViewAll){
            visitableList.add(OtherFavoriteShopModel())
            binding?.favoriteShopRv?.setHeightBasedOnCardMaxHeight()
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
