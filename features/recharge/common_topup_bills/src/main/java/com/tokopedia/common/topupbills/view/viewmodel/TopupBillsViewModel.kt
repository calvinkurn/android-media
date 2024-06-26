package com.tokopedia.common.topupbills.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.topupbills.data.MultiCheckoutButtons
import com.tokopedia.common.topupbills.data.TelcoCatalogMenuDetailData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryQuery
import com.tokopedia.common.topupbills.data.TopupBillsMenuDetail
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumber
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberData
import com.tokopedia.common.topupbills.data.catalog_plugin.RechargeCatalogPlugin
import com.tokopedia.common.topupbills.data.express_checkout.RechargeExpressCheckout
import com.tokopedia.common.topupbills.data.express_checkout.RechargeExpressCheckoutData
import com.tokopedia.common.topupbills.favoritepage.domain.usecase.RechargeFavoriteNumberUseCase
import com.tokopedia.common.topupbills.favoritepage.util.FavoriteNumberDataMapper
import com.tokopedia.common.topupbills.favoritepage.view.util.FavoriteNumberActionType
import com.tokopedia.common.topupbills.view.model.search.TopupBillsSearchNumberDataModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigital
import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigitalData
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.uimodel.PromoDigitalModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by resakemal on 28/08/19.
 */
class TopupBillsViewModel @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val digitalCheckVoucherUseCase: DigitalCheckVoucherUseCase,
    private val rechargeFavoriteNumberUseCase: RechargeFavoriteNumberUseCase,
    private val remoteConfig: RemoteConfig,
    val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    var multiCheckoutButtons: List<MultiCheckoutButtons> = listOf()

    private val _enquiryData = MutableLiveData<Result<TopupBillsEnquiryData>>()
    val enquiryData: LiveData<Result<TopupBillsEnquiryData>>
        get() = _enquiryData

    private val _menuDetailData = MutableLiveData<Result<TopupBillsMenuDetail>>()
    val menuDetailData: LiveData<Result<TopupBillsMenuDetail>>
        get() = _menuDetailData

    private val _catalogPluginData = MutableLiveData<Result<RechargeCatalogPlugin>>()
    val catalogPluginData: LiveData<Result<RechargeCatalogPlugin>>
        get() = _catalogPluginData

    private val _favNumberData = MutableLiveData<Result<List<TopupBillsSearchNumberDataModel>>>()
    val favNumberData: LiveData<Result<List<TopupBillsSearchNumberDataModel>>>
        get() = _favNumberData

    private val _seamlessFavNumberData = MutableLiveData<Result<Pair<TopupBillsSeamlessFavNumber, Boolean>>>()
    val seamlessFavNumberData: LiveData<Result<Pair<TopupBillsSeamlessFavNumber, Boolean>>>
        get() = _seamlessFavNumberData

    private val _checkVoucherData = MutableLiveData<Result<PromoData>>()
    val checkVoucherData: LiveData<Result<PromoData>>
        get() = _checkVoucherData

    private val _expressCheckoutData = MutableLiveData<Result<RechargeExpressCheckoutData>>()
    val expressCheckoutData: LiveData<Result<RechargeExpressCheckoutData>>
        get() = _expressCheckoutData

    private var checkVoucherJob: Job? = null

    fun getEnquiry(rawQuery: String, mapParam: List<TopupBillsEnquiryQuery>) {
        launch {
            val params = mapOf(PARAM_FIELDS to mapParam)
            runCatching {
                val graphqlRequest = GraphqlRequest(rawQuery, TopupBillsEnquiryData::class.java, params)
                var data: TopupBillsEnquiryData
                do {
                    data = withContext(dispatcher.io) {
                        graphqlRepository.response(listOf(graphqlRequest))
                    }.getSuccessData()

                    // If data is pending delay query call
                    with(data.enquiry) {
                        if (status == STATUS_PENDING && retryDuration > RETRY_DURATION) {
                            delay((retryDuration.toLong()) * MS_TO_S_DURATION)
                        }
                    }
                } while (data.enquiry.status != STATUS_DONE)

                val result = if (data.enquiry.attributes != null) {
                    Success(data)
                } else {
                    Fail(MessageErrorException(NULL_RESPONSE))
                }
                _enquiryData.postValue(result)
            }.onFailure {
                _enquiryData.postValue(Fail(it))
            }
        }
    }

    fun getMenuDetail(
        rawQuery: String,
        mapParam: Map<String, Any>
    ) {
        launch {
            runCatching {
                val data = withContext(dispatcher.io) {
                    val isEnableGqlCache = remoteConfig.getBoolean(RemoteConfigKey.ANDROID_ENABLE_DIGITAL_GQL_CACHE, false)
                    val graphqlRequest =
                        GraphqlRequest(rawQuery, TelcoCatalogMenuDetailData::class.java, mapParam)
                    val graphqlCacheStrategy =
                        GraphqlCacheStrategy.Builder(if (isEnableGqlCache) CacheType.CACHE_FIRST else CacheType.CLOUD_THEN_CACHE)
                            .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * FIVE_MINS_CACHE_DURATION).build()
                    graphqlRepository.response(listOf(graphqlRequest), graphqlCacheStrategy)
                }.getSuccessData<TelcoCatalogMenuDetailData>()

                _menuDetailData.postValue(Success(data.catalogMenuDetailData))
            }.onFailure {
                _menuDetailData.postValue(Fail(it))
            }
        }
    }

    fun getCatalogPluginData(rawQuery: String, mapParam: Map<String, Any>) {
        launch {
            runCatching {
                val data = withContext(dispatcher.io) {
                    val graphqlRequest =
                        GraphqlRequest(rawQuery, RechargeCatalogPlugin.Response::class.java, mapParam)
                    graphqlRepository.response(listOf(graphqlRequest))
                }.getSuccessData<RechargeCatalogPlugin.Response>().response

                if (data != null) {
                    _catalogPluginData.postValue(Success(data))
                } else {
                    _catalogPluginData.postValue(Fail(MessageErrorException(NULL_RESPONSE)))
                }
            }.onFailure {
                _catalogPluginData.postValue(Fail(it))
            }
        }
    }

    fun getFavoriteNumbers(
        categoryIds: List<Int>
    ) {
        launch {
            runCatching {
                val favoriteNumber = rechargeFavoriteNumberUseCase.apply {
                    setRequestParams(categoryIds, emptyList(), CHANNEL_FAVORITE_NUMBER_LIST)
                }.executeOnBackground()
                _favNumberData.postValue(
                    Success(
                        FavoriteNumberDataMapper
                            .mapPersoFavNumberItemToSearchDataView(favoriteNumber.persoFavoriteNumber.items)
                    )
                )
            }.onFailure {
                _favNumberData.postValue(Fail(Throwable(it.message)))
            }
        }
    }

    fun getSeamlessFavoriteNumbers(
        rawQuery: String,
        mapParam: Map<String, Any>,
        shouldRefreshInputNumber: Boolean = true,
        prevActionType: FavoriteNumberActionType? = null
    ) {
        launch {
            runCatching {
                val data = withContext(dispatcher.io) {
                    val graphqlRequest =
                        GraphqlRequest(rawQuery, TopupBillsSeamlessFavNumberData::class.java, mapParam)
                    graphqlRepository.response(listOf(graphqlRequest))
                }.getSuccessData<TopupBillsSeamlessFavNumberData>()

                _seamlessFavNumberData.postValue(Success(data.seamlessFavoriteNumber to shouldRefreshInputNumber))
            }.onFailure {
                val errMsg = when (prevActionType) {
                    FavoriteNumberActionType.UPDATE -> ERROR_FETCH_AFTER_UPDATE
                    FavoriteNumberActionType.DELETE -> ERROR_FETCH_AFTER_DELETE
                    FavoriteNumberActionType.UNDO_DELETE -> ERROR_FETCH_AFTER_UNDO_DELETE
                    else -> it.message
                }
                _seamlessFavNumberData.postValue(Fail(Throwable(errMsg)))
            }
        }
    }

    fun checkVoucher(promoCode: String, promoDigitalModel: PromoDigitalModel) {
        stopCheckVoucher()
        checkVoucherJob = CoroutineScope(coroutineContext).launch {
            delay(CHECK_VOUCHER_DEBOUNCE_DELAY)
            digitalCheckVoucherUseCase.execute(
                digitalCheckVoucherUseCase.createRequestParams(promoCode, promoDigitalModel),
                getCheckVoucherSubscriber()
            )
        }
    }

    fun stopCheckVoucher() {
        if (checkVoucherJob?.isActive == true) checkVoucherJob?.cancel()
    }

    private fun getCheckVoucherSubscriber(): Subscriber<GraphqlResponse> {
        return object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse) {
                val checkVoucherData =
                    objects.getData<CheckVoucherDigital.Response>(CheckVoucherDigital.Response::class.java).response
                _checkVoucherData.value = Success(mapVoucherData(checkVoucherData.voucherData))
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                _checkVoucherData.value = Fail(e ?: MessageErrorException(e?.message))
            }
        }
    }

    private fun mapVoucherData(data: CheckVoucherDigitalData): PromoData {
        return PromoData(
            data.isCoupon,
            data.code,
            data.message.text,
            data.titleDescription,
            state = data.message.state.mapToStatePromoCheckout()
        )
    }

    fun processExpressCheckout(rawQuery: String, mapParam: Map<String, Any>) {
        launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                val graphqlRequest =
                    GraphqlRequest(rawQuery, RechargeExpressCheckout.Response::class.java, mapParam)
                graphqlRepository.response(listOf(graphqlRequest))
            }.getSuccessData<RechargeExpressCheckout.Response>().response

            val result = when {
                data?.errors?.isNotEmpty() == true -> {
                    // TODO: Finalize error
                    Fail(MessageErrorException(data.errors[0].title))
                }
                data != null -> {
                    Success(data.data)
                }
                else -> {
                    Fail(MessageErrorException())
                }
            }
            _expressCheckoutData.postValue(result)
        }) {
            _expressCheckoutData.postValue(Fail(it))
        }
    }

    fun createEnquiryParams(
        operatorId: String,
        productId: String,
        inputData: Map<String, String>
    ): List<TopupBillsEnquiryQuery> {
        val enquiryParams = mutableListOf<TopupBillsEnquiryQuery>()
        enquiryParams.add(
            TopupBillsEnquiryQuery(
                ENQUIRY_PARAM_SOURCE_TYPE,
                ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE
            )
        )
        enquiryParams.add(
            TopupBillsEnquiryQuery(
                ENQUIRY_PARAM_DEVICE_ID,
                ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE
            )
        )
        enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_PRODUCT_ID, productId))
        inputData.forEach { (key, value) ->
            enquiryParams.add(TopupBillsEnquiryQuery(key, value))
        }
        return enquiryParams
    }

    fun createMenuDetailParams(menuId: Int, platformId: Int = 5): Map<String, Any> {
        return mapOf(PARAM_MENU_ID to menuId, PARAM_PLATFORM_ID to platformId)
    }

    fun createCatalogPluginParams(operatorId: Int, categoryId: Int): Map<String, Any> {
        val filters = mutableListOf<Map<String, Any>>()
        filters.add(createCatalogPluginFieldParam(PLUGIN_PARAM_OPERATOR, operatorId))
        filters.add(createCatalogPluginFieldParam(PLUGIN_PARAM_CATEGORY, categoryId))
        return mapOf(PARAM_FILTERS to filters)
    }

    private fun createCatalogPluginFieldParam(key: String, value: Int): Map<String, Any> {
        return mapOf(PLUGIN_PARAM_KEY to key, PLUGIN_PARAM_ID to value)
    }

    fun createSeamlessFavoriteNumberParams(categoryIds: List<String>): Map<String, Any> {
        val joinedCategoryIds = categoryIds.joinToString(separator = ",")
        val paramSource = "${FAVORITE_NUMBER_PARAM_SOURCE_PERSO}$joinedCategoryIds"

        return mapOf(
            FAVORITE_NUMBER_PARAM_FIELDS to mapOf(
                FAVORITE_NUMBER_PARAM_SOURCE to paramSource,
                FAVORITE_NUMBER_PARAM_CATEGORY_IDS to categoryIds,
                FAVORITE_NUMBER_PARAM_MIN_LAST_TRANSACTION to "",
                FAVORITE_NUMBER_PARAM_MIN_TOTAL_TRANSACTION to "",
                FAVORITE_NUMBER_PARAM_SERVICE_PLAN_TYPE to "",
                FAVORITE_NUMBER_PARAM_SUBSCRIPTION to false,
                FAVORITE_NUMBER_PARAM_LIMIT to FAVORITE_NUMBER_LIMIT
            )
        )
    }

    fun createExpressCheckoutParams(
        productId: Int,
        inputs: Map<String, String>,
        transactionAmount: Int = 0,
        voucherCode: String = "",
        checkOtp: Boolean = false,
        isInstantCheckout: Boolean = false,
        addToMyBills: Boolean = false
    ): Map<String, Any> {
        val fields = mutableListOf<Map<String, String>>()
        for ((key, value) in inputs) {
            fields.add(createExpressCheckoutFieldParam(key, value))
        }

        val params = mutableMapOf(
            PARAM_FIELDS to fields,
            EXPRESS_PARAM_INSTANT_CHECKOUT to isInstantCheckout,
            EXPRESS_PARAM_VOUCHER_CODE to voucherCode,
            EXPRESS_PARAM_PRODUCT_ID to productId,
            EXPRESS_PARAM_DEVICE_ID to EXPRESS_PARAM_DEVICE_ID_DEFAULT_VALUE,
            EXPRESS_PARAM_ADD_TO_BILLS to addToMyBills,
            EXPRESS_PARAM_CHECK_OTP to checkOtp
        )
        if (transactionAmount > 0) params[EXPRESS_PARAM_TRANSACTION_AMOUNT] = transactionAmount
        return mapOf(PARAM_CART to params)
    }

    private fun createExpressCheckoutFieldParam(key: String, value: String): Map<String, String> {
        return mapOf(EXPRESS_PARAM_NAME to key, EXPRESS_PARAM_VALUE to value)
    }

    companion object {
        const val CHANNEL_FAVORITE_NUMBER_LIST = "favorite_number_list"

        const val PARAM_FIELDS = "fields"
        const val PARAM_FILTERS = "filters"
        const val PARAM_CART = "cart"
        const val PARAM_MENU_ID = "menuID"
        const val PARAM_PLATFORM_ID = "platformID"
        const val PARAM_CATEGORY_ID = "categoryID"

        const val PLUGIN_PARAM_KEY = "Key"
        const val PLUGIN_PARAM_ID = "ID"
        const val PLUGIN_PARAM_OPERATOR = "operator"
        const val PLUGIN_PARAM_CATEGORY = "category"

        const val ENQUIRY_PARAM_OPERATOR_ID = "operator_id"
        const val ENQUIRY_PARAM_PRODUCT_ID = "product_id"
        const val ENQUIRY_PARAM_DEVICE_ID = "device_id"
        const val ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE = "5"
        const val ENQUIRY_PARAM_SOURCE_TYPE = "source_type"
        const val ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE = "c20ad4d76fe977"

        const val EXPRESS_PARAM_NAME = "name"
        const val EXPRESS_PARAM_VALUE = "value"
        const val EXPRESS_PARAM_INSTANT_CHECKOUT = "instant_checkout"
        const val EXPRESS_PARAM_TRANSACTION_AMOUNT = "transaction_amount"
        const val EXPRESS_PARAM_VOUCHER_CODE = "voucher_code"
        const val EXPRESS_PARAM_OPERATOR_ID = "operator_id"
        const val EXPRESS_PARAM_PRODUCT_ID = "product_id"
        const val EXPRESS_PARAM_CLIENT_NUMBER = "client_number"
        const val EXPRESS_PARAM_DEVICE_ID = "device_id"
        const val EXPRESS_PARAM_DEVICE_ID_DEFAULT_VALUE = "5"
        const val EXPRESS_PARAM_ADD_TO_BILLS = "add_to_my_bills"
        const val EXPRESS_PARAM_CHECK_OTP = "check_otp"

        const val FAVORITE_NUMBER_PARAM_FIELDS = "fields"
        const val FAVORITE_NUMBER_PARAM_SOURCE = "source"
        const val FAVORITE_NUMBER_PARAM_CATEGORY_IDS = "category_ids"
        const val FAVORITE_NUMBER_PARAM_MIN_LAST_TRANSACTION = "min_last_transaction"
        const val FAVORITE_NUMBER_PARAM_MIN_TOTAL_TRANSACTION = "min_total_transaction"
        const val FAVORITE_NUMBER_PARAM_SERVICE_PLAN_TYPE = "service_plan_type"
        const val FAVORITE_NUMBER_PARAM_SUBSCRIPTION = "subscription"

        const val FAVORITE_NUMBER_PARAM_LIMIT = "limit"
        const val FAVORITE_NUMBER_PARAM_SOURCE_PERSO = "digital-personalization"

        const val STATUS_DONE = "DONE"
        const val STATUS_PENDING = "PENDING"

        const val ERROR_FETCH_AFTER_UPDATE = "ERROR_UPDATE"
        const val ERROR_FETCH_AFTER_DELETE = "ERROR_DELETE"
        const val ERROR_FETCH_AFTER_UNDO_DELETE = "ERROR_UNDO_DELETE"

        const val NULL_RESPONSE = "null response"

        const val CHECK_VOUCHER_DEBOUNCE_DELAY = 1000L
        const val FAVORITE_NUMBER_LIMIT = 10
        const val RETRY_DURATION = 0
        const val MS_TO_S_DURATION = 1000
        const val FIVE_MINS_CACHE_DURATION = 5
    }
}
