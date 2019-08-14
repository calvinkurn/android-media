package com.tokopedia.affiliate.feature.dashboard.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.affiliate.feature.dashboard.data.pojo.commission.AffiliateHistoryPojo
import com.tokopedia.affiliate.feature.dashboard.data.pojo.commission.AffiliateProductDetailResponse
import com.tokopedia.affiliate.feature.dashboard.data.pojo.commission.AffiliateProductTxResponse
import com.tokopedia.affiliate.feature.dashboard.di.RawQueryKeyConstant
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.coroutinedata.CommissionData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-08-12
 */
class CommissionDetailViewModel
@Inject constructor(val baseDispatcher: CoroutineDispatcher,
                    private val graphqlRepository: GraphqlRepository,
                    private val rawQueries: Map<String, String>)
    : BaseViewModel(baseDispatcher) {

    val PARAM_AFF_ID = "affiliatedProductID"

    val commissionDetailRsp = MutableLiveData<Result<CommissionData>>()
    val transactionDetailLoadMoreRsp = MutableLiveData<Result<AffiliateProductTxResponse>>()

    init {
    }

    fun getFirstData(affId:String, forceRefresh: Boolean) {
        launchCatchError(block = {
            val firstDataDeferred = loadFirstCommissionDataAsync(affId, forceRefresh)
            commissionDetailRsp.value = Success(firstDataDeferred.await())
        }) {
            it.printStackTrace()
            commissionDetailRsp.value = Fail(it)
        }
    }

    private suspend fun loadFirstCommissionDataAsync(affId:String, forceRefresh: Boolean): Deferred<CommissionData> {
        return async(Dispatchers.IO) {
            val resultData = CommissionData()
            val productParam = mapOf(PARAM_AFF_ID to affId)
            val productRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_AFF_PRODUCT_DETAIL],
                    AffiliateProductDetailResponse::class.java, productParam)

            val txParam = mapOf(PARAM_AFF_ID to affId)
            val txRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_AFF_PRODUCT_TX_LIST],
                    AffiliateProductTxResponse::class.java, txParam)

            val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
            val requests = mutableListOf(productRequest, txRequest)
            try {
                val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)
                if (gqlResponse.getError(AffiliateProductDetailResponse::class.java).isNotEmpty() != true) {
                    val result = (gqlResponse.getData(AffiliateProductDetailResponse::class.java) as AffiliateProductDetailResponse)
                    resultData.commissionDetailHeaderViewModel = mapProductDetailData(result)
                }

                if (gqlResponse.getError(AffiliateProductTxResponse::class.java).isNotEmpty() != true) {
                    val result = (gqlResponse.getData(AffiliateProductTxResponse::class.java) as AffiliateProductTxResponse)
                    resultData.commissionTransactionViewModel = mapProductTransactionData(result)
                }

            }
            catch(t: Throwable) {}
            resultData
        }
    }

    fun loadMoreTxDetail(affId: String, cursor: String) {

    }

    //create mapping pojo ke view model
    fun mapProductDetailData(pojo: AffiliateProductDetailResponse) : CommissionDetailHeaderViewModel {
        return CommissionDetailHeaderViewModel(0)
    }

    fun mapProductTransactionData(pojo: AffiliateProductTxResponse) : CommissionTransactionViewModel {
        return CommissionTransactionViewModel(pojo.affiliatedProductTxList.hasNext,
                pojo.affiliatedProductTxList.next,
                mapCommissionTransaction(pojo.affiliatedProductTxList.history)
        )
    }

    fun mapCommissionTransaction(pojoList: List<AffiliateHistoryPojo>): MutableList<CommissionDetailItemViewModel> {
        val list: MutableList<CommissionDetailItemViewModel> = ArrayList()
        for (pojo in pojoList) {
//            list.add(CommissionDetailItemViewModel(0))
        }
        return list
    }
}