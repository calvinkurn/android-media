package com.tokopedia.vouchercreation.create.view.uimodel.voucherimage

import android.content.Context
import com.tokopedia.vouchercreation.common.consts.VoucherUrl
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getDisplayedDateString
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.enums.getVoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.initiation.PostBaseUiModel
import com.tokopedia.vouchercreation.detail.model.VoucherDetailUiModel
import com.tokopedia.vouchercreation.detail.view.adapter.factory.VoucherDetailAdapterFactory
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel

class PostVoucherUiModel(
        override var imageType: VoucherImageType,
        override val promoName: String,
        override val shopAvatar: String,
        override val shopName: String,
        val promoCode: String,
        val promoPeriod: String,
        var postBaseUiModel: PostBaseUiModel,
        val isPublic: Boolean? = null) : VoucherDetailUiModel, VoucherImage {

    companion object {
        @JvmStatic
        fun mapToUiModel(context: Context?,
                         voucherUiModel: VoucherUiModel,
                         shopAvatar: String,
                         shopName: String,
                         startDate: String,
                         endDate: String): PostVoucherUiModel? {
            return with(voucherUiModel) {
                getVoucherImageType(type, discountTypeFormatted, discountAmt, discountAmtMax)?.let { imageType ->
                    PostVoucherUiModel(
                            imageType = imageType,
                            promoName = name,
                            shopAvatar = shopAvatar,
                            shopName = shopName,
                            promoCode = code,
                            promoPeriod = getDisplayedDateString(context, startDate, endDate),
                            postBaseUiModel = PostBaseUiModel(
                                    VoucherUrl.POST_IMAGE_URL,
                                    VoucherUrl.FREE_DELIVERY_URL,
                                    VoucherUrl.CASHBACK_URL,
                                    VoucherUrl.CASHBACK_UNTIL_URL),
                            isPublic = isPublic
                    )
                }
            }
        }
    }

    override fun type(typeFactory: VoucherDetailAdapterFactory): Int =
            typeFactory.type(this)
}