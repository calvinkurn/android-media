package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ShopIdUiModel
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*

class ShopIdViewHolder(
    itemView: View,
    private val listener: ShopIdListener
) : AbstractViewHolder<ShopIdUiModel>(itemView) {

    override fun bind(element: ShopIdUiModel?) {
        val btn = itemView.findViewById<UnifyButton>(R.id.shop_id)
        btn.text = String.format(Locale.getDefault(), "Shop Id: ${listener.getShopId()}")
        btn.setOnClickListener {
            listener.onClickShopIdButton()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_id
    }

    interface ShopIdListener {
        fun onClickShopIdButton()
        fun getShopId(): String
    }
}
