package com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertarget

import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.BasicVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.DottedVoucherTipsItemUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.ImageVoucherTipsItemUiModel

interface VoucherTipsItemTypeFactory {

    fun type(imageVoucherTipsItemUiModel: ImageVoucherTipsItemUiModel): Int
    fun type(dottedVoucherTipsItemUiModel: DottedVoucherTipsItemUiModel): Int
    fun type(basicVoucherTipsItemUiModel: BasicVoucherTipsItemUiModel): Int

}