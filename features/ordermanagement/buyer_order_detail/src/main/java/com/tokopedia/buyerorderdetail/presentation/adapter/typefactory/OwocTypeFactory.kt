package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocShimmerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocTickerUiModel


interface OwocTypeFactory {

    fun type(owocTickerUiModel: OwocTickerUiModel): Int

    fun type(owocShimmerUiModel: OwocShimmerUiModel): Int

    fun type(owocProductListUiModel: OwocProductListUiModel): Int

}
