package com.tokopedia.homenav.category.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.homenav.base.diffutil.HomeNavVisitable
import com.tokopedia.homenav.category.domain.usecases.GetCategoryListUseCase
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
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel(){

    private val _categoryList = MutableLiveData<Result<List<HomeNavVisitable>>>()
    val categoryList : LiveData<Result<List<HomeNavVisitable>>> get() = _categoryList

    fun getCategory(page: String) {
        viewModelScope.launch(dispatcher) {
            getCategoryListUseCase.createParams(page)
            val result = getCategoryListUseCase.executeOnBackground()
            if(result is Success){
                _categoryList.postValue(Success(result.data.toVisitable()))
            } else {
                _categoryList.postValue(result as Fail)
            }
        }
    }

}