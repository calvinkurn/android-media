package com.tokopedia.feedcomponent.shoprecom

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem

/**
 * created by fachrizalmrsln on 14/10/22
 */
class ShopRecomWidgetCarousel(
    itemView: View,
    private val shopRecomWidgetCallback: ShopRecomWidgetCallback,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(itemView), ShopRecomWidgetCallback {

    private val shopRecomWidgetCarousel = itemView as ShopRecomWidget

    fun bind(data: ShopRecomUiModel) {
        shopRecomWidgetCarousel.setListener(lifecycleOwner, shopRecomWidgetCallback)
        shopRecomWidgetCarousel.setData(data.title, data.items)
        shopRecomWidgetCarousel.showContentShopRecom()
    }

    companion object {
        val layout = R.layout.layout_shop_recommendation_carousel
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

}
