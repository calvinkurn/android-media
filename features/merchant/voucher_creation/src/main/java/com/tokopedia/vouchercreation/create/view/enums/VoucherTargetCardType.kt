package com.tokopedia.vouchercreation.create.view.enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.VoucherDisplayUiModel

enum class VoucherTargetCardType(@DrawableRes val iconDrawableRes: Int,
                                 @StringRes val titleStringRes: Int,
                                 @StringRes val descriptionStringRes: Int,
                                 val displayPairList: List<VoucherDisplayUiModel>) {

    PUBLIC(
            R.drawable.ic_im_umum,
            R.string.mvc_create_target_public,
            R.string.mvc_create_target_public_desc,
            listOf(
                    VoucherDisplayUiModel(R.string.mvc_create_public_voucher_display_product_page, R.drawable.mvc_image_public_product),
                    VoucherDisplayUiModel(R.string.mvc_create_public_voucher_display_shop_page, R.drawable.mvc_image_public_shop),
                    VoucherDisplayUiModel(R.string.mvc_create_public_voucher_display_cart_page, R.drawable.mvc_image_public_cart)
            )),
    PRIVATE(
            R.drawable.ic_im_terbatas,
            R.string.mvc_create_target_private,
            R.string.mvc_create_target_private_desc,
            listOf(
                    VoucherDisplayUiModel(R.string.mvc_create_private_voucher_display_download_voucher, R.drawable.mvc_image_private_download),
                    VoucherDisplayUiModel(R.string.mvc_create_private_voucher_display_socmed_post, R.drawable.mvc_image_private_socmed),
                    VoucherDisplayUiModel(R.string.mvc_create_private_voucher_display_chat_share, R.drawable.mvc_image_private_chat)
            ))

}