package com.tokopedia.home_explore_category.presentation.uimodel

sealed class ExploreCategoryState<out T> {
    object Loading : ExploreCategoryState<Nothing>()
    data class Success<T>(val data: T) : ExploreCategoryState<T>()
    data class Fail<T>(val throwable: Throwable) : ExploreCategoryState<T>()
}
