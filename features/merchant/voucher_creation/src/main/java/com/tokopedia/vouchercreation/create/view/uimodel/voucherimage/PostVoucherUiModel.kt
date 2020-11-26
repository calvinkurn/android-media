package com.tokopedia.vouchercreation.create.view.uimodel.voucherimage

import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.PostBaseUiModel
import com.tokopedia.vouchercreation.detail.model.VoucherDetailUiModel
import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactory

class PostVoucherUiModel(
        override var imageType: VoucherImageType,
        override val promoName: String,
        override val shopAvatar: String,
        override val shopName: String,
        val promoCode: String,
        val promoPeriod: String,
        var postBaseUiModel: PostBaseUiModel) : VoucherDetailUiModel, VoucherImage {

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int =
            typeFactory.type(this)
}