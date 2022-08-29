package com.tokopedia.topads.sdk.listener

import com.tokopedia.topads.sdk.domain.model.ShopProductModel

interface FollowButtonClickListener {

    fun onItemClicked(shopProductModelItem: ShopProductModel.ShopProductModelItem)
}
