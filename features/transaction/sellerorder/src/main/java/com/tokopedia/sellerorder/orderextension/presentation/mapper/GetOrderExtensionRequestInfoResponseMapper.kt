package com.tokopedia.sellerorder.orderextension.presentation.mapper

import androidx.annotation.DimenRes
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.orderextension.domain.models.GetOrderExtensionRequestInfoResponse
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.sellerorder.orderextension.presentation.util.ResourceProvider
import com.tokopedia.utils.date.toDate
import javax.inject.Inject

class GetOrderExtensionRequestInfoResponseMapper @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    companion object {
        private const val ID_HEADER = 0
        private const val ID_DESCRIPTION = 1
        private const val ID_PICK_TIME_TITLE = 2
        private const val ID_OPTIONS_TITLE = 3
        private const val ID_FOOTER_TITLE = 4
        private const val FORMAT_DATE_ORDER_EXTENTION = "yyyy-MM-dd"
        private const val FORMAT_DATE_DEADLINE_ORDER_EXTENTION = "yyyy/MM/dd"

    }

    fun createLoadingData(): OrderExtensionRequestInfoUiModel = OrderExtensionRequestInfoUiModel(
        items = mutableListOf<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>().apply {
            addOrderExtensionHeader(ID_HEADER)
            addOrderExtensionDescriptionShimmer(
                resourceProvider.getLongDescriptionShimmerWidth()
            )
            addOrderExtensionPickTimeTitle(ID_PICK_TIME_TITLE)
            addOrderExtensionPickTimeShimmer()
            addOrderExtensionOptionsTitle(ID_OPTIONS_TITLE)
            addOrderExtensionOptionsShimmer()
            addOrderExtensionOptionsShimmer()
            addOrderExtensionOptionsShimmer()
            addOrderExtensionFooter(ID_FOOTER_TITLE)
        },
        processing = false,
        success = true,
        completed = false,
        refreshOnDismiss = false,
        message = "",
        throwable = null
    )

    fun mapSuccessResponseToUiModel(
        response: GetOrderExtensionRequestInfoResponse.Data.OrderExtensionRequestInfo.OrderExtensionRequestInfoData
    ): OrderExtensionRequestInfoUiModel = OrderExtensionRequestInfoUiModel(
        items = if (!response.reason.isNullOrEmpty()) {
            mutableListOf<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>().apply {
                addOrderExtensionHeader(ID_HEADER)
                addOrderExtensionDescription(ID_DESCRIPTION, response.text, response.newDeadline)
                addOrderExtensionPickTimeTitle(ID_PICK_TIME_TITLE)
                addOrderExtensionPickTimeField()
                addOrderExtensionOptionsTitle(ID_OPTIONS_TITLE)
                addOrderExtensionOptions(response.reason)
                addOrderExtensionFooter(ID_FOOTER_TITLE)
            }
        } else emptyList(),
        orderExtensionDate = OrderExtensionRequestInfoUiModel.OrderExtensionDate(
            deadLineTime = response.deadlineInfo.maxDate.toDate(FORMAT_DATE_DEADLINE_ORDER_EXTENTION),
            eligibleDates = response.eligibleDates?.map {
                OrderExtensionRequestInfoUiModel.OrderExtensionDate.EligibleDateUIModel(
                    date = it.date.toDate(FORMAT_DATE_ORDER_EXTENTION),
                    extensionTime = it.extensionTime
                )
            }.orEmpty()
        ),
        processing = false,
        success = response.isSuccess(),
        completed = response.isSuccess().not(),
        refreshOnDismiss = false,
        message = response.message.orEmpty(),
        throwable = null
    )

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

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionHeader(
        id: Int
    ) {
        add(
            OrderExtensionRequestInfoUiModel.DescriptionUiModel(
                id = id,
                fontColor = com.tokopedia.unifyprinciples.R.color.Unify_NN950,
                typographyType = OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_3,
                description = resourceProvider.getOrderExtensionRequestBottomSheetTitleComposer()
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionDescription(
        id: Int,
        text: String?,
        newDeadline: String?
    ) {
        add(
            OrderExtensionRequestInfoUiModel.DescriptionUiModel(
                id = id,
                fontColor = com.tokopedia.unifyprinciples.R.color.Unify_NN600,
                typographyType = OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.BODY_2,
                description = resourceProvider.getOrderExtensionDescriptionComposer(
                    text,
                    newDeadline
                )
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionPickTimeTitle(
        id: Int
    ) {
        add(
            OrderExtensionRequestInfoUiModel.DescriptionUiModel(
                id = id,
                fontColor = com.tokopedia.unifyprinciples.R.color.Unify_NN950,
                typographyType = OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_4,
                description = resourceProvider.getOrderExtensionRequestBottomSheetPickTimeTitleComposer()
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionPickTimeField() {
        add(OrderExtensionRequestInfoUiModel.PickTimeUiModel())
    }


    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionOptionsTitle(
        id: Int
    ) {
        add(
            OrderExtensionRequestInfoUiModel.DescriptionUiModel(
                id = id,
                fontColor = com.tokopedia.unifyprinciples.R.color.Unify_NN950,
                typographyType = OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionTextType.HEADING_4,
                description = resourceProvider.getOrderExtensionRequestBottomSheetOptionsTitleComposer()
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionOptions(
        reasons: List<GetOrderExtensionRequestInfoResponse.Data.OrderExtensionRequestInfo.OrderExtensionRequestInfoData.Reason>?
    ) {
        val options = mutableListOf<OrderExtensionRequestInfoUiModel.OptionUiModel>()
        val optionsComment = mutableListOf<OrderExtensionRequestInfoUiModel.CommentUiModel>()
        reasons?.forEachIndexed { index, reason ->
            options.addOrderExtensionOption(index = index, reason = reason)
            if (reason.mustComment) {
                optionsComment.addOrderExtensionOptionComment(index = index, reason = reason)
            }
        }
        addAll(options)
        addAll(optionsComment)
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionFooter(
        id: Int
    ) {
        add(
            OrderExtensionRequestInfoUiModel.DescriptionUiModel(
                id = id,
                alignment = OrderExtensionRequestInfoUiModel.DescriptionUiModel.DescriptionAlignment.TEXT_ALIGNMENT_TEXT_START,
                fontColor = com.tokopedia.unifyprinciples.R.color.Unify_NN600,
                description = resourceProvider.getOrderExtensionRequestBottomSheetFooterComposer(),
                show = true
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionDescriptionShimmer(@DimenRes widthResId: Int) {
        add(
            OrderExtensionRequestInfoUiModel.DescriptionShimmerUiModel(
                com.tokopedia.sellerorder.orderextension.presentation.model.DimenRes(
                    widthResId
                )
            )
        )
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionOptionsShimmer() {
        add(OrderExtensionRequestInfoUiModel.OptionShimmerUiModel())
    }

    private fun MutableList<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>.addOrderExtensionPickTimeShimmer() {
        add(OrderExtensionRequestInfoUiModel.PickTimeShimmerUiModel())
    }
}
