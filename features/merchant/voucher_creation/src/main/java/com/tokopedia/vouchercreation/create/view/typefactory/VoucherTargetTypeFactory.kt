package com.tokopedia.vouchercreation.create.view.typefactory

import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetUiModel

interface VoucherTargetTypeFactory : CreateVoucherTypeFactory {

    fun type(voucherTargetUiModel: VoucherTargetUiModel) : Int
    fun type(nextButtonUiModel: NextButtonUiModel): Int
}