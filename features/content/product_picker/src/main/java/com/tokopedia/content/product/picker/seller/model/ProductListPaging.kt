package com.tokopedia.content.product.picker.seller.model

import com.tokopedia.content.product.picker.seller.model.etalase.SelectedEtalaseModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.model.result.PageResultState
import com.tokopedia.content.product.picker.seller.model.sort.SortUiModel

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
data class ProductListPaging(
    val productList: List<ProductUiModel>,
    val resultState: PageResultState,
    val pagingType: PagingType,
) {

    companion object {
        val Empty: ProductListPaging
            get() = ProductListPaging(
                productList = emptyList(),
                resultState = PageResultState.Loading,
                pagingType = PagingType.Unknown,
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
