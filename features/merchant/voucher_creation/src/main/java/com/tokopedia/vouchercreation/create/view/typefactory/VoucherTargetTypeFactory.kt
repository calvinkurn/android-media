package com.tokopedia.vouchercreation.create.view.typefactory

import com.tokopedia.vouchercreation.create.view.uimodel.widgets.FillVoucherNameUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.widgets.VoucherTargetUiModel

interface VoucherTargetTypeFactory : CreateVoucherTypeFactory {

    fun type(voucherTargetUiModel: VoucherTargetUiModel) : Int
    fun type(fillVoucherNameUiModel: FillVoucherNameUiModel) : Int
}