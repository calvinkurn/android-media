package com.tokopedia.sellerorder.orderextension.presentation.util

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.orderextension.presentation.model.StringComposer
import com.tokopedia.unifycomponents.HtmlLinkHelper
import javax.inject.Inject

class ResourceProvider @Inject constructor() {

    companion object {
        private const val REGEX_EMPTY_STRING = "^\\s*\$"
        private const val REGEX_CHARS_COUNT = "^.{0,14}\$"
        private const val REGEX_ALLOWED_CHARS = "[^,.?!\\n A-Za-z0-9]+"
    }

    private fun createBoldText(text: String): Spannable {
        return SpannableString(text).apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                Int.ZERO,
                text.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun Spannable.setColor(context: Context?, color: Int): Spannable {
        setSpan(
            ForegroundColorSpan(getColor(context, color)),
            Int.ZERO,
            length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    private fun getColor(context: Context?, color: Int): Int {
        return MethodChecker.getColor(context, color)
    }

    fun getOrderExtensionRequestCommentNotEmptyRegex(): String {
        return REGEX_EMPTY_STRING
    }

    fun getErrorMessageOrderExtensionRequestCommentCannotEmpty(): StringComposer {
        return StringComposer {
            it.getString(R.string.bottomsheet_order_extension_request_other_options_text_area_message_empty_reason)
        }
    }

    fun getOrderExtensionRequestCommentCannotLessThanRegex(): String {
        return REGEX_CHARS_COUNT
    }

    fun getErrorMessageOrderExtensionRequestCommentCannotLessThan(): StringComposer {
        return StringComposer {
            it.getString(R.string.bottomsheet_order_extension_request_other_options_text_area_message_insufficient_reason_length)
        }
    }

    fun getOrderExtensionRequestCommentAllowedCharactersRegex(): String {
        return REGEX_ALLOWED_CHARS
    }

    fun getErrorMessageOrderExtensionRequestCommentCannotContainsIllegalCharacters(): StringComposer {
        return StringComposer {
            it.getString(R.string.bottomsheet_order_extension_request_other_options_text_area_message_illegal_characters)
        }
    }

    fun getOrderExtensionRequestBottomSheetTitleComposer(): StringComposer {
        return StringComposer { it.getString(R.string.bottomsheet_order_extension_request_title) }
    }

    fun getOrderExtensionRequestBottomSheetOptionsTitleComposer(): StringComposer {
        return StringComposer {
            it.getString(R.string.bottomsheet_order_extension_request_options_header)
        }
    }

    fun getOrderExtensionRequestBottomSheetPickTimeTitleComposer(): StringComposer {
        return StringComposer {
            it.getString(R.string.bottomsheet_order_extension_request_pick_time_header)
        }
    }

    fun getOrderExtensionRequestBottomSheetFooterComposer(): StringComposer {
        return StringComposer {
            HtmlLinkHelper(
                it,
                it.getString(
                    R.string.bottomsheet_order_extension_request_footer,
                    ContextCompat.getColor(
                        it,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    ).toColorString()
                )
            ).spannedString ?: ""
        }
    }

    fun getOrderExtensionDescriptionComposer(text: String?): StringComposer {
        return StringComposer {
            HtmlLinkHelper(
                it,
                text.orEmpty()
            ).spannedString ?: ""
        }
    }

    fun getLongDescriptionShimmerWidth(): Int {
        return R.dimen.som_order_extension_bottom_sheet_long_description_shimmer_width
    }

    fun getShortDescriptionShimmerWidth(): Int {
        return R.dimen.som_order_extension_bottom_sheet_short_description_shimmer_width
    }
}
