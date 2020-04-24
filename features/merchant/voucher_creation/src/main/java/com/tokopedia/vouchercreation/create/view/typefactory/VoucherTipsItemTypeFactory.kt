package com.tokopedia.vouchercreation.create.view.typefactory

import com.tokopedia.vouchercreation.create.view.uimodel.vouchertips.BasicVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertips.DottedVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertips.ImageVoucherTipsItemUiModel

interface VoucherTipsItemTypeFactory {

    fun type(imageVoucherTipsItemUiModel: ImageVoucherTipsItemUiModel): Int
    fun type(dottedVoucherTipsItemUiModel: DottedVoucherTipsItemUiModel): Int
    fun type(basicVoucherTipsItemUiModel: BasicVoucherTipsItemUiModel): Int

}