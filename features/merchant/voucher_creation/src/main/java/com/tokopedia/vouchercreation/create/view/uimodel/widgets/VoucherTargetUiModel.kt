package com.tokopedia.vouchercreation.create.view.uimodel.widgets

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.data.source.VoucherTargetStaticDataSource
import com.tokopedia.vouchercreation.create.view.enums.CreateVoucherBottomSheetType
import com.tokopedia.vouchercreation.create.view.enums.VoucherTargetCardType
import com.tokopedia.vouchercreation.create.view.typefactory.VoucherTargetTypeFactory
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherTargetItemUiModel

class VoucherTargetUiModel(val onShouldShowBottomSheet: (CreateVoucherBottomSheetType, VoucherTargetCardType?) -> Unit = { _,_ ->},
                           val voucherTargetList: List<VoucherTargetItemUiModel> = VoucherTargetStaticDataSource.getVoucherTargetItemUiModelList())
    : Visitable<VoucherTargetTypeFactory> {

    override fun type(typeFactory: VoucherTargetTypeFactory): Int =
            typeFactory.type(this)

}