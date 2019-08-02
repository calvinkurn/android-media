package com.tokopedia.shop.product.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.view.model.ShopProductViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

class ShopProductLimitedViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                                      private val getShopFeaturedProductUseCase: GetShopFeaturedProductUseCase,
                                                      private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
                                                      private val getShopProductUseCase: GqlGetShopProductUseCase,
                                                      private val addWishListUseCase: AddWishListUseCase,
                                                      private val removeWishlistUseCase: RemoveWishListUseCase,
                                                      private val gqlRepository: GraphqlRepository,
                                                      dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    val isEtalaseEmpty: Boolean
        get() = etalaseResponse.value?.let { (it as? Success)?.data?.isEmpty() } ?: true

    val userDeviceId: String
        get() = userSession.deviceId

    val userId: String
        get() = userSession.userId

    val isLogin: Boolean
        get() = userSession.isLoggedIn

    private val filterInput = ShopProductFilterInput()

    fun isMyShop(shopId: String) = userSession.shopId == shopId

    val featuredProductResponse = MutableLiveData<Result<List<ShopProductViewModel>>>()
    val etalaseResponse = MutableLiveData<Result<List<ShopEtalaseViewModel>>>()
    val productResponse = MutableLiveData<Result<Pair<Boolean, List<ShopProductViewModel>>>>()
    val productHighlightResp = MutableLiveData<List<List<ShopProductViewModel>>>()
    val etalaseHighLight = mutableListOf<ShopEtalaseViewModel>()

    fun getFeaturedProduct(shopId: String, isForceRefresh: Boolean = false){
        getShopFeaturedProductUseCase.params = GetShopFeaturedProductUseCase.createParams(shopId.toInt())
        getShopFeaturedProductUseCase.isFromCacheFirst = !isForceRefresh

        launchCatchError( block = {
            featuredProductResponse.value = Success( withContext(Dispatchers.IO)
                {getShopFeaturedProductUseCase.executeOnBackground()}.map { it.toProductViewModel(isMyShop(shopId))})
        }){
            featuredProductResponse.value = Fail(it)
        }

    }

    fun getShopEtalase(shopId: String){
        val params = GetShopEtalaseByShopUseCase.createRequestParams(shopId, true, false, isMyShop(shopId))
        getShopEtalaseByShopUseCase.execute(params, object : Subscriber<ArrayList<ShopEtalaseModel>>() {
            override fun onNext(list: ArrayList<ShopEtalaseModel>?) {
                list?.let {
                    etalaseHighLight.clear()
                    etalaseHighLight.addAll(it.filter { etalase -> etalase.highlighted }
                            .map { item -> item.toViewModel() })
                    etalaseResponse.value = Success(it.map { item -> item.toViewModel() })
                }
            }

            override fun onCompleted() {}

            override fun onError(t: Throwable?) {
                t?.let { etalaseResponse.value = Fail(it) }
            }

        })
    }

    fun getShopProduct(shopId: String, page: Int = 1, perPage: Int = 10, sortId: Int = 0,
                       etalase: String = "", search: String = "", isForceRefresh: Boolean = false){
        with(filterInput){
            this.page = page
            this.perPage = perPage
            this.etalaseMenu = etalase
            this.searchKeyword = search
            this.sort = sortId
        }

        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(shopId, filterInput)
        getShopProductUseCase.isFromCacheFirst = !isForceRefresh

        launchCatchError( block = {
            val getProductResp = withContext(Dispatchers.IO) {getShopProductUseCase.executeOnBackground()}
            productResponse.value = if (getProductResp.errors.isNotEmpty())
                Fail(MessageErrorException(getProductResp.errors))
            else
                Success(isHasNextPage(page, perPage, getProductResp.totalData)
                        to getProductResp.data.map { it.toProductViewModel(isMyShop(shopId)) })
        }){
            productResponse.value = Fail(it)
        }
    }

    fun getShopProductsEtalaseHighlight(shopId: String, isForceRefresh: Boolean = false){
        launchCatchError(block = {
            productHighlightResp.value =
                    etalaseHighLight.map { ShopProductFilterInput(1, ShopPageConstant.ETALASE_HIGHLIGHT_COUNT,
                            "", it.etalaseId, getSort(it.etalaseId)) }
                        .map {
                            val params = GqlGetShopProductUseCase.createParams(shopId, it)
                            val cacheStrategy = GraphqlCacheStrategy
                                    .Builder(if (isForceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
                            val gqlRequest = GraphqlRequest(getShopProductUseCase.gqlQuery, ShopProduct.Response::class.java, params)
                            async(Dispatchers.IO) {
                                try {
                                    val resp = gqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)
                                    if (resp.getError(ShopProduct.Response::class.java)?.isNotEmpty() != true){
                                        val gqlProduct = resp.getData<ShopProduct.Response>(ShopProduct.Response::class.java).getShopProduct
                                        if (gqlProduct.errors.isNotEmpty()){
                                            null
                                        } else {
                                            gqlProduct.data.map { product -> product.toProductViewModel(isMyShop(shopId)) }
                                        }
                                    } else null

                                } catch (t: Throwable){
                                    null
                                }
                            }
                        }.map { it.await() }.filterNotNull();
        }){}
    }

    private fun getSort(etalaseId: String?): Int {
        return when(etalaseId){
            SOLD_ETALASE -> ORDER_BY_MOST_SOLD
            DISCOUNT_ETALASE -> ORDER_BY_LAST_UPDATE
            else -> 0
        }
    }

    fun removeWishList(productId: String, listener: WishListActionListener) {
        removeWishlistUseCase.createObservable(productId, userId, listener)
    }

    fun addWishList(productId: String, listener: WishListActionListener) {
        addWishListUseCase.createObservable(productId, userId, listener)
    }

    fun clearEtalaseCache(){
        getShopEtalaseByShopUseCase.clearCache()
    }


    override fun onCleared() {
        super.onCleared()
        addWishListUseCase.unsubscribe()
        removeWishlistUseCase.unsubscribe()
    }

    private fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    private fun ShopFeaturedProduct.toProductViewModel(isMyOwnProduct: Boolean): ShopProductViewModel =
            ShopProductViewModel().also {
                it.id = productId.toString()
                it.name = name
                it.displayedPrice = price
                it.originalPrice = originalPrice
                it.discountPercentage = percentageAmount.toString()
                it.imageUrl = imageUri
                it.totalReview = totalReview
                if (isRated){
                    it.rating = rating.toDoubleOrZero()
                }
                if (cashback) {
                    it.cashback = cashbackDetail.cashbackPercent.toDouble()
                }
                it.isWholesale = wholesale
                it.isPo = preorder
                it.isFreeReturn = returnable
                it.isWishList = isWishlist
                it.productUrl = uri
                it.isShowWishList = !isMyOwnProduct
            }

    private fun ShopProduct.toProductViewModel(isMyOwnProduct: Boolean): ShopProductViewModel = ShopProductViewModel().also {
        it.id = productId
        it.name = name
        it.displayedPrice = price.textIdr
        it.originalPrice = campaign.originalPriceFmt
        it.discountPercentage = campaign.discountedPercentage
        it.imageUrl = primaryImage.original
        it.imageUrl300 = primaryImage.resize300
        it.totalReview = stats.reviewCount.toString()
        it.rating = stats.rating.toDouble()
        if (cashback.cashbackPercent > 0) {
            it.cashback = cashback.cashbackPercent.toDouble()
        }
        it.isWholesale = flags.isWholesale
        it.isPo = flags.isPreorder
        it.isFreeReturn = flags.isFreereturn
        it.isWishList = flags.isWishlist
        it.productUrl = productUrl
        it.isSoldOut = flags.isSold
        it.isShowWishList = !isMyOwnProduct
    }

    private fun ShopEtalaseModel.toViewModel(): ShopEtalaseViewModel {
        val _id = if (type == ShopEtalaseTypeDef.ETALASE_DEFAULT) alias else id
        return ShopEtalaseViewModel(_id, name, useAce, type, highlighted).also {
            it.etalaseCount = count.toLong()
            it.etalaseBadge = badge
        }
    }

    companion object{
        private const val SOLD_ETALASE = "sold"
        private const val DISCOUNT_ETALASE = "discount"
        private const val ORDER_BY_LAST_UPDATE = 3
        private const val ORDER_BY_MOST_SOLD = 8
    }
}