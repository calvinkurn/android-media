package com.tokopedia.checkout.view.feature.emptycart2.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.EmptyCartPlaceholderUiModel

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class EmptyCartPlaceholderViewHolder(val view: View, val listener: ActionListener) : AbstractViewHolder<EmptyCartPlaceholderUiModel>(view) {

    companion object {
        val LAYOUT = 0
    }

    override fun bind(element: EmptyCartPlaceholderUiModel?) {
//        itemView.btn_shopping_now.setOnClickListener {
//            listener.onClickShopNow()
//        }
    }

}