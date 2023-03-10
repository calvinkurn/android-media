package com.tokopedia.catalog_library.viewmodels

import androidx.lifecycle.ViewModel
import com.tokopedia.catalog_library.usecase.CatalogBrandsPopularUseCase
import javax.inject.Inject

class PopularBrandsViewModel @Inject constructor(
    private val catalogBrandsPopularUseCase: CatalogBrandsPopularUseCase
) : ViewModel() {
}
