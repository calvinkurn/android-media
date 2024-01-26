package com.tokopedia.recharge_credit_card.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.AutoCompleteModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.FavoriteChipModel
import com.tokopedia.common.topupbills.favoritepdp.domain.model.PrefillModel
import com.tokopedia.common.topupbills.favoritepdp.domain.repository.RechargeFavoriteNumberRepository
import com.tokopedia.common.topupbills.favoritepdp.util.FavoriteNumberType
import com.tokopedia.common_digital.common.usecase.GetDppoConsentUseCase
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.network.authentication.HEADER_X_TKPD_APP_VERSION
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankList
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCBankListReponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCCatalogPrefix
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCDppoConsentUimodel
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCMenuDetail
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCMenuDetailResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeCCViewModel @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val dispatcher: CoroutineDispatcher,
    private val rechargeFavoriteNumberRepo: RechargeFavoriteNumberRepository,
    private val getDppoConsentUseCase: GetDppoConsentUseCase
) : BaseViewModel(dispatcher) {

    var prefixData: RechargeCCCatalogPrefix = RechargeCCCatalogPrefix()

    val rechargeCCBankList = MutableLiveData<RechargeCCBankList>()
    val errorCCBankList = MutableLiveData<Throwable>()

    private val _prefixSelect = MutableLiveData<RechargeNetworkResult<RechargeCCCatalogPrefix>>()
    private val _favoriteChipsData = MutableLiveData<RechargeNetworkResult<List<FavoriteChipModel>>>()
    private val _autoCompleteData = MutableLiveData<RechargeNetworkResult<List<AutoCompleteModel>>>()
    private val _prefillData = MutableLiveData<RechargeNetworkResult<PrefillModel>>()
    private val _menuDetail = MutableLiveData<RechargeNetworkResult<RechargeCCMenuDetail>>()
    private val _dppoConsent = MutableLiveData<Result<RechargeCCDppoConsentUimodel>>()

    val prefixSelect: LiveData<RechargeNetworkResult<RechargeCCCatalogPrefix>> = _prefixSelect
    val favoriteChipsData: LiveData<RechargeNetworkResult<List<FavoriteChipModel>>> = _favoriteChipsData
    val autoCompleteData: LiveData<RechargeNetworkResult<List<AutoCompleteModel>>> = _autoCompleteData
    val prefillData: LiveData<RechargeNetworkResult<PrefillModel>> = _prefillData
    val menuDetail: LiveData<RechargeNetworkResult<RechargeCCMenuDetail>> = _menuDetail
    val dppoConsent: LiveData<Result<RechargeCCDppoConsentUimodel>> = _dppoConsent

    var categoryName: String = ""
    var loyaltyStatus: String = ""

    fun getMenuDetail(rawQuery: String, menuId: String) {
        launch {
            runCatching {
                val mapParam = mutableMapOf<String, Any>()
                mapParam[MENU_ID] = menuId.toIntSafely()

                val data = withContext(dispatcher) {
                    val graphqlRequest = GraphqlRequest(rawQuery, RechargeCCMenuDetailResponse::class.java, mapParam)
                    graphqlRepository.response(
                        listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                            .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * CACHE_MINUTES_MENU_DETAIL).build()
                    )
                }.getSuccessData<RechargeCCMenuDetailResponse>()

                categoryName = data.menuDetail.menuName
                loyaltyStatus = data.menuDetail.userPerso.loyaltyStatus

                _menuDetail.postValue(RechargeNetworkResult.Success(data.menuDetail))
            }.onFailure {
                _menuDetail.postValue(RechargeNetworkResult.Fail(it))
            }
        }
    }

    fun getListBank(rawQuery: String, categoryId: Int) {
        launch {
            runCatching {
                val mapParam = mutableMapOf<String, Any>()
                mapParam[CATEGORY_ID] = categoryId

                val data = withContext(dispatcher) {
                    val graphqlRequest = GraphqlRequest(rawQuery, RechargeCCBankListReponse::class.java, mapParam)
                    graphqlRepository.response(
                        listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                            .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * CACHE_MINUTES_GET_LIST_BANK).build()
                    )
                }.getSuccessData<RechargeCCBankListReponse>()

                if (data.rechargeCCBankList.messageError.isEmpty()) {
                    rechargeCCBankList.postValue(data.rechargeCCBankList)
                } else {
                    errorCCBankList.postValue(MessageErrorException(data.rechargeCCBankList.messageError))
                }
            }.onFailure {
                errorCCBankList.postValue(it)
            }
        }
    }

    fun getPrefixes(rawQuery: String, menuId: String) {
        launch {
            runCatching {
                val mapParam = mutableMapOf<String, Any>()
                mapParam[MENU_ID] = menuId.toIntSafely()

                prefixData = withContext(dispatcher) {
                    val graphqlRequest = GraphqlRequest(rawQuery, RechargeCCCatalogPrefix::class.java, mapParam)
                    graphqlRepository.response(
                        listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                            .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * CACHE_MINUTES_GET_PREFIX).build()
                    )
                }.getSuccessData()

                _prefixSelect.postValue(RechargeNetworkResult.Success(prefixData))
            }.onFailure {
                _prefixSelect.postValue(RechargeNetworkResult.Fail(it))
            }
        }
    }

    fun setFavoriteNumberLoading() {
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

    fun getDppoConsent(categoryId: Int) {
        launch {
            runCatching {
                val data = getDppoConsentUseCase.execute(categoryId)
                val uiData = RechargeCCDppoConsentUimodel(
                    description = if (data.persoData.items.isNotEmpty()) {
                        data.persoData.items[0].title
                    } else {
                        ""
                    }
                )
                _dppoConsent.postValue(Success(uiData))
            }.onFailure {
                _dppoConsent.postValue(Fail(it))
            }
        }
    }

    fun getPcidssCustomHeaders(): HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers[HEADER_X_TKPD_APP_VERSION] = "android-" + GlobalConfig.VERSION_NAME
        return headers
    }

    companion object {
        private const val CATEGORY_ID = "categoryId"
        private const val MENU_ID = "menuId"

        private const val CACHE_MINUTES_MENU_DETAIL = 5
        private const val CACHE_MINUTES_GET_LIST_BANK = 10
        private const val CACHE_MINUTES_GET_PREFIX = 10
    }
}
