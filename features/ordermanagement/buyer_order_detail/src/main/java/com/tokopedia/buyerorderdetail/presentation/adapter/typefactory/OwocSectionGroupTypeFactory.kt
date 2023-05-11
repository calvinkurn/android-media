package com.tokopedia.buyerorderdetail.presentation.adapter.typefactory

import com.tokopedia.buyerorderdetail.presentation.model.OwocErrorUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocSectionGroupUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocShimmerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocThickDividerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocTickerUiModel

interface OwocSectionGroupTypeFactory {
    fun type(owocTickerUiModel: OwocTickerUiModel): Int

    fun type(owocSectionGroupUiModel: OwocSectionGroupUiModel): Int

    fun type(owocShimmerUiModel: OwocShimmerUiModel): Int

    fun type(owocErrorUiModel: OwocErrorUiModel): Int
}
