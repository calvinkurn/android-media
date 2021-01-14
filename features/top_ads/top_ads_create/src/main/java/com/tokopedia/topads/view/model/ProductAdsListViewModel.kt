package com.tokopedia.topads.view.model

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.ETALASE
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.ROWS
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_ID
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_Id
import com.tokopedia.topads.common.data.internal.ParamObject.SORT_BY
import com.tokopedia.topads.common.data.internal.ParamObject.START
import com.tokopedia.topads.common.data.internal.ParamObject.STATUS
import com.tokopedia.topads.common.data.response.ResponseEtalase
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.ResponseKeywordSuggestion
import com.tokopedia.topads.data.response.ResponseProductList
import com.tokopedia.topads.view.RequestHelper
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 06,November,2019
 */
class ProductAdsListViewModel @Inject constructor(
        private val context: Context,
        private val dispatcher: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val gqlRepository: GraphqlRepository) : BaseViewModel(dispatcher.main) {
    private var totalCount = 0

    fun etalaseList(onSuccess: ((List<ResponseEtalase.Data.ShopShowcasesByShopID.Result>) -> Unit), onError: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(dispatcher.io) {
                        val request = RequestHelper.getGraphQlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_etalase_list),
                                ResponseEtalase.Data::class.java, hashMapOf(SHOP_Id to userSession.shopId))
                        val cacheStrategy = RequestHelper.getCacheStrategy()
                        gqlRepository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseEtalase.Data>().shopShowcasesByShopID.let {
                        onSuccess(it.result)
                    }
                },
                onError = {
                    onError(it)
                }
        )
    }

    fun productList(keyword: String, etalaseId: String, sortBy: String, isPromoted: String, rows: Int, start: Int, onSuccess: ((List<ResponseProductList.Result.TopadsGetListProduct.Data>, eof: Boolean) -> Unit),
                    onEmpty: (() -> Unit), onError: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val queryMap = hashMapOf<String, Any>(
                            KEYWORD to keyword,
                            ETALASE to etalaseId,
                            SORT_BY to sortBy,
                            ROWS to rows,
                            START to start,
                            STATUS to isPromoted,
                                SHOP_ID to Integer.parseInt(userSession.shopId)
                    )
                    val result = withContext(dispatcher.io) {
                        val request = RequestHelper.getGraphQlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_productlist),
                                ResponseProductList.Result::class.java, queryMap)
                        val cacheStrategy = RequestHelper.getCacheStrategy()
                        gqlRepository.getReseponse(listOf(request), cacheStrategy)

                    }

                    result.getSuccessData<ResponseProductList.Result>().let {
                        if (it.topadsGetListProduct.data.isEmpty()) {
                            onEmpty()
                        } else {
                            if (etalaseId.isEmpty()) {
                                totalCount = it.topadsGetListProduct.data.size
                            }
                            onSuccess(it.topadsGetListProduct.data, it.topadsGetListProduct.eof)
                        }
                    }
                },
                onError = {
                    onError(it)
                }
        )
    }

    fun addSemuaProduk(): ResponseEtalase.Data.ShopShowcasesByShopID.Result {
        return ResponseEtalase.Data.ShopShowcasesByShopID.Result(totalCount, "", "Semua Etalase", 0)
    }
}