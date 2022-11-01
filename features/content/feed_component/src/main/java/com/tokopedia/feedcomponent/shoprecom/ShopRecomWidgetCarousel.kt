package com.tokopedia.feedcomponent.shoprecom

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomWidgetModel

/**
 * created by fachrizalmrsln on 14/10/22
 */
class ShopRecomWidgetCarousel(
    itemView: View,
    private val shopRecomWidgetCallback: ShopRecomWidgetCallback,
    private val lifecycleOwner: LifecycleOwner
) : AbstractViewHolder<ShopRecomWidgetModel>(itemView), ShopRecomWidgetCallback {

    private val shopRecomWidgetCarousel = itemView as ShopRecomWidget

    override fun bind(element: ShopRecomWidgetModel?) {
        val data = element?.shopRecomUiModel ?: return
        shopRecomWidgetCarousel.setListener(lifecycleOwner, shopRecomWidgetCallback)
        shopRecomWidgetCarousel.setData(data.title, data.items)
        shopRecomWidgetCarousel.showContentShopRecom()
    }

    override fun onShopRecomCloseClicked(itemID: Long) {
        shopRecomWidgetCallback.onShopRecomCloseClicked(itemID)
    }

    override fun onShopRecomFollowClicked(itemID: Long) {
        shopRecomWidgetCallback.onShopRecomFollowClicked(itemID)
    }

    override fun onShopRecomItemClicked(
        itemID: Long,
        appLink: String,
        imageUrl: String,
        postPosition: Int
    ) {
        shopRecomWidgetCallback.onShopRecomItemClicked(itemID, appLink, imageUrl, postPosition)
    }

    override fun onShopRecomItemImpress(item: ShopRecomUiModelItem, postPosition: Int) {
        shopRecomWidgetCallback.onShopRecomItemImpress(item, postPosition)
    }

    companion object {
        val LAYOUT = R.layout.layout_shop_recommendation_carousel
    }

}
