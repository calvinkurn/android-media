package com.tokopedia.product.detail.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.res.Resources
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.ProductInfo
import com.tokopedia.product.detail.data.model.ProductParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject
import javax.inject.Named

class ProductInfoViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                               private val userSessionInterface: UserSessionInterface,
                                               @Named("Main")
                                               val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher){

    val productInfoResp = MutableLiveData<Result<ProductInfo>>()

    fun getProductInfo(rawQuery: String, productParams: ProductParams, resources: Resources) {
        val params = mapOf(PARAM_PRODUCT_ID to productParams.productId,
                PARAM_SHOP_DOMAIN to productParams.shopDomain,
                PARAM_PRODUCT_KEY to productParams.productName)
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO){
                val graphqlRequest = GraphqlRequest(rawQuery, ProductInfo.Response::class.java, params)
                val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
                graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)
            }.getSuccessData<ProductInfo.Response>()
            productInfoResp.value = Success(data.data)
        }){
            //productInfoResp.value = Fail(it)
            // for testing
            val gson = Gson()
            val response = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_product_info_p1),
                    ProductInfo.Response::class.java)
            productInfoResp.value = Success(response.data)

        }
    }

    fun isShopOwner(shopId: Int): Boolean = userSessionInterface.shopId.toIntOrNull() == shopId

    fun isUserSessionActive(): Boolean = userSessionInterface.userId != null

    companion object {
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_SHOP_DOMAIN = "shopDomain"
        private const val PARAM_PRODUCT_KEY = "productKey"
    }

}