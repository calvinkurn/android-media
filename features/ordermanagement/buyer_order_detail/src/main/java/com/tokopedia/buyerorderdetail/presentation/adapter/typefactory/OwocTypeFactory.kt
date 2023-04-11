package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import com.tokopedia.buyerorderdetail.presentation.adapter.viewholder.AddonsViewHolder
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocAddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocShimmerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocThickDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocTickerUiModel


interface OwocTypeFactory {

    fun type(owocTickerUiModel: OwocTickerUiModel): Int

    fun type(owocShimmerUiModel: OwocShimmerUiModel): Int

    fun type(owocProductListHeaderUiModel: OwocProductListUiModel.ProductListHeaderUiModel): Int

    fun type(owocProductUiModel: OwocProductListUiModel.ProductUiModel): Int

    fun type(owocProductBundlingUiModel: OwocProductListUiModel.ProductBundlingUiModel): Int

    fun type(owocProductListToggleUiModel: OwocProductListUiModel.ProductListToggleUiModel): Int

    fun type(owocAddonsListUiModel: OwocAddonsListUiModel): Int

    fun type(owocThickDividerUiModel: OwocThickDividerUiModel): Int
}
