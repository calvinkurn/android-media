package com.tokopedia.tokofood.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.home.domain.mapper.TokoFoodHomeMapper.addLoadingCategoryIntoList
import com.tokopedia.tokofood.home.domain.usecase.TokoFoodMerchantListUseCase
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodItemUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoFoodCategoryViewModel @Inject constructor(
    private val tokoFoodMerchantListUseCase: TokoFoodMerchantListUseCase,
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    val layoutList: LiveData<Result<TokoFoodListUiModel>>
        get() = _categoryLayoutList

    private val _categoryLayoutList = MutableLiveData<Result<TokoFoodListUiModel>>()

    private val categoryLayoutItemList = mutableListOf<TokoFoodItemUiModel>()


    fun getLoadingState() {
        categoryLayoutItemList.clear()
        categoryLayoutItemList.addLoadingCategoryIntoList()
        val data = TokoFoodListUiModel(
            items = getCategoryVisitableList(),
            state = TokoFoodLayoutState.LOADING
        )
        _categoryLayoutList.value = Success(data)
    }

    private fun getCategoryVisitableList(): List<Visitable<*>> {
        return categoryLayoutItemList.mapNotNull { it.layout }
    }
}