package com.tokopedia.discovery.find.data.repository

import com.tokopedia.discovery.categoryrevamp.data.filter.FilterResponse
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductListResponse
import com.tokopedia.discovery.find.data.model.RelatedLinkResponse
import com.tokopedia.discovery.find.util.FindNavConstants
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.tradein_common.repository.BaseRepository
import javax.inject.Inject
import javax.inject.Named

class FindNavRepository @Inject constructor(@Named(FindNavConstants.GQL_NAV_SEARCH_PRODUCT) val productListQuery: String,
                                            @Named(FindNavConstants.GQL_NAV_QUICK_FILTER) val quickFilterListQuery: String,
                                            @Named(FindNavConstants.GQL_NAV_DYNAMIC_FILTER) val dynamicFilterListQuery: String,
                                            @Named(FindNavConstants.GQL_NAV_RELATED_LINK) val relatedLinkListQuery: String) {

    @Inject
    lateinit var baseRepository: BaseRepository

    suspend fun getProductList(reqParams: Map<String, String>): ProductListResponse {
        return baseRepository.getGQLData(productListQuery, ProductListResponse::class.java, reqParams) as ProductListResponse
    }

    suspend fun getQuickFilterList(reqParams: Map<String, String>): MutableList<Filter>? {
        return (baseRepository.getGQLData(quickFilterListQuery, FilterResponse::class.java, reqParams) as FilterResponse).dynamicAttribute?.data?.filter as MutableList<Filter>?
    }

    suspend fun getDynamicFilterList(reqParams: Map<String, String>): DynamicFilterModel? {
        return (baseRepository.getGQLData(dynamicFilterListQuery, FilterResponse::class.java, reqParams) as FilterResponse).dynamicAttribute
    }

    suspend fun getRelatedLinkList(reqParams: Map<String, String>): RelatedLinkResponse? {
        return baseRepository.getGQLData(relatedLinkListQuery, RelatedLinkResponse::class.java, reqParams) as RelatedLinkResponse
    }
}