package com.tokopedia.officialstore.official

import com.tokopedia.officialstore.category.data.model.Category

object OfficialStoreHelper {
    fun extractCategoryId(category: Category?): String {
        val categories = category?.categories.toString()
        val categoriesWithoutOpeningSquare =
            categories.replace("[", "") // Remove Square bracket from the string
        val categoriesWithoutClosingSquare =
            categoriesWithoutOpeningSquare.replace("]", "") // Remove Square bracket from the string
        return categoriesWithoutClosingSquare
    }
}