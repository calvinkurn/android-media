package com.tokopedia.tokofood.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodCategoryMapper.addLoadingCategoryIntoList
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodCategoryMapper.mapCategoryLayoutList
import com.tokopedia.tokofood.home.domain.usecase.TokoFoodMerchantListUseCase
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoFoodCategoryViewModel @Inject constructor(
    private val tokoFoodMerchantListUseCase: TokoFoodMerchantListUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val layoutList: LiveData<Result<TokoFoodListUiModel>>
        get() = _categoryLayoutList

    private val _categoryLayoutList = MutableLiveData<Result<TokoFoodListUiModel>>()

    private val categoryLayoutItemList :MutableList<Visitable<*>> = mutableListOf()

    fun getLoadingState() {
        categoryLayoutItemList.clear()
        categoryLayoutItemList.addLoadingCategoryIntoList()
        val data = TokoFoodListUiModel(
            items = categoryLayoutItemList,
            state = TokoFoodLayoutState.LOADING
        )
        _categoryLayoutList.value = Success(data)
    }

    fun getCategoryLayout(localCacheModel: LocalCacheModel, option: Int = 0, brandId: String = "",
                          sortBy: Int = 0, orderById: Int = 0, pageKey: String = "") {
        launchCatchError(block = {

            categoryLayoutItemList.clear()
            val categoryResponse = withContext(dispatchers.io) {
                tokoFoodMerchantListUseCase.execute(localCacheModel, option, brandId, sortBy, orderById, pageKey)
            }

            categoryLayoutItemList.mapCategoryLayoutList(categoryResponse.data.merchants)
            val data = TokoFoodListUiModel(
                items = categoryLayoutItemList,
                state = TokoFoodLayoutState.SHOW
            )

            _categoryLayoutList.value = Success(data)
        }){

        }
    }
}