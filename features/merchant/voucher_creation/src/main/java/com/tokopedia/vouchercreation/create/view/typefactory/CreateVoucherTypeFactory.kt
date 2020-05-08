package com.tokopedia.vouchercreation.create.view.typefactory

import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel
import com.tokopedia.vouchercreation.create.view.viewholder.NextButtonViewHolder

interface CreateVoucherTypeFactory {
    fun type(nextButtonUiModel: NextButtonUiModel): Int = NextButtonViewHolder.LAYOUT
}