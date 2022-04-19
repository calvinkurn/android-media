package com.tokopedia.shop.sort.domain.repository

import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
import rx.Observable

/**
 * Created by sebastianuskh on 3/8/17.
 */
interface ShopProductSortRepository {
    val shopProductFilter: Observable<List<ShopProductSort>>
}