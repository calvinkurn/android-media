package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartDisabledShopBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledShopHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */

class DisabledShopViewHolder(private val binding: ItemCartDisabledShopBinding, val actionListener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_shop
    }

    fun bind(data: DisabledShopHolderData) {
        renderShopName(data)
        renderDivider(data)
        renderShopBadge(data)
        if (!data.hasShown) {
            // todo analytics
            data.hasShown = true
        }
    }

    private fun renderShopName(data: DisabledShopHolderData) {
        binding.textShopName.text = data.shopName
        binding.textShopName.setOnClickListener { actionListener?.onCartShopNameClicked(data.shopId, data.shopName, data.isTokoNow) }
    }

    private fun renderShopBadge(data: DisabledShopHolderData) {
        if (data.shopBadgeUrl.isNotBlank()) {
            ImageHandler.loadImageWithoutPlaceholder(binding.imgShopBadge, data.shopBadgeUrl)
            binding.imgShopBadge.show()
        } else {
            binding.imgShopBadge.gone()
        }
    }

    private fun renderDivider(data: DisabledShopHolderData) {
        if (data.showDivider) {
            binding.vDividerItemCartError.show()
        } else {
            binding.vDividerItemCartError.gone()
        }
    }

}