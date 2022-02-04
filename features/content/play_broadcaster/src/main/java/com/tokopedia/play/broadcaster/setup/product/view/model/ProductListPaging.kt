package com.tokopedia.play.broadcaster.setup.product.view.model

import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
data class ProductListPaging(
    val productList: List<ProductUiModel>,
    val resultState: PageResultState,
) {

    companion object {
        val Empty: ProductListPaging
            get() = ProductListPaging(
                productList = emptyList(),
                resultState = PageResultState.Loading,
            )
    }
}