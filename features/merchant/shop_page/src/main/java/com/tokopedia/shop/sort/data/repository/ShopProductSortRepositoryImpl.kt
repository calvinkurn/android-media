package com.tokopedia.shop.sort.data.repository

import com.tokopedia.shop.sort.data.source.cloud.ShopProductSortCloudDataSource
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
import com.tokopedia.shop.sort.domain.repository.ShopProductSortRepository
import rx.Observable
import javax.inject.Inject

/**
 * Created by nathan on 2/15/18.
 */
class ShopProductSortRepositoryImpl @Inject constructor(private val shopFilterCloudDataSource: ShopProductSortCloudDataSource) : ShopProductSortRepository {
    override val shopProductFilter: Observable<List<ShopProductSort>>
        get() = shopFilterCloudDataSource.dynamicFilter
}