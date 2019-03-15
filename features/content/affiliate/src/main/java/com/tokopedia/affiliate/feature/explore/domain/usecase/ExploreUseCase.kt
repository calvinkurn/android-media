package com.tokopedia.affiliate.feature.explore.domain.usecase

import android.content.Context
import android.text.TextUtils
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliate.R
import com.tokopedia.affiliate.common.viewmodel.ExploreCardViewModel
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreData
import com.tokopedia.affiliate.feature.explore.data.pojo.ExploreProduct
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreParams
import com.tokopedia.affiliate.feature.explore.view.viewmodel.ExploreViewModel
import com.tokopedia.affiliate.feature.explore.view.viewmodel.FilterViewModel
import com.tokopedia.affiliate.feature.explore.view.viewmodel.SortViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author by yfsx on 08/10/18.
 */
class ExploreUseCase @Inject constructor(
        @ApplicationContext private val context: Context,
        private val graphqlUseCase: GraphqlUseCase
) : UseCase<List<Visitable<*>>>() {

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.DAY.`val`())
                .setSessionIncluded(true)
                .build()
        )
    }

    override fun createObservable(requestParams: RequestParams?): Observable<List<Visitable<*>>> {
        return graphqlUseCase.createObservable(RequestParams.EMPTY).map {
            val data: ExploreData = it.getData(ExploreData::class.java)
            mapExploreProducts(data.exploreProduct)
        }
    }

    fun createObservable(exploreParams: ExploreParams): Observable<List<Visitable<*>>> {
        val query = GraphqlHelper.loadRawString(
                context.resources,
                R.raw.query_explore
        )
        val graphqlRequest = GraphqlRequest(
                query,
                ExploreData::class.java,
                getParam(exploreParams).parameters
        )
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return createObservable(RequestParams.EMPTY)
    }

    private fun mapExploreProducts(exploreProduct: ExploreProduct): List<Visitable<*>> {
        val itemList = ArrayList<Visitable<*>>()
        exploreProduct.products.forEach {
            itemList.add(
                    ExploreViewModel(ExploreCardViewModel(
                            it.name,
                            context.getString(R.string.af_get_commission),
                            it.commission,
                            it.image,
                            "",
                            it.adId,
                            it.productId
                    ))
            )
        }
        return itemList
    }

    private fun getParam(exploreParams: ExploreParams): RequestParams {
        val params = RequestParams.create()
        if (!TextUtils.isEmpty(exploreParams.keyword)) {
            params.putString(PARAM_KEYWORD, exploreParams.keyword)
        }
        if (!TextUtils.isEmpty(exploreParams.cursor)) {
            params.putString(PARAM_CURSOR, exploreParams.cursor)
        }
        if (exploreParams.filters.size != 0) {
            params.putObject(PARAM_FILTER, constructFilterParams(exploreParams.filters))
        }
        if (exploreParams.sort != null && !TextUtils.isEmpty(exploreParams.sort.text)) {
            params.putObject(PARAM_SORT, constructSortParams(exploreParams.sort))
        }
        return params
    }

    private fun constructFilterParams(filterList: List<FilterViewModel>): JsonArray {
        //array will be used for filter (id = 0), shop filter (id = 1), etc..
        val dataArray = JsonArray()
        if (filterList.isNotEmpty()) {
            val `object` = JsonObject()
            `object`.addProperty(PARAM_FILTER_KEY, PARAM_FILTER_KEY_DATA)
            `object`.addProperty(PARAM_FILTER_VALUE, appendIdValue(filterList))
            dataArray.add(`object`)
        }
        return dataArray
    }

    private fun appendIdValue(filterList: List<FilterViewModel>): String {
        val value = StringBuilder()
        for (j in filterList.indices) {
            val idList = filterList[j].ids
            for (i in idList.indices) {
                value.append(idList[i])
                if (i != idList.size - 1) {
                    value.append(",")
                }
            }
            if (j != filterList.size - 1) {
                value.append(",")
            }
        }
        return value.toString()
    }

    private fun constructSortParams(sort: SortViewModel): JsonObject {
        val `object` = JsonObject()
        `object`.addProperty(PARAM_SORT_KEY, sort.key)
        `object`.addProperty(PARAM_SORT_ASC, sort.isAsc)
        return `object`
    }

    companion object {
        private const val PARAM_CURSOR = "nextCursor"
        private const val PARAM_KEYWORD = "keyword"
        private const val PARAM_FILTER = "filter"
        private const val PARAM_SORT = "sort"
        private const val PARAM_FILTER_KEY = "key"
        private const val PARAM_FILTER_KEY_DATA = "d_id"
        private const val PARAM_FILTER_VALUE = "value"
        private const val PARAM_SORT_KEY = "key"
        private const val PARAM_SORT_ASC = "asc"
    }
}
