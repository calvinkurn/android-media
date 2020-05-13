package com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.widgets

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.data.source.VoucherTargetStaticDataSource
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertarget.VoucherTargetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertarget.VoucherTargetItemUiModel

class VoucherTargetUiModel(val onShouldShowBottomSheet: (CreateVoucherBottomSheetType, VoucherTargetCardType?) -> Unit = { _,_ ->},
                           val onSetActiveVoucherTargetType: (Int) -> Unit,
                           val voucherTargetList: List<VoucherTargetItemUiModel> = VoucherTargetStaticDataSource.getVoucherTargetItemUiModelList())
    : Visitable<VoucherTargetTypeFactory> {

    override fun type(typeFactory: VoucherTargetTypeFactory): Int =
            typeFactory.type(this)

}