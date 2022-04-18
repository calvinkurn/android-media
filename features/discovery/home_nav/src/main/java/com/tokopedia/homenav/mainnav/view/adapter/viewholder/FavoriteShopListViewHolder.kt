package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderFavoriteShopListBinding
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.FavoriteShopListTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.favoriteshoplist.FavoriteShopListAdapter
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist.NavOrderSpacingDecoration
import com.tokopedia.homenav.mainnav.view.datamodel.FavoriteShopListItemDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshoplist.FavoriteShopModel
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OtherTransactionModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Frenzel on 18/04/22
 */
class FavoriteShopListViewHolder(itemView: View,
                                 val mainNavListener: MainNavListener
): AbstractViewHolder<FavoriteShopListItemDataModel>(itemView) {
    private var binding: HolderFavoriteShopListBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_favorite_shop_list
    }

    override fun bind(element: FavoriteShopListItemDataModel) {
        val context = itemView.context
        val adapter = FavoriteShopListAdapter(FavoriteShopListTypeFactoryImpl(mainNavListener))

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
        visitableList.addAll(element.favoriteShopListModel.map { FavoriteShopModel(it) })
        if (element.otherFavoriteShopsCount.isMoreThanZero()) {
            visitableList.add(OtherTransactionModel(element.otherFavoriteShopsCount))
        }
        adapter.setVisitables(visitableList)
    }
}