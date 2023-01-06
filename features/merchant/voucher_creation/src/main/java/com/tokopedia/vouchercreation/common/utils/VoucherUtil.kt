package com.tokopedia.vouchercreation.common.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.view.View
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.orZero
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
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.date.toDate
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.shop.detail.view.fragment.VoucherDetailFragment
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sharebottomsheet.SocmedType
import java.util.*

private const val DATE_TIME_PICKER_TAG = "date_time_picker_tag"
private const val DATE_TIME_MINUTE_INTERVAL = 30
private const val DISCOUNT_TYPE_NOMINAL = "idr"
private const val THOUSAND = 1_000f
private const val MILLION = 1_000_000f

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

fun Fragment.setFragmentToUnifyBgColor() {
    if (activity != null && context != null) {
        activity!!.window.decorView.setBackgroundColor(
            ContextCompat.getColor(
            context!!, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }
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

fun VoucherUiModel.addParamImageGenerator(universalShareBottomSheet: UniversalShareBottomSheet, shopName: String, shopLogo: String) {
    val formattedShopName = MethodChecker.fromHtml(shopName).toString()

    val isPublic = if (this.isPublic) {
        ImageGeneratorConstants.VoucherVisibility.PUBLIC
    } else {
        ImageGeneratorConstants.VoucherVisibility.PRIVATE
    }

    val couponType = if (this.type == VoucherTypeConst.FREE_ONGKIR) {
        CouponType.FREE_SHIPPING
    } else {
        CouponType.CASHBACK
    }

    val discountType = if (this.discountTypeFormatted == DISCOUNT_TYPE_NOMINAL) {
        DiscountType.NOMINAL
    } else {
        DiscountType.PERCENTAGE
    }

    val benefitType = when (this.type) {
        VoucherTypeConst.FREE_ONGKIR -> ImageGeneratorConstants.VoucherBenefitType.GRATIS_ONGKIR
        VoucherTypeConst.CASHBACK -> ImageGeneratorConstants.VoucherBenefitType.CASHBACK
        else -> ImageGeneratorConstants.VoucherBenefitType.DISCOUNT
    }

    val cashbackType = when {
        couponType == CouponType.FREE_SHIPPING -> ImageGeneratorConstants.CashbackType.NOMINAL
        couponType == CouponType.CASHBACK && discountType == DiscountType.NOMINAL -> ImageGeneratorConstants.CashbackType.NOMINAL
        couponType == CouponType.CASHBACK && discountType == DiscountType.PERCENTAGE -> ImageGeneratorConstants.CashbackType.PERCENTAGE
        else -> ImageGeneratorConstants.CashbackType.NOMINAL
    }

    val amount = when {
        couponType == CouponType.FREE_SHIPPING -> this.discountAmt
        couponType == CouponType.CASHBACK && discountType == DiscountType.NOMINAL -> this.discountAmt
        couponType == CouponType.CASHBACK && discountType == DiscountType.PERCENTAGE -> this.discountAmtMax
        else -> this.discountAmt
    }

    val formattedDiscountAmount: Int = when {
        amount < THOUSAND -> amount
        amount >= MILLION -> (amount / MILLION).toInt()
        amount >= THOUSAND -> (amount / THOUSAND).toInt()
        else -> amount
    }

    val symbol = when {
        amount < THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
        amount >= MILLION -> ImageGeneratorConstants.VoucherNominalSymbol.JT
        amount >= THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
        else -> ImageGeneratorConstants.VoucherNominalSymbol.RB
    }

    val startTime = this.startTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
        .parseTo(DateTimeUtils.DATE_FORMAT)
    val endTime = this.finishTime.toDate(DateTimeUtils.TIME_STAMP_FORMAT)
        .parseTo(DateTimeUtils.DATE_FORMAT)

    universalShareBottomSheet.apply {
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.IS_PUBLIC,
            value = isPublic
        )
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_BENEFIT_TYPE,
            value = benefitType
        )
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_CASHBACK_TYPE,
            value = cashbackType
        )
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_CASHBACK_PERCENTAGE,
            value = this@addParamImageGenerator.discountAmt.toString()
        )
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_NOMINAL_AMOUNT,
            value = formattedDiscountAmount.toString()
        )
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_NOMINAL_SYMBOL,
            value = symbol
        )
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.SHOP_LOGO_MVC,
            value = shopLogo
        )
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.SHOP_NAME_MVC,
            value = formattedShopName
        )
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_CODE,
            value = this@addParamImageGenerator.code
        )
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_START_TIME,
            value = startTime
        )
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.VOUCHER_FINISH_TIME,
            value = endTime
        )
        addImageGeneratorData(
            key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_COUNT,
            value = "0"
        )
    }
}

private fun shareVoucherByType(context: Context,
                               @SocmedType socmedType: Int,
                               voucher: VoucherUiModel,
                               shopName: String,
                               shareUrl: String) {
    val formattedShopName = MethodChecker.fromHtml(shopName).toString()
    val shareMessage =
            if (voucher.isPublic) {
                StringBuilder().apply {
                    append(String.format(
                            context.getString(R.string.mvc_share_message_public).toBlankOrString(),
                            voucher.typeFormatted,
                            formattedShopName))
                    append("\n")
                    append(shareUrl)
                }.toString()
            } else {
                StringBuilder().apply {
                    append(String.format(
                            context.getString(R.string.mvc_share_message_private).toBlankOrString(),
                            voucher.typeFormatted,
                            voucher.code,
                            formattedShopName))
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

fun getShareMessage(context: Context, voucher: VoucherUiModel, shopName: String, shareUrl: String): String {
    val formattedShopName = MethodChecker.fromHtml(shopName).toString()
    val shareMessage =
        if (voucher.isPublic) {
            StringBuilder().apply {
                append(
                    String.format(
                    context.getString(R.string.mvc_share_message_public).toBlankOrString(),
                    voucher.typeFormatted,
                    formattedShopName
                    )
                )
                append("\n")
                append(shareUrl)
            }.toString()
        } else {
            StringBuilder().apply {
                append(
                    String.format(
                    context.getString(R.string.mvc_share_message_private).toBlankOrString(),
                    voucher.typeFormatted,
                    voucher.code,
                    formattedShopName
                    )
                )
                append("\n")
                append(shareUrl)
            }.toString()
        }
    return shareMessage
}

internal fun getTextSizeFromDimens(context: Context, @DimenRes dimensResId : Int): Float {
    return context.resources.getDimension(dimensResId) / context.resources.displayMetrics.density
}
