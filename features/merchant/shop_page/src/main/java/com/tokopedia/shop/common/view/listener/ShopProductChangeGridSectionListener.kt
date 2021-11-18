package com.tokopedia.shop.common.view.listener;


import com.tokopedia.shop.common.util.ShopProductViewGridType
/**
 * Created by normansyahputa on 2/24/18.
 */

interface ShopProductChangeGridSectionListener {
    fun onChangeProductGridClicked(initialGridType: ShopProductViewGridType, finalGridType: ShopProductViewGridType)
}
