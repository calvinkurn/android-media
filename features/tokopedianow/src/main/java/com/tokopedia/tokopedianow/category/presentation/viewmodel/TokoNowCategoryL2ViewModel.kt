package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.addChooseAddress
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2Model
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowCategoryL2ViewModel @Inject constructor(
    getCategoryProductUseCase: GetCategoryProductUseCase,
    getProductAdsUseCase: GetProductAdsUseCase,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    getShopAndWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    addressData: TokoNowLocalAddress,
    userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
): BaseCategoryViewModel(
    getCategoryProductUseCase = getCategoryProductUseCase,
    getProductAdsUseCase = getProductAdsUseCase,
    getTargetedTickerUseCase = getTargetedTickerUseCase,
    getShopAndWarehouseUseCase = getShopAndWarehouseUseCase,
    getMiniCartUseCase = getMiniCartUseCase,
    addToCartUseCase = addToCartUseCase,
    updateCartUseCase = updateCartUseCase,
    deleteCartUseCase = deleteCartUseCase,
    affiliateService = affiliateService,
    addressData = addressData,
    userSession = userSession,
    dispatchers = dispatchers
) {

    private val _onError = MutableLiveData<Throwable>()

    val onError: LiveData<Throwable> = _onError

    override fun loadFirstPage(tickerList: List<TickerData>) {
        loadCategoryPage()
    }

    override suspend fun loadNextPage() {

    }

    override fun onSuccessGetCategoryProduct(
        response: AceSearchProductModel,
        categoryL2Model: CategoryL2Model
    ) {
        val searchProduct = response.searchProduct
        val header = searchProduct.header
        val data = searchProduct.data
        val productList = data.productList.filter { !it.isOos() }


    }

    override fun onErrorGetCategoryProduct(
        error: Throwable,
        categoryL2Model: CategoryL2Model
    ) {

    }

    private fun loadCategoryPage() {
        launchCatchError(
            block = {
                visitableList.clear()

                visitableList.addChooseAddress(getAddressData())

                updateVisitableListLiveData()
//                sendOpenScreenTracker()
            },
            onError = {
                _onError.postValue(it)
            }
        )
    }
}
