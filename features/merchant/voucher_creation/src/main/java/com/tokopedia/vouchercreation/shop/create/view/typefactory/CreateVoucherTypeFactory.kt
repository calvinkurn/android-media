package com.tokopedia.vouchercreation.shop.create.view.typefactory

import com.tokopedia.vouchercreation.shop.create.view.uimodel.NextButtonUiModel
import com.tokopedia.vouchercreation.shop.create.view.viewholder.NextButtonViewHolder

interface CreateVoucherTypeFactory {
    fun type(nextButtonUiModel: NextButtonUiModel): Int = NextButtonViewHolder.LAYOUT
}