package com.tokopedia.vouchercreation.create.view.typefactory

import com.tokopedia.vouchercreation.create.view.uimodel.FillVoucherNameUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetUiModel

interface VoucherTargetTypeFactory : CreateVoucherTypeFactory {

    fun type(voucherTargetUiModel: VoucherTargetUiModel) : Int
    fun type(fillVoucherNameUiModel: FillVoucherNameUiModel) : Int
}