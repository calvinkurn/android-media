package com.tokopedia.feedcomponent.shoprecom

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomWidgetModel

/**
 * created by fachrizalmrsln on 14/10/22
 */
class ShopRecomWidgetCarousel(
    itemView: View,
    private val shopRecomWidgetCallback: ShopRecomWidgetCallback,
    private val lifecycleOwner: LifecycleOwner
) : AbstractViewHolder<ShopRecomWidgetModel>(itemView) {

    private val shopRecomWidgetCarousel = itemView as ShopRecomWidget

    @OptIn(ExperimentalStdlibApi::class)
    override fun bind(element: ShopRecomWidgetModel?) {
        val data = element?.shopRecomUiModel ?: return
        shopRecomWidgetCarousel.setListener(lifecycleOwner, shopRecomWidgetCallback)
        shopRecomWidgetCarousel.setData(data)
        shopRecomWidgetCarousel.showContentShopRecom()
    }

    companion object {
        val LAYOUT = R.layout.layout_shop_recommendation_carousel
    }

}
