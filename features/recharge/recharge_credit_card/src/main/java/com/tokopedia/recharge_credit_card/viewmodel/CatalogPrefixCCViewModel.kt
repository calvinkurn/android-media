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
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCCatalogPrefix
import com.tokopedia.recharge_credit_card.datamodel.RechargeCreditCard
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatalogPrefixCCViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                   private val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _rechargeCreditCard = MutableLiveData<RechargeCreditCard>()
    private val _errorPrefix = MutableLiveData<String>()
    private val _bankNotSupported = MutableLiveData<String>()

    val creditCardSelected: LiveData<RechargeCreditCard> = _rechargeCreditCard
    val errorPrefix: LiveData<String> = _errorPrefix
    val bankNotSupported: LiveData<String> = _bankNotSupported


    private var foundPrefix: Boolean = false

    fun getPrefixes(rawQuery: String, creditCard: String) {
        launchCatchError(block = {
            val mapParam = mutableMapOf<String, Any>()
            mapParam[MENU_ID] = CC_MENU_ID

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeCCCatalogPrefix::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 15).build())
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
        const val MENU_ID = "menuId"
        const val CC_MENU_ID = 169

        //staging
//        const val CC_MENU_ID = 86
    }
}