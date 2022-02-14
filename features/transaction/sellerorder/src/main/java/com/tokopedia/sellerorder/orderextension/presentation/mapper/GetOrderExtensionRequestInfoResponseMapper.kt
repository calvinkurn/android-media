package com.tokopedia.sellerorder.orderextension.presentation.mapper

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.orderextension.domain.models.GetOrderExtensionRequestInfoResponse
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.sellerorder.orderextension.presentation.util.ResourceProvider
import javax.inject.Inject

class GetOrderExtensionRequestInfoResponseMapper @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    fun createLoadingData(): OrderExtensionRequestInfoUiModel = OrderExtensionRequestInfoUiModel(
        items = mutableListOf<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>().apply {
            addOrderExtensionHeader()
            addOrderExtensionDescriptionShimmer(resourceProvider.getLongDescriptionShimmerWidth())
            addOrderExtensionDescriptionShimmer(resourceProvider.getLongDescriptionShimmerWidth())
            addOrderExtensionDescriptionShimmer(resourceProvider.getShortDescriptionShimmerWidth())
            addOrderExtensionOptionsTitle()
            addOrderExtensionOptionsShimmer()
            addOrderExtensionOptionsShimmer()
            addOrderExtensionOptionsShimmer()
            addOrderExtensionFooter()
        },
        processing = false,
        success = true,
        completed = false,
        refreshOnDismiss = false,
        message = ""
    )

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
        processing = false,
        success = response.isSuccess(),
        completed = response.isSuccess().not(),
        refreshOnDismiss = false,
        message = response.message.orEmpty()
    )

    fun mapError(throwable: Throwable): String {
        return resourceProvider.getErrorMessageFromThrowable(throwable)
    }

    private fun createDefaultCommentErrorChecker(): List<OrderExtensionRequestInfoUiModel.CommentUiModel.ErrorChecker> {
        return listOf(
            OrderExtensionRequestInfoUiModel.CommentUiModel.ErrorChecker(
                regex = resourceProvider.getOrderExtensionRequestCommentNotEmptyRegex(),
                errorMessage = resourceProvider.getErrorMessageOrderExtensionRequestCommentCannotEmpty()
            ),
            OrderExtensionRequestInfoUiModel.CommentUiModel.ErrorChecker(
                regex = resourceProvider.getOrderExtensionRequestCommentCannotLessThanRegex(),
                errorMessage = resourceProvider.getErrorMessageOrderExtensionRequestCommentCannotLessThan()
            ),
            OrderExtensionRequestInfoUiModel.CommentUiModel.ErrorChecker(
                regex = resourceProvider.getOrderExtensionRequestCommentAllowedCharactersRegex(),
                errorMessage = resourceProvider.getErrorMessageOrderExtensionRequestCommentCannotContainsIllegalCharacters()
            )
        )
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
                defaultMessage = resourceProvider.getErrorMessageOrderExtensionRequestCommentCannotContainsIllegalCharacters(),
                showedMessage = resourceProvider.getErrorMessageOrderExtensionRequestCommentCannotContainsIllegalCharacters(),
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
                description = resourceProvider.getOrderExtensionRequestBottomSheetTitle()
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
                description = resourceProvider.composeOrderExtensionDescription(text, newDeadline)
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionOptionsTitle() {
        add(
            OrderExtensionRequestInfoUiModel.DescriptionUiModel(
                fontColor = com.tokopedia.unifyprinciples.R.color.Unify_NN950,
                typographyType = OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_4,
                description = resourceProvider.getOrderExtensionRequestBottomSheetOptionsTitle()
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
                description = resourceProvider.getOrderExtensionRequestBottomSheetFooter(),
                show = true
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionDescriptionShimmer(
        width: Int
    ) {
        add(OrderExtensionRequestInfoUiModel.DescriptionShimmerUiModel(width))
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionOptionsShimmer() {
        add(OrderExtensionRequestInfoUiModel.OptionShimmerUiModel())
    }
}
