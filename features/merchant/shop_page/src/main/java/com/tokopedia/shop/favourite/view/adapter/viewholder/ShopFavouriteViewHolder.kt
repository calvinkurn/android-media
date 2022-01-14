package com.tokopedia.shop.favourite.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.ShopPageLabelView
import com.tokopedia.shop.databinding.ItemShopFavouriteUserBinding
import com.tokopedia.shop.favourite.view.model.ShopFollowerUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by alvarisi on 12/12/17.
 */
class ShopFavouriteViewHolder(itemView: View) : AbstractViewHolder<ShopFollowerUiModel?>(itemView) {
    private var userLabelView: ShopPageLabelView? = null
    private val viewBinding: ItemShopFavouriteUserBinding? by viewBinding()

    private fun initView() {
        userLabelView = viewBinding?.labelUser
    }

    override fun bind(shopFollowerUiModel: ShopFollowerUiModel?) {
        userLabelView?.title = shopFollowerUiModel?.name.orEmpty()
        userLabelView?.imageView?.show()
        userLabelView?.imageView?.loadImageCircle(shopFollowerUiModel?.imageUrl.orEmpty())
    }

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_shop_favourite_user
    }

    init {
        initView()
    }
}