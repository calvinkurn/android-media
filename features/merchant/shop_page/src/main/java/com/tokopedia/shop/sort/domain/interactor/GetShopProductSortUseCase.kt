package com.tokopedia.shop.sort.domain.interactor

import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
import com.tokopedia.shop.sort.domain.repository.ShopProductSortRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by normansyahputa on 2/23/18.
 */
class GetShopProductSortUseCase @Inject constructor(private val shopProductFilterRepository: ShopProductSortRepository) : UseCase<List<ShopProductSort>>() {
    override fun createObservable(requestParams: RequestParams): Observable<List<ShopProductSort>> {
        return shopProductFilterRepository.shopProductFilter
    }
}