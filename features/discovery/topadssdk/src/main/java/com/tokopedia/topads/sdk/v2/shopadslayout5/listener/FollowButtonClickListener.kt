package com.tokopedia.topads.sdk.v2.shopadslayout5.listener

import com.tokopedia.topads.sdk.v2.shopadslayout5.uimodel.ShopProductModel

interface FollowButtonClickListener {

    fun onItemClicked(shopProductModelItem: ShopProductModel.ShopProductModelItem)
}
