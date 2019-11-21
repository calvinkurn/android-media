package com.tokopedia.product.detail.view.viewmodel

import android.content.Intent
import android.text.TextUtils
import androidx.collection.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.datamodel.DynamicPDPDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.data.util.origin
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named

class DynamicProductDetailViewModel @Inject constructor(@Named("Main")
                                                        private val dispatcher: CoroutineDispatcher,
                                                        private val stickyLoginUseCase: StickyLoginUseCase,
                                                        private val getProductInfoP1UseCase: GetProductInfoP1UseCase,
                                                        private val getPdpLayoutUseCase: GetPdpLayoutUseCase,
                                                        private val getProductInfoP2ShopUseCase: GetProductInfoP2ShopUseCase,
                                                        private val getProductInfoP2LoginUseCase: GetProductInfoP2LoginUseCase,
                                                        private val getProductInfoP2GeneralUseCase: GetProductInfoP2GeneralUseCase,
                                                        private val getProductInfoP3UseCase: GetProductInfoP3UseCase,
                                                        private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
                                                        private val removeWishlistUseCase: RemoveWishListUseCase,
                                                        private val addWishListUseCase: AddWishListUseCase,
                                                        private val getRecommendationUseCase: GetRecommendationUseCase,
                                                        private val userSessionInterface: UserSessionInterface) : BaseViewModel(dispatcher) {

    val productInfoP1 = MutableLiveData<Result<ProductInfoP1>>()
    val productLayout = MutableLiveData<Result<List<DynamicPDPDataModel>>>()
    val p2ShopDataResp = MutableLiveData<ProductInfoP2ShopData>()
    val p2Login = MutableLiveData<ProductInfoP2Login>()
    val p2General = MutableLiveData<ProductInfoP2General>()
    val productInfoP3resp = MutableLiveData<ProductInfoP3>()
    var multiOrigin: WarehouseInfo = WarehouseInfo()
    val loadTopAdsProduct = MutableLiveData<List<RecommendationWidget>>()


    private var productInfoTemp = ProductInfo()
    var getProductInfoP1: ProductInfo? = null
    var listOfRecomData: List<ProductRecommendationDataModel>? = null

    fun isUserSessionActive(): Boolean = userSessionInterface.isLoggedIn
    fun isShopOwner(shopId: Int): Boolean = userSessionInterface.shopId.toIntOrNull() == shopId
    val userId: String
        get() = userSessionInterface.userId

    val isUserHasShop: Boolean
        get() = userSessionInterface.hasShop()

    val deviceId: String
        get() = userSessionInterface.deviceId

    fun getStickyLoginContent(onSuccess: (StickyLoginTickerPojo.TickerDetail) -> Unit, onError: ((Throwable) -> Unit)?) {
        stickyLoginUseCase.setParams(StickyLoginConstant.Page.PDP)
        stickyLoginUseCase.execute(
                onSuccess = {
                    if (it.response.tickers.isNotEmpty()) {
                        for (tickerDetail in it.response.tickers) {
                            if (tickerDetail.layout == StickyLoginConstant.LAYOUT_FLOATING) {
                                onSuccess.invoke(tickerDetail)
                                return@execute
                            }
                        }
                        onError?.invoke(Throwable(""))
                    } else {
                        onError?.invoke(Throwable(""))
                    }
                },
                onError = {
                    onError?.invoke(it)
                }
        )
    }

    fun getProductP1(productParams: ProductParams, forceRefresh: Boolean = false) {
        launchCatchError(block = {
            var needRequestCod = false

            val pdpLayout = getPdpLayout(productParams.productId ?: "")
            val initialLayoutData = DynamicProductDetailMapper.mapIntoVisitable(pdpLayout.data.components)

            val productInfo = getPdpData(productParams.productId?.toInt() ?: 0)
            productLayout.value = Success(initialLayoutData)
            getProductInfoP1 = productInfo.productInfo
            productInfoP1.value = Success(productInfo)
            productInfoTemp = productInfo.productInfo
            listOfRecomData = initialLayoutData.filterIsInstance(ProductRecommendationDataModel::class.java)

            val p2ShopDeferred = getProductInfoP2ShopAsync(productInfo.productInfo.basic.shopID,
                    productInfo.productInfo.basic.id.toString(),
                    "", false)

            val p2LoginDeferred: Deferred<ProductInfoP2Login>? = if (isUserSessionActive()) {
                getProductInfoP2LoginAsync(productInfo.productInfo.basic.id,
                        productInfo.productInfo.basic.shopID)
            } else null

            val userId = if (!TextUtils.isEmpty(userSessionInterface.userId)) {
                userSessionInterface.userId.toInt()
            } else {
                0
            }

            val categoryId = if (!TextUtils.isEmpty(productInfo.productInfo.category.id)) {
                productInfo.productInfo.category.id.toInt()
            } else {
                0
            }

            val p2GeneralAsync: Deferred<ProductInfoP2General> = getProductInfoP2GeneralAsync(productInfo.productInfo.basic.shopID,
                    productInfo.productInfo.basic.id, productInfo.productInfo.basic.price.toInt(),
                    productInfo.productInfo.basic.condition, productInfo.productInfo.basic.name,
                    categoryId, productInfo.productInfo.basic.catalogID.toString(), userId)

            p2ShopDataResp.value = p2ShopDeferred.await()
            p2General.value = p2GeneralAsync.await()
            p2LoginDeferred?.let {
                p2Login.value = it.await()
            }

            p2ShopDataResp.value?.let {
                multiOrigin = it.nearestWarehouse.warehouseInfo
                val domain = productParams.shopDomain ?: it.shopInfo?.shopCore?.domain
                ?: return@launchCatchError

                if (isUserSessionActive())
                    productInfoP3resp.value = getProductInfoP3(productInfo.productInfo, domain, true,
                            productInfo.productInfo.shouldShowCod, if (multiOrigin.isFulfillment) multiOrigin.origin else null)
            }

        }) {
            productLayout.value = Fail(it)
        }
    }

    fun toggleFavorite(shopID: String, onSuccess: (Boolean) -> Unit, onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            toggleFavoriteUseCase.createRequestParam(shopID)
            val result = toggleFavoriteUseCase.executeOnBackground()

            onSuccess(result.followShop.isSuccess)
        }) { onError(it) }
    }

    fun removeWishList(productId: String,
                       onSuccessRemoveWishlist: ((productId: String?) -> Unit)?,
                       onErrorRemoveWishList: ((errorMessage: String?) -> Unit)?) {
        removeWishlistUseCase.createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // no op
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                onErrorRemoveWishList?.invoke(errorMessage)
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                onSuccessRemoveWishlist?.invoke(productId)
            }
        })
    }

    fun addWishList(productId: String,
                    onErrorAddWishList: ((errorMessage: String?) -> Unit)?,
                    onSuccessAddWishlist: ((productId: String?) -> Unit)?) {
        addWishListUseCase.createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                onErrorAddWishList?.invoke(errorMessage)
            }

            override fun onSuccessAddWishlist(productId: String?) {
                onSuccessAddWishlist?.invoke(productId)
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                // no op
            }
        })
    }

    fun putChatProductInfoTo(
            intent: Intent?,
            productId: String?,
            productInfo: ProductInfo?,
            userInputVariant: String?
    ) {
        if (intent == null) return
        val variants = mapSelectedProductVariants(userInputVariant)
        val productImageUrl = productInfo?.getProductImageUrl()
        val productName = productInfo?.getProductName()
        val productPrice = productInfo?.getProductPrice()?.getCurrencyFormatted()
        val productUrl = productInfo?.getProductUrl()
        val productFsIsActive = productInfo?.getFsProductIsActive()
        val productFsImageUrl = productInfo?.getFsProductImageUrl()
        val productColorVariant = variants?.get("colour")?.get("value")
        val productColorHexVariant = variants?.get("colour")?.get("hex")
        val productSizeVariant = variants?.get("size")?.get("value")
        with(intent) {
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_ID, productId)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_IMAGE_URL, productImageUrl)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_NAME, productName)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_PRICE, productPrice)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_URL, productUrl)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_COLOR_VARIANT, productColorVariant)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_HEX_COLOR_VARIANT, productColorHexVariant)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_SIZE_VARIANT, productSizeVariant)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_FS_IS_ACTIVE, productFsIsActive)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_FS_IMAGE_URL, productFsImageUrl)
        }
    }

    fun getImageUriPaths(): ArrayList<String> {
        return ArrayList(productInfoTemp.run {
            media.map {
                if (it.type == "image") {
                    it.urlOriginal
                } else {
                    it.urlThumbnail
                }
            }
        })
    }

    fun loadRecommendation() {
        val product = (productInfoP1.value ?: return) as? Success ?: return
        launch {
            if (GlobalConfig.isCustomerApp() &&
                    loadTopAdsProduct.value == null) {
                try {
                    withContext(Dispatchers.IO) {
                        val a = getRecommendationUseCase.createObservable(getRecommendationUseCase.getRecomParams(
                                pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                                pageName = ProductDetailConstant.DEFAULT_PAGE_NAME,
                                productIds = arrayListOf(product.data.productInfo.basic.id.toString())
                        )).toBlocking()
                        loadTopAdsProduct.value = a.first()
                    }
                } catch (e: Throwable) {

                }
            }
        }
    }

    private fun mapSelectedProductVariants(userInputVariant: String?): ArrayMap<String, ArrayMap<String, String>>? {
        return getProductVariant()?.mapSelectedProductVariants(userInputVariant)
    }

    private fun getProductVariant(): ProductVariant? {
        return p2General.value?.variantResp
    }

    private fun getProductInfoP2ShopAsync(shopId: Int, productId: String,
                                          warehouseId: String,
                                          forceRefresh: Boolean = false): Deferred<ProductInfoP2ShopData> {
        return async {
            getProductInfoP2ShopUseCase.createRequestParams(shopId, productId, warehouseId, true)
            getProductInfoP2ShopUseCase.executeOnBackground()
        }

    }

    private fun getProductInfoP2LoginAsync(shopId: Int, productId: Int): Deferred<ProductInfoP2Login> {
        return async {
            getProductInfoP2LoginUseCase.createRequestParams(shopId, productId)
            getProductInfoP2LoginUseCase.executeOnBackground()
        }

    }

    private fun getProductInfoP2GeneralAsync(shopId: Int, productId: Int, productPrice: Int,
                                             condition: String, productTitle: String, categoryId: Int, catalogId: String,
                                             userId: Int,
                                             forceRefresh: Boolean = false): Deferred<ProductInfoP2General> {
        return async {
            getProductInfoP2GeneralUseCase.createRequestParams(shopId, productId, productPrice, condition, productTitle, categoryId, catalogId, userId, true)
            getProductInfoP2GeneralUseCase.executeOnBackground()
        }
    }

    private suspend fun getPdpData(productId: Int): ProductInfoP1 {
        getProductInfoP1UseCase.params = GetProductInfoP1UseCase.createParams(productId, "", "")
        val pdpData = getProductInfoP1UseCase.executeOnBackground().data
        return ProductInfoP1(pdpData ?: ProductInfo())
    }

    private suspend fun getProductInfoP3(productInfo: ProductInfo, shopDomain: String,
                                         forceRefresh: Boolean, needRequestCod: Boolean, origin: String?): ProductInfoP3 {
        getProductInfoP3UseCase.createRequestParams(productInfo, shopDomain, needRequestCod, origin
                ?: "", true)
        return getProductInfoP3UseCase.executeOnBackground()
    }

    private suspend fun getPdpLayout(productId: String): ProductDetailLayout {
        getPdpLayoutUseCase.requestParams = GetPdpLayoutUseCase.createParams(productId)
        getPdpLayoutUseCase.isFromCacheFirst = false
        return getPdpLayoutUseCase.executeOnBackground()
    }
}