package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.databinding.HolderErrorStateBinding
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ErrorStateDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.utils.view.binding.viewBinding

class ErrorStateViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<ErrorStateDataModel>(itemView) {
    private var binding: HolderErrorStateBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_error_state
    }

    override fun bind(errorStateDataModel: ErrorStateDataModel) {
        binding?.localloadErrorState?.refreshBtn?.setOnClickListener {
            when(errorStateDataModel.sectionId){
                ClientMenuGenerator.ID_WISHLIST_MENU -> mainNavListener.onErrorWishlistClicked()
                ClientMenuGenerator.ID_FAVORITE_SHOP -> mainNavListener.onErrorFavoriteShopClicked()
            }
        }
    }
}