package com.tokopedia.checkout.view.feature.emptycart2.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.EmptyCartPlaceholderUiModel

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class RecentViewViewHolder(val view: View) : AbstractViewHolder<EmptyCartPlaceholderUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_empty_cart_recent_view
    }

    override fun bind(element: EmptyCartPlaceholderUiModel?) {

    }

}