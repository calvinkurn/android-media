package com.tokopedia.shop.home.view.adapter

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductViewHolder
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopCampaignCarouselProductAdapter(
        typeFactory: ShopCampaignCarouselProductAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopCampaignCarouselProductAdapterTypeFactory>(typeFactory){


}