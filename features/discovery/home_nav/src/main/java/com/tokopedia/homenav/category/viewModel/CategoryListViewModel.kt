package com.tokopedia.homenav.category.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.mainnav.domain.usecases.GetCategoryGroupUseCase
import com.tokopedia.homenav.category.view.adapter.mapper.toVisitable
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Lukas on 21/10/20.
 */

class CategoryListViewModel @Inject constructor(
        private val getCategoryGroupUseCase: GetCategoryGroupUseCase,
        private val dispatcher: CoroutineDispatcher
): ViewModel(){

    private val _categoryList = MutableLiveData<Result<List<HomeNavVisitable>>>()
    val categoryList : LiveData<Result<List<HomeNavVisitable>>> get() = _categoryList

    fun getCategory(page: String) {
        viewModelScope.launch(dispatcher) {
            getCategoryGroupUseCase.createParams(page)
            val result = getCategoryGroupUseCase.executeOnBackground()
            if(result is Success){
                _categoryList.postValue(Success(result.data.firstOrNull()?.categoryRows?.toVisitable() ?: listOf()))
            } else {
                _categoryList.postValue(result as Fail)
            }
        }
    }

}