package com.tokopedia.recharge_credit_card.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common.topupbills.favoritepdp.domain.repository.RechargeFavoriteNumberRepository
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankList
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankListReponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCCatalogPrefix
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCMenuDetailResponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCreditCard
import com.tokopedia.recharge_credit_card.datamodel.TickerCreditCard
import com.tokopedia.recharge_credit_card.isMasked
import com.tokopedia.recharge_credit_card.util.RechargeCCConst
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCCViewModel @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val dispatcher: CoroutineDispatcher,
    private val rechargeFavoriteNumberRepo: RechargeFavoriteNumberRepository
) : BaseViewModel(dispatcher) {

    var prefixData: RechargeCCCatalogPrefix = RechargeCCCatalogPrefix()

    val rechargeCCBankList = MutableLiveData<RechargeCCBankList>()
    val errorCCBankList = MutableLiveData<Throwable>()
    val tickers = MutableLiveData<List<TickerCreditCard>>()

    private val _rechargeCreditCard = MutableLiveData<RechargeCreditCard>()
    private val _prefixSelect = MutableLiveData<RechargeNetworkResult<RechargeCCCatalogPrefix>>()
    private val _prefixValidation = MutableLiveData<Boolean>()
    private val _favoriteChipsData = MutableLiveData<RechargeNetworkResult<List<FavoriteChipModel>>>()
    private val _autoCompleteData = MutableLiveData<RechargeNetworkResult<List<AutoCompleteModel>>>()
    private val _prefillData = MutableLiveData<RechargeNetworkResult<PrefillModel>>()

    val creditCardSelected: LiveData<RechargeCreditCard> = _rechargeCreditCard
    val prefixSelect: LiveData<RechargeNetworkResult<RechargeCCCatalogPrefix>> = _prefixSelect
    val prefixValidation: LiveData<Boolean> = _prefixValidation
    val favoriteChipsData: LiveData<RechargeNetworkResult<List<FavoriteChipModel>>> = _favoriteChipsData
    val autoCompleteData: LiveData<RechargeNetworkResult<List<AutoCompleteModel>>> = _autoCompleteData
    val prefillData: LiveData<RechargeNetworkResult<PrefillModel>> = _prefillData

    var categoryName: String = ""
    var loyaltyStatus: String = ""

    var validatorJob: Job? = null

    fun getMenuDetail(rawQuery: String, menuId: String) {
        launchCatchError(block = {
            val mapParam = mutableMapOf<String, Any>()
            mapParam[MENU_ID] = menuId.toIntSafely()

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeCCMenuDetailResponse::class.java, mapParam)
                graphqlRepository.response(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * CACHE_MINUTES_MENU_DETAIL).build())
            }.getSuccessData<RechargeCCMenuDetailResponse>()

            categoryName = data.menuDetail.menuName
            loyaltyStatus = data.menuDetail.userPerso.loyaltyStatus

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
                graphqlRepository.response(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * CACHE_MINUTES_GET_LIST_BANK).build())
            }.getSuccessData<RechargeCCBankListReponse>()

            if (data.rechargeCCBankList.messageError.isEmpty()) {
                rechargeCCBankList.postValue(data.rechargeCCBankList)
            } else {
                errorCCBankList.postValue(MessageErrorException(data.rechargeCCBankList.messageError))
            }

        }) {
            errorCCBankList.postValue(it)
        }
    }

    fun getPrefixes(rawQuery: String, menuId: String) {
        launchCatchError(block = {
            val mapParam = mutableMapOf<String, Any>()
            mapParam[MENU_ID] = menuId.toIntSafely()

            prefixData = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeCCCatalogPrefix::class.java, mapParam)
                graphqlRepository.response(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * CACHE_MINUTES_GET_PREFIX).build())
            }.getSuccessData()

            _prefixSelect.postValue(RechargeNetworkResult.Success(prefixData))
        }) {
            _prefixSelect.postValue(RechargeNetworkResult.Fail(it))
        }
    }

    fun setFavoriteNumberLoading(){
        _favoriteChipsData.value = RechargeNetworkResult.Loading
    }

    fun getFavoriteNumbers(categoryIds: List<Int>, favoriteNumberTypes: List<FavoriteNumberType>) {
        launch {
            val data = rechargeFavoriteNumberRepo.getFavoriteNumbers(favoriteNumberTypes, categoryIds)
            for (type in favoriteNumberTypes) {
                when (type) {
                    FavoriteNumberType.PREFILL -> {
                        _prefillData.postValue(RechargeNetworkResult.Success(data.prefill))
                    }
                    FavoriteNumberType.CHIP -> {
                        _favoriteChipsData.postValue(RechargeNetworkResult.Success(data.favoriteChips))
                    }
                    FavoriteNumberType.LIST -> {
                        _autoCompleteData.postValue(RechargeNetworkResult.Success(data.autoCompletes))
                    }
                }
            }
        }
    }

    fun checkPrefixNumber(creditCard: String) {
        var isPrefixFound = false
        if (prefixData.prefixSelect.prefixes.isNotEmpty()) {
            prefixData.prefixSelect.prefixes.map {
                if (creditCard.startsWith(it.value)) {
                    isPrefixFound = true
                    _rechargeCreditCard.postValue(
                        RechargeCreditCard(
                            it.operator.id,
                            it.operator.attribute.defaultProductId,
                            it.operator.attribute.imageUrl,
                            it.operator.attribute.name
                        )
                    )
                }
            }
        }
        if (!isPrefixFound) {
            _rechargeCreditCard.postValue(RechargeCreditCard())
        }
    }

    fun cancelValidatorJob() {
        validatorJob?.cancel()
    }

    fun validateCCNumber(creditCard: String) {
        validatorJob = launch {
            if (creditCard.isMasked()) {
                _prefixValidation.postValue(true)
            } else {
                var isValid = false
                prefixData.prefixSelect.validations.forEach { validation ->
                    isValid = creditCard.matches(validation.rule.toRegex())
                }
                delay(RechargeCCConst.VALIDATOR_DELAY_TIME)
                _prefixValidation.postValue(isValid)
            }
        }
    }

    companion object {
        private const val CATEGORY_ID = "categoryId"
        private const val MENU_ID = "menuId"

        private const val CACHE_MINUTES_MENU_DETAIL = 5
        private const val CACHE_MINUTES_GET_LIST_BANK = 10
        private const val CACHE_MINUTES_GET_PREFIX = 10
    }
}