package com.tokopedia.shop.sort.data.source.cloud

import androidx.collection.ArrayMap
import com.tokopedia.shop.sort.data.source.cloud.api.ShopAceApi
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by normansyahputa on 2/22/18.
 */
class ShopProductSortCloudDataSource @Inject constructor(private val shopAceApi: ShopAceApi) {
    val dynamicFilter: Observable<List<ShopProductSort>>
        get() = shopAceApi.getDynamicFilter(paramSortProduct()).map(Func1 { dataResponseResponse -> dataResponseResponse?.body()?.data?.sort })

    companion object {
        private const val DEVICE = "device"
        private const val SOURCE = "source"
        private const val DEFAULT_DEVICE = "android"
        private const val SHOP_PRODUCT = "shop_product"
        private fun paramSortProduct(): Map<String?, String?> {
            val params: MutableMap<String?, String?> = ArrayMap()
            params[DEVICE] = DEFAULT_DEVICE
            params[SOURCE] = SHOP_PRODUCT
            return params
        }
    }
}