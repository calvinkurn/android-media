package com.tokopedia.vouchercreation.create.view.typefactory

import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel

interface CreateVoucherTypeFactory {
    fun type(nextButtonUiModel: NextButtonUiModel): Int
}