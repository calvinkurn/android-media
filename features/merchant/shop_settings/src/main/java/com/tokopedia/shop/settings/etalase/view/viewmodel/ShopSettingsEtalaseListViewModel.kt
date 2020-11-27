package com.tokopedia.shop.settings.etalase.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.DeleteShopEtalaseUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseUiModel
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseListFragment.Companion.PRIMARY_ETALASE_LIMIT
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopSettingsEtalaseListViewModel @Inject constructor(
        private val getShopEtalaseUseCase: GetShopEtalaseUseCase,
        private val deleteShopEtalaseUseCase: DeleteShopEtalaseUseCase,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val shopEtalase: LiveData<Result<ArrayList<ShopEtalaseUiModel>>> get() = _shopEtalase
    val deleteMessage: LiveData<Result<String>> get() = _deleteMessage
    private val _shopEtalase = MutableLiveData<Result<ArrayList<ShopEtalaseUiModel>>>()
    private val _deleteMessage = MutableLiveData<Result<String>>()

    fun getShopEtalase() {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                getShopEtalaseUseCase.params = GetShopEtalaseUseCase.createRequestParams(true)
                val shopShowcaseData = getShopEtalaseUseCase.executeOnBackground()
                shopShowcaseData.let {
                    val shopEtalaseUiModels = ArrayList<ShopEtalaseUiModel>()
                    for ((countPrimaryEtalase, shopEtalaseModel) in it.shopShowcases.result.withIndex()) {
                        shopEtalaseUiModels.add(ShopEtalaseUiModel(shopEtalaseModel, countPrimaryEtalase < PRIMARY_ETALASE_LIMIT))
                    }
                    _shopEtalase.postValue(Success(shopEtalaseUiModels))
                }
            }
        }) {
            _shopEtalase.value = Fail(it)
        }
    }

    fun deleteShopEtalase(etalaseId: String) {
        launchCatchError(block = {
            val data = withContext(dispatchers.io) {
                deleteShopEtalaseUseCase.getData(DeleteShopEtalaseUseCase.createRequestParams(etalaseId))
            }
            _deleteMessage.value = Success(data)
        }) {
            _deleteMessage.value = Fail(it)
        }
    }

}