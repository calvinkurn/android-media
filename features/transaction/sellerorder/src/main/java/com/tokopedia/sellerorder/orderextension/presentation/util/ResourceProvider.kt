package com.tokopedia.sellerorder.orderextension.presentation.util

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sellerorder.R
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.toPx
import javax.inject.Inject

class ResourceProvider @Inject constructor(
    private val context: Context
) {
    private fun createBoldText(text: String): Spannable {
        return SpannableString(text).apply {
            setSpan(StyleSpan(Typeface.BOLD), Int.ZERO, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun Spannable.setColor(color: Int): Spannable {
        setSpan(ForegroundColorSpan(getColor(color)), Int.ZERO, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    private fun getColor(color: Int): Int {
        return MethodChecker.getColor(context, color)
    }

    fun getErrorMessageFromThrowable(throwable: Throwable): String {
        return ErrorHandler.getErrorMessage(context, throwable)
    }

    fun  dpToPx(dp: Int): Int {
        return dp.toPx()
    }

    fun getOrderExtensionRequestCommentNotEmptyRegex(): String {
        return context.getString(R.string.bottomsheet_order_extension_request_comment_not_empty_regex)
    }

    fun getErrorMessageOrderExtensionRequestCommentCannotEmpty(): String {
        return context.getString(R.string.bottomsheet_order_extension_request_other_options_text_area_message_empty_reason)
    }

    fun getOrderExtensionRequestCommentCannotLessThanRegex(): String {
        return context.getString(R.string.bottomsheet_order_extension_request_comment_not_less_than_fifteen_characters_regex)
    }

    fun getErrorMessageOrderExtensionRequestCommentCannotLessThan(): String {
        return context.getString(R.string.bottomsheet_order_extension_request_other_options_text_area_message_insufficient_reason_length)
    }

    fun getOrderExtensionRequestCommentAllowedCharactersRegex(): String {
        return context.getString(R.string.bottomsheet_order_extension_request_comment_not_allowed_characters_regex)
    }

    fun getErrorMessageOrderExtensionRequestCommentCannotContainsIllegalCharacters(): String {
        return context.getString(R.string.bottomsheet_order_extension_request_other_options_text_area_message_illegal_characters)
    }

    fun getOrderExtensionRequestBottomSheetTitle(): String {
        return context.getString(R.string.bottomsheet_order_extension_request_title)
    }

    fun getOrderExtensionRequestBottomSheetOptionsTitle(): String {
        return context.getString(R.string.bottomsheet_order_extension_request_options_header)
    }

    fun getOrderExtensionRequestBottomSheetFooter(): CharSequence {
        return HtmlLinkHelper(context, context.getString(R.string.bottomsheet_order_extension_request_footer)).spannedString ?: ""
    }

    fun composeOrderExtensionDescription(text: String?, newDeadline: String?): CharSequence {
        return SpannableStringBuilder().append(text.orEmpty())
            .append(" ")
            .append(createBoldText(newDeadline.orEmpty()).setColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950))
            .append(".")
    }

    fun getLongDescriptionShimmerWidth(): Int {
        return context.resources.getDimension(R.dimen.som_order_extension_bottom_sheet_long_description_shimmer_width).toInt()
    }

    fun getShortDescriptionShimmerWidth(): Int {
        return context.resources.getDimension(R.dimen.som_order_extension_bottom_sheet_short_description_shimmer_width).toInt()
    }
}