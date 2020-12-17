package com.tokopedia.shop.settings.etalase.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.AddShopEtalaseUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.UpdateShopEtalaseUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseUiModel
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseAddEditFragment.Companion.ID
import com.tokopedia.usecase.UseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopSettingsEtalaseAddEditViewModel
@Inject constructor(
        private val addShopEtalaseUseCase: AddShopEtalaseUseCase,
        private val updateShopEtalaseUseCase: UpdateShopEtalaseUseCase,
        private val getShopEtalaseUseCase: GetShopEtalaseUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val shopEtalase: LiveData<Result<List<ShopEtalaseModel>>> get() = _shopEtalase
    val saveMessage: LiveData<Result<String>> get() = _saveMessage
    private val _shopEtalase = MutableLiveData<Result<List<ShopEtalaseModel>>>()
    private val _saveMessage = MutableLiveData<Result<String>>()

    fun isIdlePowerMerchant() = userSession.isPowerMerchantIdle
    fun isPowerMerchant() = userSession.isGoldMerchant

    fun getShopEtalase() {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                getShopEtalaseUseCase.params = GetShopEtalaseUseCase.createRequestParams(true)
                val shopShowcaseData = getShopEtalaseUseCase.executeOnBackground()
                shopShowcaseData.let {
                    _shopEtalase.postValue(Success(it.shopShowcases.result))
                }
            }
        }) {
            _shopEtalase.value = Fail(it)
        }
    }

    fun saveShopEtalase(etalaseModel: ShopEtalaseUiModel, isEdit: Boolean = false) {
        launchCatchError(block = {
            val useCase: UseCase<String> = if (!isEdit) addShopEtalaseUseCase else updateShopEtalaseUseCase
            val requestParams = AddShopEtalaseUseCase.createRequestParams(etalaseModel.name)

            if (isEdit) {
                requestParams.putString(ID, etalaseModel.id)
            }
            val data = withContext(dispatchers.io) {
                useCase.getData(requestParams)
            }
            _saveMessage.value = Success(data)
        }) {
            _saveMessage.value = Fail(it)
        }
    }
}