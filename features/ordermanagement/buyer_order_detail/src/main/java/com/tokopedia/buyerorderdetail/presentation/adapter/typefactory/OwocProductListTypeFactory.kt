package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import com.tokopedia.buyerorderdetail.presentation.model.OwocAddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel


interface OwocProductListTypeFactory {

    fun type(owocProductListHeaderUiModel: OwocProductListUiModel.ProductListHeaderUiModel): Int

    fun type(owocProductUiModel: OwocProductListUiModel.ProductUiModel): Int

    fun type(owocProductBundlingUiModel: OwocProductListUiModel.ProductBundlingUiModel): Int

    fun type(owocProductListToggleUiModel: OwocProductListUiModel.ProductListToggleUiModel): Int

    fun type(owocAddonsListUiModel: OwocAddonsListUiModel): Int
}
