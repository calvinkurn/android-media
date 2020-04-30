package com.tokopedia.vouchercreation.common.view

import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldUiModel

interface VoucherCommonTypeFactory {

    fun type(voucherTextFieldUiModel: VoucherTextFieldUiModel): Int

}