package com.tokopedia.vouchercreation.shop.create.view.enums

import com.tokopedia.imageassets.TokopediaImageUrl

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.vouchertips.VoucherDisplayUiModel

enum class VoucherTargetCardType(@DrawableRes val iconDrawableRes: Int,
                                 @StringRes val titleStringRes: Int,
                                 @StringRes val descriptionStringRes: Int,
                                 @VoucherTargetType val targetType: Int,
                                 val displayPairList: List<VoucherDisplayUiModel>) {

    PUBLIC(
            R.drawable.ic_im_umum,
            R.string.mvc_create_target_public,
            R.string.mvc_create_target_public_desc,
            VoucherTargetType.PUBLIC,
            listOf(
                    VoucherDisplayUiModel(R.string.mvc_create_public_voucher_display_product_page, VoucherDisplay.PUBLIC_PRODUCT),
                    VoucherDisplayUiModel(R.string.mvc_create_public_voucher_display_shop_page, VoucherDisplay.PUBLIC_SHOP),
                    VoucherDisplayUiModel(R.string.mvc_create_public_voucher_display_cart_page, VoucherDisplay.PUBLIC_CART)
            )),
    PRIVATE(
            R.drawable.ic_im_terbatas,
            R.string.mvc_create_target_private,
            R.string.mvc_create_target_private_desc,
            VoucherTargetType.PRIVATE,
            listOf(
                    VoucherDisplayUiModel(R.string.mvc_create_private_voucher_display_download_voucher, VoucherDisplay.PRIVATE_DOWNLOAD),
                    VoucherDisplayUiModel(R.string.mvc_create_private_voucher_display_socmed_post, VoucherDisplay.PRIVATE_SOCMED),
                    VoucherDisplayUiModel(R.string.mvc_create_private_voucher_display_chat_share, VoucherDisplay.PRIVATE_CHAT)
            ))

}

object VoucherDisplay {
    const val PUBLIC_SHOP = TokopediaImageUrl.PUBLIC_SHOP
    const val PUBLIC_PRODUCT = TokopediaImageUrl.PUBLIC_PRODUCT
    const val PUBLIC_CART = TokopediaImageUrl.PUBLIC_CART
    const val PRIVATE_SOCMED = TokopediaImageUrl.PRIVATE_SOCMED
    const val PRIVATE_CHAT = TokopediaImageUrl.PRIVATE_CHAT
    const val PRIVATE_DOWNLOAD = TokopediaImageUrl.PRIVATE_DOWNLOAD
}
