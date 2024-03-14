package com.tokopedia.topads.sdk.old.listener

import com.tokopedia.topads.sdk.domain.model.ShopProductModel

interface FollowButtonClickListener {

    fun onItemClicked(shopProductModelItem: ShopProductModel.ShopProductModelItem)
}
