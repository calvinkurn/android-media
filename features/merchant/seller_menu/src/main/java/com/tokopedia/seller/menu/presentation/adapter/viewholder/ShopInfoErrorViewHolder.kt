package com.tokopedia.seller.menu.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.R
import com.tokopedia.seller.menu.databinding.LayoutSellerMenuShopInfoErrorBinding
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoErrorUiModel

class ShopInfoErrorViewHolder(
    itemView: View,
    private val listener: ShopInfoErrorListener?
): AbstractViewHolder<ShopInfoErrorUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_seller_menu_shop_info_error
    }

    private val binding = LayoutSellerMenuShopInfoErrorBinding.bind(itemView)

    override fun bind(element: ShopInfoErrorUiModel) {
        binding.settingLocalLoad.run {
            title?.text = context.resources.getString(R.string.setting_error_message)
            description?.text = context.resources.getString(R.string.setting_error_description)
            refreshBtn?.setOnClickListener {
                listener?.onRefreshShopInfo()
            }
        }
    }

    interface ShopInfoErrorListener {
        fun onRefreshShopInfo()
    }
}