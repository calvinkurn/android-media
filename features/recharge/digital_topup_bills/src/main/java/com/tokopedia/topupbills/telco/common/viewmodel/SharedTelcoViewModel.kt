package com.tokopedia.topupbills.telco.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.TopupBillsPromo
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 10/05/19.
 */
class SharedTelcoViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _recommendation = MutableLiveData<List<TopupBillsRecommendation>>()
    val recommendations: LiveData<List<TopupBillsRecommendation>>
        get() = _recommendation

    private val _selectedRecentNumber = MutableLiveData<TopupBillsRecommendation>()
    val selectedRecentNumber: LiveData<TopupBillsRecommendation>
        get() = _selectedRecentNumber

    private val _promo = MutableLiveData<List<TopupBillsPromo>>()
    val promos: LiveData<List<TopupBillsPromo>>
        get() = _promo

    private val _catalogPrefixSelect = MutableLiveData<Result<TelcoCatalogPrefixSelect>>()
    val catalogPrefixSelect: LiveData<Result<TelcoCatalogPrefixSelect>>
        get() = _catalogPrefixSelect

    private val _titleMenu = MutableLiveData<Boolean>()
    val titleMenu: LiveData<Boolean>
        get() = _titleMenu

    private val _promoImpression = MutableLiveData<Unit>()
    val promoImpression: LiveData<Unit>
        get() = _promoImpression

    private val _recentsImpression = MutableLiveData<Unit>()
    val recentsImpression: LiveData<Unit>
        get() = _recentsImpression

    fun setRecommendationTelco(recommendationTelco: List<TopupBillsRecommendation>) {
        _recommendation.postValue(recommendationTelco)
    }

    fun setPromoTelco(promoTelco: List<TopupBillsPromo>) {
        _promo.postValue(promoTelco)
    }

    fun setSelectedRecentNumber(topupBillsRecommendation: TopupBillsRecommendation) {
        _selectedRecentNumber.postValue(topupBillsRecommendation)
    }

    fun setTitleMenu(show: Boolean) {
        _titleMenu.postValue(show)
    }

    fun setPromoImpression() {
        _promoImpression.postValue(Unit)
    }

    fun setRecentsImpression() {
        _recentsImpression.postValue(Unit)
    }

    fun getPrefixOperator(rawQuery: String, menuId: Int) {
        launchCatchError(block = {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam[KEY_MENU_ID] = menuId

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogPrefixSelect::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * EXP_TIME).build())
            }.getSuccessData<TelcoCatalogPrefixSelect>()

            delay(DELAY_TIME)
            _catalogPrefixSelect.postValue(Success(data))
        }) {
            _catalogPrefixSelect.postValue(Fail(it))
        }
    }

    companion object {
        const val KEY_MENU_ID = "menuID"
        const val EXP_TIME = 10
        const val DELAY_TIME = 200L
    }

}