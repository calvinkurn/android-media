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
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recharge_credit_card.datamodel.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject

class RechargeCCViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                              private val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val rechargeCCBankList = MutableLiveData<RechargeCCBankList>()
    val errorCCBankList = MutableLiveData<Throwable>()
    val tickers = MutableLiveData<List<TickerCreditCard>>()

    private val _rechargeCreditCard = MutableLiveData<RechargeCreditCard>()
    private val _errorPrefix = MutableLiveData<Throwable>()
    private val _bankNotSupported = MutableLiveData<Throwable>()

    val creditCardSelected: LiveData<RechargeCreditCard> = _rechargeCreditCard
    val errorPrefix: LiveData<Throwable> = _errorPrefix
    val bankNotSupported: LiveData<Throwable> = _bankNotSupported

    private var foundPrefix: Boolean = false
    var categoryName: String = ""

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

            categoryName = data.menuDetail.menuName
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
                errorCCBankList.postValue(MessageErrorException(data.rechargeCCBankList.messageError))
            }

        }) {
            errorCCBankList.postValue(MessageErrorException(it.message))
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
                                it.operator.attribute.imageUrl,
                                it.operator.attribute.name
                        ))
                    }
                }
            } else {
                _bankNotSupported.postValue(MessageErrorException(""))
            }

            if (!foundPrefix) {
                _bankNotSupported.postValue(MessageErrorException(""))
            }
        }) {
            _errorPrefix.postValue(it)
        }
    }

    companion object {
        private const val CATEGORY_ID = "categoryId"
        private const val MENU_ID = "menuId"
    }
}