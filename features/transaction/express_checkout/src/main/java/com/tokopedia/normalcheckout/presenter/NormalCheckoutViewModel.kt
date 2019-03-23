package com.tokopedia.normalcheckout.presenter

import android.arch.lifecycle.MutableLiveData
import android.content.res.Resources
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
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
import com.tokopedia.product.detail.common.data.model.ProductInfo
import com.tokopedia.product.detail.common.data.model.ProductParams
import com.tokopedia.product.detail.common.data.model.variant.ProductDetailVariantResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Named

class NormalCheckoutViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                  private val userSessionInterface: UserSessionInterface,
                                                  private val rawQueries: Map<String, String>,
                                                  @Named("Main")
                                                  val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val productInfoResp = MutableLiveData<Result<ProductInfoAndVariant>>()

    fun getProductInfo(productParams: ProductParams, resources: Resources) {

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
                        productInfo.productVariant = productVariant.data
                    }
                }
                productInfoResp.value = Success(productInfo)
            }
        }) {
            productInfoResp.value = Fail(it)
        }
    }

    fun isShopOwner(shopId: Int): Boolean = userSessionInterface.shopId.toIntOrNull() == shopId

    fun isUserSessionActive(): Boolean = userSessionInterface.userId.isNotEmpty()


}

inline fun <reified T> GraphqlResponse.getSuccessData(): T {
    val error = getError(T::class.java)
    if (error == null || error.isEmpty()){
        return getData(T::class.java)
    } else {
        throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
    }
}