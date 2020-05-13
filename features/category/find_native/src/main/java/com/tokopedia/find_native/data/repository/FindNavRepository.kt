package com.tokopedia.find_native.data.repository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common_category.model.filter.FilterResponse
import com.tokopedia.common_category.model.productModel.ProductListResponse
import com.tokopedia.find_native.data.model.RelatedLinkResponse
import com.tokopedia.find_native.util.FindNavConstants
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import javax.inject.Inject
import javax.inject.Named

class FindNavRepository @Inject constructor() {

    @Inject
    lateinit var baseRepository: BaseRepository

    @field:[Inject Named(FindNavConstants.GQL_NAV_SEARCH_PRODUCT)]
    lateinit var productListQuery: String

    @field:[Inject Named(FindNavConstants.GQL_NAV_QUICK_FILTER)]
    lateinit var quickFilterListQuery: String

    @field:[Inject Named(FindNavConstants.GQL_NAV_DYNAMIC_FILTER)]
    lateinit var dynamicFilterListQuery: String

    @field:[Inject Named(FindNavConstants.GQL_NAV_RELATED_LINK)]
    lateinit var relatedLinkListQuery: String

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