package com.tokopedia.tokopedianow.recipelist.domain.param

sealed class RecipeSortBy(val name: String) {

    object SortByOldest: RecipeSortBy("Oldest")
    object SortByNewest: RecipeSortBy("Newest")
    object SortByLeastPortion: RecipeSortBy("LeastPortion")
    object SortMostPortion: RecipeSortBy("MostPortion")
    object SortByShortestDuration: RecipeSortBy("ShortestDuration")
    object SortByLongestDuration: RecipeSortBy("LongestDuration")
}