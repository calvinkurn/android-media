package com.tokopedia.vouchercreation.common.utils

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.detail.view.fragment.VoucherDetailFragment
import com.tokopedia.vouchercreation.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.SocmedType

fun View.showErrorToaster(errorMessage: String) {
    Toaster.make(this,
            errorMessage,
            Snackbar.LENGTH_SHORT,
            Toaster.TYPE_ERROR)
}

fun View.showDownloadActionTicker(isSuccess: Boolean,
                                  isInternetProblem: Boolean = true) {
    val toasterType: Int
    val toasterMessage: String
    if (isSuccess) {
        toasterType = Toaster.TYPE_NORMAL
        toasterMessage = context?.getString(R.string.mvc_success_download_voucher).toBlankOrString()
    } else {
        toasterType = Toaster.TYPE_ERROR
        val errorMessageSuffix =
                if (isInternetProblem) {
                    context?.getString(R.string.mvc_fail_download_voucher_suffix).toBlankOrString()
                } else {
                    ""
                }
        toasterMessage = "${context?.getString(R.string.mvc_fail_download_voucher).toBlankOrString()}$errorMessageSuffix"
    }

    Toaster.make(this,
            toasterMessage,
            Toaster.LENGTH_LONG,
            toasterType,
            context?.getString(R.string.mvc_oke).toBlankOrString())
}

fun FragmentManager.dismissBottomSheetWithTags(vararg tags: String) {
    tags.forEach {
        (findFragmentByTag(it) as? BottomSheetUnify)?.dismiss()
    }
}

fun ShopBasicDataResult.shareVoucher(context: Context,
                                     @SocmedType socmedType: Int,
                                     voucher: VoucherUiModel,
                                     userId: String) {
    val shareUrl = "${TokopediaUrl.getInstance().WEB}${shopDomain}"
    val shareMessage =
            if (voucher.isPublic) {
                StringBuilder().apply {
                    append(String.format(
                            context.getString(R.string.mvc_share_message_public).toBlankOrString(),
                            voucher.typeFormatted,
                            shopName))
                    append("\n")
                    append(shareUrl)
                }.toString()
            } else {
                StringBuilder().apply {
                    append(String.format(
                            context.getString(R.string.mvc_share_message_private).toBlankOrString(),
                            voucher.typeFormatted,
                            voucher.code,
                            shopName))
                    append("\n")
                    append(shareUrl)
                }.toString()
            }
    when(socmedType) {
        SocmedType.COPY_LINK -> {
            SharingUtil.copyTextToClipboard(context, VoucherDetailFragment.COPY_PROMO_CODE_LABEL, shareMessage)
        }
        SocmedType.INSTAGRAM -> {
            SharingUtil.shareToSocialMedia(Socmed.INSTAGRAM, context, voucher.imageSquare)
        }
        SocmedType.WHATSAPP -> {
            SharingUtil.shareToSocialMedia(Socmed.WHATSAPP, context, voucher.imageSquare, shareMessage)
        }
        SocmedType.LINE -> {
            SharingUtil.shareToSocialMedia(Socmed.LINE, context, voucher.imageSquare, shareMessage)
        }
        SocmedType.TWITTER -> {
            SharingUtil.shareToSocialMedia(Socmed.TWITTER, context, voucher.imageSquare, shareMessage)
        }
        SocmedType.FACEBOOK -> {
            SharingUtil.shareToSocialMedia(Socmed.FACEBOOK, context, voucher.imageSquare, shareMessage)
        }
        SocmedType.LAINNYA -> {
            SharingUtil.otherShare(context, shareMessage)
        }
    }
    VoucherCreationTracking.sendShareClickTracking(
            socmedType = socmedType,
            userId = userId,
            isDetail = true
    )
}