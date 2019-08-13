package com.tokopedia.affiliate.feature.dashboard.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.affiliate.feature.dashboard.data.pojo.commission.AffiliateHistoryPojo
import com.tokopedia.affiliate.feature.dashboard.data.pojo.commission.AffiliateProductDetailResponse
import com.tokopedia.affiliate.feature.dashboard.data.pojo.commission.AffiliateProductTxResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-08-12
 */
class CommissionDetailViewModel
@Inject constructor(val baseDispatcher: CoroutineDispatcher,
                    private val getProductDetailUseCase: GraphqlUseCase<AffiliateProductDetailResponse>,
                    private val getTranscationDetailUseCase: GraphqlUseCase<AffiliateProductTxResponse>)
    : BaseViewModel(baseDispatcher) {

    val productDetailRsp = MutableLiveData<Result<AffiliateProductDetailResponse>>()
    val transactionDetailRsp = MutableLiveData<Result<AffiliateProductTxResponse>>()

    init {
        getProductDetailUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
    }

    fun getFirstData() {
        launch {
            val job1 = async(Dispatchers.IO) {

                delay (1000)
                getProductDetailUseCase.executeOnBackground().affiliatedProductDetail
            }
            val job2 = async(Dispatchers.IO) {
                delay (1000)
                getTranscationDetailUseCase.executeOnBackground().affiliatedProductTxList
            }
            val result = Pair(job1.await(), job2.await())
        }
    }

    fun loadMoreTxDetail() {

    }

    //create mapping pojo ke view model
    fun mapProductDetailData(pojo: AffiliateProductDetailResponse) : CommissionDetailHeaderViewModel {
        return CommissionDetailHeaderViewModel(0)
    }

    fun mapFirstProductTransactionData(pojo: AffiliateProductTxResponse) : CommissionTransactionViewModel {
        return CommissionTransactionViewModel(pojo.affiliatedProductTxList.hasNext,
                pojo.affiliatedProductTxList.next,
                mapCommissionTransaction(pojo.affiliatedProductTxList.history)
        )
    }

    fun mapCommissionTransaction(pojoList: List<AffiliateHistoryPojo>): MutableList<CommissionDetaiItemViewModel> {
        val list: MutableList<CommissionDetaiItemViewModel> = ArrayList()
        for (pojo in pojoList) {
//            list.add(CommissionDetaiItemViewModel(0))
        }
        return list
    }
}