package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCodStatus
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductInfoP2ShopUseCase @Inject constructor(private val rawQueries: Map<String, String>,
                                                      private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ProductInfoP2ShopData>() {

    var shopId: Int = 0
    var productId: String = ""
    var warehouseId: String = ""
    var forceRefresh = false

    companion object {
        private const val PARAM_SHOP_IDS = "shopIds"
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_SHOP_FIELDS = "fields"
        private val DEFAULT_SHOP_FIELDS = listOf("core", "favorite", "assets", "shipment",
                "last_active", "location", "terms", "allow_manage",
                "is_owner", "other-goldos", "status")
    }

    fun createRequestParams(shopId: Int, productId: String, warehouseId: String, forceRefresh: Boolean = false) {
        this.shopId = shopId
        this.productId = productId
        this.warehouseId = warehouseId
        this.forceRefresh = forceRefresh
    }

    override suspend fun executeOnBackground(): ProductInfoP2ShopData {
        val p2Shop = ProductInfoP2ShopData()

        gqlUseCase.clearRequest()

        val shopParams = mapOf(PARAM_SHOP_IDS to listOf(shopId),
                PARAM_SHOP_FIELDS to DEFAULT_SHOP_FIELDS)
        val shopRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP], ShopInfo.Response::class.java, shopParams)

        val nearestWarehouseParam = mapOf(
                "productIds" to listOf(productId),
                "warehouseID" to warehouseId
        )
        val nearestWarehouseRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_MULTI_ORIGIN],
                MultiOriginWarehouse.Response::class.java, nearestWarehouseParam)

        val shopCodParam = mapOf(PARAM_SHOP_ID to shopId.toString())
        val shopCodRequest = GraphqlRequest(rawQueries[RawQueryKeyConstant.QUERY_SHOP_COD_STATUS],
                ShopCodStatus.Response::class.java, shopCodParam)

        val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build()
        val requests = mutableListOf(shopRequest, shopCodRequest, nearestWarehouseRequest)

        gqlUseCase.addRequests(requests)
        gqlUseCase.setCacheStrategy(cacheStrategy)

        val gqlResponse = gqlUseCase.executeOnBackground()

        if (gqlResponse.getError(ShopInfo.Response::class.java)?.isNotEmpty() != true) {
            val result = (gqlResponse.getData(ShopInfo.Response::class.java) as ShopInfo.Response).result
            if (result.data.isNotEmpty())
                p2Shop.shopInfo = result.data.first()
        }

        if (gqlResponse.getError(ShopCodStatus.Response::class.java)?.isNotEmpty() != true) {
            p2Shop.shopCod = gqlResponse.getData<ShopCodStatus.Response>(ShopCodStatus.Response::class.java)
                    .result.shopCodStatus.isCod
        }

        if (gqlResponse.getError(MultiOriginWarehouse.Response::class.java)?.isNotEmpty() != true) {
            gqlResponse.getData<MultiOriginWarehouse.Response>(MultiOriginWarehouse.Response::class.java)
                    .result.data.firstOrNull()?.let { p2Shop.nearestWarehouse = it }
        }

        return p2Shop
    }

}