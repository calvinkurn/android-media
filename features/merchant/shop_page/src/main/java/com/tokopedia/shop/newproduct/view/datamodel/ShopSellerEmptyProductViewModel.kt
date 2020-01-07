package com.tokopedia.shop.newproduct.view.datamodel

import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct
import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.product.data.source.cloud.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductBadge
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductLabel

/**
 * Created by nathan on 2/6/18.
 */

class ShopSellerEmptyProductViewModel : BaseShopProductViewModel {

    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
