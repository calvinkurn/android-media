package com.tokopedia.play.broadcaster.setup.product.view.model

import com.tokopedia.play.broadcaster.ui.model.etalase.SelectedEtalaseModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.result.PageResultState
import com.tokopedia.play.broadcaster.ui.model.sort.SortUiModel

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
data class ProductListPaging(
    val productList: List<ProductUiModel>,
    val resultState: PageResultState,
    val page: Int,
) {

    companion object {
        val Empty: ProductListPaging
            get() = ProductListPaging(
                productList = emptyList(),
                resultState = PageResultState.Loading,
                page = 0,
            )
    }

    data class Param(
        val etalase: SelectedEtalaseModel,
        val keyword: String,
        val sort: SortUiModel?,
    ) {
        companion object {
            val Empty: Param
                get() = Param(
                    etalase = SelectedEtalaseModel.None,
                    keyword = "",
                    sort = null,
                )
        }
    }
}