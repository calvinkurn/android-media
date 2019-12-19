package com.tokopedia.affiliate.feature.dashboard.view.viewmodel

import androidx.lifecycle.MutableLiveData
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
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    val PARAM_CURSOR = "next"
    val PARAM_LIMIT = "limit"

    val commissionDetailRsp = MutableLiveData<Result<CommissionData>>()
    val transactionDetailLoadMoreRsp = MutableLiveData<Result<CommissionTransactionViewModel>>()

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

    fun loadMoreTxDetail(affId: String, cursor: String) {
        launchCatchError(block = {
            val firstDataDeferred = loadMoreCommissionTx(affId, cursor)
            transactionDetailLoadMoreRsp.value = Success(firstDataDeferred.await())
        }) {
            it.printStackTrace()
            transactionDetailLoadMoreRsp.value = Fail(it)
        }
    }

    private suspend fun loadFirstCommissionDataAsync(affId:String, forceRefresh: Boolean): Deferred<CommissionData> {
        return async(Dispatchers.IO) {
            val resultData = CommissionData()
            try {
                val productParam = mapOf(PARAM_AFF_ID to affId.toInt())
                val productRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_AFF_PRODUCT_DETAIL],
                    AffiliateProductDetailResponse::class.java, productParam)

                val txParam = mapOf(PARAM_AFF_ID to affId.toInt(), PARAM_LIMIT to 3, PARAM_CURSOR to "")
                val txRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_AFF_PRODUCT_TX_LIST],
                    AffiliateProductTxResponse::class.java, txParam)

                val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
                val requests = mutableListOf(productRequest, txRequest)
                val gqlResponse = graphqlRepository.getReseponse(requests, cacheStrategy)
                if (gqlResponse.getError(AffiliateProductDetailResponse::class.java)?.isNotEmpty() != true) {
                    val result = (gqlResponse.getData(AffiliateProductDetailResponse::class.java) as AffiliateProductDetailResponse)
                    resultData.commissionDetailHeaderViewModel = mapProductDetailData(result)
                }

                if (gqlResponse.getError(AffiliateProductTxResponse::class.java)?.isNotEmpty() != true) {
                    val result = (gqlResponse.getData(AffiliateProductTxResponse::class.java) as AffiliateProductTxResponse)
                    resultData.commissionTransactionViewModel = mapProductTransactionData(result)
                }
            } catch (e:Throwable) {

            }
            resultData
        }
    }

    private suspend fun loadMoreCommissionTx(affId:String, cursor: String): Deferred<CommissionTransactionViewModel> {
        return async(Dispatchers.IO) {
            var resultData = CommissionTransactionViewModel()
            val txParam = mapOf(PARAM_AFF_ID to affId.toInt(), PARAM_LIMIT to 3, PARAM_CURSOR to cursor)
            val txRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_AFF_PRODUCT_TX_LIST],
                    AffiliateProductTxResponse::class.java, txParam)

            val requests = mutableListOf(txRequest)
            try {
                val gqlResponse = graphqlRepository.getReseponse(requests)
                if (gqlResponse.getError(AffiliateProductTxResponse::class.java)?.isNotEmpty() != true) {
                    val result = (gqlResponse.getData(AffiliateProductTxResponse::class.java) as AffiliateProductTxResponse)
                    resultData = mapProductTransactionData(result)
                }

            }
            catch(t: Throwable) {
                t.printStackTrace()
            }
            resultData
        }
    }


    //create mapping pojo ke view model
    fun mapProductDetailData(pojo: AffiliateProductDetailResponse) : CommissionDetailHeaderViewModel {
        val data = pojo.affiliatedProductDetail
        return CommissionDetailHeaderViewModel(
                data.price,
                data.priceFormatted,
                data.isActive,
                data.commission,
                data.commissionFormatted,
                data.totalClick,
                data.totalSold,
                data.totalCommission,
                data.totalCommissionFormatted,
                data.shopID.toString(),
                data.shopName,
                data.productID,
                data.productName,
                data.productImg
        )
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
            list.add(CommissionDetailItemViewModel(
                    pojo.itemSent,
                    pojo.affCommission,
                    pojo.affCommissionFormatted,
                    pojo.affInvoice,
                    pojo.affInvoiceURL,
                    pojo.txTimeFormatted,
                    pojo.txTime,
                    pojo.tkpdInvoice,
                    pojo.tkpdInvoiceURL,
                    pojo.tkpdCommission,
                    pojo.tkpdCommissionFormatted,
                    pojo.netCommission,
                    pojo.netCommissionFormatted
            ))
        }
        return list
    }
}