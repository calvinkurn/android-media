package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.core.common.category.domain.interactor.GetCategoryListUseCase
import com.tokopedia.core.common.category.domain.interactor.GetProductCategoryNameUseCase
import com.tokopedia.core.common.category.domain.model.CategoriesLevelThree
import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaData
import com.tokopedia.product.manage.feature.filter.domain.GetProductListMetaUseCase
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.data.shopetalase.gql.ShopEtalaseByShopQuery
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

class ProductManageFilterViewModel @Inject constructor(
        private val getProductListMetaUseCase: GetProductListMetaUseCase,
        private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
        private val getCategoryListUseCase: GetCategoryListUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    companion object {
        const val DEFAULT_HIDE_NO_COUNT = true
        const val DEFAULT_HIDE_SHOWCASE_GROUP = false
        const val DEFAULT_CATEGORIES_FILTER = "seller"
    }

    private val _productListMetaData = MutableLiveData<Result<ProductListMetaData>>()
    val productListMetaData: LiveData<Result<ProductListMetaData>>
        get() = _productListMetaData

    private val _shopEtalase = MutableLiveData<Result<ArrayList<ShopEtalaseModel>>>()
    val shopEtalase: LiveData<Result<ArrayList<ShopEtalaseModel>>>
        get() = _shopEtalase

    private val _categories = MutableLiveData<Result<CategoriesResponse>>()
    val categories: LiveData<Result<CategoriesResponse>>
        get() = _categories

    fun getProductListData(shopID: String) {
        GetProductListMetaUseCase.createRequestParams(shopID)
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val productListMetaDataResult = getProductListMetaUseCase.executeOnBackground()
                productListMetaDataResult.let {
                    _productListMetaData.postValue(Success(it.productListMetaData))
                }
            }
        }) {
            _productListMetaData.value = Fail(it)
        }
    }

    fun getShopEtalase(shopId: String) {
        val params = GetShopEtalaseByShopUseCase.createRequestParams(shopId, DEFAULT_HIDE_NO_COUNT, DEFAULT_HIDE_SHOWCASE_GROUP, isMyShop(shopId))
        getShopEtalaseByShopUseCase.execute(params, object : Subscriber<ArrayList<ShopEtalaseModel>>() {
            override fun onNext(list: ArrayList<ShopEtalaseModel>?) {
                list?.let {
                    _shopEtalase.value = Success(it)
                }
            }

            override fun onCompleted() {}

            override fun onError(t: Throwable?) {
                t?.let { _shopEtalase.value = Fail(it) }
            }
        })
    }

    fun getCategories() {
        GetCategoryListUseCase.createRequestParams(DEFAULT_CATEGORIES_FILTER)
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val categories = getCategoryListUseCase.executeOnBackground()
                categories.let {
                    _categories.postValue(Success(it))
                }
            }
        }) {
            _categories.value = Fail(it)
        }
    }

    fun isMyShop(shopId: String) = userSession.shopId == shopId


}