package com.tokopedia.vouchercreation.common.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.view.View
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMaxStartDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getMinStartDate
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils.getToday
import com.tokopedia.vouchercreation.shop.detail.view.fragment.VoucherDetailFragment
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sharebottomsheet.SocmedType
import java.util.*

private const val DATE_TIME_PICKER_TAG = "date_time_picker_tag"
private const val DATE_TIME_MINUTE_INTERVAL = 30

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

fun View.highlightView(active: Boolean) {
    resources?.run {
        val borderColorRes = if (active) {
            com.tokopedia.unifyprinciples.R.color.Green_G400
        } else {
            com.tokopedia.unifyprinciples.R.color.Neutral_N75
        }
        (background as GradientDrawable).setStroke(
            getDimension(R.dimen.mvc_create_target_card_border_width).toInt().orZero(),
            MethodChecker.getColor(context, borderColorRes))
    }
}

fun FragmentManager.dismissBottomSheetWithTags(vararg tags: String) {
    tags.forEach {
        (findFragmentByTag(it) as? BottomSheetUnify)?.dismiss()
    }
}

fun Fragment.getStartDateTimePicker(
    title: String,
    info: CharSequence,
    minDate: Calendar,
    defaultDate: Calendar,
    maxDate: Calendar,
    onDateTimePicked: (calendar: Calendar) -> Unit
) {
    context?.let { context ->
        DateTimePickerUnify(context, minDate, defaultDate, maxDate, null,
            DateTimePickerUnify.TYPE_DATETIMEPICKER).apply {
            setTitle(title)
            setInfo(info)
            setInfoVisible(true)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
            minuteInterval = DATE_TIME_MINUTE_INTERVAL
            datePickerButton.text = context.getString(R.string.mvc_pick)
            datePickerButton.setOnClickListener {
                onDateTimePicked.invoke(getDate())
                dismiss()
            }
        }.show(childFragmentManager, DATE_TIME_PICKER_TAG)
    }
}

fun ImageView.tintDrawableToBlack() {
    setColorFilter(MethodChecker.getColor(context,
        com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
}

fun TextFieldUnify.setFieldOnClickListener(onClick: () -> Unit) {
    textFieldInput.inputType = InputType.TYPE_NULL
    textFieldInput.setOnClickListener {
        onClick.invoke()
    }
    textFieldInput.isFocusable = false
    textFieldInput.isClickable = true
}

fun ShopBasicDataResult.shareVoucher(context: Context,
                                     @SocmedType socmedType: Int,
                                     voucher: VoucherUiModel,
                                     userId: String,
                                     shopId: String) {
    val shareUrl = "${TokopediaUrl.getInstance().WEB}${shopDomain}"
    val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
        type = LinkerData.MERCHANT_VOUCHER
        uri = shareUrl
        id = voucher.id.toString()
        deepLink = UriUtil.buildUri(ApplinkConst.SHOP, shopId).orEmpty()
    })
    LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    linkerShareData?.url?.let {
                        shareVoucherByType(context, socmedType, voucher, shopName, it)
                    }
                }

                override fun onError(linkerError: LinkerError?) {}
            })
    )

    VoucherCreationTracking.sendShareClickTracking(
            socmedType = socmedType,
            userId = userId,
            isDetail = true
    )
}

private fun shareVoucherByType(context: Context,
                               @SocmedType socmedType: Int,
                               voucher: VoucherUiModel,
                               shopName: String,
                               shareUrl: String) {
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
        SocmedType.BROADCAST -> {
            SharingUtil.shareToBroadCastChat(context, voucher.id)
        }
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
}

internal fun getTextSizeFromDimens(context: Context, @DimenRes dimensResId : Int): Float {
    return context.resources.getDimension(dimensResId) / context.resources.displayMetrics.density
}

fun ShopBasicDataResult.shareVoucher(
    context: Context,
    @SocmedType socmedType: Int,
    voucherId: Int,
    isPublicVoucher: Boolean,
    voucherCode: String,
    voucherImageSquare: String,
    voucherTypeFormatted: String,
    userId: String,
    shopId: String
) {
    val shareUrl = "${TokopediaUrl.getInstance().WEB}${shopDomain}"
    val linkerShareData = DataMapper.getLinkerShareData(LinkerData().apply {
        type = LinkerData.MERCHANT_VOUCHER
        uri = shareUrl
        id = voucherId.toString()
        deepLink = UriUtil.buildUri(ApplinkConst.SHOP, shopId)
    })
    LinkerManager.getInstance().executeShareRequest(
        LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult?) {
                linkerShareData?.url?.let {
                    shareVoucherByType(
                        context,
                        socmedType,
                        isPublicVoucher,
                        voucherId,
                        voucherCode,
                        voucherImageSquare,
                        voucherTypeFormatted,
                        shopName,
                        shareUrl
                    )
                }
            }

            override fun onError(linkerError: LinkerError?) {}
        })
    )

    VoucherCreationTracking.sendShareClickTracking(
        socmedType = socmedType,
        userId = userId,
        isDetail = true
    )
}


private fun shareVoucherByType(context: Context,
                               @SocmedType socmedType: Int,
                               isPublicVoucher : Boolean,
                               voucherId: Int,
                               voucherCode : String,
                               voucherImageSquare : String,
                               voucherTypeFormatted : String,
                               shopName: String,
                               shareUrl: String) {
    val shareMessage =
        if (isPublicVoucher) {
            StringBuilder().apply {
                append(String.format(
                    context.getString(R.string.mvc_share_message_public).toBlankOrString(),
                    voucherTypeFormatted,
                    shopName))
                append("\n")
                append(shareUrl)
            }.toString()
        } else {
            StringBuilder().apply {
                append(String.format(
                    context.getString(R.string.mvc_share_message_private).toBlankOrString(),
                    voucherTypeFormatted,
                    voucherCode,
                    shopName))
                append("\n")
                append(shareUrl)
            }.toString()
        }
    when(socmedType) {
        SocmedType.BROADCAST -> {
            SharingUtil.shareToBroadCastChat(context, voucherId)
        }
        SocmedType.COPY_LINK -> {
            SharingUtil.copyTextToClipboard(context, VoucherDetailFragment.COPY_PROMO_CODE_LABEL, shareMessage)
        }
        SocmedType.INSTAGRAM -> {
            SharingUtil.shareToSocialMedia(Socmed.INSTAGRAM, context, voucherImageSquare)
        }
        SocmedType.WHATSAPP -> {
            SharingUtil.shareToSocialMedia(Socmed.WHATSAPP, context, voucherImageSquare, shareMessage)
        }
        SocmedType.LINE -> {
            SharingUtil.shareToSocialMedia(Socmed.LINE, context, voucherImageSquare, shareMessage)
        }
        SocmedType.TWITTER -> {
            SharingUtil.shareToSocialMedia(Socmed.TWITTER, context, voucherImageSquare, shareMessage)
        }
        SocmedType.FACEBOOK -> {
            SharingUtil.shareToSocialMedia(Socmed.FACEBOOK, context, voucherImageSquare, shareMessage)
        }
        SocmedType.LAINNYA -> {
            SharingUtil.otherShare(context, shareMessage)
        }
    }
}