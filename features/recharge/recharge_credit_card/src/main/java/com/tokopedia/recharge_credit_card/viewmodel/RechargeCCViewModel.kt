package com.tokopedia.recharge_credit_card.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankList
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankListReponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCCViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                              private val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val rechargeCCBankList = MutableLiveData<RechargeCCBankList>()
    val errorCCBankList = MutableLiveData<String>()

    fun getListBank(rawQuery: String, categoryId: Int) {
        launchCatchError(block = {
            val mapParam = mutableMapOf<String, Any>()
            mapParam[CATEGORY_ID] = categoryId

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeCCBankListReponse::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            }.getSuccessData<RechargeCCBankListReponse>()

            if (data.rechargeCCBankList.messageError.isEmpty()) {
                rechargeCCBankList.postValue(data.rechargeCCBankList)
            } else {
                errorCCBankList.postValue(data.rechargeCCBankList.messageError)
            }

        }) {
            errorCCBankList.postValue(it.message)
        }
    }

    companion object {
        const val CATEGORY_ID = "categoryId"
    }
}