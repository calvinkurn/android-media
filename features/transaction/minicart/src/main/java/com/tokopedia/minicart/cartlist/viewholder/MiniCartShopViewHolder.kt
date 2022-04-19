package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.uimodel.MiniCartShopUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartShopBinding

class MiniCartShopViewHolder(private val viewBinding: ItemMiniCartShopBinding)
    : AbstractViewHolder<MiniCartShopUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_shop
    }

    override fun bind(element: MiniCartShopUiModel) {
        renderShopBadge(element)
        renderShopLocation(element)
        renderEstimatedTimeArrival(element)
    }

    private fun renderEstimatedTimeArrival(element: MiniCartShopUiModel) {
        with(viewBinding) {
            if (element.estimatedTimeArrival.isNotBlank()) {
                textEstimatedTimeArrival.text = element.estimatedTimeArrival
                separatorEstimatedTimeArrival.show()
            } else {
                separatorEstimatedTimeArrival.gone()
            }
        }
    }

    private fun renderShopLocation(element: MiniCartShopUiModel) {
        viewBinding.tvFulfillDistrict.text = element.shopLocation
    }

    private fun renderShopBadge(element: MiniCartShopUiModel) {
        viewBinding.iuImageFulfill.let {
            if (element.shopBadgeUrl.isNotBlank()) {
                ImageHandler.loadImageWithoutPlaceholder(it, element.shopBadgeUrl)
                it.show()
            } else {
                it.gone()
            }
        }
    }

}