package com.tokopedia.discovery.find.data.repository

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.discovery.R
import com.tokopedia.discovery.categoryrevamp.data.filter.FilterResponse
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductListResponse
import com.tokopedia.discovery.find.data.model.RelatedLinkResponse
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.tradein_common.repository.BaseRepository
import javax.inject.Inject

class FindNavRepository @Inject constructor() {

    @Inject
    lateinit var baseRepository: BaseRepository

    @Inject
    lateinit var resources: Resources

    suspend fun getProductList(reqParams: Map<String, String>): ProductListResponse {
        val query = GraphqlHelper.loadRawString(resources, R.raw.gql_nav_search_product)
        return baseRepository.getGQLData(query, ProductListResponse::class.java, reqParams) as ProductListResponse
    }

    suspend fun getQuickFilterList(reqParams: Map<String, String>): MutableList<Filter>? {
        val query = GraphqlHelper.loadRawString(resources, R.raw.gql_nav_quick_filter)
        return (baseRepository.getGQLData(query, FilterResponse::class.java, reqParams) as FilterResponse).dynamicAttribute?.data?.filter
    }

    suspend fun getDynamicFilterList(reqParams: Map<String, String>): DynamicFilterModel? {
        val query = GraphqlHelper.loadRawString(resources, R.raw.gql_nav_dynamic_attribute)
        return (baseRepository.getGQLData(query, FilterResponse::class.java, reqParams) as FilterResponse).dynamicAttribute
    }

    suspend fun getRelatedLinkList(reqParams: Map<String, String>): RelatedLinkResponse? {
        val query = GraphqlHelper.loadRawString(resources, R.raw.gql_find_related_link)
        return baseRepository.getGQLData(query, RelatedLinkResponse::class.java, reqParams) as RelatedLinkResponse
    }
}