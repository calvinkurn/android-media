package com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertarget

import com.tokopedia.vouchercreation.shop.create.view.typefactory.CreateVoucherTypeFactory
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.widgets.FillVoucherNameUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.widgets.VoucherTargetUiModel

interface VoucherTargetTypeFactory : CreateVoucherTypeFactory {

    fun type(voucherTargetUiModel: VoucherTargetUiModel) : Int
    fun type(fillVoucherNameUiModel: FillVoucherNameUiModel) : Int
}