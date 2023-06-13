package com.tokopedia.search.result.product.changeview

interface ChangeViewListener {
    val viewType: ViewType

    fun onChangeViewClicked()
}
