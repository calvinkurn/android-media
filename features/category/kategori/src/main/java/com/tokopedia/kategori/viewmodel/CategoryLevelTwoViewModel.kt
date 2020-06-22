package com.tokopedia.kategori.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.kategori.model.CategoryChildItem
import com.tokopedia.kategori.subscriber.CategoryLevelTwoSubscriber
import com.tokopedia.kategori.usecase.AllCategoryQueryUseCase
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

const val categoryDepth = 2

class CategoryLevelTwoViewModel @Inject constructor(private var allCategoryQueryUseCase: AllCategoryQueryUseCase) : ViewModel() {

    var childItem: LiveData<Result<List<CategoryChildItem>>>? = null

    fun refresh(id: String) {
        val subscriber = getSubscriber(id)
        childItem = subscriber.getCategoryList()
        allCategoryQueryUseCase.execute(allCategoryQueryUseCase.createRequestParams(categoryDepth, true), subscriber)
    }

    internal fun getSubscriber(id: String): CategoryLevelTwoSubscriber {
        return CategoryLevelTwoSubscriber(id)
    }

    override fun onCleared() {
        super.onCleared()
        allCategoryQueryUseCase.unsubscribe()
    }
}