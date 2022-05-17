package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderFavoriteShopListBinding
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.FavoriteShopTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshop.FavoriteShopAdapter
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.NavOrderSpacingDecoration
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.OtherFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
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
    }

    override fun bind(element: FavoriteShopListDataModel) {
        val context = itemView.context
        val adapter = FavoriteShopAdapter(FavoriteShopTypeFactoryImpl(mainNavListener))

        val edgeMargin = 16f.toDpInt()
        val spacingBetween = 8f.toDpInt()

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
        visitableList.add(OtherFavoriteShopModel())
        adapter.setVisitables(visitableList)
    }
}