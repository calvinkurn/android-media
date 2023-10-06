package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R

/**
 * Created by rizqiaryansa on 06/03/20.
 */

class ShopLayoutLoadingShimmerViewHolder(view: View) : AbstractViewHolder<LoadingModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_page_home_shimmering
    }

    override fun bind(element: LoadingModel?) {}
}
