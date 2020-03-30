package com.tokopedia.recharge_credit_card.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_credit_card.datamodel.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCCViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                              private val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val rechargeCCBankList = MutableLiveData<RechargeCCBankList>()
    val errorCCBankList = MutableLiveData<String>()
    val tickers = MutableLiveData<List<TickerCreditCard>>()

    private val _rechargeCreditCard = MutableLiveData<RechargeCreditCard>()
    private val _errorPrefix = MutableLiveData<String>()
    private val _bankNotSupported = MutableLiveData<String>()

    val creditCardSelected: LiveData<RechargeCreditCard> = _rechargeCreditCard
    val errorPrefix: LiveData<String> = _errorPrefix
    val bankNotSupported: LiveData<String> = _bankNotSupported

    private var foundPrefix: Boolean = false

    fun getMenuDetail(rawQuery: String, menuId: String) {
        launchCatchError(block = {
            val mapParam = mutableMapOf<String, Any>()
            mapParam[MENU_ID] = menuId.toInt()

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeCCMenuDetailResponse::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5).build())
            }.getSuccessData<RechargeCCMenuDetailResponse>()

            if (data.menuDetail.tickers.isNotEmpty()) {
                tickers.postValue(data.menuDetail.tickers)
            } else {
                tickers.postValue(listOf())
            }
        }) {

        }
    }

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

    fun getPrefixes(rawQuery: String, creditCard: String, menuId: String) {
        launchCatchError(block = {
            val mapParam = mutableMapOf<String, Any>()
            mapParam[MENU_ID] = menuId.toInt()

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeCCCatalogPrefix::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            }.getSuccessData<RechargeCCCatalogPrefix>()

            if (data.prefixSelect.prefixes.isNotEmpty()) {
                data.prefixSelect.prefixes.map {
                    if (creditCard.startsWith(it.value)) {
                        foundPrefix = true
                        return@map _rechargeCreditCard.postValue(RechargeCreditCard(
                                it.operator.id,
                                it.operator.attribute.defaultProductId,
                                it.operator.attribute.imageUrl
                        ))
                    }
                }
            } else {
                _bankNotSupported.postValue("")
            }

            if (!foundPrefix) {
                _bankNotSupported.postValue("")
            }
        }) {
            _errorPrefix.postValue(it.message)
        }
    }

    companion object {
        private const val CATEGORY_ID = "categoryId"
        private const val MENU_ID = "menuId"

        //production
        //const val CC_MENU_ID = 169
        //staging
        //const val CC_MENU_ID = 86
    }
}