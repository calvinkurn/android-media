package com.tokopedia.normalcheckout.presenter

import android.arch.lifecycle.MutableLiveData
import android.content.res.Resources
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.normalcheckout.di.RawQueryKeyConstant
import com.tokopedia.normalcheckout.model.ProductInfoAndVariant
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_PRODUCT_ID
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_PRODUCT_KEY
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_SHOP_DOMAIN
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.variant.ProductDetailVariantResponse
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.transactiondata.insurance.entity.request.InsuranceRecommendationRequest
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceRecommendationGqlResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class NormalCheckoutViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                  private val userSessionInterface: UserSessionInterface,
                                                  private val rawQueries: Map<String, String>,
                                                  private val addToCartUseCase: AddToCartUseCase,
                                                  private val addToCartOcsUseCase: AddToCartOcsUseCase,
                                                  @Named("Main")
                                                  val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val productInfoResp = MutableLiveData<Result<ProductInfoAndVariant>>()
    var insuranceRecommendationResponse = InsuranceRecommendationGqlResponse()
    var warehouses: Map<String, MultiOriginWarehouse> = mapOf()
    var selectedwarehouse: MultiOriginWarehouse? = null

    fun getProductInfo(productParams: ProductParams, resources: Resources, insuranceRecommendationRequest: InsuranceRecommendationRequest) {

        launchCatchError(block = {
            val paramsInfo = mapOf(PARAM_PRODUCT_ID to productParams.productId?.toInt(),
                    PARAM_SHOP_DOMAIN to productParams.shopDomain,
                    PARAM_PRODUCT_KEY to productParams.productName)
            val graphqlInfoRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_PRODUCT_INFO], ProductInfo.Response::class.java, paramsInfo)
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
            val productInfoData = withContext(Dispatchers.IO) {
                graphqlRepository.getReseponse(listOf(graphqlInfoRequest), cacheStrategy)
            }
            productInfoData.getSuccessData<ProductInfo.Response>().data?.let {
                val productInfo = ProductInfoAndVariant()
                productInfo.productInfo = it
                if (it.variant.isVariant) {
                    val productVariantData = withContext(Dispatchers.IO) {
                        val paramsVariant = mapOf(PARAM_PRODUCT_ID to productParams.productId)
                        val graphqlVariantRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_VARIANT], ProductDetailVariantResponse::class.java, paramsVariant)
                        graphqlRepository.getReseponse(listOf(graphqlVariantRequest), cacheStrategy)
                    }
                    productVariantData.getSuccessData<ProductDetailVariantResponse>().let { productVariant ->
                        val productIds = productVariant.data.children.map { child -> child.productId.toString() }
                        val nearestWarehouseParam = mapOf("productIds" to productIds)
                        val nearestWarehouseRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_MULTI_ORIGIN],
                                MultiOriginWarehouse.Response::class.java, nearestWarehouseParam)
                        val response = withContext(Dispatchers.IO) {
                            graphqlRepository.getReseponse(listOf(nearestWarehouseRequest), cacheStrategy)
                        }
                        if (response.getError(MultiOriginWarehouse.Response::class.java)?.isNotEmpty() != true) {
                            warehouses = response.getData<MultiOriginWarehouse.Response>(MultiOriginWarehouse.Response::class.java)
                                    .result.data.groupBy { warehouse -> warehouse.productId }
                                    .filterValues { warehousesInfos -> warehousesInfos.isNotEmpty() }
                                    .mapValues { warehousesInfos -> warehousesInfos.value.first() }
                            selectedwarehouse = warehouses[productVariant.data.defaultChild.toString()]
                        }
                        productInfo.productVariant = productVariant.data
                    }
                } else {
                    val nearestWarehouseParam = mapOf("productIds" to listOf(productParams.productId
                            ?: ""))
                    val nearestWarehouseRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_MULTI_ORIGIN],
                            MultiOriginWarehouse.Response::class.java, nearestWarehouseParam)
                    val response = withContext(Dispatchers.IO) {
                        graphqlRepository.getReseponse(listOf(nearestWarehouseRequest), cacheStrategy)
                    }
                    if (response.getError(MultiOriginWarehouse.Response::class.java)?.isNotEmpty() != true) {
                        selectedwarehouse = response.getData<MultiOriginWarehouse.Response>(MultiOriginWarehouse.Response::class.java)
                                .result.data.firstOrNull()
                    }
                }

                if (isUserSessionActive()) {
                    val insuranceParams = mapOf("digitalProducts" to insuranceRecommendationRequest)
                    val graphqlInsuranceRecommendationRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_INSURANCE_RECOMMENDATION],
                            InsuranceRecommendationGqlResponse::class.java, insuranceParams)

                    val insuranceResponse = withContext(Dispatchers.IO) {
                        graphqlRepository.getReseponse(listOf(graphqlInsuranceRecommendationRequest))
                    }

                    insuranceResponse.getSuccessData<InsuranceRecommendationGqlResponse>().let {
                        insuranceRecommendationResponse = it
                    }

                    productInfo.insuranceRecommendation = insuranceRecommendationResponse
                }

                productInfoResp.value = Success(productInfo)
            }
        }) {
            productInfoResp.value = Fail(it)
        }
    }

    fun isShopOwner(shopId: Int): Boolean = userSessionInterface.shopId.toIntOrNull() == shopId

    fun isUserSessionActive(): Boolean = userSessionInterface.userId.isNotEmpty()

    fun addToCartProduct(atcParams: Any,
                         onSuccessAtc: (addToCartResult: AddToCartDataModel?, onFinish: (message: String?, cartId: String?) -> Unit) -> Unit,
                         onErrorAtc: (e: Throwable?, () -> Unit) -> Unit,
                         onFinish: (message: String?, cartId: String?) -> Unit,
                         onRetryWhenError: () -> Unit) {
        if (atcParams is AddToCartRequestParams) {
            val requestParams = RequestParams.create()
            requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, atcParams)
            addToCartUseCase.createObservable(requestParams)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<AddToCartDataModel>() {
                        override fun onNext(addToCartResult: AddToCartDataModel?) {
                            onSuccessAtc(addToCartResult, onFinish)
                        }

                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable?) {
                            onErrorAtc(e, onRetryWhenError)
                        }
                    })
        } else if (atcParams is AddToCartOcsRequestParams) {
            val requestParams = RequestParams.create()
            requestParams.putObject(AddToCartOcsUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, atcParams)
            addToCartOcsUseCase.createObservable(requestParams)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Subscriber<AddToCartDataModel>() {
                        override fun onNext(addToCartResult: AddToCartDataModel?) {
                            onSuccessAtc(addToCartResult, onFinish)
                        }

                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable?) {
                            onErrorAtc(e, onRetryWhenError)
                        }
                    })
        }
    }

    fun unsubscribe() {
        addToCartUseCase.unsubscribe()
        addToCartOcsUseCase.unsubscribe()
    }
}

inline fun <reified T> GraphqlResponse.getSuccessData(): T {
    val error = getError(T::class.java)
    if (error == null || error.isEmpty()) {
        return getData(T::class.java)
    } else {
        throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
    }
}