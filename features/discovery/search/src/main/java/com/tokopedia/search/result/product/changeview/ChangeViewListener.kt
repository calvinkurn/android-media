package com.tokopedia.search.result.product.changeview

import com.tokopedia.search.result.product.grid.ProductGridType

interface ChangeViewListener {
    val viewType: ViewType
    val productGridType: ProductGridType

    fun onChangeViewClicked()

    fun checkShouldShowViewTypeOnBoarding()
}
