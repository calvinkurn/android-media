package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderFavoriteShopListBinding
import com.tokopedia.homenav.mainnav.view.datamodel.FavoriteShopListItemDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.utils.view.binding.viewBinding

class FavoriteShopListViewHolder(itemView: View,
                                 val mainNavListener: MainNavListener
): AbstractViewHolder<FavoriteShopListItemDataModel>(itemView) {
    private var binding: HolderFavoriteShopListBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_favorite_shop_list
    }

    override fun bind(element: FavoriteShopListItemDataModel) {
    }
}