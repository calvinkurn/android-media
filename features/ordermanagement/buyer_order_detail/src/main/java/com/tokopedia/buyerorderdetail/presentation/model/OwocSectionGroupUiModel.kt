package com.tokopedia.buyerorderdetail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocSectionGroupTypeFactoryImpl

class OwocSectionGroupUiModel(
    val owocProductListUiModel: List<OwocProductListUiModel>
): Visitable<OwocSectionGroupTypeFactoryImpl> {

    override fun type(typeFactory: OwocSectionGroupTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

}
