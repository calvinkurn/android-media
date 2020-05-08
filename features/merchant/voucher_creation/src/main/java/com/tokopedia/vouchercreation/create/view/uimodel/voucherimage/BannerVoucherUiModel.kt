package com.tokopedia.vouchercreation.create.view.uimodel.voucherimage

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.typefactory.voucherimage.VoucherImageTypeFactory

data class BannerVoucherUiModel<T : VoucherImageTypeFactory>(
        override val imageType: VoucherImageType,
        override val promoName: String,
        override val shopName: String,
        override val shopAvatar: String
) : Visitable<T>, VoucherImage {

    override fun type(typeFactory: T): Int = typeFactory.type(this)
}