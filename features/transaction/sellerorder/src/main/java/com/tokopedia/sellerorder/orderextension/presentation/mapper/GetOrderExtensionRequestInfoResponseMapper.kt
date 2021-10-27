package com.tokopedia.sellerorder.orderextension.presentation.mapper

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.orderextension.domain.models.GetOrderExtensionRequestInfoResponse
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import javax.inject.Inject

class GetOrderExtensionRequestInfoResponseMapper @Inject constructor(
    private val context: Context
) {
    fun mapSuccessResponseToUiModel(
        response: GetOrderExtensionRequestInfoResponse.Data.OrderExtensionRequestInfo.OrderExtensionRequestInfoData
    ): OrderExtensionRequestInfoUiModel = OrderExtensionRequestInfoUiModel(
        items = if (!response.reason.isNullOrEmpty()) {
            mutableListOf<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>().apply {
                addOrderExtensionHeader()
                addOrderExtensionDescription(response.text, response.newDeadline)
                addOrderExtensionOptionsTitle()
                addOrderExtensionOptions(response.reason)
                addOrderExtensionFooter()
            }
        } else emptyList(),
        success = response.messageCode == 1,
        errorMessage = response.message.orEmpty()
    )

    private fun mapOrderExtensionDescription(text: String?, newDeadline: String?): CharSequence {
        return SpannableStringBuilder().append(text.orEmpty())
            .append(" ")
            .append(createBoldText(newDeadline.orEmpty()).setColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950))
            .append(".")
    }

    private fun createDefaultCommentErrorChecker(): List<OrderExtensionRequestInfoUiModel.CommentUiModel.ErrorChecker> {
        return listOf(
            OrderExtensionRequestInfoUiModel.CommentUiModel.ErrorChecker(
                regex = context.getString(R.string.bottomsheet_order_extension_request_comment_not_empty_regex),
                errorMessage = context.getString(R.string.bottomsheet_order_extension_request_other_options_text_area_message_empty_reason)
            ),
            OrderExtensionRequestInfoUiModel.CommentUiModel.ErrorChecker(
                regex = context.getString(R.string.bottomsheet_order_extension_request_comment_not_less_than_fifteen_characters_regex),
                errorMessage = context.getString(R.string.bottomsheet_order_extension_request_other_options_text_area_message_insufficient_reason_length)
            ),
            OrderExtensionRequestInfoUiModel.CommentUiModel.ErrorChecker(
                regex = context.getString(R.string.bottomsheet_order_extension_request_comment_not_allowed_characters_regex),
                errorMessage = context.getString(R.string.bottomsheet_order_extension_request_other_options_text_area_message_illegal_characters)
            )
        )
    }

    private fun createBoldText(text: String): Spannable {
        return SpannableString(text).apply {
            setSpan(StyleSpan(Typeface.BOLD), Int.ZERO, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun Spannable.setColor(color: Int): Spannable {
        setSpan(ForegroundColorSpan(MethodChecker.getColor(context, color)), Int.ZERO, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.OptionUiModel>.addOrderExtensionOption(
        index: Int,
        reason: GetOrderExtensionRequestInfoResponse.Data.OrderExtensionRequestInfo.OrderExtensionRequestInfoData.Reason
    ) {
        add(
            OrderExtensionRequestInfoUiModel.OptionUiModel(
                code = reason.reasonCode,
                name = reason.reasonTitle,
                selected = index == Int.ZERO,
                mustComment = reason.mustComment,
                hideKeyboardOnClick = reason.mustComment.not()
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.CommentUiModel>.addOrderExtensionOptionComment(
        index: Int,
        reason: GetOrderExtensionRequestInfoResponse.Data.OrderExtensionRequestInfo.OrderExtensionRequestInfoData.Reason
    ) {
        add(
            OrderExtensionRequestInfoUiModel.CommentUiModel(
                optionCode = reason.reasonCode,
                defaultMessage = context.getString(R.string.bottomsheet_order_extension_request_other_options_text_area_message_illegal_characters),
                showedMessage = context.getString(R.string.bottomsheet_order_extension_request_other_options_text_area_message_illegal_characters),
                show = index == Int.ZERO,
                requestFocus = index == Int.ZERO,
                errorCheckers = createDefaultCommentErrorChecker()
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionHeader() {
        add(
            OrderExtensionRequestInfoUiModel.DescriptionUiModel(
                fontColor = com.tokopedia.unifyprinciples.R.color.Unify_NN950,
                typographyType = OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_3,
                description = context.getString(R.string.bottomsheet_order_extension_request_title)
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionDescription(
        text: String?,
        newDeadline: String?
    ) {
        add(
            OrderExtensionRequestInfoUiModel.DescriptionUiModel(
                typographyType = OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.BODY_2,
                description = mapOrderExtensionDescription(text, newDeadline)
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionOptionsTitle() {
        add(
            OrderExtensionRequestInfoUiModel.DescriptionUiModel(
                fontColor = com.tokopedia.unifyprinciples.R.color.Unify_NN950,
                typographyType = OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_4,
                description = context.getString(R.string.bottomsheet_order_extension_request_options_header)
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionOptions(
        reasons: List<GetOrderExtensionRequestInfoResponse.Data.OrderExtensionRequestInfo.OrderExtensionRequestInfoData.Reason>?
    ) {
        val options = mutableListOf<OrderExtensionRequestInfoUiModel.OptionUiModel>()
        val optionsComment = mutableListOf<OrderExtensionRequestInfoUiModel.CommentUiModel>()
        reasons?.forEachIndexed { index, reason ->
            options.addOrderExtensionOption(index, reason)
            if (reason.mustComment) {
                optionsComment.addOrderExtensionOptionComment(index, reason)
            }
        }
        addAll(options)
        addAll(optionsComment)
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionFooter() {
        add(
            OrderExtensionRequestInfoUiModel.DescriptionUiModel(
                alignment = OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionAlignment.TEXT_ALIGNMENT_CENTER,
                fontColor = com.tokopedia.unifyprinciples.R.color.Unify_NN600,
                description = HtmlLinkHelper(
                    context,
                    context.getString(R.string.bottomsheet_order_extension_request_footer)
                ).spannedString ?: "",
                show = true
            )
        )
    }
}
