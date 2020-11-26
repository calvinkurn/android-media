package com.tokopedia.vouchercreation.create.view.typefactory.vouchertarget

import com.tokopedia.vouchercreation.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.widgets.FillVoucherNameUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.widgets.VoucherTargetUiModel

interface VoucherTargetTypeFactory : CreateVoucherTypeFactory {

    fun type(voucherTargetUiModel: VoucherTargetUiModel) : Int
    fun type(fillVoucherNameUiModel: FillVoucherNameUiModel) : Int
}