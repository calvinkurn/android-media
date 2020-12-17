package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoErrorUiModel
import kotlinx.android.synthetic.main.layout_seller_menu_shop_info_error.view.*

class ShopInfoErrorViewHolder(
    itemView: View,
    private val listener: ShopInfoErrorListener?
): AbstractViewHolder<ShopInfoErrorUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_seller_menu_shop_info_error
    }

    override fun bind(element: ShopInfoErrorUiModel) {
        itemView.settingLocalLoad.run {
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